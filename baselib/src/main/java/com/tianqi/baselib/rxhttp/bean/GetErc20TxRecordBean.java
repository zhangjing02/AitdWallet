package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/11/14.
 * Describe :
 */
public class GetErc20TxRecordBean {
    /**
     * index : 51
     * block_no : 11254430
     * token : 0xdac17f958d2ee523a2206206994597c13d831ec7
     * tokenAddr : 0xdac17f958d2ee523a2206206994597c13d831ec7
     * tokenSymbol : USDT
     * tokenDecimals : 6
     * time : 1605338330
     * txid : 0x3ac4db77503e49006dd08da007709b66a4c6bce57ed599e9573e3742229bc90b
     * tokenInfo : {"h":"0xdac17f958d2ee523a2206206994597c13d831ec7","f":"Tether USD","s":"USDT","d":"6"}
     * from : 0x740b402e31408f03717d2b9377ee8bcc65c3993e
     * to : 0x08f2f632796c69e3f4e8fe0dbeaf3edc450e374f
     * value : 24
     * conformations : 211
     */

    private int index;
    private int block_no;
    private String token;
    private String tokenAddr;
    private String tokenSymbol;
    private String tokenDecimals;
    private int time;
    private String txid;
    private TokenInfoBean tokenInfo;
    private String from;
    private String to;
    private String value;
    private int conformations;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getBlock_no() {
        return block_no;
    }

    public void setBlock_no(int block_no) {
        this.block_no = block_no;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenAddr() {
        return tokenAddr;
    }

    public void setTokenAddr(String tokenAddr) {
        this.tokenAddr = tokenAddr;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(String tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public TokenInfoBean getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfoBean tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getConformations() {
        return conformations;
    }

    public void setConformations(int conformations) {
        this.conformations = conformations;
    }

    public static class TokenInfoBean {
        /**
         * h : 0xdac17f958d2ee523a2206206994597c13d831ec7
         * f : Tether USD
         * s : USDT
         * d : 6
         */

        private String h;
        private String f;
        private String s;
        private String d;

        public String getH() {
            return h;
        }

        public void setH(String h) {
            this.h = h;
        }

        public String getF() {
            return f;
        }

        public void setF(String f) {
            this.f = f;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public String getD() {
            return d;
        }

        public void setD(String d) {
            this.d = d;
        }
    }
}
