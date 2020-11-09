package com.tianqi.baselib.rxhttp.bean;

/**
 * Create by zhangjing on 2020/9/21.
 * Describe :
 */
public class GetSimpleRpcBean {

    /**
     * result : 020000000200ced5b4e2b3c8e9fd77914cbd7e06c4639c333617bbcac50da67e7e04c5bbb7000000000000000000031c477fa174759f67fc244f5cab42010c96add27741e6d9b9ed26151903251a0000000000000000000110110000000000001976a914b4f421d848ca79fae2357f77a56a2a183f01ecd288ac00000000
     * error : null
     * id : testnet3
     */

    private String result;
    private Object error;
    private String id;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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


    @Override
    public String toString() {
        return "GetSimpleRpcBean{" +
                "result='" + result + '\'' +
                ", error=" + error +
                ", id='" + id + '\'' +
                '}';
    }
}
