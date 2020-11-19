package com.tianqi.aitdwallet.ui.activity.wallet.eth;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.utils.eth.EthWalletManager;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.ToastUtil;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class EthTransactionTestActivity extends BaseActivity {
    @BindView(R.id.tv_eth_balance)
    TextView tvEthBalance;
    @BindView(R.id.tv_eth_token_balance)
    TextView tvEthTokenBalance;
    private CoinInfo coinEthFrAddress;
    Web3j mWeb3j;
    private static final String HEX_PREFIX = "0x";
    private static final String TAG = "EthTransactionActivity";
    private WalletFile mWalletFile,mWalletFile2;

    @Override
    protected int getContentView() {
        return R.layout.activity_eth_test_transaction;
    }

    @Override
    protected void initView() {
        String coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        coinEthFrAddress = CoinInfoManager.getCoinFrAddress(Constant.TRANSACTION_COIN_NAME_ETH, coin_address);

        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(this).API()
                .getEthAddressBalance("eth",coinEthFrAddress.getCoin_address(), map).compose(RxHelper.io_main())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    public void onSuccess(String data, String msg) {
                        LogUtil.i(TAG, "onSuccess: 我们看得到的数据是？"+data);
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        EthWalletManager.getInstance().loadWallet(this, coinEthFrAddress, wallet -> {
            initWeb3j("http://192.168.1.16:8545");
            LogUtil.i(TAG, coinEthFrAddress.getCoin_address()+"initData: 001我们看看加载出来的钱包是啥？");
            if (coinEthFrAddress.getCoin_address().substring(2).toLowerCase().equals(wallet.getAddress())){
                mWalletFile=wallet;
                LogUtil.i(TAG, "initData: 002我们看看加载出来的钱包是啥？"+mWalletFile.getAddress());
            }
        });
    }
    private Web3j initWeb3j(String url) {
        mWeb3j = Web3jFactory.build(new HttpService(url));
        return mWeb3j;
    }

    @OnClick({R.id.tv_eth_token_balance, R.id.btn_get_balance,R.id.btn_transaction_send,R.id.btn_erc20_transaction_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_eth_token_balance:
                break;
            case R.id.btn_get_balance:
                break;
            case R.id.btn_transaction_send:
                if (mWalletFile!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                BigInteger transactionCount = mWeb3j.ethGetTransactionCount(coinEthFrAddress.getCoin_address(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
                                BigInteger gasPrice = null;
                                gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();
                                Log.d(TAG, "run: " + transactionCount + ", " + gasPrice);
                                BigInteger gasLimit = new BigInteger("200000");
                                BigDecimal value = Convert.toWei("123", Convert.Unit.ETHER);  //转账金额。
                                Log.d(TAG, "run: value wei" + value.toPlainString());
                                String to = "0x05c5d1b89d0d4b5bddb424c991db94557977c01c";              //转账地址
                                RawTransaction etherTransaction = RawTransaction.createEtherTransaction(transactionCount, gasPrice, gasLimit, to, value.toBigInteger());
                                ECKeyPair ecKeyPair = Wallet.decrypt("Aa123456", mWalletFile);
                                Credentials credentials = Credentials.create(ecKeyPair);
                                byte[] bytes = TransactionEncoder.signMessage(etherTransaction, credentials);
                                String hexValue = Numeric.toHexString(bytes);
                                Log.d(TAG, "--------run: 001transactionHash "+hexValue );
                            } catch (IOException e) {
                                Log.d(TAG, "--------run: 002transactionHash "+e.getMessage() );
                                e.printStackTrace();
                            } catch (CipherException e) {
                                Log.d(TAG, "--------run: 003transactionHash "+e.getMessage() );
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else {
                    ToastUtil.showToast(this,"初始化未完成");
                }
                break;
            case R.id.btn_erc20_transaction_send:
                if (mWalletFile!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String to = "0x05c5d1b89d0d4b5bddb424c991db94557977c01c";
                            String amount = "123";
                            Function transfer = transfer(to, new BigInteger(amount));
                            ECKeyPair ecKeyPair = null;
                            try {
                                ecKeyPair = Wallet.decrypt("Aa123456", mWalletFile);
                                Credentials credentials = Credentials.create(ecKeyPair);
                                String transactionHash = execute(credentials, transfer, to);
                                Log.d(TAG, "--------run: 004transactionHash "+transactionHash );
                            } catch (CipherException e) {
                                Log.d(TAG, "--------run: 005transactionHash "+e.getMessage() );
                                e.printStackTrace();
                            } catch (Exception e) {
                                Log.d(TAG, "--------run: 006transactionHash "+e.getMessage() );
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else {
                    ToastUtil.showToast(this,"初始化未完成");
                }
                break;
        }
    }

    private Function transfer(String to, BigInteger value) {
        return new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(value)),
                Collections.singletonList(new TypeReference<Bool>() {}));
    }

    private String execute(
            Credentials credentials, Function function, String contractAddress) throws Exception {
        BigInteger nonce =  mWeb3j.ethGetTransactionCount(coinEthFrAddress.getCoin_address(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
        BigInteger gasPrice = mWeb3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = new BigInteger("200000");
        String encodedFunction = FunctionEncoder.encode(function);

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