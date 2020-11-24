package com.tianqi.baselib.dbManager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;

import com.tianqi.baselib.dbgreendao.gen.DaoMaster;
import com.tianqi.baselib.dbgreendao.gen.DaoSession;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;

import org.greenrobot.greendao.database.Database;


public class DaoManager {
    private static String DB_NAME = "AITD-Wallet";
    private static DaoManager mDaoManager;
    private static MySqlLiteOpenHelper mySqlLiteOpenHelper;
    private static DaoSession mDaoSession;
    private static SQLiteDatabase mDatabase;
    private DaoManager() {}

    public static DaoManager getInstance(){
        if (mDaoManager == null){
            mDaoManager = new DaoManager();
        }
        return mDaoManager;
    }

    public synchronized static void init(Context context){
        mySqlLiteOpenHelper = new MySqlLiteOpenHelper(context,DB_NAME,null);

        if (isApkInDebug(context)){  //debug模式下，不加密。
            mDatabase = mySqlLiteOpenHelper.getWritableDatabase(); //原始的写法
          //  Database encryptedWritableDb = mySqlLiteOpenHelper.getEncryptedWritableDb(Constant.ACCESS_PASSWORD);//加密的写法
            mDaoSession = new DaoMaster(mDatabase).newSession();
        }else {  //release模式下，加密。
           // mDatabase = mySqlLiteOpenHelper.getWritableDatabase(); //原始的写法
            Database encryptedWritableDb = mySqlLiteOpenHelper.getEncryptedWritableDb(Constant.ACCESS_PASSWORD);//加密的写法
            mDaoSession = new DaoMaster(encryptedWritableDb).newSession();
        }

    }
    public DaoSession getDaoSession(){
        return mDaoSession;
    }


    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

}