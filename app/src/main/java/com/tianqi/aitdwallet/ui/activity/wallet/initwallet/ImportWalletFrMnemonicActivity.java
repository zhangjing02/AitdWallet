package com.tianqi.aitdwallet.ui.activity.wallet.initwallet;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.WalletCoinAdapter;
import com.tianqi.aitdwallet.ui.activity.MainActivityForTab;
import com.tianqi.aitdwallet.service.DataManageService;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.utils.WalletUtils;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.CoinRateInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.CoinRateInfoManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.rxtool.RxToolUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class ImportWalletFrMnemonicActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_create_wallet_tittle)
    ImageView ivCreateWalletTittle;
    @BindView(R.id.tv_create_wallet_notice)
    TextView tvCreateWalletNotice;
    @BindView(R.id.gv_wallet_coin)
    GridView gvWalletCoin;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;
    private WalletCoinAdapter walletCoinAdapter;
    private List<Map<String, Object>> coin_list;
    private int index_loading = -1;
    private List<String> list;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<CoinRateInfo> coinRateBeans;
    private Gson gson;
    private DataManageService service = null;
    private boolean isBind = false;
    private AnimationDrawable animationDrawable;

    @Override
    protected int getContentView() {
        return R.layout.activity_create_wallet;
    }

    @Override
    protected void initView() {
        getToolBar();
        Intent intent = new Intent(this, DataManageService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        ivCreateWalletTittle.setBackground(null);
        ivCreateWalletTittle.setImageResource(R.drawable.anim_create_wallet);
        animationDrawable = (AnimationDrawable) ivCreateWalletTittle.getDrawable();
        if(!animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            DataManageService.MyBinder myBinder = (DataManageService.MyBinder) binder;
            service = myBinder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    private void initLoadingCoin() {
        coin_list = new ArrayList<>();
        Map<String, Object> map = new HashMap();
        map.put(Constant.ACTION_IMAGE, R.mipmap.ic_circle_unloading_btc);
        coin_list.add(map);

        map = new HashMap();
        map.put(Constant.ACTION_IMAGE, R.mipmap.ic_circle_unloading_eth);
        coin_list.add(map);

        map = new HashMap();
        map.put(Constant.ACTION_IMAGE, R.mipmap.ic_circle_unloading_usdt);
        coin_list.add(map);

        map = new HashMap();
        map.put(Constant.ACTION_IMAGE, R.mipmap.ic_circle_unloading_usdt);
        coin_list.add(map);

//        map = new HashMap();
//        map.put(Constant.ACTION_IMAGE, R.mipmap.ic_circle_unloading_aitd);
//        coin_list.add(map);

        walletCoinAdapter = new WalletCoinAdapter(this, coin_list);
        gvWalletCoin.setAdapter(walletCoinAdapter);

    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_import_wallet);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        String get_mnemonic_str = getIntent().getStringExtra(Constants.INTENT_PUT_MNEMONIC);
        list = Arrays.asList(get_mnemonic_str.split(" "));

        initLoadingCoin();
        Observable.create((ObservableOnSubscribe<ECKeyPair>) emitter -> {
            index_loading++;
            walletCoinAdapter.loadingIndex(index_loading);
            //此处的文件夹只做临时存储，便于后两个页面获取wallet，正式的wallet存储文件夹，不用这个。
            ECKeyPair master = WalletUtils.importCoinMaser(CoinTypes.Bitcoin, list);
            emitter.onNext(master);
        }).delay(2000, TimeUnit.MILLISECONDS).map(master -> {
            //1.创建btc币种。
            //钱包数据库
            WalletInfo walletInfo = createWalletInfo(master.getAddress(), Constant.TRANSACTION_COIN_NAME_BTC);
            //币种数据库
            insertCoinInfo(master, walletInfo);
            runOnUiThread(() -> {  //只能在主线程更新ui
                index_loading++;
                walletCoinAdapter.loadingIndex(index_loading);
            });
            return walletInfo;
        }).delay(2000, TimeUnit.MILLISECONDS).map(walletInfo -> {
                    //创建ETH币种。
                    //钱包数据库
                    ECKeyPair master = WalletUtils.importCoinMaser(CoinTypes.Ethereum, list);
                    WalletInfo eht_walletInfo = createWalletInfo(master.getAddress(), Constant.TRANSACTION_COIN_NAME_ETH);

                    //币种数据库
                    insertCoinInfo(master, eht_walletInfo);
                    // TODO: 2020/10/10 随后应该加入盼错逻辑，即使报错，也应该继续更新进度。
                    runOnUiThread(() -> {   //只能在主线程更新ui
                        index_loading++;
                        walletCoinAdapter.loadingIndex(index_loading);
                    });
                    return eht_walletInfo;
        }).delay(2000, TimeUnit.MILLISECONDS).map(walletInfo -> {
            //创建USDT币种。
            //币种数据库,USDT用的东西都是比特币那一套，所以都用bitcoin的方式创建
            ECKeyPair master = WalletUtils.importCoinMaser(CoinTypes.Bitcoin,list);
            WalletInfo usdt_walletInfo = createWalletInfo(master.getAddress(), Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            insertCoinInfo(master, usdt_walletInfo);

            runOnUiThread(() -> {  //只能在主线程更新ui
                index_loading++;
                walletCoinAdapter.loadingIndex(index_loading);
            });
            return usdt_walletInfo;
        }).delay(2000, TimeUnit.MILLISECONDS).map(walletInfo -> {
            //创建ETH的代币---usdt-erc20。
            //钱包数据库
            ECKeyPair master = WalletUtils.importCoinMaser(CoinTypes.Ethereum, list);
            WalletInfo eht_walletInfo = createWalletInfo(master.getAddress(), Constant.TRANSACTION_COIN_NAME_USDT_ERC20);

            //币种数据库
            insertCoinInfo(master, eht_walletInfo);
            // TODO: 2020/10/10 随后应该加入盼错逻辑，即使报错，也应该继续更新进度。
            runOnUiThread(() -> {   //只能在主线程更新ui
                index_loading++;
                walletCoinAdapter.loadingIndex(index_loading);
            });
            return eht_walletInfo;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    tvCreateWalletNotice.setText(R.string.notice_create_success);

                    if (animationDrawable.isRunning()){
                        animationDrawable.stop();
                    }
                    ivCreateWalletTittle.setImageResource(R.drawable.anim_create_wallet_successful);
                    animationDrawable = (AnimationDrawable) ivCreateWalletTittle.getDrawable();
                    if(!animationDrawable.isRunning()){
                        animationDrawable.start();
                    }
                    RxToolUtil.cancel();
                    btnCreateWallet.setVisibility(View.VISIBLE);
                });
    }

    private WalletInfo createWalletInfo(String address, String wallet_id) {
        //钱包数据库。
        UserInformation userInfo = UserInfoManager.getUserInfo();
        WalletInfo walletInfo = new WalletInfo();
        walletInfo.setUserId(userInfo.getUserId());
        walletInfo.setWalletName(wallet_id);
        walletInfo.setAlias_name(wallet_id);
        walletInfo.setWalletType(Constant.WALLET_TYPE_HD);
        walletInfo.setWallet_id(wallet_id);
        walletInfo.setIsImportToCreate(true);
        if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getCoinRatefoFrCoinId(Constant.TRANSACTION_COIN_NAME_BTC);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            }
            walletInfo.setResource_id(R.mipmap.ic_circle_btc);
        } else if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getCoinRatefoFrCoinId(Constant.TRANSACTION_COIN_NAME_ETH);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            }
            walletInfo.setResource_id(R.mipmap.ic_circle_eth);
        } else if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getCoinRatefoFrCoinId(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            }
            walletInfo.setResource_id(R.mipmap.ic_circle_usdt_omni);
        }else if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getCoinRatefoFrCoinId(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            }
            walletInfo.setResource_id(R.mipmap.ic_circle_usdt_erc20);
        }

        WalletInfoManager.insertOrUpdate(walletInfo);
        return walletInfo;
    }

    private void insertCoinInfo(ECKeyPair master, WalletInfo walletInfo) {
        CoinInfo coinInfo = new CoinInfo();
        coinInfo.setCoin_address(master.getAddress());
        coinInfo.setPrivateKey(master.getPrivateKey());
        coinInfo.setPublicKey(master.getPublicKey());
        coinInfo.setWallet_id(walletInfo.getWallet_id());
        if (walletInfo.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            List<CoinInfo> walletBtcInfo = CoinInfoManager.getWalletBtcInfo();
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_BTC);
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_IMPORT);
            coinInfo.setCoin_id(Constant.TRANSACTION_COIN_NAME_BTC+walletBtcInfo.size());
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_BTC);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_BTC);
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_BTC);
            coinInfo.setResourceId(R.mipmap.ic_circle_btc);
        } else if (walletInfo.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            List<CoinInfo> walletBtcInfo = CoinInfoManager.getCoinEthImportInfo();
            coinInfo.setCoin_id(Constant.TRANSACTION_COIN_NAME_ETH+walletBtcInfo.size());
            coinInfo.setCoin_address(Constants.HEX_PREFIX + master.getAddress());
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_ETH);
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_IMPORT);
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_ETH);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_ETH);
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_ETH);
            coinInfo.setResourceId(R.mipmap.ic_circle_eth);
