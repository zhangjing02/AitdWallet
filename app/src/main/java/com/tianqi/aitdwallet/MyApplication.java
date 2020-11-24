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
import com.tianqi.baselib.dbManager.DaoManager;
import com.tianqi.baselib.utils.LogUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DaoManager.getInstance().init(this);

        CrashReport.initCrashReport(getApplicationContext(), "3c51c83f91", false);
        LogUtil.setLogAtt(isApkInDebug(this));

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
