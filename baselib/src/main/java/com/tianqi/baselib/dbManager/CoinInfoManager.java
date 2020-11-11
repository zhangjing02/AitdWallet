package com.tianqi.baselib.dbManager;

import android.util.Log;

import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbgreendao.gen.CoinInfoDao;
import com.tianqi.baselib.utils.Constant;

import java.util.List;

public class CoinInfoManager {
    public static void insertOrUpdate(CoinInfo scaleRecord) {
        getScaleRecordDao().insertOrReplace(scaleRecord);
    }

    public static void insertOrUpdateList(List<CoinInfo> scaleRecord) {
        getScaleRecordDao().insertOrReplaceInTx(scaleRecord);
    }

    public static void insert(CoinInfo scaleRecord) {
        getScaleRecordDao().insert(scaleRecord);
    }

    private static CoinInfoDao getScaleRecordDao() {
        return DaoManager.getInstance().getDaoSession().getCoinInfoDao();
    }

    public static void update(CoinInfo scaleRecord) {
        getScaleRecordDao().update(scaleRecord);
    }

    public static void clearScaleRecord() {
        getScaleRecordDao().deleteAll();
    }

    public static void deleteScaleRecord(CoinInfo scaleRecord) {
        getScaleRecordDao().delete(scaleRecord);
    }
    public static void deleteScaleRecords(List<CoinInfo> scaleRecord) {
        getScaleRecordDao().deleteInTx(scaleRecord);
    }

    //查询用户
    public static List<CoinInfo> getCoinInfo() {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                .orderAsc(CoinInfoDao.Properties.Coin_name)//按照名字正序输
                .build()
                .list();
        // return getScaleRecordDao().loadAll();
        return list;
    }
//    public static List<CoinInfo> getHdCoinInfo() {
//        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
//                .where(CoinInfoDao.Properties.Coin_ComeType.eq(0))//数据筛选，只获取 Name = "btc" 的数据。
//                .orderAsc(CoinInfoDao.Properties.Coin_name)//按照名字正序输
//                .build()
//                .list();
//        // return getScaleRecordDao().loadAll();
//        return list;
//    }
    //查询用户
    public static List<CoinInfo> getCreateCoinInfo() {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                .where(CoinInfoDao.Properties.Coin_ComeType.eq(0))//数据筛选，只获取 Name = "btc" 的数据。
                .orderAsc(CoinInfoDao.Properties.Coin_name)//按照名字正序输
                .build()
                .list();
        // return getScaleRecordDao().loadAll();
        return list;
    }

    public static List<CoinInfo> getImportCoinInfo() {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                .where(CoinInfoDao.Properties.Coin_ComeType.eq(1))//数据筛选，只获取 Name = "btc" 的数据。
                .orderAsc(CoinInfoDao.Properties.Coin_name)//按照名字正序输
                .build()
                .list();
        // return getScaleRecordDao().loadAll();
        return list;
    }

    //查询比特币的币种
    public static List<CoinInfo> getWalletBtcInfo() {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                .where(CoinInfoDao.Properties.Coin_name.eq(Constant.TRANSACTION_COIN_NAME_BTC))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        return list;
    }

    public static List<CoinInfo> getWalletEthInfo() {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                .where(CoinInfoDao.Properties.Coin_name.eq(Constant.TRANSACTION_COIN_NAME_ETH))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        return list;
    }
    public static List<CoinInfo> getCoinEthImportInfo() {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                .where(CoinInfoDao.Properties.Coin_name.eq(Constant.TRANSACTION_COIN_NAME_ETH),CoinInfoDao.Properties.Coin_ComeType.eq(1))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        return list;
    }
    public static List<CoinInfo> getWalletUsdtInfo() {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                .where(CoinInfoDao.Properties.Coin_name.eq(Constant.TRANSACTION_COIN_NAME_USDT))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        return list;
    }

    public static CoinInfo getCoinFrAddress(String coin_name, String coin_address) {
        List<CoinInfo> coinInfos = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinInfoDao.Properties.Coin_address.eq(coin_address),CoinInfoDao.Properties.Coin_name.eq(coin_name))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        if (coinInfos.size()>0){
            return coinInfos.get(0);
        }else {
            return null;
        }
    }

    public static List<CoinInfo> getCoinFrPrivateKey(String coin_name, String coin_private) {
        List<CoinInfo> coinInfo = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinInfoDao.Properties.PrivateKey.eq(coin_private),CoinInfoDao.Properties.Coin_name.eq(coin_name))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        return coinInfo;
    }
    public static CoinInfo getWalletBtcFrCoinId(String coin_id) {
        CoinInfo coinInfo= getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinInfoDao.Properties.Coin_id.eq(coin_id))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list().get(0);
        return coinInfo;
    }

    public static CoinInfo getMainCoinFrCoinId(String coin_id) {
       List <CoinInfo> coinInfos= getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinInfoDao.Properties.Coin_id.eq(coin_id))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
       if (coinInfos.size()>0){
           return coinInfos.get(0);
       }else {
           return null;
       }

    }
    public static List<CoinInfo> getCoinFrWalletId(String wallet_id) {
        List<CoinInfo> coinInfo= getScaleRecordDao().queryBuilder()
                .where(CoinInfoDao.Properties.Wallet_id.eq(wallet_id))//数据筛选，
                .build()
                .list();
        return coinInfo;
    }

    //查询特定的币种
    public static List<CoinInfo> getSpecCoinInfo(String coin_name) {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinInfoDao.Properties.Coin_name.eq(coin_name))//数据筛选，只获取 Name = coin_name 的数据。
                .build()
                .list();
        return list;
    }

    //查询特定的币种,以及是创建的币种，代表是HD钱包来的。
    public static List<CoinInfo> getSpecHdCoinInfo(String coin_name) {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinInfoDao.Properties.Coin_name.eq(coin_name),CoinInfoDao.Properties.Coin_ComeType.eq(0))//数据筛选，只获取 Name = coin_name 的数据。
                .build()
                .list();
        return list;
    }
    //查询特定的币种,以及是创建的币种，代表是HD钱包来的。
    public static List<CoinInfo> getNoHiddenSpecHdCoinInfo(String coin_name) {
        List<CoinInfo> list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
                // .limit(3)//只获取结果集的前 3 个数据
                .where(CoinInfoDao.Properties.Coin_name.eq(coin_name),CoinInfoDao.Properties.Coin_ComeType.eq(0),CoinInfoDao.Properties.IsHidden.eq(false))//数据筛选，只获取 Name = coin_name 的数据。
                .build()
                .list();
        return list;
    }
}
