package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/11/13.
 * Describe :
 */
public class GetErc20BalanceBean {
    /**
     * network : ETH
     * hash : 0xdac17f958d2ee523a2206206994597c13d831ec7
     * tokenInfo : {"h":"0xdac17f958d2ee523a2206206994597c13d831ec7","f":"Tether USD","s":"USDT","d":"6"}
     * transferCnt : 1
     * balance : 2000000
     */

    private String network;
    private String hash;
    private TokenInfoBean tokenInfo;
    private int transferCnt;
    private String balance;

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public TokenInfoBean getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(TokenInfoBean tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public int getTransferCnt() {
        return transferCnt;
    }

    public void setTransferCnt(int transferCnt) {
        this.transferCnt = transferCnt;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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
