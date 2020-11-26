package com.tianqi.baselib.base;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/*******************************************************
 *
 * Created by julis.wang on 2019/04/29 10:03
 *
 * Description :
 * History   :
 *
 *******************************************************/

public abstract class BaseActivity extends AppCompatActivity {
    private Dialog loadingDialog;

    private static final String TAG = "BaseActivity";

    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;
    /**
     * 是否允许全屏
     **/
    private boolean isAllowFullScreen = false;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRotate = true;
    /**
     * context
     **/
    protected Context ctx;


    protected abstract int getContentView();

    /**
     * 初始化界面
     **/
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().getDecorView().setBackground(null);
        if (isAllowFullScreen) {
            setFullScreen();
        }
        setContentView(getContentView());
        ButterKnife.bind(this);

        initView();
        initData();
        ctx = this;
        if (isSetStatusBar) {
            steepStatusBar();
        }

        if (!isAllowScreenRotate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            //未注册才注册
            EventBus.getDefault().register(this);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(EventMessage event) {

    }

    /**
     * 窗口全屏
     */
    private void setFullScreen() {
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    /**
     * [是否允许全屏]
     *
     * @param allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.isAllowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param isSetStatusBar
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [是否允许屏幕旋转]
     *
     * @param isAllowScreenRoate
     */
    public void setScreenRoate(boolean isAllowScreenRoate) {
        this.isAllowScreenRotate = isAllowScreenRoate;
    }

    public void showLoadingDialog() {
        if (loadingDialog==null){
            loadingDialog=LoadingDialogUtils.createLoadingDialog(this,"");
        }else {
            loadingDialog.show();
        }
    }

    public void stopLoadingDialog() {
        if (loadingDialog!=null){
            LoadingDialogUtils.closeDialog(loadingDialog);
        }
    }

    /**
     * 设置 actionBar title 以及 up 按钮事件
     *
     * @param title
     */
    protected void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            if (null != title) {
                actionBar.setTitle(title);
            }

            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            finishActivity(RESULT_OK);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            //已注册注销
            EventBus.getDefault().unregister(this);
        }
    }
}
