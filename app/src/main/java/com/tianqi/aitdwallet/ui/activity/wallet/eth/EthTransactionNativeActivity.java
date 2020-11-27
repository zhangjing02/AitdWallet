package com.tianqi.aitdwallet.ui.activity.wallet.eth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.quincysx.crypto.utils.HexUtils;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.bean.GetUtxoBean;
import com.tianqi.aitdwallet.ui.activity.tool.ScanActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.widget.dialog.BottomDialog;
import com.tianqi.aitdwallet.widget.dialog.PaymentDialog;
import com.tianqi.aitdwallet.widget.dialog.SuccessDialog;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
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
import com.tianqi.baselib.rxhttp.bean.GetSimpleRpcBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.digital.AESCipher;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.display.ScreenUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
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


public class EthTransactionNativeActivity extends BaseActivity {

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

    private List<GetUtxoBean.ResultBean> utxo_list, utxo_for_pay;
    private double account_balance = 0;
    // private ECKey key;
    private double pay_amount;
    double miner_fee_single = 0.000003;//单个input的最佳手续费
    double miner_fee_total=0.000003 ;//最终总和手续费

    double cushion_fee = 0.0000055;//留有余量防止粉尘交易。
    double total_value = 0;//用于交易的未交易总余额
    private ECKeyPair master = null;
    private WalletInfo walletInfo;
    private CoinInfo walletBtcFrAddress;
    private Dialog mLoadDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_bitcoin_wallet;
    }

    @Override
    protected void initView() {
        getToolBar();
        //普通进度条
        seekBarMinerCost.setEnabled(true);
        seekBarMinerCost.setMax(100);
        seekBarMinerCost.setProgress(10);
        walletInfo= WalletInfoManager.getHdWalletInfo();
        setMinerFeeText(miner_fee_single);

        seekBarMinerCost.setOnChangeListener(new CustomSeekBar.OnChangeListener() {
            @Override
            public void onProgressChanged(CustomSeekBar seekBar) {
                int select_miner = 200 + (1000 - 200) * seekBar.getProgress() / 100;
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

        UserInformation userInformation= UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
            String miner_fee_str = getString(R.string.text_miner_fee) + DataReshape.double2int(miner_fee_show, 0) +
                    "sat" + " ≈$" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_USDPrice() / 100000000, 2);
            SpannableString spannableString = new SpannableString(miner_fee_str);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
            spannableString.setSpan(colorSpan, spannableString.toString().indexOf("sat") + 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            etPaymentMinerFee.setText(spannableString);
        }else {
            String miner_fee_str = getString(R.string.text_miner_fee) + DataReshape.double2int(miner_fee_show, 0) +
                    "sat" + " ≈￥" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_CNYPrice() / 100000000, 2);
            SpannableString spannableString = new SpannableString(miner_fee_str);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
            spannableString.setSpan(colorSpan, spannableString.toString().indexOf("sat") + 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            etPaymentMinerFee.setText(spannableString);
        }
    }

    @Override
    protected void initData() {
        initWallet();
    }

    private void initWallet() {
        String coin_address=getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        walletBtcFrAddress = CoinInfoManager.getCoinFrAddress(Constant.TRANSACTION_COIN_NAME_ETH,coin_address);

// TODO: 2020/10/7 此处应该用列表里面穿过来的私钥去生成三件套，去进行转账。
        try {
            master = BitCoinECKeyPair.parseWIF(walletBtcFrAddress.getPrivateKey());
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        getBtcUtxo();
    }

    private void getToolBar() {
        toolbarTitle.setText("ETH转账");
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
        Observable.create((ObservableOnSubscribe<Response>) emitter -> {
            Map<String, Object> listunspentParams = new HashMap<>();
            listunspentParams.put("jsonrpc", "1.0");
            listunspentParams.put("id", "testnet3");
            listunspentParams.put("method", "listunspent");
            List<Object> params = new ArrayList<>();
            params.add(1);
            params.add(999999);
            params.add(new String[]{master.getAddress()});
            listunspentParams.put("params", params);
            Gson gson = new Gson();
            Response response = HttpClientUtil.getInstance().postBtcJson(gson.toJson(listunspentParams));
            if (response != null) {
                emitter.onNext(response);
            } else {
                emitter.onComplete();
            }
        }).map(response -> {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    GetUtxoBean utxoBean = new Gson().fromJson(body.string(), GetUtxoBean.class);
                    for (GetUtxoBean.ResultBean result : utxoBean.getResult()) {
                        account_balance = account_balance + result.getAmount();
                    }
                    return utxoBean;
                }
            }
            return null;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    utxo_list = baseEntity.getResult();
                    Log.e(TAG, "我们看用户的余额有多少？" + DataReshape.doubleBig(account_balance, 8));
                });
    }

    @SuppressLint("CheckResult")
    private void createTxToBroadcastApi() {
        Observable.create((ObservableOnSubscribe<List<GetUtxoBean.ResultBean>>) emitter -> {
            //计算出可用的utxo交易块,然后通过请求创建交易的hex。
            List<GetUtxoBean.ResultBean> list = getUtxoCountForPay();
            emitter.onNext(list);
        }).map(resultBeans -> {
            //把得到的未交易数据，去请求创建交易。得到本次交易的hex。
            return createTransaction(resultBeans);
        }).map(response -> {
            //把交易的hex，做签名处理。
            BTCTransaction btcTransaction = new BTCTransaction(HexUtils.fromHex(response));
            byte[] sign = btcTransaction.sign(master);
            //网络请求，得到交易的输出hex，然后请求广播交易的接口。
            Response tx_response = HttpClientUtil.getInstance().postBtcJson(makeBroadcastTxParams(HexUtils.toHex(sign)));
            if (tx_response != null) {
                if (tx_response.isSuccessful()) {
                    ResponseBody body = tx_response.body();
                    if (body != null) {
                        return body.string();
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
                        LogUtil.i(TAG, baseEntity + "createOutputApi: 005打印线程执行情况" + Thread.currentThread().getName());
                        ScreenUtils.hideKeyboard(this);
                        Gson gson = new Gson();
                        GetSimpleRpcBean getCreateTransactionBean = gson.fromJson(baseEntity, GetSimpleRpcBean.class);
                        // TODO: 2020/9/23  把交易详情存入数据库中。
                        TransactionRecord tx_record = new TransactionRecord();
                        tx_record.setAddress(walletBtcFrAddress.getCoin_address());
                        tx_record.setAmount(Double.valueOf(etPaymentAmount.getText().toString()));
                        tx_record.setTxid(getCreateTransactionBean.getResult());
                        //  tx_record.setId(0);
                        tx_record.setCoin_type(Constant.TRANSACTION_COIN_BTC);//0代表比特币。
                        tx_record.setStatus(Constant.TRANSACTION_STATE_SUCCESS);
                        tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);//0转账，1收款
                        tx_record.setCoin_id(walletBtcFrAddress.getCoin_id());
                        Calendar calendar = Calendar.getInstance();

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                        tx_record.setTimeStr(format.format(calendar.getTime()));

                        tx_record.setUnit(Constant.TRANSACTION_COIN_NAME_BTC);
                        TransactionRecordManager.insertOrUpdate(tx_record);
                        EventMessage message = new EventMessage();
                        message.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
                        EventBus.getDefault().post(message);

                        SuccessDialog successDialog = new SuccessDialog(this);
                        successDialog.show();
                    } else {
                        ToastUtil.showToast(this, "转账失败");
                    }
                });
    }

    private String createTransaction(List<GetUtxoBean.ResultBean> utxo_for_pay_list) {
        Map<String, Object> listunspentParams = new HashMap<>();
        listunspentParams.put("jsonrpc", "1.0");
        listunspentParams.put("id", "testnet3");
        listunspentParams.put("method", "createrawtransaction");
        List<List<Map<String, Object>>> total_params = new ArrayList<>();

        List<Map<String, Object>> pay_params = new ArrayList<>();

        for (int i = 0; i < utxo_for_pay_list.size(); i++) {
            Map<String, Object> pay_param = new HashMap<>();
            pay_param.put("sequence", 0);
            pay_param.put("txid", utxo_for_pay_list.get(i).getTxid());
            pay_param.put("vout", utxo_for_pay_list.get(i).getVout());
            pay_params.add(pay_param);
        }

        total_params.add(pay_params);
        List<Map<String, Object>> send_params = new ArrayList<>();
        Map<String, Object> send_param = new HashMap<>();
        send_param.put(etPaymentAddress.getText().toString(), Double.valueOf(etPaymentAmount.getText().toString()));
        send_params.add(send_param);

        //如果剩余的金额大于550聪，就加入找零的输出，输出的地址是自己。
        double change_value = total_value - pay_amount - miner_fee_total;
        if (change_value > cushion_fee) {
            Map<String, Object> send_param_me = new HashMap<>();
            send_param_me.put(master.getAddress(), DataReshape.doubleBig(change_value, 8));
            send_params.add(send_param_me);
        }
        total_params.add(send_params);
        listunspentParams.put("params", total_params);

        LogUtil.i(TAG, "createTransaction: 我们看请求的参数是多少？" + new Gson().toJson(listunspentParams));

        Response response = HttpClientUtil.getInstance().postBtcJson(new Gson().toJson(listunspentParams));
        String tx_hex = null;
        try {
            tx_hex = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.i(TAG, "createTransaction: 我们看得到的交易hex是？" + tx_hex);
        GetSimpleRpcBean simpleRpcBean = new Gson().fromJson(tx_hex, GetSimpleRpcBean.class);
        return simpleRpcBean.getResult();
    }

    @OnClick({R.id.btn_collect, R.id.tv_expect_time, R.id.btn_create_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent, 25);
                break;
            case R.id.tv_expect_time:
                break;
            case R.id.btn_create_wallet://开始转账
                if (judgeSelectInput()) {
                    String fiat_value;
                    UserInformation userInformation= UserInfoManager.getUserInfo();
                    if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
                         fiat_value=DataReshape.doubleBig(miner_fee_total, 8) + " ≈$"
                                + DataReshape.doubleBig(miner_fee_total* walletInfo.getCoin_USDPrice(), 2);
                    }else {
                         fiat_value=DataReshape.doubleBig(miner_fee_total, 8) + " ≈￥"
                                + DataReshape.doubleBig(miner_fee_total* walletInfo.getCoin_CNYPrice(), 2);
                    }

                    BottomDialog bottomDialog = new BottomDialog(this, etPaymentAmount.getText().toString(),
                            etPaymentAddress.getText().toString(),fiat_value );

                    bottomDialog.show();
                    bottomDialog.setDialogFirstClickListener(() -> {
                        //2.调取创建交易的api，生成我们的输出hex_code码：
                        PaymentDialog paymentDialog = new PaymentDialog(this, R.style.MyDialogInput);
                        paymentDialog.show();
                        paymentDialog.setOnDialogClickListener((view1, password, type) -> {
                            //验证密码是否正确。
                            bottomDialog.dismiss();
                            UserInformation userInfo = UserInfoManager.getUserInfo();
                            userInfo.getPasswordStr();
                            String aes_decode_str = AESCipher.decrypt(Constant.PSD_KEY, password);
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

    private List<GetUtxoBean.ResultBean> getUtxoCountForPay() {
        pay_amount = Double.valueOf(etPaymentAmount.getText().toString());
        //1.首先计算出用于支付的utxo需要几个。
        double amount_value = 0;
        utxo_for_pay = new ArrayList<>();
        double R_miner_fee=0;
        for (int i = 0; i < utxo_list.size(); i++) {
            miner_fee_total=miner_fee_single*(i+1);
            if (pay_amount + miner_fee_total < amount_value) {//如果支付金额+交易费小于拼装的utxo，就可以进行支付了，此时可以跳出循环。
                break;
            } else {
                amount_value = amount_value + utxo_list.get(i).getAmount();
                utxo_for_pay.add(utxo_list.get(i));
            }
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
        params.add(0);//交易签名数组
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
        }
        return true;
    }
}
