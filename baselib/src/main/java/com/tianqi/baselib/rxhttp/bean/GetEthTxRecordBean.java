package com.tianqi.baselib.rxhttp.bean;

import java.util.List;

/**
 * Create by zhangjing on 2020/11/10.
 * Describe :
 */
public class GetEthTxRecordBean {
    /**
     * addressType : normal
     * balance : 0.228354
     * hash : 0x05c5d1b89d0d4b5bddb424c991db94557977c01c
     * network : ETH
     * nonce : 1
     * normalTxCount : 6
     * receive : 1.431
     * spend : -1.202646
     * txs : [{"block_no":10557739,"confirmations":669451,"fee":"0.000956466928473","from":"0x60ec215fbc0afbeb980b26d8df24225ee0c3d1b1","gasLimit":21000,"gasPrice":45546044213,"gasUsed":21000,"height":10557739,"index":160,"network":"ETH","nonce":306,"time":1596068943,"to":"0x05c5d1b89d0d4b5bddb424c991db94557977c01c","txid":"0xd4b710f13e79370620a2313706b6adc74a2ecaa9e42eb6c86dc91f53c80d03bb","type":"tx","value":"0.2"},{"block_no":10539804,"confirmations":687386,"fee":"0.001848","from":"0x05c5d1b89d0d4b5bddb424c991db94557977c01c","gasLimit":21000,"gasPrice":88000000000,"gasUsed":21000,"height":10539804,"index":139,"network":"ETH","nonce":1,"time":1595829028,"to":"0x7a8e88e8f4f6dfaf8599479d727b96057ac9b9f0","txid":"0x0e8cb5072a74eba7bd24a49dabfced6aac5bec767057cd58b091b26e9c44ec3b","type":"tx","value":"0.2"},{"block_no":10493267,"confirmations":733923,"fee":"0.000966","from":"0xddf6c769f0d95c2f6d8bc013f507d43bb4928ff3","gasLimit":90000,"gasPrice":46000000000,"gasUsed":21000,"height":10493267,"index":147,"network":"ETH","nonce":174,"time":1595206015,"to":"0x05c5d1b89d0d4b5bddb424c991db94557977c01c","txid":"0xb4f88b1b1d4aae9eb37823ca1bf39706bfaebe3235565e96af7eafe99d2f5aed","type":"tx","value":"0.2"},{"block_no":10431093,"confirmations":796097,"fee":"0.000798","from":"0x05c5d1b89d0d4b5bddb424c991db94557977c01c","gasLimit":21000,"gasPrice":38000000000,"gasUsed":21000,"height":10431093,"index":192,"network":"ETH","nonce":0,"time":1594375391,"to":"0xf315ebc518650fb4e297d39c913eecc724fbd162","txid":"0x8422e975199aa1bc66a292e133b42b239be33ffeaf6e492bd8b8edfcec208c77","type":"tx","value":"1"},{"block_no":10431086,"confirmations":796104,"fee":"0.000819","from":"0x904486de5e829f10427ffbda26d70a94e0512df8","gasLimit":21000,"gasPrice":39000000000,"gasUsed":21000,"height":10431086,"index":178,"network":"ETH","nonce":16,"time":1594375299,"to":"0x05c5d1b89d0d4b5bddb424c991db94557977c01c","txid":"0x079fddc5b7ccc164b32bac2973e1c1e22d82341d96fc5b636235aa9906dfb8db","type":"tx","value":"0.7"},{"block_no":10431071,"confirmations":796119,"fee":"0.000861000030639","from":"0x2d2fa090e3a0893a545b83b97e3801003b229b38","gasLimit":21000,"gasPrice":41000001459,"gasUsed":21000,"height":10431071,"index":71,"network":"ETH","nonce":198,"time":1594375047,"to":"0x05c5d1b89d0d4b5bddb424c991db94557977c01c","txid":"0x35c938a28798f1dfd3d493b316525e7e80c95cb6f9d5b2f716935fc519a5f091","type":"tx","value":"0.331"}]
     * type : address
     */

    private String addressType;
    private String balance;
    private String hash;
    private String network;
    private int nonce;
    private int normalTxCount;
    private String receive;
    private String spend;
    private String type;
    private List<TxsBean> txs;

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public int getNormalTxCount() {
        return normalTxCount;
    }

    public void setNormalTxCount(int normalTxCount) {
        this.normalTxCount = normalTxCount;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getSpend() {
        return spend;
    }

    public void setSpend(String spend) {
        this.spend = spend;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TxsBean> getTxs() {
        return txs;
    }

    public void setTxs(List<TxsBean> txs) {
        this.txs = txs;
    }

    public static class TxsBean {
        /**
         * block_no : 10557739
         * confirmations : 669451
         * fee : 0.000956466928473
         * from : 0x60ec215fbc0afbeb980b26d8df24225ee0c3d1b1
         * gasLimit : 21000
         * gasPrice : 45546044213
         * gasUsed : 21000
         * height : 10557739
         * index : 160
         * network : ETH
         * nonce : 306
         * time : 1596068943
         * to : 0x05c5d1b89d0d4b5bddb424c991db94557977c01c
         * txid : 0xd4b710f13e79370620a2313706b6adc74a2ecaa9e42eb6c86dc91f53c80d03bb
         * type : tx
         * value : 0.2
         */

        private int block_no;
        private int confirmations;
        private String fee;
        private String from;
        private int gasLimit;
        private long gasPrice;
        private int gasUsed;
        private int height;
        private int index;
        private String network;
        private int nonce;
        private int time;
        private String to;
        private String txid;
        private String type;
        private String value;

        public int getBlock_no() {
            return block_no;
        }

        public void setBlock_no(int block_no) {
            this.block_no = block_no;
        }

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getGasLimit() {
            return gasLimit;
        }

        public void setGasLimit(int gasLimit) {
            this.gasLimit = gasLimit;
        }

        public long getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(long gasPrice) {
            this.gasPrice = gasPrice;
        }

        public int getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(int gasUsed) {
            this.gasUsed = gasUsed;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public int getNonce() {
            return nonce;
        }

        public void setNonce(int nonce) {
            this.nonce = nonce;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
