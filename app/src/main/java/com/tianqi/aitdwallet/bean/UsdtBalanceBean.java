package com.tianqi.aitdwallet.bean;

/**
 * Create by zhangjing on 2020/10/13.
 * Describe :
 */
public class UsdtBalanceBean {

    /**
     * result : {"balance":"190.00000000","reserved":"0.00000000","frozen":"0.00000000"}
     * error : null
     * id : testnet3
     */

    private ResultBean result;
    private Object error;
    private String id;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class ResultBean {
        /**
         * balance : 190.00000000
         * reserved : 0.00000000
         * frozen : 0.00000000
         */

        private String balance;
        private String reserved;
        private String frozen;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getReserved() {
            return reserved;
        }

        public void setReserved(String reserved) {
            this.reserved = reserved;
        }

        public String getFrozen() {
            return frozen;
        }

        public void setFrozen(String frozen) {
            this.frozen = frozen;
        }
    }
}
