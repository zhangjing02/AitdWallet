package com.tianqi.aitdwallet.ui.activity.setting;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.MainActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.display.LocaleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageSettingActivity extends BaseActivity {


    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.line_middle)
    View lineMiddle;
    @BindView(R.id.rb_select_simple_chinese)
    RadioButton rbSelectSimpleChinese;
    @BindView(R.id.rb_select_english)
    RadioButton rbSelectEnglish;
    private UserInformation userInformation;

    @Override
    protected int getContentView() {
        return R.layout.activity_language_setting;
    }

    @Override
    protected void initView() {
        getToolBar();
        userInformation = UserInfoManager.getUserInfo();
        int languageId = userInformation.getLanguageId();
        switch (languageId) {
            case Constants.LANGUAGE_CHINA:
                rbSelectSimpleChinese.setChecked(true);
                break;
            case Constants.LANGUAGE_ENGLISH:
                rbSelectEnglish.setChecked(true);
                break;
            default:
                break;
        }
    }
    @Override
    protected void initData() {
        rbSelectSimpleChinese.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                userInformation.setLanguageId(Constants.LANGUAGE_CHINA);
                UserInfoManager.insertOrUpdate(userInformation);
            }
        });
        rbSelectEnglish.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                userInformation.setLanguageId(Constants.LANGUAGE_ENGLISH);
                UserInfoManager.insertOrUpdate(userInformation);
            }
        });
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.setting_language_tittle);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }


    /**
     * 重启当前Activity
     */
    private void restartAct() {
        finish();
        Intent _Intent = new Intent(this, MainActivity.class);
        startActivity(_Intent);
        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);
    }


    @OnClick(R.id.btn_change_language)
    public void onViewClicked() {
        int languageId = userInformation.getLanguageId();
        switch (languageId) {
            case Constants.LANGUAGE_CHINA:
                rbSelectSimpleChinese.setChecked(true);
                if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_CHINESE)) {
                    LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_CHINESE);
                    userInformation.setLanguageId(Constants.LANGUAGE_CHINA);
                    UserInfoManager.insertOrUpdate(userInformation);
                    restartAct();
                }
                break;
            case Constants.LANGUAGE_ENGLISH:
                rbSelectEnglish.setChecked(true);
                if (LocaleUtils.needUpdateLocale(this, LocaleUtils.LOCALE_ENGLISH)) {
                    LocaleUtils.updateLocale(this, LocaleUtils.LOCALE_ENGLISH);
                    userInformation.setLanguageId(Constants.LANGUAGE_ENGLISH);
                    UserInfoManager.insertOrUpdate(userInformation);
                    restartAct();
                }
                break;
            default:
                break;
        }
    }
}