package com.tianqi.baselib.dbManager;

import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbgreendao.gen.CoinInfoDao;
import com.tianqi.baselib.dbgreendao.gen.WalletInfoDao;
import com.tianqi.baselib.utils.Constant;

import java.util.List;

public class WalletInfoManager {
    public static void insertOrUpdate(WalletInfo scaleRecord) {
        getScaleRecordDao().insertOrReplace(scaleRecord);
    }

    private static WalletInfoDao getScaleRecordDao() {
        return DaoManager.getInstance().getDaoSession().getWalletInfoDao();
    }

    public static void update(WalletInfo scaleRecord) {
        getScaleRecordDao().update(scaleRecord);
    }

    public static void clearScaleRecord() {
        getScaleRecordDao().deleteAll();
    }

    public static void deleteScaleRecord(WalletInfo scaleRecord) {
        getScaleRecordDao().delete(scaleRecord);
    }

    //查询用户
    public static List<WalletInfo> getWalletInfo() {
        return getScaleRecordDao().loadAll();
    }

    //查询用户
    public static List<WalletInfo> getWalletInfoNoHidden(boolean isHidden) {
        List<WalletInfo> walletInfo = getScaleRecordDao().queryBuilder()
                .where(WalletInfoDao.Properties.IsHide.eq(isHidden))//数据筛选，只获取usdt的。
                .build()
                .list();
        return walletInfo;
    }

    public static List<WalletInfo> getUsdtWalletInfo() {
        List<WalletInfo> walletInfo = getScaleRecordDao().queryBuilder()
                .where(WalletInfoDao.Properties.Wallet_id.eq(Constant.TRANSACTION_COIN_NAME_USDT))//数据筛选，只获取usdt的。
                .build()
                .list();
        return walletInfo;
    }

    //查询用户
    public static WalletInfo getHdWalletInfo() {
        List<WalletInfo> walletInfos = getScaleRecordDao().queryBuilder()
                .where(WalletInfoDao.Properties.WalletType.eq(0))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        if (walletInfos.size()>0){
            return walletInfos.get(0);
        }else {
            return null;
        }

    }
    //查询用户
    public static WalletInfo getHdWalletInfoFrId(String wallet_id) {
        WalletInfo walletInfo = getScaleRecordDao().queryBuilder()
                .where(WalletInfoDao.Properties.Wallet_id.eq(wallet_id))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list().get(0);

        return walletInfo;
    }

    public static List<WalletInfo> getCreateWalletInfos() {
        List<WalletInfo> walletInfo = getScaleRecordDao().queryBuilder()
                .where(WalletInfoDao.Properties.WalletType.eq(0))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();

        return walletInfo;
    }


    //查询用户
    public static WalletInfo getWalletFrName(String wallet_name) {
        List<WalletInfo> walletInfo = getScaleRecordDao().queryBuilder()
                .where(WalletInfoDao.Properties.WalletName.eq(wallet_name))//数据筛选，只获取 Name = "btc" 的数据。
                .build()
                .list();
        if (walletInfo.size()>0){
            return walletInfo.get(0);
        }else {
            return null;
        }
    }
}
