package com.tianqi.baselib.dbManager;

import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.dbgreendao.gen.TransactionRecordDao;
import com.tianqi.baselib.utils.Constant;

import org.greenrobot.greendao.Property;

import java.util.List;

import static com.tianqi.baselib.utils.Constant.TRANSACTION_COIN_NAME_BTC;

public class TransactionRecordManager {

    public static void insertOrUpdate(TransactionRecord scaleRecord) {
        getScaleRecordDao().insertOrReplace(scaleRecord);
    }

    public static void insert(TransactionRecord scaleRecord) {
        getScaleRecordDao().insert(scaleRecord);
    }

    private static TransactionRecordDao getScaleRecordDao() {
        return DaoManager.getInstance().getDaoSession().getTransactionRecordDao();
    }

    public static void update(TransactionRecord scaleRecord) {
        getScaleRecordDao().update(scaleRecord);
    }

    public static void clearScaleRecord() {
        getScaleRecordDao().deleteAll();
    }

    public static void deleteScaleRecord(TransactionRecord scaleRecord) {
        getScaleRecordDao().delete(scaleRecord);
    }

    //查询用户
    public static List<TransactionRecord> geAllTransactionInfo() {
        return getScaleRecordDao().loadAll();
    }

    //查询比特币
    public static List<TransactionRecord> getWalletBtcInfo(String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Unit.eq(Constant.TRANSACTION_COIN_NAME_BTC),TransactionRecordDao.Properties.Coin_id.eq(coin_id))//数据筛选，只获取 Name = "btc" 的数据。
               .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }
    //查询比特币
    public static TransactionRecord getTxFrId(String tx_id) {
        TransactionRecord list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Txid.eq(tx_id))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list().get(0);
        return list;
    }

    //查询比特币
    public static List<TransactionRecord> getTxFrAddress(String coin_address) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Address.eq(coin_address))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    //查询比特币CoinInfoDao.Properties.Coin_name
    public static List<TransactionRecord> getTxFrAddressAndCoinId(String coin_address,String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Address.eq(coin_address),TransactionRecordDao.Properties.Coin_id.eq(coin_id))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }
    //查询比特币发送
    public static List<TransactionRecord> getWalletBtcSend(String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Unit.eq(Constant.TRANSACTION_COIN_NAME_BTC),TransactionRecordDao.Properties.Coin_id.eq(coin_id),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_SEND))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    //查询交易，确认中的交易id。
    public static List<TransactionRecord> getT(String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Unit.eq(Constant.TRANSACTION_COIN_NAME_BTC),TransactionRecordDao.Properties.Coin_id.eq(coin_id),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_SEND))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }
    public static List<TransactionRecord> getTxBtcSend(String coin_address) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Unit.eq(Constant.TRANSACTION_COIN_NAME_BTC),TransactionRecordDao.Properties.Address.eq(coin_address),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_SEND))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    public static List<TransactionRecord> getTxSendFrAddress(String coin_address) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Address.eq(coin_address),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_SEND))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }
    public static List<TransactionRecord> getTxSendFrAddressAndCoin(String coin_address,String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Address.eq(coin_address),TransactionRecordDao.Properties.Coin_id.eq(coin_id),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_SEND))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    public static List<TransactionRecord> getTxSendFrAddressAndCoinId(String coin_address,String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Address.eq(coin_address),TransactionRecordDao.Properties.Coin_id.eq(coin_id),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_SEND))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }
    //查询比特币接受
    public static List<TransactionRecord> getWalletBtcReceive(String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Unit.eq(Constant.TRANSACTION_COIN_NAME_BTC),TransactionRecordDao.Properties.Coin_id.eq(coin_id),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_RECEIVE))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    //查询比特币接受
    public static List<TransactionRecord> getTxBtcReceive(String coin_address) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Address.eq(coin_address),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_RECEIVE))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    public static List<TransactionRecord> getTxReceiveFrAddressAndCoinId(String coin_address, String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Address.eq(coin_address),TransactionRecordDao.Properties.Coin_id.eq(coin_id),
                        TransactionRecordDao.Properties.TransType.eq(Constant.TRANSACTION_TYPE_RECEIVE))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    //查询比特币失败的交易
    public static List<TransactionRecord> getWalletBtcFail(String coin_address) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Unit.eq(Constant.TRANSACTION_COIN_NAME_BTC),TransactionRecordDao.Properties.Status.eq(Constant.TRANSACTION_STATE_FAIL),
                        TransactionRecordDao.Properties.Address.eq(coin_address))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }

    //查询比特币失败的交易
    public static List<TransactionRecord> getWalletBtcFail(String coin_address,String coin_id) {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Coin_id.eq(coin_id),TransactionRecordDao.Properties.Status.eq(Constant.TRANSACTION_STATE_FAIL),
                        TransactionRecordDao.Properties.Address.eq(coin_address))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }



    public static List<TransactionRecord> getWalletEthInfo() {
        List<TransactionRecord > list = getScaleRecordDao().queryBuilder()
                //.offset(1)//偏移量，相当于 SQL 语句中的 skip
               // .limit(3)//只获取结果集的前 3 个数据
                .where(TransactionRecordDao.Properties.Unit.eq(Constant.TRANSACTION_COIN_NAME_ETH))//数据筛选，只获取 Name = "btc" 的数据。
                .orderDesc(TransactionRecordDao.Properties.TimeStr)
                .build()
                .list();
        return list;
    }
}
