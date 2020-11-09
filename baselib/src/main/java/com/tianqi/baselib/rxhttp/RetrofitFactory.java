package com.tianqi.baselib.rxhttp;

import android.content.Context;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tianqi.baselib.rxhttp.config.HttpConfig;
import com.tianqi.baselib.utils.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author zhangjing
 * @date 2018/12/3
 * @description
 */

public class RetrofitFactory {
    private static RetrofitFactory mRetrofitFactory;
    private static Api mAPIFunction;
    public static boolean type;
    private RetrofitFactory(Context context) {
        File cacheFile = new File(context.getCacheDir(), "cache");
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(message -> LogUtil.e("token", message));
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.HTTP_TIME, TimeUnit.MILLISECONDS)
                //.dns(OkHttpDns.getInstance())
                .readTimeout(HttpConfig.HTTP_TIME, TimeUnit.MILLISECONDS)
                .writeTimeout(HttpConfig.HTTP_TIME, TimeUnit.MILLISECONDS)
                //.addInterceptor(new MyInter())
                .addInterceptor(InterceptorUtil.tokenInterceptor(context))
                //.cache(cache)
                .build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(HttpConfig.BASE_FORMAL_URL)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .client(mOkHttpClient)
                .build();
        mAPIFunction = mRetrofit.create(Api.class);
    }

    public static RetrofitFactory getInstence(Context context) {
        if (mRetrofitFactory == null) {
            synchronized (RetrofitFactory.class) {
                if (mRetrofitFactory == null)
                    mRetrofitFactory = new RetrofitFactory(context.getApplicationContext());
            }
        }
        return mRetrofitFactory;
    }

    public Api API() {
        return mAPIFunction;
    }

    public void Cancel(){
        mRetrofitFactory=null;

    }

}
