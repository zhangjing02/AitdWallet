package com.tianqi.aitdwallet.ui.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_message_tittle)
    TextView tvMessageTittle;
    @BindView(R.id.tv_message_content)
    TextView tvMessageContent;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initView() {
        getToolBar();
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_message_center);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {
    }

}