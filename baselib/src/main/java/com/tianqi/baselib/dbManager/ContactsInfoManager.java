package com.tianqi.baselib.dbManager;

import com.tianqi.baselib.dao.ContactsInfo;
import com.tianqi.baselib.dbgreendao.gen.CoinInfoDao;
import com.tianqi.baselib.dbgreendao.gen.ContactsInfoDao;

import java.util.List;

public class ContactsInfoManager {
    public static void insertOrUpdate(ContactsInfo scaleRecord) {
        getScaleRecordDao().insertOrReplace(scaleRecord);
    }

    private static ContactsInfoDao getScaleRecordDao() {
        return DaoManager.getInstance().getDaoSession().getContactsInfoDao();
    }

    public static void update(ContactsInfo scaleRecord) {
        getScaleRecordDao().update(scaleRecord);
    }

    public static void clearScaleRecord() {
        getScaleRecordDao().deleteAll();
    }

    public static void deleteScaleRecord(ContactsInfo scaleRecord) {
        getScaleRecordDao().delete(scaleRecord);
    }

    //查询用户
    public static List<ContactsInfo> getWalletInfo() {
        return getScaleRecordDao().loadAll();
    }
}
