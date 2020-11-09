package com.tianqi.baselib.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by zhangjing on 2020/9/26.
 * Describe :
 */

@Entity
public class TransactionRecord {

    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String txid;

    private int transType;   //转账 0 收款 1
    private int status;      //交易状态，成功，失败，等待打包(0，1，2)
    private int coin_type;   //币种类型(0,60)
    private String address;  //地址，一般都是自己的地址
    private int confirmations;   //确认数，一般计算交易时间
    private double amount;  //转账金额
    private String timeStr; //转账时间(当前时间减去 确认数 * 10 * 60)
    private String unit;     //金额单位(BTC,ETH)
    private String coin_id;   //标记是哪个币种的第几个币，对应的是币种表里面的coin_id
    private int vout_id;      //此笔交易的输出角标。
    private String remark;    //交易的时候的备注信息。
    private String targetAddress;// 交易的目标的地址。（如果是转账，就是转给谁的地址，如果是收款就应该是转钱人的地址）
    private String input_id;      //交易的输入信息。（防止listunspent更新不及时，导致使用同一笔输入）
    private double input_spent;      //交易的输入信息花费了多少。（防止listunspent更新不及时，导致使用同一笔输入）
    private String list_unspent_consume;//未花费的交易，使用了多少
    private double miner_fee;          //交易花费了多少费用。


    @Generated(hash = 907977045)
    public TransactionRecord(Long id, String txid, int transType, int status,
            int coin_type, String address, int confirmations, double amount,
            String timeStr, String unit, String coin_id, int vout_id, String remark,
            String targetAddress, String input_id, double input_spent,
            String list_unspent_consume, double miner_fee) {
        this.id = id;
        this.txid = txid;
        this.transType = transType;
        this.status = status;
        this.coin_type = coin_type;
        this.address = address;
        this.confirmations = confirmations;
        this.amount = amount;
        this.timeStr = timeStr;
        this.unit = unit;
        this.coin_id = coin_id;
        this.vout_id = vout_id;
        this.remark = remark;
        this.targetAddress = targetAddress;
        this.input_id = input_id;
        this.input_spent = input_spent;
        this.list_unspent_consume = list_unspent_consume;
        this.miner_fee = miner_fee;
    }
    @Generated(hash = 1215017002)
    public TransactionRecord() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTxid() {
        return this.txid;
    }
    public void setTxid(String txid) {
        this.txid = txid;
    }
    public int getTransType() {
        return this.transType;
    }
    public void setTransType(int transType) {
        this.transType = transType;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getCoin_type() {
        return this.coin_type;
    }
    public void setCoin_type(int coin_type) {
        this.coin_type = coin_type;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getConfirmations() {
        return this.confirmations;
    }
    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }
    public double getAmount() {
        return this.amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getTimeStr() {
        return this.timeStr;
    }
    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getCoin_id() {
        return this.coin_id;
    }
    public void setCoin_id(String coin_id) {
        this.coin_id = coin_id;
    }
    public int getVout_id() {
        return this.vout_id;
    }
    public void setVout_id(int vout_id) {
        this.vout_id = vout_id;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getTargetAddress() {
        return this.targetAddress;
    }
    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }
    public String getInput_id() {
        return this.input_id;
    }
    public void setInput_id(String input_id) {
        this.input_id = input_id;
    }
    public double getInput_spent() {
        return this.input_spent;
    }
    public void setInput_spent(double input_spent) {
        this.input_spent = input_spent;
    }
    public String getList_unspent_consume() {
        return this.list_unspent_consume;
    }
    public void setList_unspent_consume(String list_unspent_consume) {
        this.list_unspent_consume = list_unspent_consume;
    }
    public double getMiner_fee() {
        return this.miner_fee;
    }
    public void setMiner_fee(double miner_fee) {
        this.miner_fee = miner_fee;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "id=" + id +
                ", txid='" + txid + '\'' +
                ", transType=" + transType +
                ", status=" + status +
                ", coin_type=" + coin_type +
                ", address='" + address + '\'' +
                ", confirmations=" + confirmations +
                ", amount=" + amount +
                ", timeStr='" + timeStr + '\'' +
                ", unit='" + unit + '\'' +
                ", coin_id='" + coin_id + '\'' +
                ", vout_id=" + vout_id +
                ", remark='" + remark + '\'' +
                ", targetAddress='" + targetAddress + '\'' +
                ", input_id='" + input_id + '\'' +
                ", input_spent=" + input_spent +
                ", list_unspent_consume='" + list_unspent_consume + '\'' +
                ", miner_fee=" + miner_fee +
                '}';
    }
}
