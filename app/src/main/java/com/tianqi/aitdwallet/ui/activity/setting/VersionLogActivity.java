package com.tianqi.aitdwallet.ui.activity.setting;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.VersionLogAdapter;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.rxhttp.HttpClientUtil;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetVersionLogBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VersionLogActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_version_log)
    ListView lvVersionLog;

    private VersionLogAdapter versionLogAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_version_log;
    }

    @Override
    protected void initView() {
        getToolBar();
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_version_log_text);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("123");
        }).map(response -> {
            //计算出可用的utxo交易块,然后通过请求创建交易的hex。
            Response response222 = HttpClientUtil.getInstance().postBackendJson(makeBroadcastTxParams002());
            if (response222.isSuccessful()) {
                ResponseBody body = response222.body();
                if (body != null) {
                    //return gson.fromJson(formalUtxoJson.body().string(), GetUnspentTxBean.class);
                    return response222.body().string();
                }
            }
            return Constant.HTTP_ERROR;
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    if (baseEntity!=null&&!baseEntity.equals(Constant.HTTP_ERROR)){
                        Gson gson = new Gson();
                        GetVersionLogBean versionLogBean=gson.fromJson(baseEntity, GetVersionLogBean.class);
                        versionLogAdapter=new VersionLogAdapter(this,versionLogBean.getData());
                        lvVersionLog.setAdapter(versionLogAdapter);
                    }else {
                        ToastUtil.showToast(this,getString(R.string.notice_unspent_data_error));
                    }
                });








    }


    private String makeBroadcastTxParams002() {
        Map<String, Object> listunspentParams = new HashMap<>();

        listunspentParams.put("type", 2);

        return new Gson().toJson(listunspentParams);
    }


}