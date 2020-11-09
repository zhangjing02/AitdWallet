package com.tianqi.baselib.rxhttp.base;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.net.ParseException;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.tianqi.baselib.R;
import com.tianqi.baselib.rxhttp.config.HttpConfig;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.display.ToastUtil;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author zhangjing
 * @date 2018/12/3
 * @description 订阅的基类。
 */

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {
    protected Context mContext;
    protected boolean isShowLoading;
    protected boolean showToast;
    private int countNum = 0;
    private long time;
    private Dialog mLoadDialog;
    private static final String TAG = "BaseObserver";
    private Disposable mDisposable;

    //    public BaseObserver() {
//    }
    public BaseObserver(Context context) {
        mContext = context;
    }

    public BaseObserver(Context context, boolean showToast) {
        mContext = context;
        this.showToast = showToast;
    }

    public BaseObserver(Context context, boolean isShowLoading, boolean showToast) {//默认showToast为flase  即toast提示错误信息
        mContext = context;
        this.isShowLoading = isShowLoading;
        this.showToast = showToast;

    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        if (isShowLoading) {
            mLoadDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
            mLoadDialog.setOnCancelListener(dialog -> {
                if (mDisposable != null) {
                    mDisposable.dispose();
                }
            });
        }
    }

    @Override
    public void onNext(BaseEntity<T> tBaseEntity) {
        mDisposable = null;
        if (isShowLoading) {
            LoadingDialogUtils.closeDialog(mLoadDialog);
        }
        LogUtil.d(HttpConfig.ZJ_HTTP, "请求001");
        switch (tBaseEntity.getCode()) {
            case BaseEntity.CODE://成功
            case BaseEntity.CODE_BTC://成功
            case BaseEntity.CODE_BTC_NO_DATA://成功
                LogUtil.d(HttpConfig.ZJ_HTTP, "请求002成功");
                onSuccess(tBaseEntity.getData(), tBaseEntity.getMsg());
                break;
            case BaseEntity.LOGOUT_CODE:
            case BaseEntity.SIGN_ERROR:
                LogUtil.d(HttpConfig.ZJ_HTTP, tBaseEntity.getMsg() + "onFailure---003请重新登录" + tBaseEntity.toString());
                onFailure(tBaseEntity.getCode(), tBaseEntity.getMsg());
                break;
            case BaseEntity.SYS_MAINTEN_PSD_ERROR://支付密码错误
                onFailure(tBaseEntity.getCode(), tBaseEntity.getMsg());
                if (mContext != null && showToast) {
                    Toast.makeText(mContext, tBaseEntity.msg, Toast.LENGTH_SHORT).show();
                }
                break;
            case BaseEntity.USER_NOT_ACTIVE://用户未激活
                onFailure(tBaseEntity.getCode(), tBaseEntity.getMsg());
                break;
            default:
                //请求失败
                LogUtil.d(HttpConfig.ZJ_HTTP, "请求失败: 004----" + tBaseEntity.getCode());
                if (tBaseEntity != null) {
                    if (mContext != null && showToast) {
                        Toast.makeText(mContext, tBaseEntity.msg, Toast.LENGTH_SHORT).show();
                    }
                    onFailure(tBaseEntity.getCode(), tBaseEntity.getMsg());
                }
                break;
        }
    }

    @Override
    public void onError(Throwable e) {
        mDisposable = null;
        if (isShowLoading) {
            LoadingDialogUtils.closeDialog(mLoadDialog);
        }
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                if (mContext != null) {
                    ToastUtil.showToast(mContext, mContext.getString(R.string.network_error));
                }
                onFailure(-1, e.getMessage());
                onError(e.getMessage());
            } else {
                if (e instanceof JsonParseException
                        || e instanceof JSONException
                        || e instanceof ParseException) {
                    ToastUtil.showToast(mContext, mContext.getString(R.string.json_error));
                } else if (e instanceof SocketTimeoutException) {
                    ToastUtil.showToast(mContext, mContext.getString(R.string.network_timeout));
                } else {
                    ToastUtil.showToast(mContext, mContext.getString(R.string.network_error));
                }
                LogUtil.d(HttpConfig.ZJ_HTTP, "--006onFailure:其他问题 " + e.getMessage());
                onFailure(-1, "" + e.getMessage());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 返回成功
     *
     * @param
     * @throws Exception
     */
    public abstract void onSuccess(T data, String msg);

    /**
     * 返回成功了,但是code错误
     *
     * @param t
     * @throws Exception
     */
    protected void onCodeError(BaseEntity<T> t) throws Exception {
    }

    /**
     * 返回失败
     *
     * @param
     * @param code 异常时传-1
     * @throws Exception
     */
    protected abstract void onFailure(int code, String msg);

    /**
     * 网络异常
     *
     * @param
     * @throws Exception
     */
    protected void onError(String msg) {
    }

}
