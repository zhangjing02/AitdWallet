package com.tianqi.baselib.rxhttp.bean;

import java.util.List;

/**
 * Create by zhangjing on 2020/11/17.
 * Describe :
 */
public class GetVersionLogBean {
    /**
     * msg : SUCCESS
     * code : 200
     * data : [{"channelName":"开发-渠道","forceFlag":false,"channelType":0,"remark":"备注内容","version":"v1.10.12","url":"http://baidu.com","content":"3.优化智能实名制认证流程\n2.修复已知问题，优化产品体验，\\n优化智能实名制认证流程\n2.修复已知问题，优化产品体验，优化智能实名制认证流程\n2.修复已知问题，优化产品体验，优化智能实名制认证流程\n2.修复已知问题，\\n优化产品体验","contentEn":"ee"},{"channelName":"开发-渠道","forceFlag":false,"channelType":0,"remark":"备注内容","version":"v1.10.12","url":"http://baidu.com","content":"3.优化智能实名制认证流程\n2.修复已知问题，优化产品体验","contentEn":"ee"},{"channelName":"开发-渠道","forceFlag":false,"channelType":0,"remark":"备注内容","version":"v1.10.12","url":"http://baidu.com","content":"3.优化智能实名制认证流程\n2.修复已知问题，优化产品体验","contentEn":"ee"},{"channelName":"开发-渠道","forceFlag":false,"channelType":0,"remark":"备注内容","version":"v1.10.12","url":"http://baidu.com","content":"3.优化智能实名制认证流程\n2.修复已知问题，优化产品体验","contentEn":"ee"},{"channelName":"123","forceFlag":false,"channelType":2,"remark":"2","version":"123","url":"12321321321","content":"2.优化智能实名制认证流程\n2.修复已知问题，优化产品体验","contentEn":"ff"},{"channelName":"TF-0020","forceFlag":true,"channelType":1,"remark":"1.优化智能实名制认证流程\n2.修复已知问题，优化产品体验","version":"1.0.0","url":"https://wallet.aitdcoin.com/wallet-ios/app.plist","content":"1.优化智能实名制认证流程\n2.修复已知问题，优化产品体验","contentEn":"1.Optimizing smart real name authentication processes\n2.Fixing existed problems, Optimizing product experiences"}]
     */

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * channelName : 开发-渠道
         * forceFlag : false
         * channelType : 0
         * remark : 备注内容
         * version : v1.10.12
         * url : http://baidu.com
         * content : 3.优化智能实名制认证流程
         2.修复已知问题，优化产品体验，\n优化智能实名制认证流程
         2.修复已知问题，优化产品体验，优化智能实名制认证流程
         2.修复已知问题，优化产品体验，优化智能实名制认证流程
         2.修复已知问题，\n优化产品体验
         * contentEn : ee
         */

        private String channelName;
        private boolean forceFlag;
        private int channelType;
        private String remark;
        private String version;
        private String url;
        private String content;
        private String contentEn;

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public boolean isForceFlag() {
            return forceFlag;
        }

        public void setForceFlag(boolean forceFlag) {
            this.forceFlag = forceFlag;
        }

        public int getChannelType() {
            return channelType;
        }

        public void setChannelType(int channelType) {
            this.channelType = channelType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentEn() {
            return contentEn;
        }

        public void setContentEn(String contentEn) {
            this.contentEn = contentEn;
        }
    }
}
