package com.tianqi.baselib.dbManager;

import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.CoinRateInfo;
import com.tianqi.baselib.dbgreendao.gen.CoinInfoDao;
import com.tianqi.baselib.dbgreendao.gen.CoinRateInfoDao;
import com.tianqi.baselib.dbgreendao.gen.ContactsInfoDao;
import com.tianqi.baselib.utils.Constant;

import java.util.List;

public class CoinRateInfoManager {
    public static void insertOrUpdate(CoinRateInfo scaleRecord) {
        getScaleRecordDao().insertOrReplace(scaleRecord);
    }

    public static void insertOrUpdateList(List<CoinRateInfo> scaleRecord) {
        getScaleRecordDao().insertOrReplaceInTx(scaleRecord);
    }

    public static void insert(CoinRateInfo scaleRecord) {
        getScaleRecordDao().insert(scaleRecord);
    }

    private static CoinRateInfoDao getScaleRecordDao() {
        return DaoManager.getInstance().getDaoSession().getCoinRateInfoDao();
    }

    public static void update(CoinRateInfo scaleRecord) {
        getScaleRecordDao().update(scaleRecord);
    }

    public static void clearScaleRecord() {
        getScaleRecordDao().deleteAll();
    }

    public static void deleteScaleRecord(CoinRateInfo scaleRecord) {
        getScaleRecordDao().delete(scaleRecord);
    }

    public static CoinRateInfo getCoinRatefoFrCoinId(String coin_id) {
        List<CoinRateInfo> coinInfos= getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinRateInfoDao.Properties.Id.eq(coin_id))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        if (coinInfos.size()>0){
            return coinInfos.get(0);
        }else {
            return null;
        }
    }

    public static List<CoinRateInfo> getCoins() {
        List<CoinRateInfo> coinInfos= getScaleRecordDao().queryBuilder()
                .build()
                .list();
        return coinInfos;
    }
}
