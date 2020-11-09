package com.tianqi.baselib.rxhttp.bean;

import java.util.List;

/**
 * Create by zhangjing on 2020/11/5.
 * Describe :
 */
public class GetUnspentTxBean {

    /**
     * code : 1
     * msg : 成功
     * data : [{"block_no":1,"output_no":1,"index":"0","txid":"ab52f84c523238ee78e37232ddd0257ad1bc53f25e31f642f750412018437722","hex":"76a914b3ee4d0db13609252844f53250937a9dade6f0bf88ac","confirmations":1,"value":"2.553e-05"},{"block_no":1,"output_no":0,"index":"0","txid":"b3ab1e6743b9cb47877196f43ff000a0e2ec9e1cd17d7b96dd97d383bd4e1b22","hex":"76a914b3ee4d0db13609252844f53250937a9dade6f0bf88ac","confirmations":1,"value":"0.000010"},{"block_no":1,"output_no":1,"index":"0","txid":"c7405f9264a2566b0a9e69e945cf9799bf22b2fd306b65fbb94ba2017d79ef1e","hex":"76a914b3ee4d0db13609252844f53250937a9dade6f0bf88ac","confirmations":1,"value":"5.46e-06"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * block_no : 1
         * output_no : 1
         * index : 0
         * txid : ab52f84c523238ee78e37232ddd0257ad1bc53f25e31f642f750412018437722
         * hex : 76a914b3ee4d0db13609252844f53250937a9dade6f0bf88ac
         * confirmations : 1
         * value : 2.553e-05
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
    }
}
