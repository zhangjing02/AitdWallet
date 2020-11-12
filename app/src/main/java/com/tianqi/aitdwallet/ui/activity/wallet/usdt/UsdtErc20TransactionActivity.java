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
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.tool.ScanActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionRecordActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.utils.eth.EthWalletManager;
import com.tianqi.aitdwallet.widget.dialog.ExplainTxMinerFeeDialog;
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
import com.tianqi.baselib.rxhttp.bean.GetSimpleRpcBean;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.NetworkUtil;
import com.tianqi.baselib.utils.digital.AESCipher;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.ScreenUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.CustomSeekBar;

import org.greenrobot.eventbus.EventBus;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
 * @description eth的代币erc20交易。
 */

public class UsdtErc20TransactionActivity extends BaseActivity {

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

    private double account_balance = 0;
    int miner_fee_single = 1;//单个input的最佳手续费
    private WalletInfo walletInfo;
    private CoinInfo walletBtcFrAddress;
    private Dialog mLoadDialog;
    private Web3j mWeb3j;
    private WalletFile mWalletFile;
    private double eth_balance;

    @Override
    protected int getContentView() {
        return R.layout.activity_eth_transaction;
    }

    @Override
    protected void initView() {
        getToolBar();
        initWeb3j("http://192.168.1.16:8545");
        //普通进度条
        seekBarMinerCost.setEnabled(true);
        seekBarMinerCost.setMax(100);
        seekBarMinerCost.setProgress(15);
        walletInfo= WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
        Log.i(TAG, walletInfo.getCoin_CNYPrice()+"------initView: 汇率是多少？"+walletInfo.getCoin_USDPrice());
        setMinerFeeText(miner_fee_single);

        seekBarMinerCost.setOnChangeListener(new CustomSeekBar.OnChangeListener() {
            @Override
            public void onProgressChanged(CustomSeekBar seekBar) {
                int select_miner = 1+seekBar.getProgress();
                Log.i(TAG, seekBar.getProgress() + "onProgressChanged: 我们计算的费用是？" + select_miner);
                miner_fee_single = select_miner;
                setMinerFeeText(select_miner);
            }

            @Override
            public void onTrackingTouchFinish(CustomSeekBar seekBar) {
            }
        });
    }

