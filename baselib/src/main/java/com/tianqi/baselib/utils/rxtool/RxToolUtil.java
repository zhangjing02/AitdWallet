package com.tianqi.baselib.utils.rxtool;

/**
 * @创建者 zhangjing
 * @创时间 2019/12/1210:57
 * @描述
 * @版本 HamdanToken
 * @更新时间 2019/12/12
 * @更新描述 TODO
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;


import androidx.annotation.NonNull;

import com.tianqi.baselib.utils.LogUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Instruction:Rxjava2.x实现定时器
 * <p>
 * Author:pei
 * Date: 2017/6/29
 * Description:
 */

public class RxToolUtil {

    private static RxToolUtil sRxTimerUtil;
    private static Disposable mDisposable;
    private static final String FINGER_PRINT = "fingerprint";
    private static final String TAG = "RxToolUtil";

//    public static RxTimerUtil getInstence() {
//        if (sRxTimerUtil == null) {
//            synchronized (RetrofitFactory.class) {
//                if (sRxTimerUtil == null)
//                    sRxTimerUtil = new RxTimerUtil();
//            }
//        }
//        return sRxTimerUtil;
//    }

    /** milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void timer(long milliseconds, final IRxNext next) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable=disposable;
                    }
                    @Override
                    public void onNext(@NonNull Long number) {
                        if(next!=null){
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //取消订阅
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                       // cancel();
                    }
                });
    }


    /** 每隔milliseconds毫秒后执行next操作
     *
     * @param milliseconds
     * @param next
     */
    public static void interval(long milliseconds, final IRxNext next){
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable=disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if(next!=null){
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mDisposable.dispose();
                    }
                });
    }

    public  void take(List<Integer> list, final IRxNext next){
        Observable observable =  Observable.fromIterable(list) ;
        observable.take( 3 )
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable=disposable;
                    }

                    @Override
                    public void onNext(Integer value) {
                        if(next!=null){
                            next.doNext(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //取消订阅
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                        cancel();
                    }
                })   ;
    }

    public static void take2(List<Integer> list, final IRxNext next){
        Observable.range(1,3)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return String.format("我从Integer %s 变成了String %s",integer,integer);
                    }
                }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("Map",s);
                    }
                });


    }

    public static void take3(String content, final IRxNextTxt next){
       // Observable observable =  Observable.fromIterable(list) ;
        Observable observable =  Observable.just(content);
        observable.debounce(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mDisposable=disposable;
                    }

                    @Override
                    public void onNext(String value) {
                        if(next!=null){
                            next.doNext(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //取消订阅
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        //取消订阅
                        cancel();
                    }
                })   ;
    }


    public static void take04(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> observableEmitter) throws Exception {
                Log.i(TAG,"current thread : "+Thread.currentThread().getName());
                observableEmitter.onNext("123456");
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }
                    @Override
                    public void onNext(@NonNull String bitmap) {
                        Log.i(TAG,bitmap+"current thread : "+Thread.currentThread().getName());
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                    }
                });

    }


    /**
     * 从SharedPreferences 文件获取设备指纹
     *
     * @return fingerprint 设备指纹
     */
    public static String readFingerprintFromFile(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FINGER_PRINT, null);
    }

    /**
     * 取消订阅
     */
    public static void cancel(){
        if(mDisposable!=null&&!mDisposable.isDisposed()){
            mDisposable.dispose();
            LogUtil.e("====定时器取消======");
        }
    }

    public interface IRxNext{
        void doNext(long number);
    }
    public interface IRxNextTxt{
        void doNext(String number);
    }





}
