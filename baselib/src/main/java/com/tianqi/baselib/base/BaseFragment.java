package com.tianqi.baselib.base;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.scrollview.BaseDetailFrag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/*******************************************************
 *
 * Created by julis.wang on 2019/09/27 11:41
 *
 * Description :
 * History   :
 *
 *******************************************************/

public abstract class BaseFragment extends BaseDetailFrag {
    private View mContentView;
    private Context mContext;
    private Dialog loadingDialog;
    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(),container,false);
        unbinder = ButterKnife.bind(this, mContentView);
        mContext = getContext();

        if (!EventBus.getDefault().isRegistered(this)) {
            //未注册才注册
            EventBus.getDefault().register(this);
        }

        initView();
        init();
        initData();
        return mContentView;
    }





    /**
     * 一些View的相关操作
     */
    protected abstract void initView();

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    protected abstract int setLayoutResourceID();


    /**
     * 一些Data的相关操作
     */
    protected abstract void initData();

    /**
     * 此方法用于初始化成员变量及获取Intent传递过来的数据
     * 注意：这个方法中不能调用所有的View，因为View还没有被初始化，要使用View在initView方法中调用
     */
    protected void init() {

    }



    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(EventMessage event) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            //已注册注销
            EventBus.getDefault().unregister(this);
        }
        unbinder.unbind();
    }

    public View getContentView() {
        return mContentView;
    }

    public Context getMContext() {
        return mContext;
    }


    public void showLoadingDialog() {
        if (loadingDialog==null){
            loadingDialog=LoadingDialogUtils.createLoadingDialog(getActivity(),"");
        }else {
            loadingDialog.show();
        }
    }
    public void showLoadingDialog(String loadingText) {
        if (loadingDialog==null){
            loadingDialog=LoadingDialogUtils.createLoadingDialog(getActivity(),loadingText);
        }else {
            loadingDialog.show();
        }
    }
    public void stopLoadingDialog() {
        if (loadingDialog!=null){
            LoadingDialogUtils.closeDialog(loadingDialog);
        }
    }

}
