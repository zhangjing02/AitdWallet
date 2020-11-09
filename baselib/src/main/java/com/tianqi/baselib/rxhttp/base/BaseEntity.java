package com.tianqi.baselib.rxhttp.base;

public class BaseEntity<T> {

    public static final int CODE = 200;
    public static final int CODE_BTC = 1;
    public static final int CODE_BTC_NO_DATA = 404;
    public static final int LOGOUT_CODE = 405;
    public static final int SIGN_ERROR = 415;
    public static final int SYS_MAINTEN_PSD_ERROR = 401;//（支付密码错误）
    public static final int GATE_WAY_SAVING_NOT_ZERO = 403;//（网关下的金额不为0，导致不能取消信任）
    public static final int USER_NOT_ACTIVE = 407;//（用户未激活，需要弹框提醒。）
    public static final int USER_SELF_WALLET_ADDRESS = 408;//（用户提币填入的是自己的钱包地址）
    public static final int TOO_MONY_PSD_ERROR = 9990;//（多次支付密码错误）
    public int code;
    public String msg;
    public T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
