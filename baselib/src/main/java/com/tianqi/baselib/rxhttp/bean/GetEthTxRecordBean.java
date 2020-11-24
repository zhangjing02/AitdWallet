package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/11/10.
 * Describe :
 */
public class GetEthTxRecordBean {

    /**
     * block_no : 11253285
     * confirmations : 59799
     * fee : 0.003
     * from : 0x08f2f632796c69e3f4e8fe0dbeaf3edc450e374f
     * gasLimit : 60000
     * gasPrice : 50000000000
     * gasUsed : 60000
     * height : 11253285
     * index : 18
     * network : ETH
     * nonce : 3
     * receiptErr : Err status 0x0
     * time : 1605322798
     * to : 0xdac17f958d2ee523a2206206994597c13d831ec7
     * toIsContract : 1
     * traceErr : Bad instruction
     * txid : 0xd4540f035faaaf9538a6c0debf4c01f35e2587c36d754615b31b114438b0ef47
     * type : tx
     * value : 0
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
    private String receiptErr;
    private int time;
    private String to;
    private int toIsContract;
    private String traceErr;
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

    public String getReceiptErr() {
        return receiptErr;
    }

    public void setReceiptErr(String receiptErr) {
        this.receiptErr = receiptErr;
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

    public int getToIsContract() {
        return toIsContract;
    }

    public void setToIsContract(int toIsContract) {
        this.toIsContract = toIsContract;
    }

    public String getTraceErr() {
        return traceErr;
    }

    public void setTraceErr(String traceErr) {
        this.traceErr = traceErr;
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
