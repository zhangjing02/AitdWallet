package com.tianqi.baselib.dbManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.tianqi.baselib.dbgreendao.gen.CoinInfoDao;
import com.tianqi.baselib.dbgreendao.gen.CoinRateInfoDao;
import com.tianqi.baselib.dbgreendao.gen.DaoMaster;
import com.tianqi.baselib.dbgreendao.gen.TransactionRecordDao;
import com.tianqi.baselib.dbgreendao.gen.UserInformationDao;
import com.tianqi.baselib.dbgreendao.gen.WalletInfoDao;

import org.greenrobot.greendao.database.Database;

public class MySqlLiteOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = "MySqlLiteOpenHelper";

    public MySqlLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.e(TAG, "001onUpgrade: " + oldVersion + " newVersion = " + newVersion);
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                Log.e(TAG, "002onUpgrade: " + db.toString() + " newVersion = " + ifNotExists);
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                Log.e(TAG, "003onUpgrade: " + db.toString() + " newVersion = " + ifExists);
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, UserInformationDao.class, CoinInfoDao.class, TransactionRecordDao.class, WalletInfoDao.class,CoinRateInfoDao.class);
        Log.e(TAG, "004onUpgrade: " + oldVersion + " newVersion = " + newVersion);
    }

}

