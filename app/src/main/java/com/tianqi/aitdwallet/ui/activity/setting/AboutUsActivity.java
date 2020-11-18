package com.tianqi.aitdwallet.ui.activity.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.tv_version_log)
    TextView tvVersionLog;
    @BindView(R.id.tv_version_site)
    TextView tvVersionSite;
    @BindView(R.id.tv_version_site_address)
    TextView tvVersionSiteAddress;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.tv_email_tag)
    TextView tvEmailTag;
    @BindView(R.id.tv_email_address)
    TextView tvEmailAddress;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_twitter_tag)
    TextView tvTwitterTag;
    @BindView(R.id.tv_twitter_address)
    TextView tvTwitterAddress;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.tv_wechat_tag)
    TextView tvWechatTag;
    @BindView(R.id.tv_wechat_address)
    TextView tvWechatAddress;

    @Override
    protected int getContentView() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        getToolBar();

    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_about_us_text);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });

    }


    @Override
    protected void initData() {
        tvVersionName.setText(getString(R.string.text_version_name_tag) + getVersionCode());
    }

    private String getVersionCode() {
        // 包管理器 可以获取清单文件信息
        PackageManager packageManager = getPackageManager();
        try {
            // 获取包信息
            // 参1 包名 参2 获取额外信息的flag 不需要的话 写0
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @OnClick({R.id.tv_version_log, R.id.tv_version_site_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_version_log:
                Intent intent = new Intent(this, VersionLogActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_version_site_address:
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(tvVersionSiteAddress.getText().toString());
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
    }
}