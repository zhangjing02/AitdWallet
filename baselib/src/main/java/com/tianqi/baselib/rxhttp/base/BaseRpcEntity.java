package com.tianqi.baselib.rxhttp.base;

public class BaseRpcEntity<T> {

    /**
     * result : null
     * error : null
     * id : testnet3
     */

    private T result;
    private Object error;
    private String id;

    public void setResult(T result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
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
}