    private void setMinerFeeText(double miner) {
        double miner_fee_show = miner;
        UserInformation userInformation = UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
//            String miner_fee_str = DataReshape.double2int(miner_fee_show, 0) +
//                    "sat" + " ≈$" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_USDPrice() / 100000000, 2);
//        SpannableString spannableString = new SpannableString(miner_fee_str);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
//        spannableString.setSpan(colorSpan, spannableString.toString().indexOf("sat") + 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            String miner_fee_str02 = DataReshape.doubleBig(miner, 8) +
                    "Gwei" + " ≈$" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_USDPrice()/1000000000f, 8);
            etPaymentMinerFee.setText(miner_fee_str02);
        } else {
//            String miner_fee_str = DataReshape.double2int(miner_fee_show, 0) +
//                    "sat" + " ≈￥" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_CNYPrice() / 100000000, 2);
            String miner_fee_str02 = DataReshape.doubleBig(miner, 8) +
                    "Gwei" + " ≈￥" + DataReshape.doubleBig(miner_fee_show * walletInfo.getCoin_CNYPrice()/1000000000f, 8);
//        SpannableString spannableString = new SpannableString(miner_fee_str);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
//        spannableString.setSpan(colorSpan, spannableString.toString().indexOf("sat") + 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            etPaymentMinerFee.setText(miner_fee_str02);
        }
    }


    private Web3j initWeb3j(String url) {
        mWeb3j = Web3jFactory.build(new HttpService(url));
        return mWeb3j;
    }

    @Override
    protected void initData() {
        initWallet();
        etPaymentAddress.addTextChangedListener(textWatcher);
        etPaymentAmount.addTextChangedListener(textWatcher);
        getEthBalance();

    }

    private void getEthBalance() {
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getEthAddressBalance("eth",walletBtcFrAddress.getCoin_address(), map).compose(RxHelper.io_main())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    public void onSuccess(String data, String msg) {
                        Log.i(TAG, "onSuccess: 我们看得到的数据是？"+data);
                        eth_balance=Double.valueOf(eth_balance);
                        tvBalance.setText(data);
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                    }
                });
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
            if (!TextUtils.isEmpty(etPaymentAddress.getText().toString()) && !TextUtils.isEmpty(etPaymentAmount.getText().toString())) {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
            } else {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
            }
        }
    };

    private void initWallet() {
        String coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        walletBtcFrAddress = CoinInfoManager.getCoinFrAddress(Constant.TRANSACTION_COIN_NAME_USDT_ERC20, coin_address);
        tvBalance.setText(DataReshape.doubleBig(walletBtcFrAddress.getCoin_totalAmount(), 8));
        EthWalletManager.getInstance().loadWallet(this, walletBtcFrAddress, wallet -> {
            initWeb3j("http://192.168.1.16:8545");
            Log.i(TAG, walletBtcFrAddress.getCoin_address()+"initData: 001我们看看加载出来的钱包是啥？");
            if (walletBtcFrAddress.getCoin_address().substring(2).toLowerCase().equals(wallet.getAddress())){
                mWalletFile=wallet;
                Log.i(TAG, "initData: 002我们看看加载出来的钱包是啥？"+mWalletFile.getAddress());
            }
        });
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.eth_transaction_tittle);
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
                if (NetworkUtil.isNetworkAvailable(this)) {
                    // TODO: 2020/11/10 这里是转账逻辑。
                    if (judgeSelectInput()) {
                        createTxToBroadcastApi();
                    }
                } else {
                    ToastUtil.showToast(this, getString(R.string.network_error));
                }
                break;
        }
    }



    @SuppressLint("CheckResult")
    private void createTxToBroadcastApi() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("123");
        }).map(resultBeans -> {
            String to_address=etPaymentAddress.getText().toString().toLowerCase();
            // String to = "0x05c5d1b89d0d4b5bddb424c991db94557977c01c";              //转账地址
            String to = to_address.startsWith("0x")?to_address:"0x"+to_address;
            String to_credential="0x952edadb1709d976e85af33fa8f9030207b7af3e";
           // String amount = "123";

            BigDecimal value = Convert.toWei(etPaymentAmount.getText().toString(), Convert.Unit.KWEI);  //转账金额。
            //BigDecimal value = BigDecimal.valueOf(Long.valueOf(etPaymentAmount.getText().toString()));  //转账金额。
            Log.i(TAG, to+"-------createTxToBroadcastApi: 我们计算的金额是多少？"+value);

            Function transfer = transfer(to, value.toBigInteger());
            ECKeyPair ecKeyPair = null;
            try {

                UserInformation userInformation=UserInfoManager.getUserInfo();
                ecKeyPair = Wallet.decrypt(AESCipher.decrypt(Constant.PSD_KEY,userInformation.getPasswordStr()), mWalletFile);
                Credentials credentials = Credentials.create(ecKeyPair);
                String transactionHash = execute(credentials, transfer, to_credential);
                Log.d(TAG, "--------run: 004transactionHash "+transactionHash );

            } catch (CipherException e) {
                Log.d(TAG, "--------run: 005transactionHash "+e.getMessage() );
                e.printStackTrace();
            } catch (Exception e) {
                Log.d(TAG, "--------run: 006transactionHash "+e.getMessage() );
                e.printStackTrace();
            }
            return Constant.HTTP_ERROR;
        }).map(response -> {
            //把交易的hex，做签名处理。
            if (response != null&&!response.equals(Constant.HTTP_ERROR)) {
                Log.i(TAG, "createTxToBroadcastApi: 看看签名如何？"+response);
                //可以用btc的域名进行请求，请求参数换成eth的就行。
                Response tx_response = HttpClientUtil.getInstance().postFormalEthJson(makeBroadcastTxParams002(response));
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
                        Log.i(TAG, "createTxToBroadcastApi: 我们看交易成功后的id是？"+baseEntity);
                        ScreenUtils.hideKeyboard(this);
                        Gson gson = new Gson();
                        GetSimpleRpcBean getCreateTransactionBean = gson.fromJson(baseEntity, GetSimpleRpcBean.class);
                        if (getCreateTransactionBean.getResult()!=null){
                            // TODO: 2020/9/23  把交易详情存入数据库中。
                            TransactionRecord tx_record = new TransactionRecord();
                            tx_record.setAddress(walletBtcFrAddress.getCoin_address());
                            tx_record.setAmount(Double.valueOf(etPaymentAmount.getText().toString()));

                            //  tx_record.setId(0);
                            tx_record.setCoin_type(Constant.TRANSACTION_COIN_ETH);//0代表比特币。
                            tx_record.setStatus(Constant.TRANSACTION_STATE_WAITING);
                            tx_record.setTransType(Constant.TRANSACTION_TYPE_SEND);//0转账，1收款
                            tx_record.setCoin_id(walletBtcFrAddress.getCoin_id());
                            if (!TextUtils.isEmpty(etPaymentRemark.getText().toString())) {
                                tx_record.setRemark(etPaymentRemark.getText().toString());
                            }
                            tx_record.setTxid(getCreateTransactionBean.getResult());
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                            tx_record.setTimeStr(format.format(calendar.getTime()));

                            tx_record.setUnit(Constant.TRANSACTION_COIN_NAME_ETH);
                            TransactionRecordManager.insertOrUpdate(tx_record);
                            EventMessage message = new EventMessage();
                            message.setType(EventMessage.TRANSACTION_RECORD_UPDATE);
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
                        }else {
                            ToastUtil.showToast(this, getString(R.string.notice_transaction_error));
                        }
                    } else {
                        ToastUtil.showToast(this, getString(R.string.notice_transaction_error));
                    }
                });
    }


    private String makeBroadcastTxParams002(String transaction_hex) {
        Map<String, Object> listunspentParams = new HashMap<>();
        listunspentParams.put("jsonrpc", "2.0");
        listunspentParams.put("id", "viewtoken");
        listunspentParams.put("method", "eth_sendRawTransaction");
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
        } else if (etPaymentAddress.getText().toString().length()!=40&&etPaymentAddress.getText().toString().length()!=42){
            ToastUtil.showToast(this, getString(R.string.notice_address_error));
            return false;
        }else if (TextUtils.isEmpty(etPaymentAmount.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.input_transaction_amount));
            return false;
        } else if (etPaymentAddress.getText().toString().equals(walletBtcFrAddress.getCoin_address())) {
            ToastUtil.showToast(this, getString(R.string.notice_trans_to_me_refuse));
            return false;
        }
