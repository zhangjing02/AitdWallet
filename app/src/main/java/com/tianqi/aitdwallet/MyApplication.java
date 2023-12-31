package com.tianqi.aitdwallet;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tianqi.aitdwallet.utils.LocaleUtils;
import com.tianqi.baselib.dbManager.DaoManager;
import com.tianqi.baselib.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    private static Context mContext;


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //   layout.setPrimaryColorsId(android.R.color.white, android.R.color.black);//全局设置主题颜色
            return new ClassicsHeader(context).setDrawableSize(20);
        });

    }

    public static void setmContext(Context mContext) {
        MyApplication.mContext = mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();

        DaoManager.getInstance().init(this);

        CrashReport.initCrashReport(getApplicationContext(), "3c51c83f91", false);
        LogUtil.setLogAtt(isApkInDebug(this));

        JPushInterface.setDebugMode(isApkInDebug(this)); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }

    public static Context getmContext() {
        return mContext;
    }

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
