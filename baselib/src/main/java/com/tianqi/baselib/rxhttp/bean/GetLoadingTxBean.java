package com.tianqi.baselib.rxhttp.bean;

import java.util.List;

/**
 * Create by zhangjing on 2020/11/5.
 * Describe :
 */
public class GetLoadingTxBean {


    /**
     * fee : 0.00000900
     * inputCnt : 2
     * inputs : [{"address":"1HQPRnHKgkTrwavs3DoUkoTBmYEfb9jikH","input_no":0,"received_from":{"output_no":0,"txid":"b3ab1e6743b9cb47877196f43ff000a0e2ec9e1cd17d7b96dd97d383bd4e1b22"},"value":"0.00001"},{"address":"1HQPRnHKgkTrwavs3DoUkoTBmYEfb9jikH","input_no":1,"received_from":{"output_no":1,"txid":"ab52f84c523238ee78e37232ddd0257ad1bc53f25e31f642f750412018437722"},"value":"0.00002553"}]
     * network : BTC
     * outputCnt : 2
     * outputs : [{"address":"1Vmja2XGhuqt5JG9mQHkV7xCQ4mEgzjgM","output_no":0,"value":"0.000006"},{"address":"1HQPRnHKgkTrwavs3DoUkoTBmYEfb9jikH","output_no":1,"value":"0.00002053"}]
     * time : 1604460726
     * txid : dd190297dbc4e9eba41a029a51805f439092fccc3f8c441d96a69457f751735c
     * type : tx
     */

    private String fee;
    private int inputCnt;
    private String network;
    private int outputCnt;
    private int time;
    private String txid;
    private String type;
    private List<InputsBean> inputs;
    private List<OutputsBean> outputs;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public int getInputCnt() {
        return inputCnt;
    }

    public void setInputCnt(int inputCnt) {
        this.inputCnt = inputCnt;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public int getOutputCnt() {
        return outputCnt;
    }

    public void setOutputCnt(int outputCnt) {
        this.outputCnt = outputCnt;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
         * address : 1HQPRnHKgkTrwavs3DoUkoTBmYEfb9jikH
         * input_no : 0
         * received_from : {"output_no":0,"txid":"b3ab1e6743b9cb47877196f43ff000a0e2ec9e1cd17d7b96dd97d383bd4e1b22"}
         * value : 0.00001
         */

        private String address;
        private int input_no;
        private ReceivedFromBean received_from;
        private String value;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getInput_no() {
            return input_no;
        }

        public void setInput_no(int input_no) {
            this.input_no = input_no;
        }

        public ReceivedFromBean getReceived_from() {
            return received_from;
        }

        public void setReceived_from(ReceivedFromBean received_from) {
            this.received_from = received_from;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static class ReceivedFromBean {
            /**
             * output_no : 0
             * txid : b3ab1e6743b9cb47877196f43ff000a0e2ec9e1cd17d7b96dd97d383bd4e1b22
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
         * address : 1Vmja2XGhuqt5JG9mQHkV7xCQ4mEgzjgM
         * output_no : 0
         * value : 0.000006
         */

        private String address;
        private int output_no;
        private String value;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getOutput_no() {
            return output_no;
        }

        public void setOutput_no(int output_no) {
            this.output_no = output_no;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
