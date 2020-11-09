package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/11/4.
 * Describe :
 */
public class GetNewVersionBean {

    /**
     * forceFlag : false
     * messageEn : 1.Optimizing smart real name authentication processes
     2.Fixing existed problems, Optimizing product experiences
     * message : 版本內容
     1.新增智能实名认证功能
     2.优化被邀用户账号隐私问题
     3.优化邀请码规则
     4.修复已知问题，优化产品体验
     * version : 3.4.3
     * url : http://www.aitdcoin.com/file/app/android/app-online-release.apk
     */

    private boolean forceFlag;
    private String messageEn;
    private String message;
    private String version;
    private String url;

    public boolean isForceFlag() {
        return forceFlag;
    }

    public void setForceFlag(boolean forceFlag) {
        this.forceFlag = forceFlag;
    }

    public String getMessageEn() {
        return messageEn;
    }

    public void setMessageEn(String messageEn) {
        this.messageEn = messageEn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "GetNewVersionBean{" +
                "forceFlag=" + forceFlag +
                ", messageEn='" + messageEn + '\'' +
                ", message='" + message + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
