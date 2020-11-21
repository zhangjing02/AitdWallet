package com.tianqi.aitdwallet.ui.service;

/**
 * Create by zhangjing on 2020/11/21.
 * Describe :数据中台，用户需要耗时操作的数据获取，存储，和发送通知。
 */

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.TransactionRecordManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetErc20BalanceBean;
import com.tianqi.baselib.rxhttp.bean.GetErc20TxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetEthTxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetListUnspentBean;
import com.tianqi.baselib.rxhttp.bean.GetLoadingTxBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Kathy on 17-2-6.
 */

public class DataManageService extends Service {

    private CoinInfo txCoinInfo;
    private WalletInfo txWalletInfo;
    private String coin_id;
    private double btc_account_balance;
    private List<CoinInfo> allBtccCoinInfos, allusdtCoinInfos, allEthcCoinInfos, allERC20cCoinInfos;
    String coin_type_params = null;
    private int btc_quest_count, usdt_quest_count, eth_quest_count, erc20_quest_count;

    //client 可以通过Binder获取Service实例
    public class MyBinder extends Binder {
        public DataManageService getService() {
            return DataManageService.this;
        }
    }

    //通过binder实现调用者client与Service之间的通信
    private MyBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        coin_id = intent.getStringExtra(Constants.TRANSACTION_COIN_ID);
        if (!TextUtils.isEmpty(coin_id)){
            txCoinInfo = CoinInfoManager.getMainCoinFrCoinId(coin_id);
            txWalletInfo = WalletInfoManager.getWalletFrName(txCoinInfo.getCoin_name());
        }
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    /**
     * 此处是交易记录，请求接口和数据存储的逻辑。
     * 策略上，分页加载更多，应该从此处数据获取，加载第一页如果够50条的话，应该去自动加载第二页（此功能未加入）
     *
     */

