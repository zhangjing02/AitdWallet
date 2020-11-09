package com.tianqi.baselib.dbManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tianqi.baselib.dbgreendao.gen.DaoMaster;
import com.tianqi.baselib.dbgreendao.gen.DaoSession;
import com.tianqi.baselib.utils.Constant;

import org.greenrobot.greendao.database.Database;


public class DaoManager {
    private static String DB_NAME = "AITD-User";
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
        //mDatabase = mySqlLiteOpenHelper.getWritableDatabase(); //原始的写法
        Database encryptedWritableDb = mySqlLiteOpenHelper.getEncryptedWritableDb(Constant.ACCESS_PASSWORD);//加密的写法
        mDaoSession = new DaoMaster(encryptedWritableDb).newSession();
    }
    public DaoSession getDaoSession(){
        return mDaoSession;
    }
}