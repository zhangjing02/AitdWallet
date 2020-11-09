package com.tianqi.baselib.dbManager;


import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbgreendao.gen.UserInformationDao;

import java.util.List;

/**
 * @创建者 zhangjing
 * @创时间 2019/10/2811:20
 * @描述
 * @版本 HamdanToken
 * @更新时间 2019/10/28
 * @更新描述 TODO
 */
public class UserInfoManager {

    public static void insertOrUpdate(UserInformation scaleRecord) {
        getScaleRecordDao().insertOrReplace(scaleRecord);
    }

    private static UserInformationDao getScaleRecordDao() {
        return DaoManager.getInstance().getDaoSession().getUserInformationDao();
    }

    public static void update(UserInformation scaleRecord) {
        getScaleRecordDao().update(scaleRecord);
    }

    public static void clearScaleRecord() {
        getScaleRecordDao().deleteAll();
    }

    public static void deleteScaleRecord(UserInformation scaleRecord) {
        getScaleRecordDao().delete(scaleRecord);
    }

    //查询用户
    public static UserInformation getUserInfo() {
        List<UserInformation> list = getScaleRecordDao().loadAll();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
