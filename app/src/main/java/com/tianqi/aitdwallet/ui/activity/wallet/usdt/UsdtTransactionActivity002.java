package com.tianqi.aitdwallet.ui.activity.wallet.usdt;

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
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetListUnspentBean;
import com.tianqi.baselib.rxhttp.bean.GetSimpleRpcBean;
import com.tianqi.baselib.rxhttp.bean.GetUnspentTxBean;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.NetworkUtil;
import com.tianqi.baselib.utils.digital.AESCipher;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.display.ScreenUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author zhangjing
 * @date 2020/11/9
 * @description usdt交易暂时使用类，这里加入了listunspent过滤逻辑，防止第三方服务没有及时清理listunspent交易，导致双花的问题。
 */
public class UsdtTransactionActivity002 extends BaseActivity {

    private static final String TAG = "UsdtTransactionActivity";
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
    private int vin_id;
    private double total_listunspent_value;
    double usdt_consume_fee = 0.00000546;//转账usdt会固定消耗btc546聪到对方账户。
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
        seekBarMinerCost.setProgress(10);
        walletInfo = WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
        setMinerFeeText(miner_fee_single);

        seekBarMinerCost.setOnChangeListener(new CustomSeekBar.OnChangeListener() {
            @Override
            public void onProgressChanged(CustomSeekBar seekBar) {
                int select_miner = 300 + (5000 - 300) * seekBar.getProgress() / 100;
                Log.i(TAG, seekBar.getProgress()+"onProgressChanged: 我们计算的费用是？"+select_miner);
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

            String miner_fee_str02 = DataReshape.doubleBig(miner, 8) +
                    "BTC" + " ≈$" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_USDPrice() / 100000000, 2);

//        SpannableString spannableString = new SpannableString(miner_fee_str);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
//        spannableString.setSpan(colorSpan, spannableString.toString().indexOf("sat") + 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

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
        tvBalance.setText(DataReshape.doubleBig(walletBtcFrAddress.getCoin_totalAmount(), 8));
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
            Log.i(TAG, "afterTextChanged: 001我们收到了啥？");
            if (!TextUtils.isEmpty(etPaymentAddress.getText().toString()) && !TextUtils.isEmpty(etPaymentAmount.getText().toString())) {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
            } else {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
            }
        }
    };

    private void initWallet() {
        String coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        walletBtcFrAddress = CoinInfoManager.getCoinFrAddress(Constant.TRANSACTION_COIN_NAME_USDT_OMNI, coin_address);
        transactionRecords =TransactionRecordManager.getTxFrAddress(walletBtcFrAddress.getCoin_address());
        getUsdtBalance(walletBtcFrAddress);

// TODO: 2020/10/7 此处应该用列表里面穿过来的私钥去生成三件套，去进行转账。
        try {
            master = BitCoinECKeyPair.parseWIF(walletBtcFrAddress.getPrivateKey());
            Log.i(TAG, "initWallet: 我们看私钥是？" + master.getPrivateKey());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        if (NetworkUtil.isNetworkAvailable(this)){
            getBtcUtxo();
        }else {
            ToastUtil.showToast(this,getString(R.string.network_error));
        }

    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.usdt_transaction_tittle);
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
        }
    }


    @SuppressLint("CheckResult")
    private void getBtcUtxo() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
