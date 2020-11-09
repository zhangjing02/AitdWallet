package com.tianqi.aitdwallet.ui.activity.wallet;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.bip32.ExtendedKey;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.bip39.SeedCalculator;
import com.quincysx.crypto.bip44.AddressIndex;
import com.quincysx.crypto.bip44.BIP44;
import com.quincysx.crypto.bip44.CoinPairDerive;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.bean.GetUtxoBean;
import com.tianqi.baselib.rxhttp.HttpClientUtil;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.rxhttp.base.RxHelper;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionConfidence;
import org.bitcoinj.core.Utils;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.SendRequest;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;

import java.io.File;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static java.sql.DriverManager.println;


public class TestBtcActivity extends BaseActivity {

    @BindView(R.id.btn_create_wallet)
    Button btnCreateWallet;
    @BindView(R.id.btn_register_wallet)
    Button btnRegisterWallet;
    @BindView(R.id.btn_wallet_balance)
    Button btnWalletBalance;
    @BindView(R.id.btn_wallet_sign)
    Button btnWalletSign;


    private boolean bit_test_net;
    private ECKeyPair master;
    private String btc_sign_msg;
    private Transaction transaction;
    private Wallet wallet1;
    private NetworkParameters params;
    private static final String TAG = "TestBtcActivity";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i("tttttttttttt", "handleMessage: 主线程得到一定的回复" + msg);

        }
    };

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_test_btc;
    }


    @OnClick({R.id.btn_create_wallet, R.id.btn_register_wallet, R.id.btn_wallet_balance, R.id.btn_wallet_sign, R.id.btn_wallet_verify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_wallet:
                List<String> mnemonicWordsInAList = new ArrayList<>();
                mnemonicWordsInAList.add("also");
                mnemonicWordsInAList.add("rotate");
                mnemonicWordsInAList.add("swarm");
                mnemonicWordsInAList.add("glove");
                mnemonicWordsInAList.add("rack");
                mnemonicWordsInAList.add("mixed");
                mnemonicWordsInAList.add("tower");
                mnemonicWordsInAList.add("ocean");
                mnemonicWordsInAList.add("despair");
                mnemonicWordsInAList.add("enact");
                mnemonicWordsInAList.add("harbor");
                mnemonicWordsInAList.add("view");
                try {
                    byte[] seed = new SeedCalculator().calculateSeed(mnemonicWordsInAList, "");  //助记词生成种子。

                    Log.e("=ttttttttt-----=", "==========开始============" + mnemonicWordsInAList.toString().replaceAll(",", " "));

                    ExtendedKey extendedKey = ExtendedKey.create(seed);
                    AddressIndex address = BIP44.m().purpose44()
                            .coinType(CoinTypes.Bitcoin)
                            .account(0)
                            .external()
                            .address(0);

                    CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
                    master = coinKeyPair.derive(address);


                    Log.e("ttttttttt----private", "==" + master.getPrivateKey());
                    Log.e("=ttttttttt---public=", master.getPublicKey());
                    Log.e("=ttttttttt---address=", master.getAddress());
                    Log.e("=ttttttttt-----=", "======================");
                } catch (ValidationException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_register_wallet:

                getBtcUtxo();

                break;
            case R.id.btn_wallet_balance:
//                String url = "https://chain.api.btc.com/v3/address/" + address;
//                String request = HttpUtils.getRequest(url);
//                JSONObject data = JSONObject.parseObject(request);
//                JSONObject jsonObject = data.getJSONObject("data");
//                Double bal = jsonObject.getDouble("banlance");
//                return bal ;
                String filePrefix;
                String[] args = new String[]{"forwarding", "testnet"};
                if (args[1].equals("testnet")) {
                    params = TestNet3Params.get();
                    filePrefix = "forwarding-service-testnet";
                } else if (args[1].equals("regtest")) {
                    params = RegTestParams.get();
                    filePrefix = "forwarding-service-regtest";
                } else {
                    params = MainNetParams.get();
                    filePrefix = "forwarding-service";
                }

                WalletAppKit kit = new WalletAppKit(params, new File("aitd_wallet"), filePrefix) {
                    @Override
                    protected void onSetupCompleted() {
                        // This is called in a background thread after startAndWait is called, as setting up various objects
                        // can do disk and network IO that may cause UI jank/stuttering in wallet apps if it were to be done
                        // on the main thread.
                        if (wallet().getKeyChainGroupSize() < 1)
                            wallet().importKey(new ECKey());
                    }
                };

                if (params == RegTestParams.get()) {
                    // Regression test mode is designed for testing and development only, so there's no public network for it.
                    // If you pick this mode, you're expected to be running a local "bitcoind -regtest" instance.
                    kit.connectToLocalHost();
                }

// Download the block chain and wait until it's done.
                kit.startAsync();
                kit.awaitRunning();


                kit.wallet().addCoinsReceivedEventListener((wallet, tx, prevBalance, newBalance) -> {
                    wallet1 = wallet;

                    transaction = tx;
                    Coin value = tx.getValueSentToMe(wallet);
                    System.out.println("Received tx for " + value.toFriendlyString() + ": " + tx);
                    System.out.println("Transaction will be forwarded after it confirms.");


                    Log.i("tttttttttttttt", "onCoinsReceived: 001我们看收到了哪些东西呢？" + wallet.toString());
                    Log.i("tttttttttttttt", "onCoinsReceived: 002我们看收到了哪些东西呢？" + tx.toString());
                    Log.i("tttttttttttttt", "onCoinsReceived: 003我们看收到了哪些东西呢？" + newBalance.toString());
                    ListenableFuture<TransactionConfidence> future = tx.getConfidence().getDepthFuture(1);

                    Futures.addCallback(tx.getConfidence().getDepthFuture(1), new FutureCallback<TransactionConfidence>() {
                        @Override
                        public void onSuccess(TransactionConfidence result) {
                            // "result" here is the same as "tx" above, but we use it anyway for clarity.
                            // forwardCoins(result);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                });


                Address forwardingAddress = new Address(params, args[0]);

                Coin value = transaction.getValueSentToMe(kit.wallet());
                System.out.println("Forwarding " + value.toFriendlyString() + " BTC");
// Now send the coins back! Send with a small fee attached to ensure rapid confirmation.
                final Coin amountToSend = value.subtract(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
                final Wallet.SendResult sendResult;

                try {
                    // sendResult = kit.wallet().sendCoins(kit.peerGroup(), forwardingAddress, amountToSend);

                    SendRequest req = SendRequest.to(forwardingAddress, value);
                    req.feePerKb = Coin.parseCoin("0.0005");
                    sendResult = kit.wallet().sendCoins(kit.peerGroup(), req);
                    Transaction createdTx = sendResult.tx;


                    // Register a callback that is invoked when the transaction has propagated across the network.
// This shows a second style of registering ListenableFuture callbacks, it works when you don't
// need access to the object the future returns.
                    sendResult.broadcastComplete.addListener(new Runnable() {
                        @Override
                        public void run() {
                            // The wallet has changed now, it'll get auto saved shortly or when the app shuts down.
                            System.out.println("Sent coins onwards! Transaction hash is " + sendResult.tx.getHashAsString());
                        }
                    }, runInUIThread);
                } catch (InsufficientMoneyException e) {
                    e.printStackTrace();
                }
                System.out.println("Sending ...");


                Address a = wallet1.currentReceiveAddress();
                ECKey b = wallet1.currentReceiveKey();
                Address c = wallet1.freshReceiveAddress();

                b.toAddress(wallet1.getParams()).equals(a);


                DeterministicSeed seed = wallet1.getKeyChainSeed();
                println("Seed words are: " + Joiner.on(" ").join(seed.getMnemonicCode()));
                println("Seed birthday is: " + seed.getCreationTimeSeconds());

                String seedCode = "yard impulse luxury drive today throw farm pepper survey wreck glass federal";
                long creationtime = 1409478661L;

                try {
                    DeterministicSeed seed2 = null;
                    seed2 = new DeterministicSeed(seedCode, null, "", creationtime);
                    Wallet restoredWallet = Wallet.fromSeed(params, seed2);
                } catch (UnreadableWalletException e) {
                    e.printStackTrace();
                }

// now sync the restored wallet as described below.

//                println("You have " + amountToSend.FRIENDLY_FORMAT.format(wallet1.getBalance()));
//
//                // Get the address 1RbxbA1yP2Lebauuef3cBiBho853f7jxs in object form.
//                Address targetAddress = new Address(params, "1RbxbA1yP2Lebauuef3cBiBho853f7jxs");
//// Do the send of 1 BTC in the background. This could throw InsufficientMoneyException.
//                Wallet.SendResult result = wallet1.sendCoins(peerGroup, targetAddress, Coin.COIN);
//// Save the wallet to disk, optional if using auto saving (see below).
//                wallet1.saveToFile(....);
//// Wait for the transaction to propagate across the P2P network, indicating acceptance.
//                Transaction transaction = result.broadcastComplete.get();
//                transaction.toString()

                wallet1.getEarliestKeyCreationTime();


                break;
            case R.id.btn_wallet_sign:
                btc_sign_msg = signMsg("我是张二胖", master.getPrivateKey());
                Log.i("tttttttttttttt", "onViewClicked: 我们签名" + btc_sign_msg);
                break;
            case R.id.btn_wallet_verify:
//                Intent intent = new Intent(this, BitcoinTransactionActivity.class);
//                startActivity(intent);
                break;
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
            params.add(new String[]{"mtoaVrQp3kZC79RTTtMJ7PiM2jSjZbRhby"});
            listunspentParams.put("params", params);
            Gson gson = new Gson();
            Response response = HttpClientUtil.getInstance().postBtcJson(gson.toJson(listunspentParams));
            emitter.onNext(response);
        }).map(response -> {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    return new Gson().fromJson(body.string(), GetUtxoBean.class);
                }
            }
            return null;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    Log.e(TAG, "map 线程:" + Thread.currentThread().getName() + "--------"+baseEntity.getResult().size());
                });
    }




    Executor runInUIThread = runnable -> {
        // For Android: handler was created in an Activity.onCreate method.
        handler.post(runnable);
    };

    /**
     * @param msg        要签名的信息
     * @param privateKey 私钥
     * @return
     */
    public String signMsg(@NonNull String msg, @NonNull String privateKey) {
        NetworkParameters networkParameters = null;
        if (!bit_test_net)
            networkParameters = MainNetParams.get();
        else
            networkParameters = TestNet3Params.get();
        DumpedPrivateKey priKey = DumpedPrivateKey.fromBase58(networkParameters, privateKey);
        ECKey ecKey = priKey.getKey();
        return ecKey.signMessage(msg);
    }

    /**
     * @param msg          明文
     * @param signatureMsg 签名好的信息
     * @param pubkey       公钥
     * @return
     */
    public boolean verifyMessage(@NonNull String msg, @NonNull String signatureMsg, @NonNull String pubkey) {
        boolean result = false;
        ECKey ecKey = ECKey.fromPublicOnly(Utils.HEX.decode(pubkey));
        try {
            ecKey.verifyMessage(msg, signatureMsg);
            result = true;
        } catch (SignatureException e) {
            result = false;
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}