//            UserInformation information = UserInfoManager.getUserInfo();
//            String aes_decode_str = AESCipher.decrypt(Constant.PSD_KEY, information.getPasswordStr());
//            EthECKeyPair ethECKeyPair = null;
//            try {
//                ethECKeyPair = new EthECKeyPair(HexUtils.fromHex(master.getPrivateKey()));
//                KeyStoreFile light = KeyStore.createLight(aes_decode_str, ethECKeyPair);
//                String keystore_str = light.toString();
//                coinInfo.setKeystoreStr(keystore_str);
//            } catch (ValidationException e) {
//                e.printStackTrace();
//            } catch (CipherException e) {
//                e.printStackTrace();
//            }
            //保存一个文件形式，方便加载的时候，能很快加载出钱包。否则每次去生成会很慢。
            //把keystore的生成，放到服务后台去生成，不用在此浪费加载时间。
            if (isBind){
                service.createEthWallet(coinInfo);
            }
        } else if (walletInfo.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            List<CoinInfo> walletUsdtInfo = CoinInfoManager.getWalletUsdtInfo();
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_USDT_OMNI);
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_IMPORT);
            coinInfo.setCoin_id(Constant.TRANSACTION_COIN_NAME_USDT_OMNI +walletUsdtInfo.size());
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_USDT);
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            coinInfo.setResourceId(R.mipmap.ic_circle_usdt_omni);
        }else if (walletInfo.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)) {
            List<CoinInfo> walletBtcInfo = CoinInfoManager.getCoinEthImportInfo();
            coinInfo.setCoin_id(Constant.TRANSACTION_COIN_NAME_USDT_ERC20+walletBtcInfo.size());
            coinInfo.setCoin_address(Constants.HEX_PREFIX + master.getAddress());
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_USDT_ERC20);
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_IMPORT);
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_ETH);
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
            coinInfo.setResourceId(R.mipmap.ic_circle_usdt_erc20);
//            UserInformation information = UserInfoManager.getUserInfo();
//            String aes_decode_str = AESCipher.decrypt(Constant.PSD_KEY, information.getPasswordStr());
//            EthECKeyPair ethECKeyPair = null;
//            try {
//                ethECKeyPair = new EthECKeyPair(HexUtils.fromHex(master.getPrivateKey()));
//                KeyStoreFile light = KeyStore.createLight(aes_decode_str, ethECKeyPair);
//                String keystore_str = light.toString();
//                coinInfo.setKeystoreStr(keystore_str);
//            } catch (ValidationException e) {
//                e.printStackTrace();
//            } catch (CipherException e) {
//                e.printStackTrace();
//            }
            if (isBind){
                service.createEthWallet(coinInfo);
            }

        }
        // coinInfo.setIsCollect();
        //  coinInfo.setWalletLimit();  //不需要限制。
        LogUtil.i("WalletFragment", "insertBtcCoinInfo: 我们看插入的币种信息是？" + coinInfo.toString());
        CoinInfoManager.insertOrUpdate(coinInfo);
    }


    @OnClick(R.id.btn_create_wallet)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivityForTab.class);
        startActivity(intent);
        finish();
    }
}