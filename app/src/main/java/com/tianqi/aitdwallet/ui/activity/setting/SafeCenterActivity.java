package com.tianqi.aitdwallet.ui.activity.setting;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.VerifySecurityPsdActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SafeCenterActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_change_psd)
    TextView btnChangePsd;

    @Override
    protected int getContentView() {
        return R.layout.activity_safe_center;
    }


    @Override
    protected void initView() {
        getToolBar();
    }


    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_safe_center);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {
    }


    @OnClick(R.id.btn_change_psd)
    public void onViewClicked() {
        Intent intent=new Intent(this, VerifySecurityPsdActivity.class);
        intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_change_safe_psd));
        startActivity(intent);
        finish();
    }
}