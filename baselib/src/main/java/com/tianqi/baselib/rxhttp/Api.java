package com.tianqi.baselib.rxhttp;

import com.tianqi.baselib.rxhttp.base.BaseEntity;
import com.tianqi.baselib.rxhttp.bean.CoinRateBean;
import com.tianqi.baselib.rxhttp.bean.GetErc20BalanceBean;
import com.tianqi.baselib.rxhttp.bean.GetErc20TxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetEthTxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetFormalUtxoBean;
import com.tianqi.baselib.rxhttp.bean.GetListUnspentBean;
import com.tianqi.baselib.rxhttp.bean.GetLoadingTxBean;
import com.tianqi.baselib.rxhttp.bean.GetNewVersionBean;
import com.tianqi.baselib.rxhttp.bean.GetSimpleRpcBean;
import com.tianqi.baselib.rxhttp.bean.GetTxDetailBean;
import com.tianqi.baselib.rxhttp.config.HttpConfig;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface Api {
    /**
     * 獲取用戶數據
     */
    @POST("SocialFinance/user/token/login")
    Observable<BaseEntity<Object>> token_Login(@Body Map<String, Object> map);

    /**
     * 獲取用戶數據
     */
    @POST("/createwallet")
    Observable<BaseEntity<Object>> create_wallet(@Body Map<String, Object> map);


    /**
     *  获取BTC钱包地址余额（1/1是余额，1/50是交易记录）
     */
    @GET("/vipapi//address/{coin_type}/{btc_address}/{type}?")
    Observable<BaseEntity<List<GetListUnspentBean>>> getBtcAddressBalance(@Path("coin_type") String coin_type
            ,@Path("btc_address") String btc_address , @Path("type") String type, @QueryMap Map<String, Object> map);



    @GET("vipapi/market/exchange?")
    Observable<CoinRateBean> getCoinRate(@QueryMap Map<String, Object> map);


    /**
     *  btc的广播交易。
     *
     */
    @POST("/vipapi/onchainwallet/btc?")
    Observable<GetSimpleRpcBean> sendrawtransaction(@QueryMap Map<String, Object> map1, @Body Map<String, Object> map2);


    /**
     *  获取btc的utxo，未花费，用于交易。
     *
     */
    @GET("/vipapi/unspent/btc/{btc_address}/{type}?")
    Observable<BaseEntity<List<GetFormalUtxoBean>>> getUtxoForTx(@Path("btc_address") String btc_address , @Path("type") String type, @QueryMap Map<String, Object> map);
    /**
     *  获取btc的utxo，未花费，用于交易。
     *
     */
    @GET("/vipapi/unspent/btc/{btc_address}/{type}?")
    Observable<BaseEntity<List<GetFormalUtxoBean>>> getUtxoForTx(@Path("btc_address") String btc_address , @Path("type") String type);


    /**
     *  获取交易详情。
     *
     */
    @GET("/vipapi/tx/{coin_type}/{tx_id}?")
    Observable<BaseEntity<GetTxDetailBean>> getTxDetail(@Path("coin_type") String coin_type , @Path("tx_id") String tx_id , @QueryMap Map<String, Object> map);


    /**
     *  获取正在确认的交易
     *
     */
    @GET("/vipapi/pending/ntx/{coin_type}/{coin_address}?")
    Observable<BaseEntity<List<GetLoadingTxBean>>> getLoadingTx(@Path("coin_type") String coin_type , @Path("coin_address") String coin_address , @QueryMap Map<String, Object> map);


    /**
     *  版本更新
     *
     */
    @GET(HttpConfig.APP_VERSION_UPDATE_METHOD)
    Observable<BaseEntity<GetNewVersionBean>> getNewVersion();


    /**
     *  获取ETH钱包地址余额
     */
    @GET("/vipapi/addr/b/{coin_type}/{eth_address}/?")
    Observable<BaseEntity<String>> getEthAddressBalance(@Path("coin_type") String coin_type,@Path("eth_address") String btc_address ,@QueryMap Map<String, Object> map);


    /**
     *  获取ERC20钱包地址余额
     */
    @GET("/vipapi/{coin_type}/address/tokenbalance/{eth_address}/??")
    Observable<BaseEntity<List<GetErc20BalanceBean>>> getErc20AddressBalance(@Path("coin_type") String coin_type, @Path("eth_address") String btc_address, @QueryMap Map<String, Object> map);


    /**
     *  获取ETH钱包地址交易历史
     */
    @GET("/vipapi/{coin_type}/address/{eth_address}/?")
    Observable<BaseEntity<GetEthTxRecordBean>> getEthTxRecord(@Path("coin_type") String coin_type, @Path("eth_address") String btc_address , @QueryMap Map<String, Object> map);


    /**
     *  获取ETH钱包地址交易历史
     */
    @GET("/vipapi/eth/address/tokentrans/{eth_address}/{contract_address}/{page}?")
    Observable<BaseEntity<List<GetErc20TxRecordBean>>> getErc20TxRecord(@Path("eth_address") String eth_address, @Path("contract_address") String contract_address ,
                                                                        @Path("page") String page , @QueryMap Map<String, Object> map);

}