//        else if (Double.valueOf(etPaymentAmount.getText().toString())>=eth_balance){
//            ToastUtil.showToast(this, getString(R.string.notice_balance_not_enough));
//            return false;
//        }
        return true;
    }


    private Function transfer(String to, BigInteger value) {
        return new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(value)),
                Collections.singletonList(new TypeReference<Bool>() {}));
    }


    private String execute(
            Credentials credentials, Function function, String contractAddress) throws Exception {
        BigInteger nonce =  mWeb3j.ethGetTransactionCount(walletBtcFrAddress.getCoin_address(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
       // BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();
      //  BigInteger gasPrice = BigInteger.valueOf(miner_fee_single*1000000000l) ;//单价费用，需要我们自己设定。单位是：wei。
        BigInteger gasPrice = BigInteger.valueOf(miner_fee_single*1000000000l) ;//单价费用，需要我们自己设定。单位是：wei。
        BigInteger gasLimit = new BigInteger("200000");
        String encodedFunction = FunctionEncoder.encode(function);

        Log.i(TAG, walletBtcFrAddress.getCoin_address()+"------------execute: 我们看erc20的值是多少？"+encodedFunction);

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                contractAddress,
                encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        // TODO: 2020/11/10 这里就是只能合约的，生成和广播？
//        EthSendTransaction transactionResponse = mWeb3j.ethSendRawTransaction(hexValue)
//                .sendAsync().get();
        return hexValue;
    }
}
