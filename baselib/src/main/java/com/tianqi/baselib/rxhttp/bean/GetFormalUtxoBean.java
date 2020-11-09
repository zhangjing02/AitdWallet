package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/9/17.
 * Describe :
 */
public class GetFormalUtxoBean {
    /**
     * block_no : 651614
     * output_no : 1
     * index : 1958
     * txid : b3ab1e6743b9cb47877196f43ff000a0e2ec9e1cd17d7b96dd97d383bd4e1b22
     * hex : 76a9140570fd2c31c9aa277647805c3c30e97a0943f18b88ac
     * confirmations : 2692
     * value : 0.000877
     */

    private int block_no;
    private int output_no;
    private String index;
    private String txid;
    private String hex;
    private int confirmations;
    private String value;

    public int getBlock_no() {
        return block_no;
    }

    public void setBlock_no(int block_no) {
        this.block_no = block_no;
    }

    public int getOutput_no() {
        return output_no;
    }

    public void setOutput_no(int output_no) {
        this.output_no = output_no;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GetFormalUtxoBean{" +
                "block_no=" + block_no +
                ", output_no=" + output_no +
                ", index='" + index + '\'' +
                ", txid='" + txid + '\'' +
                ", hex='" + hex + '\'' +
                ", confirmations=" + confirmations +
                ", value='" + value + '\'' +
                '}';
    }
}
