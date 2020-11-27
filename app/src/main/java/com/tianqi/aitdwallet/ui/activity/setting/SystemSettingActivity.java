package com.tianqi.aitdwallet.ui.activity.setting;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;

import butterknife.BindView;
import butterknife.OnClick;

public class SystemSettingActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_currency_tag)
    TextView tvCurrencyTag;
    @BindView(R.id.tv_currency_unit)
    TextView tvCurrencyUnit;
    @BindView(R.id.layout_currency_unit)
    LinearLayout layoutCurrencyUnit;
    @BindView(R.id.tv_language_tag)
    TextView tvLanguageTag;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.layout_language)
    LinearLayout layoutLanguage;
    private UserInformation userInformation;

    @Override
    protected int getContentView() {
        return R.layout.activity_system_setting;
    }

    @Override
    protected void initView() {
        getToolBar();

    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.sys_setting_tittle);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInformation = UserInfoManager.getUserInfo();
        String fiat_unit = userInformation.getFiatUnit();
        tvCurrencyUnit.setText(fiat_unit);
    }

    @Override
    protected void initData() {
        userInformation = UserInfoManager.getUserInfo();
        switch (userInformation.getLanguageId()) {
            case Constants.LANGUAGE_CHINA:
                tvLanguage.setText(getString(R.string.text_simple_chinese));
                break;
            case Constants.LANGUAGE_ENGLISH:
                tvLanguage.setText(getString(R.string.text_english));
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.layout_currency_unit, R.id.layout_language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_currency_unit:
                Intent intent=new Intent(this,CoinUnitSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_language:
                intent=new Intent(this,LanguageSettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}