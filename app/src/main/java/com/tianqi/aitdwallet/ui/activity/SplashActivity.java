package com.tianqi.aitdwallet.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinRateInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinRateInfoManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.HttpClientUtil;
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.CoinRateBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.LocaleUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {
    private UserInformation userInfo;
    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        userInfo = UserInfoManager.getUserInfo();
        if (userInfo!=null){
            int languageId = userInfo.getLanguageId();
            if (languageId== Constants.LANGUAGE_CHINA){  //设置语言，如果系统的语言和app设置的语言不统一，我们就设置成app需要的语言。
                if (LocaleUtils.needUpdateLocale(SplashActivity.this, LocaleUtils.LOCALE_CHINESE)){
                    LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_CHINESE);
                }
            }else if (languageId==Constants.LANGUAGE_ENGLISH){
                if (LocaleUtils.needUpdateLocale(SplashActivity.this, LocaleUtils.LOCALE_ENGLISH)){
                    LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH);
                }
            }
        }

        //获取各个币种的汇率，存入币种汇率的数据库中。
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        //获取各个币种的汇率，存入币种汇率的数据库中。
        RetrofitFactory.getInstence(this).API()
                .getCoinRate(map).compose(RxHelper.io_main())
                .subscribe(new Observer<CoinRateBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onNext(CoinRateBean coinRateBean) {
                        WalletInfo walletInfo= WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_BTC);
                        CoinRateInfo coinRateInfo=new CoinRateInfo();
                        coinRateInfo.setId(Constant.TRANSACTION_COIN_NAME_BTC);
                        coinRateInfo.setPrice_usd(coinRateBean.getBtc());
                        double value1,value2,value3;
                        value1=coinRateBean.getBtccny1();
                        value2=coinRateBean.getBtccny2();
                        value3=coinRateBean.getBtccny3();
                        coinRateInfo.setPrice_cny((value1+value2+value3)/3f);

                        CoinRateInfoManager.insertOrUpdate(coinRateInfo);

                        if (walletInfo!=null){
                            walletInfo.setCoin_USDPrice(coinRateInfo.getPrice_usd());
                            walletInfo.setCoin_CNYPrice(coinRateInfo.getPrice_cny());
                            WalletInfoManager.insertOrUpdate(walletInfo);
                        }

                        walletInfo= WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_ETH);
                        coinRateInfo=new CoinRateInfo();
                        coinRateInfo.setId(Constant.TRANSACTION_COIN_NAME_ETH);
                        coinRateInfo.setPrice_usd(coinRateBean.getEth());

                        value1=coinRateBean.getEthcny1();
                        value2=coinRateBean.getEthcny2();
                        value3=coinRateBean.getEthcny3();
                        coinRateInfo.setPrice_cny((value1+value2+value3)/3f);

                        CoinRateInfoManager.insertOrUpdate(coinRateInfo);

                        if (walletInfo!=null){
                            walletInfo.setCoin_USDPrice(coinRateInfo.getPrice_usd());
                            walletInfo.setCoin_CNYPrice(coinRateInfo.getPrice_cny());
                            WalletInfoManager.insertOrUpdate(walletInfo);
                        }

                        walletInfo= WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
                        coinRateInfo=new CoinRateInfo();
                        coinRateInfo.setId(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
                        coinRateInfo.setPrice_usd(coinRateBean.getUsdt());

                        value1=coinRateBean.getUsdtcny1();
                        value2=coinRateBean.getUsdtcny2();
                        value3=coinRateBean.getUsdtcny3();
                        coinRateInfo.setPrice_cny(value1+value2+value3/3f);;

                        CoinRateInfoManager.insertOrUpdate(coinRateInfo);

                        if (walletInfo!=null){
                            walletInfo.setCoin_USDPrice(coinRateInfo.getPrice_usd());
                            walletInfo.setCoin_CNYPrice(coinRateInfo.getPrice_cny());
                            WalletInfoManager.insertOrUpdate(walletInfo);
                        }

                        walletInfo= WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
                        coinRateInfo=new CoinRateInfo();
                        coinRateInfo.setId(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
                        coinRateInfo.setPrice_usd(coinRateBean.getUsdt());

                        value1=coinRateBean.getUsdtcny1();
                        value2=coinRateBean.getUsdtcny2();
                        value3=coinRateBean.getUsdtcny3();
                        coinRateInfo.setPrice_cny(value1+value2+value3/3f);;

                        CoinRateInfoManager.insertOrUpdate(coinRateInfo);

                        if (walletInfo!=null){
                            walletInfo.setCoin_USDPrice(coinRateInfo.getPrice_usd());
                            walletInfo.setCoin_CNYPrice(coinRateInfo.getPrice_cny());
                            WalletInfoManager.insertOrUpdate(walletInfo);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (WalletInfoManager.getWalletInfo().size() > 0) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);

//                            WalletInfo walletInfo = WalletInfoManager.getWalletInfo().get(0);
//                            if (walletInfo != null && walletInfo.getWalletType() >= 0 && !walletInfo.getIsImportToCreate()) {
//                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
//                                Intent intent = new Intent(SplashActivity.this, BackupMemoryWordActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(SplashActivity.this, SetSecurityPsdActivity.class);
//                                intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_CREATE_WALLET);
//                                //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
//                                startActivity(intent);
//                            }
                        } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
                            Intent intent=new Intent(SplashActivity.this,GuidePageActivity.class);
                            startActivity(intent);
//
//                            Intent intent = new Intent(SplashActivity.this, BackupMemoryWordActivity.class);
//                            startActivity(intent);
                        } else {
                            Intent intent=new Intent(SplashActivity.this,GuidePageActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                    @Override
                    public void onComplete() {
                        if (WalletInfoManager.getWalletInfo().size() > 0) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);

//                            WalletInfo walletInfo = WalletInfoManager.getWalletInfo().get(0);
//                            if (walletInfo != null && walletInfo.getWalletType() >= 0 && !walletInfo.getIsImportToCreate()) {
//                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                                startActivity(intent);
//                            } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
//                                Intent intent = new Intent(SplashActivity.this, BackupMemoryWordActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(SplashActivity.this, SetSecurityPsdActivity.class);
//                                intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_CREATE_WALLET);
//                                //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
//                                startActivity(intent);
//                            }
                        } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
                            Intent intent=new Intent(SplashActivity.this,GuidePageActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent=new Intent(SplashActivity.this,GuidePageActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                });
    }

    @Override
    protected void initData() {
        //获取各个币种的汇率，存入币种汇率的数据库中。
//        Map<String,Object>map=new HashMap<>();
//        map.put("type",2);
//        RetrofitFactory.getInstence(this).API()
//                .getVersionList(map).compose(RxHelper.io_main())
//                .subscribe(new BaseObserver<Object>(this) {
//                    @Override
//                    public void onSuccess(Object data, String msg) {
//                        Log.i("ttttttttttttttt", "onSuccess: ");
//                    }
//                    @Override
//                    protected void onFailure(int code, String msg) {
//                    }
//                });

    }

}