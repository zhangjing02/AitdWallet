package com.tianqi.aitdwallet.ui.activity.wallet.initwallet;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.WalletCoinAdapter;
import com.tianqi.aitdwallet.ui.activity.MainActivity;
import com.tianqi.aitdwallet.utils.WalletUtils;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.CoinRateInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.CoinRateInfoManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.HttpClientUtil;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.rxtool.RxToolUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Response;

public class CreateWalletActivity extends BaseActivity {

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

    @Override
    protected int getContentView() {
        return R.layout.activity_create_wallet;
    }

    @Override
    protected void initView() {
        getToolBar();
    }

    private void initLoadingCoin() {
        coin_list = new ArrayList<>();
        Map<String, Object> map = new HashMap();
        map.put(Constant.ACTION_IMAGE, R.mipmap.ic_circle_unloading_btc);
        coin_list.add(map);

//        map = new HashMap();
//        map.put(Constant.ACTION_IMAGE, R.mipmap.ic_circle_unloading_eth);
//        coin_list.add(map);


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
        toolbarTitle.setText(R.string.tittle_create_wallet);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        initLoadingCoin();
        Observable.create((ObservableOnSubscribe<ECKeyPair>) emitter -> {
            index_loading++;
            walletCoinAdapter.loadingIndex(index_loading);
            //此处的文件夹只做临时存储，便于后两个页面获取wallet，正式的wallet存储文件夹，不用这个。
            ECKeyPair master = WalletUtils.createCoinMaser(CoinTypes.Bitcoin);
            emitter.onNext(master);
        }).map(master -> {
            //获取各个币种的汇率，存入币种汇率的数据库中。
            gson = new Gson();
//            Response response = HttpClientUtil.getInstance().getJson("https://fxhapi.feixiaohao.com/public/v1/ticker?limit=10&convert=CNY");
//            if (response != null && response.isSuccessful()) {
//                Type listType = new TypeToken<List<CoinRateInfo>>() {
//                }.getType();
//                coinRateBeans = gson.fromJson(response.body().string(), listType);
//                Log.i("WalletFragment", "initData: 插入001" + coinRateBeans.size());
//                CoinRateInfoManager.insertOrUpdateList(coinRateBeans);
//            } else {
//                Type listType = new TypeToken<List<CoinRateInfo>>() {
//                }.getType();
//                coinRateBeans = gson.fromJson(Constant.COIN_RATE_JSON, listType);
//                Log.i("WalletFragment", "initData: 插入002" + coinRateBeans.size());
//                CoinRateInfoManager.insertOrUpdateList(coinRateBeans);
//            }
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
        })
//                .map(walletInfo -> {
//            //创建ETH币种。
//            //钱包数据库
//            ECKeyPair master = WalletUtils.createCoinMaser(CoinTypes.Ethereum);
//            WalletInfo eht_walletInfo = createWalletInfo(master.getAddress(), Constant.TRANSACTION_COIN_NAME_ETH);
//            //币种数据库
//            insertCoinInfo(master, eht_walletInfo);
//
//            // TODO: 2020/10/10 随后应该加入盼错逻辑，即使报错，也应该继续更新进度。
//            runOnUiThread(() -> {   //只能在主线程更新ui
//                index_loading++;
//                walletCoinAdapter.loadingIndex(index_loading);
//            });
//            return eht_walletInfo;
//        })
                .map(walletInfo -> {
            //创建USDT币种。
            //币种数据库,USDT用的东西都是比特币那一套，所以都用bitcoin的方式创建
            ECKeyPair master = WalletUtils.createCoinMaser(CoinTypes.Bitcoin);
            WalletInfo usdt_walletInfo = createWalletInfo(master.getAddress(), Constant.TRANSACTION_COIN_NAME_USDT);
            insertCoinInfo(master, usdt_walletInfo);

            runOnUiThread(() -> {  //只能在主线程更新ui
                index_loading++;
                walletCoinAdapter.loadingIndex(index_loading);
            });
            return usdt_walletInfo;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    //模拟新建多个币种的，依次进行loading的效果，实际项目中，以创建初始化完一个币种后，再进行下一个币种的创建和初始化。
//                    RxToolUtil.interval(5000, number -> {
//                        index_loading++;
//                        walletCoinAdapter.loadingIndex(index_loading);
//                        if (index_loading >= coin_list.size()) {
//                            tvCreateWalletNotice.setText(R.string.notice_create_success);
//                            GlideUtils.loadResourceImage(this,R.mipmap.ic_create_wallet_finish,ivCreateWalletTittle);
//                            RxToolUtil.cancel();
//                            btnCreateWallet.setVisibility(View.VISIBLE);
//                        }
//                    });
                    tvCreateWalletNotice.setText(R.string.notice_create_success);
                    GlideUtils.loadResourceImage(this,R.mipmap.ic_create_wallet_finish,ivCreateWalletTittle);
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
        if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getWalletBtcFrCoinId(Constant.TRANSACTION_COIN_NAME_BTC);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            }
            walletInfo.setResource_id(R.mipmap.ic_circle_btc);
        } else if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getWalletBtcFrCoinId(Constant.TRANSACTION_COIN_NAME_ETH);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            }
            walletInfo.setResource_id(R.mipmap.ic_circle_eth);
        } else if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_USDT)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getWalletBtcFrCoinId(Constant.TRANSACTION_COIN_NAME_USDT);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            }
            walletInfo.setResource_id(R.mipmap.ic_circle_usdt);
        }

        WalletInfoManager.insertOrUpdate(walletInfo);
        return walletInfo;
    }

    private void insertCoinInfo(ECKeyPair master, WalletInfo walletInfo) {
        CoinInfo coinInfo = new CoinInfo();
        coinInfo.setCoin_address(master.getAddress());
        if (walletInfo.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_BTC);
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_CREATE);
            coinInfo.setCoin_id(Constant.TRANSACTION_COIN_NAME_BTC);
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_BTC);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_BTC);
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_BTC);
            coinInfo.setResourceId(R.mipmap.ic_circle_btc);
        } else if (walletInfo.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_ETH);
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_CREATE);
            coinInfo.setCoin_id(Constant.TRANSACTION_COIN_NAME_ETH);
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_ETH);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_USDT);
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_ETH);
            coinInfo.setResourceId(R.mipmap.ic_circle_eth);
        } else if (walletInfo.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_USDT)) {
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_USDT);
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_CREATE);
            coinInfo.setCoin_id(Constant.TRANSACTION_COIN_NAME_USDT);
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_USDT);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_USDT);
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_USDT);
            coinInfo.setResourceId(R.mipmap.ic_circle_usdt);
        }
        // coinInfo.setIsCollect();
        coinInfo.setPrivateKey(master.getPrivateKey());
        coinInfo.setPublicKey(master.getPublicKey());
        coinInfo.setWallet_id(walletInfo.getWallet_id());

        //  coinInfo.setWalletLimit();  //不需要限制。
        Log.i("WalletFragment", "insertBtcCoinInfo: 我们看插入的币种信息是？" + coinInfo.toString());
        CoinInfoManager.insertOrUpdate(coinInfo);
    }



    @OnClick(R.id.btn_create_wallet)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}