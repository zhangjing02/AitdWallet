package com.tianqi.aitdwallet.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by zhangjing on 2020/10/22.
 * Describe :
 */
public class TransactionDetailBean {

    /**
     * result : {"amount":0,"confirmations":637,"generated":true,"blockhash":"556690c24758ede1dd240a06e9adb2f9d9cca0a32805cc692baa0245257521b5","blockindex":0,"blocktime":1603268890,"txid":"88a1fa363f7e2788c61589dece876ef3726186286f9b4b69b8a641742e7ccd02","walletconflicts":[],"time":1603268884,"timereceived":1603268884,"bip125-replaceable":"no","details":[],"hex":"020000000001010000000000000000000000000000000000000000000000000000000000000000ffffffff0502650c0101ffffffff0250090000000000001976a914404f02b2d0d318b32dc53b47ff77ea702cb1a28788ac0000000000000000266a24aa21a9ede2f61c3f71d1defd3fa999dfa36953755c690689799962b48bebd836974e8cf90120000000000000000000000000000000000000000000000000000000000000000000000000"}
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
         * amount : 0
         * confirmations : 637
         * generated : true
         * blockhash : 556690c24758ede1dd240a06e9adb2f9d9cca0a32805cc692baa0245257521b5
         * blockindex : 0
         * blocktime : 1603268890
         * txid : 88a1fa363f7e2788c61589dece876ef3726186286f9b4b69b8a641742e7ccd02
         * walletconflicts : []
         * time : 1603268884
         * timereceived : 1603268884
         * bip125-replaceable : no
         * details : []
         * hex : 020000000001010000000000000000000000000000000000000000000000000000000000000000ffffffff0502650c0101ffffffff0250090000000000001976a914404f02b2d0d318b32dc53b47ff77ea702cb1a28788ac0000000000000000266a24aa21a9ede2f61c3f71d1defd3fa999dfa36953755c690689799962b48bebd836974e8cf90120000000000000000000000000000000000000000000000000000000000000000000000000
         */

        private double amount;
        private int confirmations;
        private boolean generated;
        private String blockhash;
        private int blockindex;
        private int blocktime;
        private String txid;
        private int time;
        private int timereceived;
        @SerializedName("bip125-replaceable")
        private String bip125replaceable;
        private String hex;
        private List<?> walletconflicts;
        private List<?> details;

        public double getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public boolean isGenerated() {
            return generated;
        }

        public void setGenerated(boolean generated) {
            this.generated = generated;
        }

        public String getBlockhash() {
            return blockhash;
        }

        public void setBlockhash(String blockhash) {
            this.blockhash = blockhash;
        }

        public int getBlockindex() {
            return blockindex;
        }

        public void setBlockindex(int blockindex) {
            this.blockindex = blockindex;
        }

        public int getBlocktime() {
            return blocktime;
        }

        public void setBlocktime(int blocktime) {
            this.blocktime = blocktime;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getTimereceived() {
            return timereceived;
        }

        public void setTimereceived(int timereceived) {
            this.timereceived = timereceived;
        }

        public String getBip125replaceable() {
            return bip125replaceable;
        }

        public void setBip125replaceable(String bip125replaceable) {
            this.bip125replaceable = bip125replaceable;
        }

        public String getHex() {
            return hex;
        }

        public void setHex(String hex) {
            this.hex = hex;
        }

        public List<?> getWalletconflicts() {
            return walletconflicts;
        }

        public void setWalletconflicts(List<?> walletconflicts) {
            this.walletconflicts = walletconflicts;
        }

        public List<?> getDetails() {
            return details;
        }

        public void setDetails(List<?> details) {
            this.details = details;
        }
    }
}
