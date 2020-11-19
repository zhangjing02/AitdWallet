package com.tianqi.aitdwallet.ui.activity.wallet.btc;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.bitcoin.BTCTransaction;
import com.quincysx.crypto.bitcoin.BitCoinECKeyPair;
import com.quincysx.crypto.bitcoin.BitcoinException;
import com.quincysx.crypto.utils.HexUtils;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.address.ContactsAddressManageActivity;
import com.tianqi.aitdwallet.ui.activity.tool.ScanActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionRecordActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.widget.dialog.BottomDialog;
import com.tianqi.aitdwallet.widget.dialog.ExplainTxMinerFeeDialog;
import com.tianqi.aitdwallet.widget.dialog.PaymentDialog;
import com.tianqi.aitdwallet.widget.dialog.SuccessDialog;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.TransactionRecordManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.HttpClientUtil;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetLoadingTxBean;
import com.tianqi.baselib.rxhttp.bean.GetSimpleRpcBean;
import com.tianqi.baselib.rxhttp.bean.GetUnspentTxBean;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.NetworkUtil;
import com.tianqi.baselib.utils.digital.AESCipher;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.display.ScreenUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author zhangjing
 * @date 2020/11/9
 * @description btc交易暂时使用类，这里加入了listunspent过滤逻辑，防止第三方服务没有及时清理listunspent交易，导致双花的问题。
 */

public class BitcoinTransactionActivity002 extends BaseActivity {

    private static final String TAG = "BitcoinWalletActivity";
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_payment_address)
    EditText etPaymentAddress;
    @BindView(R.id.et_payment_amount)
    EditText etPaymentAmount;
    @BindView(R.id.et_payment_remark)
    EditText etPaymentRemark;
    @BindView(R.id.et_payment_miner_fee)
    TextView etPaymentMinerFee;
    @BindView(R.id.tv_miner_fee_notice)
    TextView tvMinerFeeNotice;
    @BindView(R.id.tv_miner_fee_recommend)
    TextView tvMinerFeeRecommend;
    @BindView(R.id.seek_bar_miner_cost)
    CustomSeekBar seekBarMinerCost;
    @BindView(R.id.btn_transaction_send)
    TextView btnTransactionSend;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_transaction_request)
    ImageView tvTransactionRequest;
    @BindView(R.id.tv_transaction_arrive_time)
    TextView tvTransactionArriveTime;

    private List<GetUnspentTxBean.DataBean> utxo_list, utxo_for_pay;
    private double account_balance = 0;
    // private ECKey key;
    private double pay_amount;
    double miner_fee_single = 0.00001;//单个input的最佳手续费
    double miner_fee_total = 0.00001;//最终总和手续费

    double cushion_fee = 0.0000055;//留有余量防止粉尘交易。
    double total_value = 0;//用于交易的未交易总余额
    private ECKeyPair master = null;
    private WalletInfo walletInfo;
    private CoinInfo walletBtcFrAddress;
    private Dialog mLoadDialog;
    private List<GetLoadingTxBean>unLoading_unspend;
    private int vin_id;
    private  List<TransactionRecord>transactionRecords;

    @Override
    protected int getContentView() {
        return R.layout.activity_bitcoin_transaction;
    }

    @Override
    protected void initView() {
        getToolBar();
        //普通进度条
        seekBarMinerCost.setEnabled(true);
        seekBarMinerCost.setMax(100);
        seekBarMinerCost.setProgress(15);
        walletInfo = WalletInfoManager.getHdWalletInfo();
        setMinerFeeText(miner_fee_single);

        seekBarMinerCost.setOnChangeListener(new CustomSeekBar.OnChangeListener() {
            @Override
            public void onProgressChanged(CustomSeekBar seekBar) {
                int select_miner = 300 + (5000 - 300) * seekBar.getProgress() / 100;
                LogUtil.i(TAG, seekBar.getProgress()+"onProgressChanged: 我们计算的费用是？"+select_miner);
                miner_fee_single = select_miner / 100000000f;
                setMinerFeeText(miner_fee_single);
            }
            @Override
            public void onTrackingTouchFinish(CustomSeekBar seekBar) {
            }
        });
    }

    private void setMinerFeeText(double miner) {
        double miner_fee_show = miner * 100000000;
        UserInformation userInformation = UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
//            String miner_fee_str = DataReshape.double2int(miner_fee_show, 0) +
//                    "sat" + " ≈$" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_USDPrice() / 100000000, 2);
//        SpannableString spannableString = new SpannableString(miner_fee_str);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
//        spannableString.setSpan(colorSpan, spannableString.toString().indexOf("sat") + 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            String miner_fee_str02 = DataReshape.doubleBig(miner, 8) +
                    "BTC" + " ≈$" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_USDPrice() / 100000000, 2);
            etPaymentMinerFee.setText(miner_fee_str02);
        } else {
//            String miner_fee_str = DataReshape.double2int(miner_fee_show, 0) +
//                    "sat" + " ≈￥" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_CNYPrice() / 100000000, 2);
            String miner_fee_str02 = DataReshape.doubleBig(miner, 8) +
                    "BTC" + " ≈￥" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_CNYPrice() / 100000000, 2);