    public void getTxRecordData(String coin_id) {
        txCoinInfo = CoinInfoManager.getMainCoinFrCoinId(coin_id);
        if (txCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            postEthTxRecord(txCoinInfo);
        } else if (txCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC) || txCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            postBtcTxRecord(txCoinInfo);
            postBtcLoadingRecord(txCoinInfo);
        } else if (txCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)) {
            postErc20TxRecore(txCoinInfo);
        }
    }

    private void postEthTxRecord(CoinInfo coinInfo) {
        String mAddress = coinInfo.getCoin_address().startsWith("0x") ? coinInfo.getCoin_address().toLowerCase() : Constants.HEX_PREFIX + coinInfo.getCoin_address().toLowerCase();
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getEthTxRecord("eth", mAddress, map).compose(RxHelper.pool_io())
                .subscribe(new BaseObserver<GetEthTxRecordBean>(this) {
                    @Override
                    public void onSuccess(GetEthTxRecordBean data, String msg) {
                        if (data != null && data.getTxs() != null && data.getTxs().size() > 0) {
                            btc_account_balance = Double.valueOf(data.getSpend()) + Double.valueOf(data.getReceive());
                            coinInfo.setCoin_totalAmount(btc_account_balance);
                            CoinInfoManager.insertOrUpdate(coinInfo);
                            for (int i = 0; i < data.getTxs().size(); i++) {
                                if (Double.valueOf(data.getTxs().get(i).getValue()) > 0) {
                                    insertEthTxRecord(data.getTxs().get(i), coinInfo, i >= data.getTxs().size() - 1);
                                }
                            }
                        }else {
                            EventMessage eventMessage = new EventMessage();
                            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                            EventBus.getDefault().post(eventMessage);
                        }
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                        EventMessage eventMessage = new EventMessage();
                        eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                        EventBus.getDefault().post(eventMessage);
                    }
                });
    }

    private void insertEthTxRecord(GetEthTxRecordBean.TxsBean txsBean, CoinInfo eth_coinInfo, boolean isLast) {
        //创建（转出）数据库交易，存入数据库
        TransactionRecord tx_record = new TransactionRecord();
        tx_record.setAddress(eth_coinInfo.getCoin_address());
        tx_record.setAmount(Double.valueOf(txsBean.getValue()));
        tx_record.setCoin_type(Constant.TRANSACTION_COIN_ETH);//0代表比特币。
        tx_record.setStatus(Constant.TRANSACTION_STATE_SUCCESS);
        if (txsBean.getFrom().equals(eth_coinInfo.getCoin_address().toLowerCase())) {  //如果是自己的地址，证明是转出。
            tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);
            tx_record.setTargetAddress(txsBean.getTo());
        } else {
            tx_record.setTransType(Constant.TRANSACTION_TYPE_RECEIVE);
            tx_record.setTargetAddress(txsBean.getFrom());
        }
        tx_record.setCoin_id(eth_coinInfo.getCoin_id());
        tx_record.setVout_id(txsBean.getIndex());
        tx_record.setConfirmations(txsBean.getConfirmations());
        tx_record.setTxid(txsBean.getTxid());
        Calendar calendar = Calendar.getInstance();
        tx_record.setBlock_no(String.valueOf(txsBean.getBlock_no()));
        calendar.setTimeInMillis(txsBean.getTime() * 1000l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        tx_record.setTimeStr(format.format(calendar.getTime()));
        tx_record.setUnit(Constant.COIN_UNIT_ETH);
        tx_record.setMiner_fee(Double.valueOf(txsBean.getFee()));
        TransactionRecordManager.insertOrUpdate(tx_record);
        if (isLast) {
            EventMessage eventMessage = new EventMessage();
            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
            EventBus.getDefault().post(eventMessage);
        }
    }

    private void postBtcTxRecord(CoinInfo coinInfo) {
        String coin_type_params = null;
        if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            coin_type_params = "btc";
        } else if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            coin_type_params = "usdt";
        }

        if (!TextUtils.isEmpty(coin_type_params)) {
            Map<String, Object> map = new HashMap<>();
            map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
            RetrofitFactory.getInstence(this).API()
                    .getBtcAddressBalance(coin_type_params, coinInfo.getCoin_address(), "1/50", map).compose(RxHelper.pool_io())
                    .subscribe(new BaseObserver<List<GetListUnspentBean>>(this) {
                        @Override
                        public void onSuccess(List<GetListUnspentBean> data, String msg) {
                            if (data != null && data.size() > 0) {
                                btc_account_balance = Double.valueOf(data.get(0).getSpend()) + Double.valueOf(data.get(0).getReceive());
                                coinInfo.setCoin_totalAmount(btc_account_balance);
                                CoinInfoManager.insertOrUpdate(coinInfo);
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getHash().equals(coinInfo.getCoin_address())) {
                                        for (int j = 0; j < data.get(i).getTxs().size(); j++) {   //tx循环下。
                                            if (data.get(i).getTxs().get(j).getInputs().get(0).getAddress().equals(coinInfo.getCoin_address())) {  //如果是自己的地址，证明是转出。
                                                insertTxRecord(data, i, j, Constant.TRANSACTION_TYPE_SEND, coinInfo);
                                            } else {  //此处代表是转入的交易。
                                                insertTxRecord(data, i, j, Constant.TRANSACTION_TYPE_RECEIVE, coinInfo);
                                            }
                                        }
                                    }
                                }
                            } else {
                                EventMessage eventMessage = new EventMessage();
                                eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                                EventBus.getDefault().post(eventMessage);
                            }
                        }

                        @Override
                        protected void onFailure(int code, String msg) {
                            EventMessage eventMessage = new EventMessage();
                            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                            EventBus.getDefault().post(eventMessage);
                        }
                    });
        }
    }

    private void insertTxRecord(List<GetListUnspentBean> data, int i, int j, int type, CoinInfo coinInfo) {
        double spend_value = 0;
        for (int k = 0; k < data.get(i).getTxs().get(j).getOutputs().size(); k++) {  //输出循环中
            if (type == Constant.TRANSACTION_TYPE_SEND) {
                if (!data.get(i).getTxs().get(j).getOutputs().get(k).getAddress().equals(coinInfo.getCoin_address())) {
                    //因为是转出，所以判断转出地址不是自己的，才是转账金额，否则是找零给自己的地址。
                    spend_value = spend_value + Double.valueOf(data.get(i).getTxs().get(j).getOutputs().get(k).getValue());
                }
            } else if (type == Constant.TRANSACTION_TYPE_RECEIVE) {
                if (data.get(i).getTxs().get(j).getOutputs().get(k).getAddress() != null) {
                    if (data.get(i).getTxs().get(j).getOutputs().get(k).getAddress().equals(coinInfo.getCoin_address())) {
                        //因为是转入，所以判断转出地址是自己的，才是转账金额，否则是找零给自己的地址。
                        spend_value = spend_value + Double.valueOf(data.get(i).getTxs().get(j).getOutputs().get(k).getValue());
                    }
                }
            }
        }
        //如果是转出，但是在output中没找到不是自己的地址，则应该是自己给自己转账，我们就拿第一比输出作为转金额。
        if (type == Constant.TRANSACTION_TYPE_SEND && spend_value == 0) {
            if (data.get(i).getTxs().get(j).getOutputs().size() > 0) {
                spend_value = Double.valueOf(data.get(i).getTxs().get(j).getOutputs().get(0).getValue());
            }
        }
        //创建（转出）数据库交易，存入数据库
        TransactionRecord tx_record = new TransactionRecord();
        tx_record.setAddress(coinInfo.getCoin_address());
        tx_record.setAmount(spend_value);
        tx_record.setCoin_type(Constant.TRANSACTION_COIN_BTC);//0代表比特币。
        tx_record.setStatus(Constant.TRANSACTION_STATE_SUCCESS);
        tx_record.setTransType(type);//0转账，1收款
        tx_record.setCoin_id(coinInfo.getCoin_id());
        tx_record.setVout_id(0);

        tx_record.setTxid(data.get(i).getTxs().get(j).getTxid());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data.get(i).getTxs().get(j).getTime() * 1000l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        tx_record.setTimeStr(format.format(calendar.getTime()));

        if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            tx_record.setUnit(Constant.COIN_UNIT_BTC);
        } else if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            tx_record.setUnit(Constant.COIN_UNIT_USDT);
        }
        TransactionRecordManager.insertOrUpdate(tx_record);

        if (i == data.size() - 1 && j == data.get(i).getTxs().size() - 1) {
            EventMessage eventMessage = new EventMessage();
            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
            EventBus.getDefault().post(eventMessage);
        }
    }

    private void postBtcLoadingRecord(CoinInfo coinInfo) {
        String coin_type_params = null;
        if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            coin_type_params = "btc";
        } else if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            coin_type_params = "usdt";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getLoadingTx(coin_type_params, coinInfo.getCoin_address(), map).compose(RxHelper.pool_io())
                .subscribe(new BaseObserver<List<GetLoadingTxBean>>(this) {
                    @Override
                    public void onSuccess(List<GetLoadingTxBean> data, String msg) {
                        if (data != null && data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                for (int j = 0; j < data.get(i).getInputs().size(); j++) {
                                    if (data.get(i).getInputs().get(j).getAddress().equals(coinInfo.getCoin_address())) {
                                        //如果input是自己的，就是转出。所以可以拿output（不是自己地址的output）当成本次转账的金额。
                                        for (int k = 0; k < data.get(i).getOutputs().size(); k++) {
                                            if (!data.get(i).getOutputs().get(k).getAddress().equals(coinInfo.getCoin_address())) {
                                                insertLoadingTx(data, i, k, Constant.TRANSACTION_TYPE_SEND);
                                            }
                                        }
                                    } else {
                                        //input地址不是自己的，那就是收到的钱。
                                        for (int k = 0; k < data.get(i).getOutputs().size(); k++) {
                                            //output地址是自己的，那就是收到的钱多少。
                                            if (data.get(i).getOutputs().get(k).getAddress().equals(coinInfo.getCoin_address())) {
                                                insertLoadingTx(data, i, k, Constant.TRANSACTION_TYPE_RECEIVE);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            EventMessage eventMessage = new EventMessage();
                            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                            EventBus.getDefault().post(eventMessage);
                        }
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        EventMessage eventMessage = new EventMessage();
                        eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                        EventBus.getDefault().post(eventMessage);
                    }
                });
    }

    private void insertLoadingTx(List<GetLoadingTxBean> data, int i, int k, int transactionTypeReceive) {
        double receive_value;
        receive_value = Double.valueOf(data.get(i).getOutputs().get(k).getValue());
        //创建（转出）数据库交易，存入数据库
        TransactionRecord tx_record = new TransactionRecord();
        tx_record.setAddress(txCoinInfo.getCoin_address());
        tx_record.setTargetAddress(data.get(i).getOutputs().get(k).getAddress());
        tx_record.setAmount(receive_value);
        tx_record.setCoin_type(Constant.TRANSACTION_COIN_BTC);//0代表比特币。
        tx_record.setStatus(Constant.TRANSACTION_STATE_WAITING);
        tx_record.setTransType(transactionTypeReceive);//0转账，1收款
        tx_record.setCoin_id(txCoinInfo.getCoin_id());
        tx_record.setVout_id(0);

        tx_record.setTxid(data.get(i).getTxid());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data.get(i).getTime() * 1000l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        tx_record.setTimeStr(format.format(calendar.getTime()));
        if (txCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            tx_record.setUnit(Constant.COIN_UNIT_BTC);
        } else if (txCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            tx_record.setUnit(Constant.COIN_UNIT_USDT);
        }
        TransactionRecordManager.insertOrUpdate(tx_record);

        if (i == data.size() - 1 && k == data.get(i).getOutputs().size() - 1) {
            EventMessage eventMessage = new EventMessage();
            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
            EventBus.getDefault().post(eventMessage);
        }
    }

    private void postErc20TxRecore(CoinInfo coinInfo) {
        String mAddress = coinInfo.getCoin_address().startsWith("0x") ? coinInfo.getCoin_address().toLowerCase() : Constants.HEX_PREFIX + coinInfo.getCoin_address().toLowerCase();
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getErc20TxRecord(mAddress, Constant.CONTRACT_ADDRESS, "1/50", map).compose(RxHelper.pool_io())
                .subscribe(new BaseObserver<List<GetErc20TxRecordBean>>(this) {
                    @Override
                    public void onSuccess(List<GetErc20TxRecordBean> datas, String msg) {
                        if (datas != null && datas.size() > 0) {
                            for (int i = 0; i < datas.size(); i++) {
                                insertErc20TxRecord(datas, i, coinInfo, i == datas.size() - 1);
                            }
                        } else {
                            EventMessage eventMessage = new EventMessage();
                            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                            EventBus.getDefault().post(eventMessage);
                        }
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                        EventMessage eventMessage = new EventMessage();
                        eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                        EventBus.getDefault().post(eventMessage);
                    }
                });
    }

    private void insertErc20TxRecord(List<GetErc20TxRecordBean> datas, int i, CoinInfo coinInfo, boolean isLast) {
        //创建（转出）数据库交易，存入数据库
        TransactionRecord tx_record = new TransactionRecord();
        tx_record.setAddress(coinInfo.getCoin_address());
        tx_record.setAmount(Double.valueOf(datas.get(i).getValue()) / Math.pow(10, Integer.valueOf(datas.get(i).getTokenInfo().getD())));
        tx_record.setCoin_type(Constant.TRANSACTION_COIN_ETH);//0代表比特币。
        tx_record.setStatus(Constant.TRANSACTION_STATE_SUCCESS);
        if (datas.get(i).getFrom().equals(coinInfo.getCoin_address().toLowerCase())) {  //如果是自己的地址，证明是转出。
            tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);
            tx_record.setTargetAddress(datas.get(i).getTo());
        } else {
            tx_record.setTransType(Constant.TRANSACTION_TYPE_RECEIVE);
            tx_record.setTargetAddress(datas.get(i).getFrom());
        }
        tx_record.setCoin_id(coinInfo.getCoin_id());
        tx_record.setVout_id(datas.get(i).getIndex());
        tx_record.setConfirmations(datas.get(i).getConformations());
        tx_record.setTxid(datas.get(i).getTxid());
        Calendar calendar = Calendar.getInstance();
        tx_record.setBlock_no(String.valueOf(datas.get(i).getBlock_no()));
        calendar.setTimeInMillis(datas.get(i).getTime() * 1000l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        tx_record.setTimeStr(format.format(calendar.getTime()));
        tx_record.setUnit(Constant.COIN_UNIT_USDT);
        // tx_record.setMiner_fee(Double.valueOf(datas.get(i).getFee()));   //此接口没有费用，所以暂时不管。
        TransactionRecordManager.insertOrUpdate(tx_record);
        if (isLast) {
            EventMessage eventMessage = new EventMessage();
            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
            EventBus.getDefault().post(eventMessage);
        }
    }

    /**
     * 此处是钱包首页，刷新钱包余额，和币种余额的接口，和数据存储。
     *
     */

    @SuppressLint("CheckResult")
    public void getWalletBalance() {
        //通过币种的rpc接口信息，给钱包的总金额赋值。
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            allBtccCoinInfos = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_BTC);
            //先按照顺序，调取btc的余额。如果调取完成，或原数据为空，就走完成逻辑。
            if (allBtccCoinInfos != null && allBtccCoinInfos.size() > 0) {
                for (int i = 0; i < allBtccCoinInfos.size(); i++) {
                    emitter.onNext(i);
                    if (i == allBtccCoinInfos.size() - 1) {
                        emitter.onComplete();
                    }
                }
            } else {
                // emitter.onComplete();
                emitter.onNext(-1);
            }
        }).map(index -> {
            if (index >= 0) {
                getBtcUtxo(allBtccCoinInfos.get(index));
            }
            return "123";
        }).map((Function<String, String>) s -> {
            allEthcCoinInfos = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_ETH);
            if (allEthcCoinInfos != null && allEthcCoinInfos.size() > 0) {
                eth_quest_count = 0;
                for (int i = 0; i < allEthcCoinInfos.size(); i++) {
                    getEthBalance(allEthcCoinInfos.get(i));
                }
            }
            return "456";
        }).map(index -> {
            if (index != null) {
                allERC20cCoinInfos = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
                if (allERC20cCoinInfos != null && allERC20cCoinInfos.size() > 0) {
                    erc20_quest_count = 0;
                    for (int i = 0; i < allERC20cCoinInfos.size(); i++) {
                        getErc20Balance(allERC20cCoinInfos.get(i));
                    }
                }
            }
            return "789";
        }).delay(2, TimeUnit.SECONDS).doOnComplete(() -> {
            //当BTC调完后（但结果未必都返回了，所以要做2S延迟）完成的时候，我们做一个2S的延时，然后去循环调用usdt的余额。
            allusdtCoinInfos = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            if (allusdtCoinInfos != null && allusdtCoinInfos.size() > 0) {
                usdt_quest_count = 0;
                for (int i = 0; i < allusdtCoinInfos.size(); i++) {
                    // getUsdtBalance(usdtCoinInfo.get(i));
                    getBtcUtxo(allusdtCoinInfos.get(i));
                }
            }
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    //此处留空，方便后续拓展。
                });
    }

    @SuppressLint("CheckResult")
    private void getBtcUtxo(CoinInfo specCoinInfo) {
        if (specCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            coin_type_params = "btc";
        } else if (specCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            coin_type_params = "usdt";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getBtcAddressBalance(coin_type_params, specCoinInfo.getCoin_address(), "1/1", map).compose(RxHelper.pool_io())
                .subscribe(new BaseObserver<List<GetListUnspentBean>>(this) {
                    @Override
                    public void onSuccess(List<GetListUnspentBean> data, String msg) {
                        if (data != null && data.size() > 0) {
                            double xxx = Double.valueOf(data.get(0).getReceive()) + Double.valueOf(data.get(0).getSpend());
                            specCoinInfo.setCoin_totalAmount(Double.valueOf(data.get(0).getReceive()) + Double.valueOf(data.get(0).getSpend()));
                            CoinInfoManager.insertOrUpdate(specCoinInfo);

                            if (data.get(0).getNetwork().equals(Constant.COIN_UNIT_BTC)) {
                                btc_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWalletBalance(0);
                            } else if (data.get(0).getNetwork().equals(Constant.COIN_UNIT_USDT)) {
                                usdt_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWalletBalance(1);
                            }
                        } else {
                            if (coin_type_params != null && coin_type_params.equals("btc")) {
                                btc_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWalletBalance(0);
                            } else if (coin_type_params != null && coin_type_params.equals("usdt")) {
                                usdt_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWalletBalance(1);
                            }
                        }
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                        if (coin_type_params != null && coin_type_params.equals("btc")) {
                            btc_quest_count++;
                            //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                            updateWalletBalance(0);
                        } else if (coin_type_params != null && coin_type_params.equals("usdt")) {
                            usdt_quest_count++;
                            //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                            updateWalletBalance(1);
                        }
                    }
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });
    }

    private void getEthBalance(CoinInfo specCoinInfo) {
        String mAddress = specCoinInfo.getCoin_address().startsWith("0x") ? specCoinInfo.getCoin_address().toLowerCase() : Constants.HEX_PREFIX + specCoinInfo.getCoin_address().toLowerCase();
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getEthAddressBalance("eth", mAddress, map).compose(RxHelper.pool_io())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    public void onSuccess(String data, String msg) {
                        eth_quest_count++;
                        specCoinInfo.setCoin_totalAmount(Double.valueOf(data));
                        CoinInfoManager.insertOrUpdate(specCoinInfo);
                        updateWalletBalance(2);
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        EventMessage eventMessage = new EventMessage();
                        eventMessage.setType(EventMessage.HOME_WALLET_BALANCE_UPDATE);
                        EventBus.getDefault().post(eventMessage);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                });
    }

    private void getErc20Balance(CoinInfo specCoinInfo){
        String mAddress=specCoinInfo.getCoin_address().startsWith("0x")?specCoinInfo.getCoin_address().toLowerCase():Constants.HEX_PREFIX+specCoinInfo.getCoin_address().toLowerCase();
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getErc20AddressBalance("eth",mAddress, map).compose(RxHelper.pool_io())
                .subscribe(new BaseObserver<List<GetErc20BalanceBean>>(this) {
                    @Override
                    public void onSuccess(List<GetErc20BalanceBean> data, String msg) {
                        erc20_quest_count++;
                        double erc20_balance=0;
                        if (data!=null&&data.size()>0){
                            for (int i = 0; i <data.size() ; i++) {
                                if (data.get(i).getHash().equals(Constant.CONTRACT_ADDRESS)){
                                    erc20_balance=erc20_balance+Double.valueOf(data.get(i).getBalance())/Math.pow(10,Integer.valueOf(data.get(i).getTokenInfo().getD()));
                                }
                            }
                        }
                        specCoinInfo.setCoin_totalAmount(erc20_balance);
                        CoinInfoManager.insertOrUpdate(specCoinInfo);
                        updateWalletBalance(3);
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                        EventMessage eventMessage = new EventMessage();
                        eventMessage.setType(EventMessage.HOME_WALLET_BALANCE_UPDATE);
                        EventBus.getDefault().post(eventMessage);
                    }
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                });
    }

    private void updateWalletBalance(int coin_type) {
        if (coin_type == 0) {
            if (btc_quest_count >= allBtccCoinInfos.size()) {
                WalletInfo walletInfo = WalletInfoManager.getWalletFrId(Constant.TRANSACTION_COIN_NAME_BTC);
                List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(Constant.TRANSACTION_COIN_NAME_BTC);
                double wallet_balance = 0;
                for (int j = 0; j < coinFrWalletIds.size(); j++) {
                    wallet_balance = wallet_balance + coinFrWalletIds.get(j).getCoin_totalAmount();
                }
                walletInfo.setWalletBalance(wallet_balance);
                WalletInfoManager.insertOrUpdate(walletInfo);

                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.HOME_WALLET_BALANCE_UPDATE);
                EventBus.getDefault().post(eventMessage);
            }
        } else if (coin_type == 1) {
            if (usdt_quest_count >= allusdtCoinInfos.size()) {
                WalletInfo walletInfo = WalletInfoManager.getWalletFrId(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
                List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
                    double wallet_balance = 0;
                    for (int j = 0; j < coinFrWalletIds.size(); j++) {
                        wallet_balance = wallet_balance + coinFrWalletIds.get(j).getCoin_totalAmount();
                    }
                    walletInfo.setWalletBalance(wallet_balance);
                    WalletInfoManager.insertOrUpdate(walletInfo);

                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.HOME_WALLET_BALANCE_UPDATE);
                EventBus.getDefault().post(eventMessage);
            }
        } else if (coin_type == 2) {
            if (eth_quest_count >= allEthcCoinInfos.size()) {
                WalletInfo walletInfo = WalletInfoManager.getWalletFrId(Constant.TRANSACTION_COIN_NAME_ETH);
                List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(Constant.TRANSACTION_COIN_NAME_ETH);
                    double wallet_balance = 0;
                    for (int j = 0; j < coinFrWalletIds.size(); j++) {
                        wallet_balance = wallet_balance + coinFrWalletIds.get(j).getCoin_totalAmount();
                    }
                    walletInfo.setWalletBalance(wallet_balance);
                    WalletInfoManager.insertOrUpdate(walletInfo);

                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.HOME_WALLET_BALANCE_UPDATE);
                EventBus.getDefault().post(eventMessage);
            }
        } else if (coin_type == 3) {
            if (erc20_quest_count >= allERC20cCoinInfos.size()) {
                WalletInfo walletInfo = WalletInfoManager.getWalletFrId(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
                List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
                    double wallet_balance = 0;
                    for (int j = 0; j < coinFrWalletIds.size(); j++) {
                        wallet_balance = wallet_balance + coinFrWalletIds.get(j).getCoin_totalAmount();
                    }
                    walletInfo.setWalletBalance(wallet_balance);
                    WalletInfoManager.insertOrUpdate(walletInfo);

                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.HOME_WALLET_BALANCE_UPDATE);
                EventBus.getDefault().post(eventMessage);
            }
        }
    }
}
