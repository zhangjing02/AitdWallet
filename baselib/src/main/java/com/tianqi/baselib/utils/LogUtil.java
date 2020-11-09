package com.tianqi.baselib.utils;

import android.util.Log;


public class LogUtil {

    /**
     * app日志标签
     */
    public static final String ISTAG = "wallet";

    /**
     * 是否打印日志
     */
    public static boolean isDMLog = true;

    private static boolean VERBOSE = true;

    private static boolean DEBUG = true;

    private static boolean INFO = true;

    private static boolean WARN = true;

    private static boolean ERROR = true;

    public static void setLogAtt(boolean result) {
        isDMLog = result;
        VERBOSE = VERBOSE && isDMLog;
        DEBUG = DEBUG && isDMLog;
        INFO = INFO && isDMLog;
        WARN = WARN && isDMLog;
        ERROR = ERROR && isDMLog;
    }

    public static void v(String tag, String msg, Throwable th) {
        
        if (VERBOSE && isDMLog)
            Log.v(tag, msg, th);
    }

    public static void d(String tag, String msg, Throwable th) {

        if (DEBUG && isDMLog)
            Log.d(tag, msg, th);
    }

    public static void i(String tag, String msg, Throwable th) {

        if (INFO && isDMLog)
            Log.i(tag, msg, th);
    }

    public static void w(String tag, String msg, Throwable th) {

        if (WARN && isDMLog)
            Log.w(tag, msg, th);
    }

    public static void e(String tag, String msg, Throwable th) {

        if (ERROR && isDMLog)
            Log.e(tag, msg, th);
    }

    public static void v(String tag, String msg) {

        if (VERBOSE && isDMLog)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {

        if (DEBUG && isDMLog)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {

        if (INFO && isDMLog)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {

        if (WARN && isDMLog)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {

        if (ERROR && isDMLog)
            Log.e(tag, msg);
    }

    public static void v(String msg) {
        if (VERBOSE && isDMLog)
            Log.v(LogUtil.ISTAG, msg);
    }

    public static void d(String msg) {

        if (DEBUG && isDMLog)
            Log.d(LogUtil.ISTAG, msg);
    }

    public static void i(String msg) {

        if (INFO && isDMLog)
            Log.i(LogUtil.ISTAG, msg);
    }

    public static void w(String msg) {
        if (WARN && isDMLog)
            Log.w(LogUtil.ISTAG, msg, null);
    }

    public static void e(String msg) {

        if (ERROR && isDMLog)
            Log.e(LogUtil.ISTAG, msg, null);
    }

}