//        SpannableString spannableString = new SpannableString(miner_fee_str);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
//        spannableString.setSpan(colorSpan, spannableString.toString().indexOf("sat") + 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            etPaymentMinerFee.setText(miner_fee_str02);
        }
    }

    @Override
    protected void initData() {
        initWallet();
        etPaymentAddress.addTextChangedListener(textWatcher);
        etPaymentAmount.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            LogUtil.i(TAG, "afterTextChanged: 001我们收到了啥？");
            if (!TextUtils.isEmpty(etPaymentAddress.getText().toString()) && !TextUtils.isEmpty(etPaymentAmount.getText().toString())) {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
            } else {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
            }
        }
    };

    private void initWallet() {
        String coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        walletBtcFrAddress = CoinInfoManager.getCoinFrAddress(Constant.TRANSACTION_COIN_NAME_BTC, coin_address);
        tvBalance.setText(DataReshape.doubleBig(walletBtcFrAddress.getCoin_totalAmount(), 8));
        transactionRecords =TransactionRecordManager.getTxFrAddress(walletBtcFrAddress.getCoin_address());

// TODO: 2020/10/7 此处应该用列表里面穿过来的私钥去生成三件套，去进行转账。
        try {
            master = BitCoinECKeyPair.parseWIF(walletBtcFrAddress.getPrivateKey());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
//        RetrofitFactory.getInstence(this).API()
//                .getLoadingTx("btc",master.getAddress(),map).compose(RxHelper.io_main())
//                .subscribe(new BaseObserver<List<GetLoadingTxBean>>(this) {
//                    @Override
//                    public void onSuccess(List<GetLoadingTxBean> data, String msg) {
//                        Log.i(TAG, master.getAddress()+"----onSuccess: 这里获取的是正在确认的交易" + data.size());
//                        unLoading_unspend=data;
//
//                    }
//                    @Override
//                    protected void onFailure(int code, String msg) {
//                    }
//                });
        if (NetworkUtil.isNetworkAvailable(this)){
            getBtcUtxo();
        }else {
            ToastUtil.showToast(this,getString(R.string.network_error));
        }
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.btc_transaction_tittle);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnCollect.setVisibility(View.VISIBLE);
        btnCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_scan));
    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.SCAN_EVENT) {
            etPaymentAddress.setText(event.getMsg());
            etPaymentAddress.setSelection(event.getMsg().length());
        }else if (event.getType()==EventMessage.SELECT_ADDRESS_UPDATE){
            etPaymentAddress.setText(event.getMsg());
            etPaymentAddress.setSelection(event.getMsg().length());
        }
    }

    @SuppressLint("CheckResult")
    private void getBtcUtxo() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