//        RetrofitFactory.getInstence(this).API()
//                .getUtxoForTx(master.getAddress(),"1/50",map).compose(RxHelper.io_main())
//                .subscribe(new BaseObserver<List<GetUnspentTxBean.DataBean>>(this) {
//                    @Override
//                    public void onSuccess(List<GetUnspentTxBean.DataBean> data, String msg) {
//                        Log.i(TAG, "onSuccess: 001我们看获取的未交易是？"+data.get(0).toString());
//                        utxo_list=new ArrayList<>();
//                        for (GetUnspentTxBean.DataBean result : data) {
//                            account_balance = account_balance + Double.valueOf(result.getValue());
//                            if (Double.valueOf(result.getValue())>0){
//                                utxo_list.add(result);
//                            }
//                        }
//                        Log.i(TAG, "onSuccess: 002我们看获取的未交易是？"+utxo_list.size());
//                        if (account_balance<=0){
//                            ToastUtil.showToast(UsdtTransactionActivity002.this,getString(R.string.notice_cost_not_enough));
//                        }
//                    }
//                    @Override
//                    protected void onFailure(int code, String msg) {
//                    }
//                });

        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext(master.getAddress());
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
                });
    }

    private void getUsdtBalance(CoinInfo specCoinInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getBtcAddressBalance("usdt",specCoinInfo.getCoin_address(),"1/1",map).compose(RxHelper.io_main())
                .subscribe(new BaseObserver<List<GetListUnspentBean>>(this) {
                    @Override
                    public void onSuccess(List<GetListUnspentBean> data, String msg) {
                        if (data!=null&&data.size()>0){
                            account_balance=Double.valueOf(data.get(0).getReceive())+Double.valueOf(data.get(0).getSpend());
                            specCoinInfo.setCoin_totalAmount(account_balance);
                            CoinInfoManager.insertOrUpdate(specCoinInfo);
                            tvBalance.setText(DataReshape.doubleBig(account_balance, 8)+"");
                        }
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                    }
           });

    }

    @SuppressLint("CheckResult")
    private void createTxToBroadcastApi() {
        Observable.create((ObservableOnSubscribe<List<GetUnspentTxBean.DataBean>>) emitter -> {
            //计算出可用的utxo交易块
            emitter.onNext(getUtxoCountForPay());
        }).map(resultBeans -> {
            //把得到的未交易数据，去请求创建负载。得到本次交易负载的hex。
            if (resultBeans.size() > 0) {
                return createOmniTransaction(resultBeans);
            } else {
                return Constant.HTTP_ERROR;
            }
        }).map(response -> {
            String join_tx_hex = null;
            //把omni交易hex和上面的负载hex通过接口拼接成一起。
            if (!Constant.HTTP_ERROR.equals(response)) {
                if (createFinalTransaction(response) != null) {
                    join_tx_hex = createFinalTransaction(response);
                    Log.i(TAG, "createTxToBroadcastApi: 001我们看签名后的数据是？"+join_tx_hex);
                } else {
                    join_tx_hex = Constant.HTTP_ERROR;
                }
            } else {
                join_tx_hex = Constant.HTTP_ERROR;
            }
            return join_tx_hex;
        }).map(response -> {
            //把交易的hex，做签名处理。
            String tx_sign_hex_str = Constant.HTTP_ERROR;
            if (!Constant.HTTP_ERROR.equals(response)) {
                BTCTransaction btcTransaction = new BTCTransaction(HexUtils.fromHex(response));
                byte[] sign = btcTransaction.sign(master);
                //网络请求，得到交易的输出hex，然后请求广播交易的接口。
                //Response tx_response = HttpClientUtil.getInstance().postUsdtJson(makeBroadcastTxParams(HexUtils.toHex(sign)));
                Log.i(TAG, "createTxToBroadcastApi: 看看签名如何？"+response);
                Response tx_response = HttpClientUtil.getInstance().postFormalUsdtJson(makeBroadcastTxParams002(response));
                if (tx_response != null) {
                    if (tx_response.isSuccessful()) {
                        ResponseBody body = tx_response.body();
                        if (body != null) {
                            tx_sign_hex_str = body.string();
                        }
                    }
                }
            } else {
                tx_sign_hex_str = Constant.HTTP_ERROR;
            }
            return tx_sign_hex_str;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    if (mLoadDialog != null) {
                        mLoadDialog.dismiss();
                    }
                    if (baseEntity != null && baseEntity.equals(Constant.HTTP_ERROR)) {
                        ToastUtil.showToast(this, getString(R.string.notice_transaction_error));
                    } else if (baseEntity != null) {
                        Log.i(TAG, baseEntity + "createOutputApi: 005打印线程执行情况" + Thread.currentThread().getName());
                        ScreenUtils.hideKeyboard(this);
                        Gson gson = new Gson();
                        GetSimpleRpcBean getCreateTransactionBean = gson.fromJson(baseEntity, GetSimpleRpcBean.class);
                        // TODO: 2020/9/23  把交易详情存入数据库中。
                        TransactionRecord tx_record = new TransactionRecord();
                        tx_record.setAddress(walletBtcFrAddress.getCoin_address());
                        tx_record.setAmount(Double.valueOf(etPaymentAmount.getText().toString()));
                        tx_record.setTxid(getCreateTransactionBean.getResult());
                        //  tx_record.setId(0);
                        tx_record.setCoin_type(Constant.TRANSACTION_COIN_USDT);//0代表比特币。
                        tx_record.setStatus(Constant.TRANSACTION_STATE_WAITING);
                        tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);//0转账，1收款
                        tx_record.setCoin_id(walletBtcFrAddress.getCoin_id());
                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        tx_record.setTimeStr(format.format(calendar.getTime()));

                        String use_listunspent_str=null;
                        for (int i = 0; i <utxo_for_pay.size() ; i++) {
                            use_listunspent_str=utxo_for_pay.get(i).getTxid()+"&&"+use_listunspent_str;
                        }
                        tx_record.setInput_id(use_listunspent_str);   //保存一个已使用过的，listunspent.防止后续再使用它，

                        tx_record.setUnit(Constant.COIN_UNIT_USDT);
                        TransactionRecordManager.insertOrUpdate(tx_record);
                        EventMessage message = new EventMessage();
                        message.setType(EventMessage.TRANSACTION_RECORD_UPDATE_USDT);
                        EventBus.getDefault().post(message);

                        SuccessDialog successDialog = new SuccessDialog(this);
                        successDialog.show();

                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(this, TransactionRecordActivity.class);
                            Log.i(TAG, walletBtcFrAddress.getCoin_name() + "createTxToBroadcastApi: 传过去的是啥？" + walletBtcFrAddress.getCoin_id());
                            intent.putExtra(Constants.TRANSACTION_COIN_NAME, walletBtcFrAddress.getCoin_name());
                            intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, walletBtcFrAddress.getCoin_address());
                            intent.putExtra(Constants.TRANSACTION_COIN_ID, walletBtcFrAddress.getCoin_id());
                            startActivity(intent);
                            finish();
                        }, 2000);
                    }
                });
    }

    private String createOmniTransaction(List<GetUnspentTxBean.DataBean> utxo_for_pay_list) {
        //1.创建负载（里面只需要用于支付的listunspent的hex）
        BTCTransaction.Output output = new BTCTransaction.Output(0, null);
        BTCTransaction.Input[] inputs = new BTCTransaction.Input[utxo_for_pay_list.size()];
        for (int i = 0; i < utxo_for_pay_list.size(); i++) {
            if (Double.valueOf(utxo_for_pay_list.get(i).getValue())>0){
                Log.i(TAG, i+"----------createOmniTransaction: 我们看拼接了几个未交易"+utxo_for_pay_list.get(i).getTxid());
                BTCTransaction.OutPoint outPoint = new BTCTransaction.OutPoint(HexUtils.fromHex(utxo_for_pay_list.get(i).getTxid()), utxo_for_pay_list.get(i).getOutput_no());
                inputs[i] = new BTCTransaction.Input(outPoint, null, 0);
            }
        }

//        BTCTransaction.OutPoint outPoint = new BTCTransaction.OutPoint(HexUtils.fromHex(utxo_for_pay_list.get(1).getTxid()), utxo_for_pay_list.get(1).getVout());
//        inputs[0] = new BTCTransaction.Input(outPoint, null, 0);

        BTCTransaction unsignedTransaction = new BTCTransaction(inputs, new BTCTransaction.Output[]{output}, 0);
        byte[] sign = new byte[0];
        try {
            sign = unsignedTransaction.sign(master);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        String toHex = HexUtils.toHex(sign);
        String tx_cut_str = "0000000000ffffffff0000000000";
        String connet_tx_str = "010000000000000000166a146f6d6e6900000000";
        String omni_origal_data = "000000000000001f0000000000000000";
        //创建裸交易
        String create_load_tx = toHex.substring(0, utxo_for_pay_list.size()*82-8) + tx_cut_str;
        //创建omni交易
        String strHex = null;
        //BigInteger omni_value= BigInteger.valueOf(Integer.valueOf(etPaymentAmount.getText().toString())*100000000);
        double bigInteger=Double.valueOf(etPaymentAmount.getText().toString())*100000000l;

        Scanner cin = new Scanner(DataReshape.double2int(bigInteger,0));     //金额：单位聪。
        while (cin.hasNext()) {
            String str = cin.next();
            strHex = new BigInteger(str, 10).toString(16);//10进制转换2进制
        }
        cin.close();

        String omni_str = omni_origal_data.substring(8, omni_origal_data.length() - strHex.length()) + strHex + "00000000";

        //把负载和omni转账，拼接成一个hex，然后形成一个代表omni转账的特殊output。
        String total_tx_str = create_load_tx.substring(0, create_load_tx.lastIndexOf(tx_cut_str) + 18) + connet_tx_str + omni_str;
        Log.i("ttttttttttttt", strHex + "---------onViewClicked: 002我们看这个" + total_tx_str);
        return total_tx_str;
    }

    private String createFinalTransaction(String omni_tx_hex) {
        BTCTransaction.Script script = null;  //对方的地址
        Log.i("ttttttttttttt", "---------onViewClicked: 003我们看这个" + omni_tx_hex);
        try {
            BTCTransaction omni_tx = new BTCTransaction(HexUtils.fromHex(omni_tx_hex));
            Log.i("ttttttttttttt", "---------onViewClicked: 003---1我们看这个" + omni_tx_hex);
            BTCTransaction.Script script002 = BTCTransaction.Script.buildOutput(master.getAddress());  //自己的地址
            Log.i("ttttttttttttt", "---------onViewClicked: 003---2我们看这个" + omni_tx_hex);
            BTCTransaction.Input[] inputs = new BTCTransaction.Input[utxo_for_pay.size()];
            for (int i = 0; i < utxo_for_pay.size(); i++) {
                BTCTransaction.OutPoint outPoint = new BTCTransaction.OutPoint(HexUtils.fromHex(utxo_for_pay.get(i).getTxid()), utxo_for_pay.get(i).getOutput_no());
                BTCTransaction.Input input = new BTCTransaction.Input(outPoint, script002, 0);
                inputs[i] = input;
            }
            Log.i("ttttttttttttt", "---------onViewClicked: 003---3我们看这个" + omni_tx_hex);
            script = BTCTransaction.Script.buildOutput(etPaymentAddress.getText().toString());
            BTCTransaction.Script script003 = BTCTransaction.Script.buildOutput(master.getAddress());
            double change_value = total_listunspent_value - usdt_consume_fee;
            BTCTransaction.Output[] outputs;
            if (change_value > cushion_fee) {
                //如果找零大于粉尘临界值550，就把找零归还自己。
                outputs = new BTCTransaction.Output[3];
                long out_btc_sat = (long) (usdt_consume_fee * 100000000l);
                BTCTransaction.Output output = new BTCTransaction.Output(out_btc_sat, script);
                outputs[0] = output;
                long out_change_btc_sat = (long) ((change_value-miner_fee_total) * 100000000l);
                BTCTransaction.Output output2 = new BTCTransaction.Output(out_change_btc_sat, script003);
                outputs[1] = output2;
                outputs[2] = omni_tx.outputs[0];
            } else {
                //如果找零小于粉尘临界值550，就不要了。
                outputs = new BTCTransaction.Output[2];
                long out_btc_sat = (long) ((usdt_consume_fee) * 100000000l);
                BTCTransaction.Output output = new BTCTransaction.Output(out_btc_sat, script);
                outputs[0] = output;
                outputs[1] = omni_tx.outputs[0];
            }
            BTCTransaction unsignedTransaction = new BTCTransaction(inputs, outputs, 0);
            byte[] sign = unsignedTransaction.sign(master);
            String toHex = HexUtils.toHex(sign);
            Log.i("tttttttttttttttttttt", master.getAddress() + "------onViewClicked: 我们看签名后的数据是？" + toHex);
            return toHex;
        } catch (BitcoinException e) {
            Log.i("ttttttttttttt", "---------onViewClicked: 004我们看这个" + e.getMessage());
            e.printStackTrace();
        } catch (ValidationException e) {
            Log.i("ttttttttttttt", "---------onViewClicked: 005我们看这个" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @OnClick({R.id.btn_collect, R.id.btn_transaction_send, R.id.btn_balance_all, R.id.tv_transaction_request})
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
            case R.id.btn_transaction_send://开始转账
                if (judgeSelectInput()) {
                    if (NetworkUtil.isNetworkAvailable(this)){
                        ToastUtil.showToast(this, getString(R.string.network_error));
                        return;
                    }
                    if (account_balance < 0.000009) {
                        ToastUtil.showToast(this, getString(R.string.notice_cost_not_enough));
                        return;
                    }
                  //  createTxApi();
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
                            etPaymentAddress.getText().toString(), fiat_value,master.getAddress());

                    bottomDialog.setTransactionCoinType(1);

                    bottomDialog.show();
                    bottomDialog.setDialogFirstClickListener(() -> {
                        PaymentDialog paymentDialog = new PaymentDialog(this, R.style.MyDialogInput);
                        paymentDialog.show();
                        paymentDialog.setOnDialogClickListener((view1, password, type) -> {
                            //验证密码是否正确。
                            bottomDialog.dismiss();
                            UserInformation userInfo = UserInfoManager.getUserInfo();
                            userInfo.getPasswordStr();
                            String aes_decode_str = AESCipher.decrypt(Constant.PSD_KEY, password.trim());
                            if (password != null && userInfo.getPasswordStr().equals(aes_decode_str)) {
                                mLoadDialog = LoadingDialogUtils.createLoadingDialog(this, "");
                                createTxToBroadcastApi();
                            } else {
                                ToastUtil.showToast(this, getString(R.string.notice_psd_error));
                            }
                        });
                    });
                }
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void createTxApi() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String omni_tx_hex = Constant.HTTP_ERROR;
            //先创建独立的omni交易，通过接口获取其hex，方便用于后续的拼接。
            Response tx_response = HttpClientUtil.getInstance().postUsdtJson(makeOmniTxParams());
            if (tx_response != null) {
                if (tx_response.isSuccessful()) {
                    ResponseBody body = tx_response.body();
                    if (body != null) {
                        try {
                            GetSimpleRpcBean bean = new Gson().fromJson(body.string(), GetSimpleRpcBean.class);
                            omni_tx_hex = bean.getResult();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            emitter.onNext(omni_tx_hex);
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    String fiat_value;
                    if (baseEntity != null && baseEntity.equals(Constant.HTTP_ERROR)) {
                        ToastUtil.showToast(this, getString(R.string.notice_request_error));
                    } else if (baseEntity != null) {

                    }

                });
    }

    private List<GetUnspentTxBean.DataBean> getUtxoCountForPay() {
        pay_amount = Double.valueOf(etPaymentAmount.getText().toString());
        //1.首先计算出用于支付的utxo需要几个。
        if (utxo_list.size()>0){
            total_listunspent_value = Double.valueOf(utxo_list.get(0).getValue());
            utxo_for_pay = new ArrayList<>();
            double R_miner_fee = 0;
            Log.i(TAG, total_listunspent_value + "getUtxoCountForPay: 001我们计算费用所需" + (miner_fee_single + usdt_consume_fee));
            if (total_listunspent_value >= miner_fee_single + usdt_consume_fee) {  //如果一个listunspent，就足以支付费用（300+546聪）就只拿一个去进行交易即可。
                miner_fee_total = miner_fee_single;  //因为一笔交易，需要一个输入，一个输出，usdt暂时定一个300、
                utxo_for_pay.add(utxo_list.get(0));
            } else {
                for (int i = 0; i < utxo_list.size(); i++) {
                    miner_fee_total = miner_fee_single * i;
                    if (miner_fee_total < total_listunspent_value) {//如果支付金额+交易费小于拼装的utxo，就可以进行支付了，此时可以跳出循环。
                        break;
                    } else {
                        total_listunspent_value = total_listunspent_value + Double.valueOf(utxo_list.get(i).getValue());
                        utxo_for_pay.add(utxo_list.get(i));
                    }
                }
            }
            Log.i(TAG, "getUtxoCountForPay: 002我们计算费用所需" + utxo_for_pay.size());
        }
        return utxo_for_pay;
    }

    private String makeBroadcastTxParams(String transaction_hex) {
        Map<String, Object> listunspentParams = new HashMap<>();
        listunspentParams.put("jsonrpc", "1.0");
        listunspentParams.put("id", "testnet3");
        listunspentParams.put("method", "sendrawtransaction");
        List<Object> params = new ArrayList<>();

        params.add(transaction_hex);//交易签名数组
        // params.add(0);//交易签名数组
        listunspentParams.put("params", params);

        Log.i(TAG, "makeOutputApiParams: 002我们拼接的请求体是？" + new Gson().toJson(listunspentParams));
        return new Gson().toJson(listunspentParams);
    }


    private String jointOmniToTxParams(String load_hex, String omni_tx_hex) {
        Map<String, Object> listunspentParams = new HashMap<>();
        listunspentParams.put("jsonrpc", "1.0");
        listunspentParams.put("id", "testnet3");
        listunspentParams.put("method", "omni_createrawtx_opreturn");
        List<Object> params = new ArrayList<>();

        params.add(load_hex);//负载数据
        params.add(omni_tx_hex);//交易omni的hex 。
        listunspentParams.put("params", params);

        Log.i(TAG, "makeOutputApiParams: 004我们拼接的请求体是？" + new Gson().toJson(listunspentParams));
        return new Gson().toJson(listunspentParams);
    }

    private String jointAddressToTxParams(String join_tx_hex) {
        Map<String, Object> listunspentParams = new HashMap<>();
        listunspentParams.put("jsonrpc", "1.0");
        listunspentParams.put("id", "testnet3");
        listunspentParams.put("method", "omni_createrawtx_reference");
        List<Object> params = new ArrayList<>();

        params.add(join_tx_hex);//负载数据
        params.add(etPaymentAddress.getText().toString());//转账给谁的地址 。
        listunspentParams.put("params", params);

        Log.i(TAG, "makeOutputApiParams: 005我们拼接的请求体是？" + new Gson().toJson(listunspentParams));
        return new Gson().toJson(listunspentParams);
    }

    private String makeOmniTxParams() {
        Map<String, Object> listunspentParams = new HashMap<>();
        listunspentParams.put("jsonrpc", "1.0");
        listunspentParams.put("id", "testnet3");
        listunspentParams.put("method", "omni_createpayload_simplesend");
        List<Object> params = new ArrayList<>();

        params.add(1);//1代表测试网，正式网用的是31
        params.add(etPaymentAmount.getText().toString());//交易omni的金额。
        listunspentParams.put("params", params);

        Log.i(TAG, "makeOutputApiParams: 006我们拼接的请求体是？" + new Gson().toJson(listunspentParams));
        return new Gson().toJson(listunspentParams);
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

        Log.i(TAG, "makeOutputApiParams: 002我们拼接的请求体是？" + new Gson().toJson(listunspentParams));
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
        }
        return true;
    }

}
