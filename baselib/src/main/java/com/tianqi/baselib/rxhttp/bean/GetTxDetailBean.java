package com.tianqi.baselib.rxhttp.bean;

import java.util.List;

/**
 * Create by zhangjing on 2020/11/4.
 * Describe :
 */
public class GetTxDetailBean {
    /**
     * type : tx
     * network : BTC
     * block_no : 651614
     * height : 651614
     * blockHash : 000000000000000000095bbc19c6a085d38ad29c7c9cc813318b493aebb5323d
     * index : 1958
     * time : 1602054931
     * txid : b3ab1e6743b9cb47877196f43ff000a0e2ec9e1cd17d7b96dd97d383bd4e1b22
     * fee : 0.000113
     * confirmations : 3723
     * inputs : [{"input_no":0,"address":"1Vmja2XGhuqt5JG9mQHkV7xCQ4mEgzjgM","value":"0.001","received_from":{"output_no":6,"txid":"286d06b7d76b9ecab11a1962cf82b3acd53c3607fd0e9a161fa08ef0da81badf"}}]
     * outputs : [{"output_no":0,"address":"1HQPRnHKgkTrwavs3DoUkoTBmYEfb9jikH","value":"0.00001"},{"output_no":1,"address":"1Vmja2XGhuqt5JG9mQHkV7xCQ4mEgzjgM","value":"0.000877"}]
     * inputCnt : 1
     * outputCnt : 2
     */

    private String type;
    private String network;
    private int block_no;
    private int height;
    private String blockHash;
    private int index;
    private int time;
    private String txid;
    private String fee;
    private int confirmations;
    private int inputCnt;
    private int outputCnt;
    private List<InputsBean> inputs;
    private List<OutputsBean> outputs;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public int getBlock_no() {
        return block_no;
    }

    public void setBlock_no(int block_no) {
        this.block_no = block_no;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public int getInputCnt() {
        return inputCnt;
    }

    public void setInputCnt(int inputCnt) {
        this.inputCnt = inputCnt;
    }

    public int getOutputCnt() {
        return outputCnt;
    }

    public void setOutputCnt(int outputCnt) {
        this.outputCnt = outputCnt;
    }

    public List<InputsBean> getInputs() {
        return inputs;
    }

    public void setInputs(List<InputsBean> inputs) {
        this.inputs = inputs;
    }

    public List<OutputsBean> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputsBean> outputs) {
        this.outputs = outputs;
    }

    public static class InputsBean {
        /**
         * input_no : 0
         * address : 1Vmja2XGhuqt5JG9mQHkV7xCQ4mEgzjgM
         * value : 0.001
         * received_from : {"output_no":6,"txid":"286d06b7d76b9ecab11a1962cf82b3acd53c3607fd0e9a161fa08ef0da81badf"}
         */

        private int input_no;
        private String address;
        private String value;
        private ReceivedFromBean received_from;

        public int getInput_no() {
            return input_no;
        }

        public void setInput_no(int input_no) {
            this.input_no = input_no;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public ReceivedFromBean getReceived_from() {
            return received_from;
        }

        public void setReceived_from(ReceivedFromBean received_from) {
            this.received_from = received_from;
        }

        public static class ReceivedFromBean {
            /**
             * output_no : 6
             * txid : 286d06b7d76b9ecab11a1962cf82b3acd53c3607fd0e9a161fa08ef0da81badf
             */

            private int output_no;
            private String txid;

            public int getOutput_no() {
                return output_no;
            }

            public void setOutput_no(int output_no) {
                this.output_no = output_no;
            }

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }
        }
    }

    public static class OutputsBean {
        /**
         * output_no : 0
         * address : 1HQPRnHKgkTrwavs3DoUkoTBmYEfb9jikH
         * value : 0.00001
         */

        private int output_no;
        private String address;
        private String value;

        public int getOutput_no() {
            return output_no;
        }

        public void setOutput_no(int output_no) {
            this.output_no = output_no;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
