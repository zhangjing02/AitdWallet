package com.tianqi.aitdwallet.ui.activity.wallet.record;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.TransRecordAdapter;
import com.tianqi.aitdwallet.ui.activity.wallet.btc.BitcoinTransactionActivity002;
import com.tianqi.aitdwallet.ui.activity.wallet.eth.EthTransactionActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.property.CoinAddressQrActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.usdt.UsdtErc20TransactionActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.usdt.UsdtTransactionActivity002;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.TransactionRecordManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetErc20TxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetEthTxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetListUnspentBean;
import com.tianqi.baselib.rxhttp.bean.GetLoadingTxBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

/**
 * @author zhangjing
 * @date 2020/11/9
 * @description  交易记录的类，此处还缺少分页加载的逻辑。
 */

/**
 * @author zhangjing
 * @date 2020/11/9
 * @description  交易记录的类，此处还缺少分页加载的逻辑。
 */

public class TransactionRecordActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout_out)
    TabLayout tablayoutOut;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
//    @BindView(R.id.iv_wallet_coin)
//    ImageView ivWalletCoin;
//    @BindView(R.id.tv_currency_balance)
//    TextView tvCurrencyBalance;
//    @BindView(R.id.tv_fiat_balance)
//    TextView tvFiatBalance;
//    @BindView(R.id.tablayout)
//    TabLayout tablayout;
//    @BindView(R.id.viewpager)
//    NoScrollViewPager viewpager;
//    @BindView(R.id.btn_transaction_send)
//    TextView btnTransactionSend;
//    @BindView(R.id.btn_transaction_receive)
//    TextView btnTransactionReceive;

    private String[] titles;
    List<Fragment> fragments;
    @BindView(R.id.rcv_transaction_record)
    RecyclerView rcvTransactionRecord;

    private String coin_tittle, coin_id;
    private String coin_address;
    private WalletInfo walletInfo;
    private CoinInfo walletBtcInfo;
    private UserInformation userInformation;
    private boolean isHttpErro;
    private double btc_account_balance;
    private View record_header, record_header02, record_empty;
    private TransRecordAdapter recordAdapter;
    private List<TransactionRecord> mMessageBeans, mMessageBeans02;
    private int select_index;
    private TabLayout tablayout;
    private LinearLayoutManager mLayoutManager;

    private TextView btnTransactionSend, btnTransactionReceive;

    @Override
    protected int getContentView() {
        return R.layout.activity_transcation_record002;
    }

    @Override
    protected void initView() {
        getToolBar();
        coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        coin_id = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ID);
        walletBtcInfo = CoinInfoManager.getCoinFrAddress(coin_tittle, coin_address);
        walletInfo = WalletInfoManager.getWalletFrName(walletBtcInfo.getCoin_name());
        Log.i("ttttttttttttt", "initView: 我们看以太坊的私钥是？"+walletBtcInfo.getPrivateKey());

        mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rcvTransactionRecord.setLayoutManager(mLayoutManager);

        recordAdapter = new TransRecordAdapter(R.layout.adapter_transaction_record, mMessageBeans);
        rcvTransactionRecord.setAdapter(recordAdapter);

        record_header = getLayoutInflater().inflate(R.layout.activity_transcation_record_header, (ViewGroup) rcvTransactionRecord.getParent(), false);
        record_header02 = getLayoutInflater().inflate(R.layout.activity_transcation_record_header02, (ViewGroup) rcvTransactionRecord.getParent(), false);
        record_empty = getLayoutInflater().inflate(R.layout.adapter_empty_view, (ViewGroup) rcvTransactionRecord.getParent(), false);
        ImageView iv_no_data=record_empty.findViewById(R.id.iv_no_data);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_no_data.getLayoutParams();

        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        if (height<=1920){
            layoutParams.setMargins(0,40,0,0);
            iv_no_data.setLayoutParams(layoutParams);
        }
        recordAdapter.addHeaderView(record_header);
        recordAdapter.addHeaderView(record_header02);

        if (mMessageBeans.size()<=0){
            recordAdapter.addHeaderView(record_empty);
        }
        ImageView ivWalletCoin = record_header.findViewById(R.id.iv_wallet_coin);
        TextView tvCurrencyBalance = record_header.findViewById(R.id.tv_currency_balance);
        TextView tvFiatBalance = record_header.findViewById(R.id.tv_fiat_balance);
        btnTransactionSend = record_header.findViewById(R.id.btn_transaction_send);
        btnTransactionReceive = record_header.findViewById(R.id.btn_transaction_receive);
        View lineMiddle = record_header.findViewById(R.id.line_middle);
        tablayout = record_header02.findViewById(R.id.tablayout);
        titles = new String[]{getString(R.string.titttle_record_all), getString(R.string.tittle_record_send), getString(R.string.tittle_record_receive), getString(R.string.tittle_record_failure)};

        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
            tablayout.getTabAt(i).setText(titles[i]);
            tablayoutOut.addTab(tablayoutOut.newTab());
            tablayoutOut.getTabAt(i).setText(titles[i]);
        }

        tablayout.addOnTabSelectedListener(onTabSelectedListener);
        tablayoutOut.addOnTabSelectedListener(onTabSelectedListener);

        GlideUtils.loadResourceImage(this, walletBtcInfo.getResourceId(), ivWalletCoin);

        if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 8 ));
        } else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 4));
        }else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 6));
        }else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)){
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 6));
        }
        UserInformation userInformation = UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
            tvFiatBalance.setText("≈$" + DataReshape.doubleBig(walletBtcInfo.getCoin_totalAmount() * walletInfo.getCoin_USDPrice(), 2));
        } else {
            tvFiatBalance.setText("≈￥" + DataReshape.doubleBig(walletBtcInfo.getCoin_totalAmount() * walletInfo.getCoin_CNYPrice(), 2));
        }
        setListener();
        //刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            initData();
        });
    }

    private void getToolBar() {
        coin_tittle = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);
        toolbarTitle.setText(coin_tittle);

        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    TabLayout.BaseOnTabSelectedListener onTabSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            select_index = tab.getPosition();
            //  select_tittle = tab.getText().toString();
            switch (select_index) {
                case 1:
                    mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 2:
                    mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 3:
                    mMessageBeans = TransactionRecordManager.getWalletBtcFail(coin_address, coin_id);
                    break;
                case 0:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                default:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                    break;
            }

            tablayout.getTabAt(select_index).select();
            tablayoutOut.getTabAt(select_index).select();
            moveToPosition(0);
            // refreshLayout.autoRefresh();
            // rcvTransactionRecord.setAdapter(recordAdapter);
            //   fdfdfdfd
            recordAdapter.setNewData(mMessageBeans);

            if (mMessageBeans.size() <= 0) {
                recordAdapter.removeAllHeaderView();
                recordAdapter.addHeaderView(record_header);
                recordAdapter.addHeaderView(record_header02);
                recordAdapter.addHeaderView(record_empty);
            } else {
                recordAdapter.removeHeaderView(record_empty);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };


    private void moveToPosition(int position) {
        if (position != -1) {
            rcvTransactionRecord.scrollToPosition(position);
            LinearLayoutManager mLayoutManager =
                    (LinearLayoutManager) rcvTransactionRecord.getLayoutManager();
            tablayoutOut.setVisibility(View.GONE);
            mLayoutManager.scrollToPositionWithOffset(position, 00);
        }
    }

    @Override
    protected void initData() {
         if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_ETH)){
            initEthTxRecord(walletBtcInfo);
        }else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)||walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)){
             initBtcTxRecord(walletBtcInfo);
             initBtcLoadingRecord(walletBtcInfo);
         }else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)){
             initErc20TxRecore(walletBtcInfo);
         }
    }

    private void initErc20TxRecore(CoinInfo coinInfo) {
        String mAddress=coinInfo.getCoin_address().startsWith("0x")?coinInfo.getCoin_address().toLowerCase():Constants.HEX_PREFIX+coinInfo.getCoin_address().toLowerCase();
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getErc20TxRecord( mAddress, Constant.CONTRACT_ADDRESS,"1/50",map).compose(RxHelper.io_io())
                .subscribe(new BaseObserver<List<GetErc20TxRecordBean>>(this) {
                    @Override
                    public void onSuccess(List<GetErc20TxRecordBean> datas, String msg) {
                        if (datas!=null&&datas.size()>0){
                            for (int i = 0; i <datas.size() ; i++) {
                                inserErc20TxRecord(datas, i, coinInfo,i==datas.size()-1);
                            }
                        }
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void inserErc20TxRecord(List<GetErc20TxRecordBean> datas, int i, CoinInfo coinInfo,boolean isLast) {
        //创建（转出）数据库交易，存入数据库
        TransactionRecord tx_record = new TransactionRecord();
        tx_record.setAddress(coinInfo.getCoin_address());
        tx_record.setAmount(Double.valueOf(datas.get(i).getValue())/Math.pow(10,Integer.valueOf(datas.get(i).getTokenInfo().getD())));
        tx_record.setCoin_type(Constant.TRANSACTION_COIN_ETH);//0代表比特币。
        tx_record.setStatus(Constant.TRANSACTION_STATE_SUCCESS);
        if (datas.get(i).getFrom().equals(coinInfo.getCoin_address().toLowerCase())){  //如果是自己的地址，证明是转出。
            tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);
            tx_record.setTargetAddress(datas.get(i).getTo());
        }else {
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
        if (isLast){
            refreshLayout.finishRefresh();
            EventMessage eventMessage = new EventMessage();
            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
            EventBus.getDefault().post(eventMessage);
        }
    }

    private void initEthTxRecord(CoinInfo coinInfo) {
        String mAddress=coinInfo.getCoin_address().startsWith("0x")?coinInfo.getCoin_address().toLowerCase():Constants.HEX_PREFIX+coinInfo.getCoin_address().toLowerCase();
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getEthTxRecord("eth", mAddress, map).compose(RxHelper.io_io())
                .subscribe(new BaseObserver<GetEthTxRecordBean>(this) {
                    @Override
                    public void onSuccess(GetEthTxRecordBean data, String msg) {
                        if (data != null && data.getTxs()!=null&&data.getTxs().size() > 0) {
                            btc_account_balance = Double.valueOf(data.getSpend()) + Double.valueOf(data.getReceive());
                            coinInfo.setCoin_totalAmount(btc_account_balance);
                            CoinInfoManager.insertOrUpdate(coinInfo);
                            for (int i = 0; i < data.getTxs().size(); i++) {
                                if (Double.valueOf(data.getTxs().get(i).getValue())>0){
                                    insertEthTxRecord(data.getTxs().get(i),coinInfo,i>=data.getTxs().size()-1);
                                }
                            }
                        }
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                        refreshLayout.finishRefresh();
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
        if (txsBean.getFrom().equals(eth_coinInfo.getCoin_address().toLowerCase())){  //如果是自己的地址，证明是转出。
            tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);
            tx_record.setTargetAddress(txsBean.getTo());
        }else {
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
        if (isLast){
            refreshLayout.finishRefresh();
            EventMessage eventMessage = new EventMessage();
            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
            EventBus.getDefault().post(eventMessage);
        }
    }

    private void initBtcTxRecord(CoinInfo coinInfo) {
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
                    .getBtcAddressBalance(coin_type_params, coinInfo.getCoin_address(), "1/50", map).compose(RxHelper.io_io())
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
                            }
                        }

                        @Override
                        protected void onFailure(int code, String msg) {
                            refreshLayout.finishRefresh();
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
        if (type == Constant.TRANSACTION_TYPE_SEND&&spend_value==0){
            if (data.get(i).getTxs().get(j).getOutputs().size()>0){
                spend_value=Double.valueOf(data.get(i).getTxs().get(j).getOutputs().get(0).getValue());
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
            refreshLayout.finishRefresh();
        }
    }


    private void initBtcLoadingRecord(CoinInfo coinInfo) {
        String coin_type_params = null;
        if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            coin_type_params = "btc";
        } else if (coinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            coin_type_params = "usdt";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getLoadingTx(coin_type_params, walletBtcInfo.getCoin_address(), map).compose(RxHelper.io_main())
                .subscribe(new BaseObserver<List<GetLoadingTxBean>>(this) {
                    @Override
                    public void onSuccess(List<GetLoadingTxBean> data, String msg) {
                        if (data != null && data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                for (int j = 0; j < data.get(i).getInputs().size(); j++) {
                                    if (data.get(i).getInputs().get(j).getAddress().equals(walletBtcInfo.getCoin_address())) {
                                        //如果input是自己的，就是转出。所以可以拿output（不是自己地址的output）当成本次转账的金额。
                                        for (int k = 0; k < data.get(i).getOutputs().size(); k++) {
                                            if (!data.get(i).getOutputs().get(k).getAddress().equals(walletBtcInfo.getCoin_address())) {
                                                insertLoadingTx(data, i, k, Constant.TRANSACTION_TYPE_SEND);
                                            }
                                        }
                                    } else {
                                        //input地址不是自己的，那就是收到的钱。
                                        for (int k = 0; k < data.get(i).getOutputs().size(); k++) {
                                            //output地址是自己的，那就是收到的钱多少。
                                            if (data.get(i).getOutputs().get(k).getAddress().equals(walletBtcInfo.getCoin_address())) {
                                                insertLoadingTx(data, i, k, Constant.TRANSACTION_TYPE_RECEIVE);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void insertLoadingTx(List<GetLoadingTxBean> data, int i, int k, int transactionTypeReceive) {
        double receive_value;
        receive_value = Double.valueOf(data.get(i).getOutputs().get(k).getValue());
        //创建（转出）数据库交易，存入数据库
        TransactionRecord tx_record = new TransactionRecord();
        tx_record.setAddress(walletBtcInfo.getCoin_address());
        tx_record.setTargetAddress(data.get(i).getOutputs().get(k).getAddress());
        tx_record.setAmount(receive_value);
        tx_record.setCoin_type(Constant.TRANSACTION_COIN_BTC);//0代表比特币。
        tx_record.setStatus(Constant.TRANSACTION_STATE_WAITING);
        tx_record.setTransType(transactionTypeReceive);//0转账，1收款
        tx_record.setCoin_id(walletBtcInfo.getCoin_id());
        tx_record.setVout_id(0);

        tx_record.setTxid(data.get(i).getTxid());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data.get(i).getTime() * 1000l);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        tx_record.setTimeStr(format.format(calendar.getTime()));
        if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            tx_record.setUnit(Constant.COIN_UNIT_BTC);
        } else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            tx_record.setUnit(Constant.COIN_UNIT_USDT);
        }
        TransactionRecordManager.insertOrUpdate(tx_record);

        if (i == data.size() - 1 && k == data.get(i).getOutputs().size() - 1) {
            EventMessage eventMessage = new EventMessage();
            eventMessage.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
            EventBus.getDefault().post(eventMessage);
        }
    }

    /**
     * 设置RecyclerView的滚动监听
     */
    private int mSuspensionHeight, mCurrentFirstVisibleIndex;

    private void setListener() {
        rcvTransactionRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取头部View的高度
                mSuspensionHeight = tablayout.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurrentFirstVisibleIndex != mLayoutManager.findFirstVisibleItemPosition()) {
                    // 假如屏幕中显示的第一个条目索引与当前获取到的不一致, 就更新第一条目索引值
                    mCurrentFirstVisibleIndex = mLayoutManager.findFirstVisibleItemPosition();
                }
                // 找到当前屏幕中显示的第二条目
                View view = mLayoutManager.findViewByPosition(mCurrentFirstVisibleIndex + 1);
                if (view != null) {
                    if (view.getTop() <= mSuspensionHeight) {
                        tablayoutOut.setVisibility(View.VISIBLE);
                        // 当前屏幕中第二Item离控件顶部距离如果小于悬挂条高度.
                        // 则悬挂条随着RecyclerView的滚动移动, 这个悬挂条一直遮挡着Item条目的头.
                        //tablayout.setY(-(mSuspensionHeight - view.getTop()));
                        if (mCurrentFirstVisibleIndex < 1) {
                            tablayoutOut.setVisibility(View.GONE);
                        }
                    } else {
                    }
                }
                //Log.e(TAG, "dy  " + dy);
            }
        });

        btnTransactionSend.setOnClickListener(view -> {
            switch (coin_tittle) {
                case Constant.TRANSACTION_COIN_NAME_BTC:
                    // Intent intent=new Intent(this, BitcoinTransactionNativeActivity.class);
                    Intent intent = new Intent(this, BitcoinTransactionActivity002.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
                case Constant.TRANSACTION_COIN_NAME_USDT_OMNI:
                    intent = new Intent(this, UsdtTransactionActivity002.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
                case Constant.TRANSACTION_COIN_NAME_ETH:
                    intent = new Intent(this, EthTransactionActivity.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
                case Constant.TRANSACTION_COIN_NAME_USDT_ERC20:
                    intent = new Intent(this, UsdtErc20TransactionActivity.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
            }
        });
        btnTransactionReceive.setOnClickListener(view -> {
            Intent intent = new Intent(this, CoinAddressQrActivity.class);
            intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
            intent.putExtra(Constants.TRANSACTION_COIN_NAME, walletBtcInfo.getCoin_name());
            startActivity(intent);
        });
    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        Log.i("ttttttttttttt", event.getType() + "---onDataSynEvent: 有进来了么？" + select_index);
        if (event.getType() == EventMessage.TRANSACTION_RECORD_UPDATE) {
            switch (select_index) {
                case 1:
                    mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 2:
                    mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 3:
                    mMessageBeans = TransactionRecordManager.getWalletBtcFail(coin_address, coin_id);
                    break;
                case 0:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                default:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                    break;
            }
//            mMessageBeans.addAll(mMessageBeans02);
//            mMessageBeans.addAll(mMessageBeans02);
//            mMessageBeans.addAll(mMessageBeans02);
//            mMessageBeans.addAll(mMessageBeans02);
            Log.i("ttttttttttttt", "onDataSynEvent: 002有进来了么？" + mMessageBeans.size());
            recordAdapter.setNewData(mMessageBeans);
        } else if (event.getType() == EventMessage.TRANSACTION_RECORD_UPDATE_USDT) {
            switch (select_index) {
                case 1:
                    mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 2:
                    mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 3:
                    mMessageBeans = TransactionRecordManager.getWalletBtcFail(coin_address, coin_id);
                    break;
                case 0:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                default:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                    break;
            }
            recordAdapter.setNewData(mMessageBeans);
        }
        if (mMessageBeans.size() <= 0) {
            recordAdapter.addHeaderView(record_empty);
        } else {
            recordAdapter.removeHeaderView(record_empty);
        }
    }
}