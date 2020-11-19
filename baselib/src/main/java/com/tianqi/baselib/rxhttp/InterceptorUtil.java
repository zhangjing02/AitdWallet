package com.tianqi.baselib.rxhttp;

import android.content.Context;
import android.util.Log;


import com.tianqi.baselib.dbManager.PrefUtils;
import com.tianqi.baselib.rxhttp.config.HttpConfig;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;

/**
 * @author zhangjing
 * @date 2018/12/3
 * @description 拦截器工具类!
 */
public class InterceptorUtil {
   // private static final String TAG = "InterceptorUtil";
    private static final String TAG = HttpConfig.ZJ_HTTP;
    private static String Token = "";
    public final static Charset UTF8 = Charset.forName("UTF-8");
    private static String params;
    private static long time_before;

    /**
     * 日志拦截器
     *
     * @return
     */
    public static HttpLoggingInterceptor LogInterceptor() {
        //  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("OKHttp----------------", message);
               /* try {
                    String text = URLDecoder.decode(message, "utf-8");
                    Log.e("OKHttp----------------", text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e("OKHttp  Error---------", message);
                }*/
            }
        });
        //这里可以builder(). 添加更多的内容 具体看需求
        //  mClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        //这行必须加 不然默认不打印
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    /**
     * token验证的拦截器
     *
     * @return
     */
    public static Interceptor tokenInterceptor(final Context context) {
        return chain -> {
            Request.Builder builder = chain.request().newBuilder();
           // builder.addHeader(Constant.TOKEN_NAME, PrefUtils.getString(context, Constant.ACCESS_TOKEN, ""));
            String str_token = PrefUtils.getString(context, PrefUtils.HTTP_TOKEN, "");
            String credential = Credentials.basic("dev", "a");

            builder.header(Constant.TOKEN_NAME,  credential);
           // builder.header("Content-Type",  "application/x-www-form-urlencoded;charset=UTF-8");


            // builder.post(RequestBody.create(MEDIA_TYPE_MARKDOWN, paramStr));

            Request request = chain.request();
           // printParams(request.body());
            Response mResponse = chain.proceed(builder.build());
            if (request.toString().contains(HttpConfig.APP_VERSION_UPDATE_METHOD)){  //因为获取验证码的端口号和正常的接口端口号不一样，此处暂时这样处理，为了联调而已。
                //String newURL=request.toString().replace("9007","9005");
                String newURL=HttpConfig.APP_VERSION_UPDATE_URL;
                Request newRequest =request
                        .newBuilder()
                        .url(newURL)
                        .build();
                mResponse=chain.proceed(newRequest);
            }else if (request.toString().contains("unspent/btc/")){
                String unspent_url_str=request.toString();
                String substring = unspent_url_str.substring(unspent_url_str.indexOf("unspent/btc/"), unspent_url_str.length());
                String test_unspent_url="http://www.tokenview.com:8088/tq"+substring+"1/50";
                LogUtil.i(TAG, "tokenInterceptor: 我们看截取的内容是"+test_unspent_url);
                Request newRequest =request
                        .newBuilder()
                        .url(test_unspent_url)
                        .build();
                mResponse=chain.proceed(newRequest);
            }

            ResponseBody body = mResponse.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    LogUtil.e(HttpConfig.ZJ_HTTP, "responseBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        String resp = body.string();
                        LogUtil.e(HttpConfig.ZJ_HTTP, mResponse.code()+"请求地址："+request.toString()+" | 返回数据：responseBody's content : " + unicodeToUTF_8(resp));
                        body = ResponseBody.create(mediaType, resp);
                        return mResponse.newBuilder().body(body).build();
                    } else {
                        LogUtil.e(HttpConfig.ZJ_HTTP, "responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            return mResponse;
        };
    }



    public static boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    ) {
                return true;
            }
        }
        return false;
    }

    public static String unicodeToUTF_8(String src) {
        if (null == src) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < src.length();) {
            char c = src.charAt(i);
            if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                String hex = src.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
                i = i + 6;
            } else {
                out.append(src.charAt(i));
                ++i;
            }
        }
        return out.toString();
    }
    private static void printParams(RequestBody body) {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            params = buffer.readString(charset);
            LogUtil.e(HttpConfig.ZJ_HTTP, "请求参数： | " + params);

        } catch (IOException e) {
            LogUtil.e(HttpConfig.ZJ_HTTP, "请求异常： | " + e.getMessage());
            e.printStackTrace();
        }
    }
}