//        RetrofitFactory.getInstence(this).API()
//                .getUtxoForTx(master.getAddress(),"1/50").compose(RxHelper.io_main())
//                .subscribe(new BaseObserver<List<GetFormalUtxoBean>>(this) {
//                    @Override
//                    public void onSuccess(List<GetFormalUtxoBean> data, String msg) {
//                        Log.i(TAG, "onSuccess: 我们看获取的未交易是？"+data.get(0).toString());
//                        utxo_list=new ArrayList<>();
//                        for (GetFormalUtxoBean result : data) {
//                            account_balance = account_balance + Double.valueOf(result.getValue());
//                            if (Double.valueOf(result.getValue())>0){
//                                utxo_list.add(result);
//                            }
//                        }
//                        tvBalance.setText(DataReshape.doubleBig(account_balance, 8)+"");
//                    }
//                    @Override
//                    protected void onFailure(int code, String msg) {
//                    }
//                });

        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext(walletBtcFrAddress.getCoin_address());
        }).map(response -> {
            //计算出可用的utxo交易块,然后通过请求创建交易的hex。
            Response formalUtxoJson = HttpClientUtil.getInstance().getFormalUtxoJson("http://www.tokenview.com:8088/tqunspent/btc/"+response+"/1/50");
            if (formalUtxoJson.isSuccessful()) {
                ResponseBody body = formalUtxoJson.body();
                if (body != null) {
                    //return gson.fromJson(formalUtxoJson.body().string(), GetUnspentTxBean.class);
                    return formalUtxoJson.body().string();
                }
            }
            return Constant.HTTP_ERROR;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    if (baseEntity!=null&&!baseEntity.equals(Constant.HTTP_ERROR)){
                        Gson gson = new Gson();
                        GetUnspentTxBean getUnspentTxBean=gson.fromJson(baseEntity, GetUnspentTxBean.class);
                        utxo_list=new ArrayList<>();
                        if (getUnspentTxBean!=null&&getUnspentTxBean.getData()!=null){
                            for (GetUnspentTxBean.DataBean result : getUnspentTxBean.getData()) {
                                account_balance = account_balance + Double.valueOf(result.getValue());
                                if (Double.valueOf(result.getValue())>0){
                                    utxo_list.add(result);
                                }
                            }
                            if (transactionRecords!=null&&transactionRecords.size()>0&&utxo_list!=null&&utxo_list.size()>0){
                                for (int i = 0; i <transactionRecords.size() ; i++) {
                                    for (int j = 0; j <utxo_list.size() ; j++) {
                                        if (transactionRecords.get(i).getInput_id()!=null){
                                            if (transactionRecords.get(i).getInput_id().contains(utxo_list.get(j).getTxid())){
                                                //  Log.i(TAG, utxo_list.get(j).getTxid()+"-----getBtcUtxo: 我们记录的inpu_id"+transactionRecords.get(i).getInput_id());
                                                utxo_list.remove(j);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // Log.i(TAG, utxo_list.size()+"getBtcUtxo: 我们看赛选出来的结果是？");
                    }else {
                        ToastUtil.showToast(this,getString(R.string.notice_unspent_data_error));
                    }
                    // Log.i(TAG, utxo_list.size()+"getBtcUtxo: 我们看赛选出来的结果是？"+utxo_list.get(0).getTxid());
                    tvBalance.setText(DataReshape.doubleBig(account_balance, 8)+"");
                });

    }

    @SuppressLint("CheckResult")
    private void createTxToBroadcastApi() {
        Observable.create((ObservableOnSubscribe<List<GetUnspentTxBean.DataBean>>) emitter -> {
            //计算出可用的utxo交易块,然后通过请求创建交易的hex。
            List<GetUnspentTxBean.DataBean> list = getUtxoCountForPay();
            emitter.onNext(list);
        }).map(resultBeans -> {
            //把得到的未交易数据，去请求创建交易。得到本次交易的hex。
            return createTransaction(resultBeans);
        }).map(response -> {
            //把交易的hex，做签名处理。
            if (response != null) {
                //把得到的签名hex，去掉接口广播出去。
               // Response tx_response = HttpClientUtil.getInstance().postBtcJson(makeBroadcastTxParams(response));
                LogUtil.i(TAG, "createTxToBroadcastApi: 看看签名如何？"+response);
                Response tx_response = HttpClientUtil.getInstance().postFormalBtcJson(makeBroadcastTxParams002(response));
                if (tx_response != null) {
                    if (tx_response.isSuccessful()) {
                        ResponseBody body = tx_response.body();
                        if (body != null) {
                            return body.string();
                        }
                    }
                }
            }
            return Constant.HTTP_ERROR;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    if (mLoadDialog != null) {
                        mLoadDialog.dismiss();
                    }
                    if (baseEntity != null && !baseEntity.equals(Constant.HTTP_ERROR)) {
                        LogUtil.i(TAG, "createTxToBroadcastApi: 我们看交易成功后的id是？"+baseEntity);
                        ScreenUtils.hideKeyboard(this);
                        Gson gson = new Gson();
                        GetSimpleRpcBean getCreateTransactionBean = gson.fromJson(baseEntity, GetSimpleRpcBean.class);
                        if (getCreateTransactionBean.getResult()!=null){
                            // TODO: 2020/9/23  把交易详情存入数据库中。
                            TransactionRecord tx_record = new TransactionRecord();
                            tx_record.setAddress(walletBtcFrAddress.getCoin_address());
                            tx_record.setAmount(Double.valueOf(etPaymentAmount.getText().toString()));

                            //  tx_record.setId(0);
                            tx_record.setCoin_type(Constant.TRANSACTION_COIN_BTC);//0代表比特币。
                            tx_record.setStatus(Constant.TRANSACTION_STATE_WAITING);
                            tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);//0转账，1收款
                            tx_record.setCoin_id(walletBtcFrAddress.getCoin_id());
                            if (!TextUtils.isEmpty(etPaymentRemark.getText().toString())) {
                                tx_record.setRemark(etPaymentRemark.getText().toString());
                            }
                            tx_record.setVout_id(vin_id);
                            tx_record.setTxid(getCreateTransactionBean.getResult());
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                            tx_record.setTimeStr(format.format(calendar.getTime()));
                            String use_listunspent_str=null;
                            for (int i = 0; i <utxo_for_pay.size() ; i++) {
                                use_listunspent_str=utxo_for_pay.get(i).getTxid()+"&&"+use_listunspent_str;
                            }
                            tx_record.setInput_id(use_listunspent_str);   //保存一个已使用过的，listunspent.防止后续再使用它，

                            tx_record.setUnit(Constant.TRANSACTION_COIN_NAME_BTC);
                            TransactionRecordManager.insertOrUpdate(tx_record);
                            EventMessage message = new EventMessage();
                            message.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                            EventBus.getDefault().post(message);

                            SuccessDialog successDialog = new SuccessDialog(this);
                            successDialog.show();

                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(this, TransactionRecordActivity.class);
                                LogUtil.i(TAG, walletBtcFrAddress.getCoin_name() + "createTxToBroadcastApi: 传过去的是啥？" + walletBtcFrAddress.getCoin_id());
                                intent.putExtra(Constants.TRANSACTION_COIN_NAME, walletBtcFrAddress.getCoin_name());
                                intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, walletBtcFrAddress.getCoin_address());
                                intent.putExtra(Constants.TRANSACTION_COIN_ID, walletBtcFrAddress.getCoin_id());
                                startActivity(intent);
                                finish();
                            }, 2000);
                        }else {
                            ToastUtil.showToast(this, getString(R.string.notice_transaction_error));
                        }
                    } else {
                        ToastUtil.showToast(this, getString(R.string.notice_transaction_error));
                    }
                });
    }

    private String createTransaction(List<GetUnspentTxBean.DataBean> utxo_for_pay_list) {
        try {
            BTCTransaction.Input[] inputs = new BTCTransaction.Input[utxo_for_pay_list.size()];
            for (int i = 0; i < utxo_for_pay_list.size(); i++) {
                BTCTransaction.OutPoint outPoint = new BTCTransaction.OutPoint(HexUtils.fromHex(utxo_for_pay_list.get(i).getTxid()), utxo_for_pay_list.get(i).getOutput_no());
                BTCTransaction.Script script002 = BTCTransaction.Script.buildOutput(master.getAddress());  //自己的地址
                BTCTransaction.Input input = new BTCTransaction.Input(outPoint, script002, 0);
                inputs[i] = input;
            }

            //如果剩余的金额大于550聪，就加入找零的输出，输出的地址是自己。
            BTCTransaction.Output[] outputs;
            double change_value = total_value - pay_amount - miner_fee_total;
            LogUtil.i(TAG, total_value + "-----createTransaction: 001我们看自己的找零是多少？" + change_value);
            LogUtil.i(TAG, pay_amount + "-----createTransaction: 002我们看自己的找零是多少？" + miner_fee_total);
            if (change_value > cushion_fee) {
                outputs = new BTCTransaction.Output[2];
                BTCTransaction.Script script = BTCTransaction.Script.buildOutput(etPaymentAddress.getText().toString());  //对方的地址
                long trans_value = (long) (Double.valueOf(etPaymentAmount.getText().toString()) * 100000000l);
                BTCTransaction.Output output = new BTCTransaction.Output(trans_value, script);
                outputs[0] = output;
                BTCTransaction.Script script_change = BTCTransaction.Script.buildOutput(master.getAddress());  //找零地址，即自己的地址
                long change_value_l = (long) (change_value * 100000000l);
                BTCTransaction.Output output_change = new BTCTransaction.Output(change_value_l, script_change);
                outputs[1] = output_change;
            } else {
                outputs = new BTCTransaction.Output[1];
                BTCTransaction.Script script = BTCTransaction.Script.buildOutput(etPaymentAddress.getText().toString());  //对方的地址
                long trans_value = (long) (Double.valueOf(etPaymentAmount.getText().toString()) * 100000000l);
                BTCTransaction.Output output = new BTCTransaction.Output(trans_value, script);
                outputs[0] = output;
            }

            BTCTransaction unsignedTransaction = new BTCTransaction(inputs, outputs, 0);
            byte[] sign = unsignedTransaction.sign(master);   //签名。
            String toHex = HexUtils.toHex(sign);              //把签名转成hex
            return toHex;
        } catch (BitcoinException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @OnClick({R.id.btn_collect, R.id.btn_transaction_send, R.id.btn_balance_all, R.id.tv_transaction_request,R.id.iv_receive_address_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent, 25);
                break;
            case R.id.btn_balance_all:
                // TODO: 2020/10/27 把余额都添加进去。
                if (account_balance > 0.000001) {
                    etPaymentAmount.setText(DataReshape.doubleBig((account_balance - 0.000001), 8));
                }
                break;
            case R.id.tv_transaction_request:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                ExplainTxMinerFeeDialog shotNoticeDialog = new ExplainTxMinerFeeDialog(this, R.style.MyDialog2);
                shotNoticeDialog.show();
                break;
            case R.id.iv_receive_address_account:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent1=new Intent(this, ContactsAddressManageActivity.class);
                intent1.putExtra(Constants.INTENT_PUT_TAG,Constants.INTENT_PUT_TRANSACTION);
                startActivity(intent1);
                break;
            case R.id.btn_transaction_send://开始转账
                if (NetworkUtil.isNetworkAvailable(this)){
                    if (utxo_list!=null&&utxo_list.size()>0){
                        if (judgeSelectInput()) {
                            String fiat_value;
                            UserInformation userInformation = UserInfoManager.getUserInfo();
                            if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
                                fiat_value = DataReshape.doubleBig(miner_fee_total, 8) + " ≈$"
                                        + DataReshape.doubleBig(miner_fee_total * walletInfo.getCoin_USDPrice(), 2);
                            } else {
                                fiat_value = DataReshape.doubleBig(miner_fee_total, 8) + " ≈￥"
                                        + DataReshape.doubleBig(miner_fee_total * walletInfo.getCoin_CNYPrice(), 2);
                            }

                            BottomDialog bottomDialog = new BottomDialog(this, etPaymentAmount.getText().toString(),
                                    etPaymentAddress.getText().toString(), fiat_value, walletBtcFrAddress.getCoin_address());

                            bottomDialog.show();
                            bottomDialog.setDialogFirstClickListener(() -> {
                                //2.调取创建交易的api，生成我们的输出hex_code码：
                                bottomDialog.dismiss();
                                PaymentDialog paymentDialog = new PaymentDialog(this, R.style.MyDialogInput);
                                paymentDialog.show();
                                paymentDialog.setOnDialogClickListener((view1, password, type) -> {
                                    //验证密码是否正确。
                                    bottomDialog.dismiss();
                                    UserInformation userInfo = UserInfoManager.getUserInfo();
                                    userInfo.getPasswordStr();
                                    String aes_decode_str = null;
                                    try {
                                        aes_decode_str = AESCipher.decrypt(Constant.PSD_KEY,password);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (password != null && userInfo.getPasswordStr().equals(aes_decode_str)) {
                                        mLoadDialog = LoadingDialogUtils.createLoadingDialog(this, "");
                                        createTxToBroadcastApi();
                                    } else {
                                        ToastUtil.showToast(this, getString(R.string.notice_psd_error));
                                    }
                                });
                            });
                        }
                    }else {
                        ToastUtil.showToast(this,getString(R.string.notice_unspent_not_enough));
                    }
                }else {
                    ToastUtil.showToast(this,getString(R.string.network_error));
                }
                break;
        }
    }

    private List<GetUnspentTxBean.DataBean> getUtxoCountForPay() {
        pay_amount = Double.valueOf(etPaymentAmount.getText().toString());
        //1.首先计算出用于支付的utxo需要几个。
        double amount_value = 0;
        utxo_for_pay = new ArrayList<>();
        double R_miner_fee = 0;
        if (utxo_list!=null&&utxo_list.size()>0){
            for (int i = utxo_list.size()-1; i >=0; i--) {
                miner_fee_total = miner_fee_single * (i + 1);
                if (pay_amount + miner_fee_total < amount_value) {//如果支付金额+交易费小于拼装的utxo，就可以进行支付了，此时可以跳出循环。
                    break;
                } else {
                    amount_value = amount_value + Double.valueOf(utxo_list.get(i).getValue());
                    utxo_for_pay.add(utxo_list.get(i));
                }
            }
            total_value = amount_value;
        }
        return utxo_for_pay;
    }

    private String makeBroadcastTxParams002(String transaction_hex) {
        Map<String, Object> listunspentParams = new HashMap<>();
        listunspentParams.put("jsonrpc", "2.0");
        listunspentParams.put("id", "viewtoken");
        listunspentParams.put("method", "sendrawtransaction");
        List<Object> params = new ArrayList<>();

        params.add(transaction_hex);//交易签名数组
        // params.add(0);//交易签名数组
        listunspentParams.put("params", params);

        LogUtil.i(TAG, "makeOutputApiParams: 002我们拼接的请求体是？" + new Gson().toJson(listunspentParams));
        return new Gson().toJson(listunspentParams);
    }

    /**
     * @return 判断输入是否合法
     */
    private boolean judgeSelectInput() {
        if (TextUtils.isEmpty(etPaymentAddress.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.input_receive_address_notice));
            return false;
        } else if (TextUtils.isEmpty(etPaymentAmount.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.input_transaction_amount));
            return false;
        }else if (etPaymentAddress.getText().toString().equals(master.getAddress())){
            ToastUtil.showToast(this, getString(R.string.notice_trans_to_me_refuse));
            return false;
        }else if (Double.valueOf(etPaymentAmount.getText().toString())>=0.00000001){
            ToastUtil.showToast(this, getString(R.string.notice_amount_too_little));
            return false;
        }
        return true;
    }

}
