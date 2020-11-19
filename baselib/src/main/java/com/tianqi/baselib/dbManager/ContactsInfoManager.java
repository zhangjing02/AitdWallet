package com.tianqi.baselib.dbManager;

import com.tianqi.baselib.dao.CoinInfo;
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
    public static List<ContactsInfo> getAllContactsInfo() {
        return getScaleRecordDao().loadAll();
    }

    public static List<ContactsInfo> getCoinFrCoinNameInfo(String coin_name) {
        List<ContactsInfo> list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(ContactsInfoDao.Properties.ContactsCoinName.eq(coin_name))//数据筛选，只获取 Name = coin_name 的数据。
                .build()
                .list();
        return list;
    }
    public static List<ContactsInfo> getCoinFrIdInfo(String contact_id) {
        List<ContactsInfo> list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(ContactsInfoDao.Properties.ContactsID.eq(contact_id))//数据筛选，只获取 Name = coin_name 的数据。
                .build()
                .list();
        return list;
    }

    public static List<ContactsInfo> getCoinFrCoinNameAddressInfo(String coin_name, String address) {
        List<ContactsInfo> list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(ContactsInfoDao.Properties.ContactsCoinName.eq(coin_name),ContactsInfoDao.Properties.ContactsCoinAddress.eq(address))//数据筛选，只获取 Name = coin_name 的数据。
                .build()
                .list();
        return list;
    }
}
