package com.tianqi.baselib.rxhttp;

import com.tianqi.baselib.rxhttp.config.HttpConfig;
import com.tianqi.baselib.utils.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OKHttp3
 * <p>
 * Description:
 * </p>
 *
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-07-29 14:05:08
 * @see
 */
public class HttpClientUtil {
    private static final int READ_TIMEOUT = 100;
    private static final int CONNECT_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType URLENCODED = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    private static final byte[] LOCKER = new byte[0];
    private static HttpClientUtil mInstance;
    private OkHttpClient okHttpClient;
    private static final String TAG = "HttpClientUtil";

    private HttpClientUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        // 读取超时
        clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        // 连接超时
        clientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        //写入超时
        clientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient = clientBuilder.build();
    }

    /**
     * 单例模式获取 NetUtils
     *
     * @return {@link HttpClientUtil}
     */
    public static HttpClientUtil getInstance() {
        if (mInstance == null) {
            synchronized (LOCKER) {
                mInstance = new HttpClientUtil();
            }
        }
        return mInstance;
    }

//    public Response postJson(String json) throws IOException {
//        String credential = Credentials.basic(HttpConfig.RPC_HEADER_USER, HttpConfig.RPC_HEADER_PSD);
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(HttpConfig.BASE_BTC_URL)
//                .post(body)
//                .addHeader(Constant.TOKEN_NAME, credential)
//                .build();
//        Response response =okHttpClient.newCall(request).execute();
//        if (response.isSuccessful()) {
//            return response;
//        } else {
//            throw new IOException("Unexpected code " + response.message());
//        }
//    }

    public Response postBtcJson(String json) {
        String credential = Credentials.basic(HttpConfig.RPC_HEADER_USER, HttpConfig.RPC_HEADER_PSD);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(HttpConfig.BASE_BTC_URL)
                .post(body)
                .addHeader(Constant.TOKEN_NAME, credential)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                // TODO: 2020/9/21 此处要处理异常，不能这样抛异常，会引起闪退。
                return null;
                //  throw new IOException("Unexpected code " + response.message());
            }
        } catch (IOException e) {
            return null;
            //  e.printStackTrace();
        }
    }


    public Response postFormalBtcJson(String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(HttpConfig.BASE_BTC_FORMAL_URL)
                .post(body)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                // TODO: 2020/9/21 此处要处理异常，不能这样抛异常，会引起闪退。
                return null;
                //  throw new IOException("Unexpected code " + response.message());
            }
        } catch (IOException e) {
            return null;
            //  e.printStackTrace();
        }
    }


    public Response postFormalUsdtJson(String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(HttpConfig.BASE_USDT_FORMAL_URL)
                .post(body)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                // TODO: 2020/9/21 此处要处理异常，不能这样抛异常，会引起闪退。
                return null;
                //  throw new IOException("Unexpected code " + response.message());
            }
        } catch (IOException e) {
            return null;
            //  e.printStackTrace();
        }
    }

    public Response postUsdtJson(String json) {
        String credential = Credentials.basic(HttpConfig.RPC_HEADER_USER, HttpConfig.RPC_HEADER_PSD);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(HttpConfig.BASE_USDT_URL)
                .post(body)
                .addHeader(Constant.TOKEN_NAME, credential)
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response;
            } else {
                // TODO: 2020/9/21 此处要处理异常，不能这样抛异常，会引起闪退。
                return null;
                //  throw new IOException("Unexpected code " + response.message());
            }
        } catch (IOException e) {
            return null;
            //  e.printStackTrace();
        }
    }


    public Response getJson(String baseUrl) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl)
                // .get()
                .method("GET", null)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;


//        if (response.isSuccessful()) {
//            return response;
//        } else {
//            // TODO: 2020/9/21 此处要处理异常，不能这样抛异常，会引起闪退。
//            throw new IOException("Unexpected code " + response.message());
//        }
    }


    public Response getFormalUtxoJson(String baseUrl) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl)
                // .get()
                .method("GET", null)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response;


//        if (response.isSuccessful()) {
//            return response;
//        } else {
//            // TODO: 2020/9/21 此处要处理异常，不能这样抛异常，会引起闪退。
//            throw new IOException("Unexpected code " + response.message());
//        }
    }
}
