package com.tianqi.baselib.rxhttp.config;

import retrofit2.http.PUT;

/**
 * @author yemao
 * @date 2017/4/9
 * @description 关于网络的配置
 */

public class HttpConfig {
    public static int HTTP_TIME = 20000;
    // public static String BASE_URL = "http://192.168.0.102/";
    // public static String BASE_URL = "";
    public static final String ZJ_HTTP = "zhangjing-http";
    //public static final String BASE_URL = "http://52.77.221.113:9007/"; //新環境测试环境
   // public static final String BASE_URL = "https://api-test.aitdcoin.com/"; //新環境测试环境

   // public static final String BASE_URL = "http://192.168.3.10:2888"; //钱包开发域名
   // public static final String BASE_URL = "http://wallet-admin-dev.aitdcoin.com"; //钱包开发域名
    public static final String BASE_URL = "http://192.168.1.11:18443"; //钱包开发域名
    public static final String BASE_FORMAL_URL = "https://services.tokenview.com"; //钱包开发域名正式服用的地址

    public static final String BASE_BTC_FORMAL_URL = "https://services.tokenview.com/vipapi/onchainwallet/btc?apikey=AnqHS6Rs2WX0hwFXlrv"; //btc钱包正式网广播交易地址。
    public static final String BASE_USDT_FORMAL_URL = "https://services.tokenview.com/vipapi/onchainwallet/usdt?apikey=AnqHS6Rs2WX0hwFXlrv"; //usdt钱包正式网广播交易地址。


    public static final String BASE_BTC_URL = "http://192.168.1.16:18332"; //钱包开发域名
    public static final String BASE_USDT_URL = "http://192.168.1.16:18332"; //钱包开发域名
    public static final String RPC_HEADER_USER = "dev";  //比特币rpc请求头的用户名
    public static final String RPC_HEADER_PSD= "a";      //比特币rpc请求头的密码




    public static final String APP_VERSION_UPDATE_URL= "http://wallet.aitdcoin.com/WalletAdmin/app/new/edition?type=1";      //版本更新所使用的的接口地址。
    public static final String APP_VERSION_UPDATE_METHOD="WalletAdmin/app/new/edition";

    public static final String PRIVACY_TERMS_URL= "http://wallet.aitdcoin.com/aitd-coin.html";      //隐私条款的跳转地址。

}
