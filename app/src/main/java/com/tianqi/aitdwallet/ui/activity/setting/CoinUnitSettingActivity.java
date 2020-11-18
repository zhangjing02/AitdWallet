package com.tianqi.aitdwallet.ui.activity.setting;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.MainActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoinUnitSettingActivity extends BaseActivity {
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rb_select_cny)
    RadioButton rbSelectCny;
    @BindView(R.id.line_middle)
    View lineMiddle;
    @BindView(R.id.rb_select_usd)
    RadioButton rbSelectUsd;
    @BindView(R.id.rg_fiat_unit)
    RadioGroup rgFiatUnit;
    private UserInformation userInformation;

    @Override
    protected int getContentView() {
        return R.layout.activity_coin_unit_setting;
    }

    @Override
    protected void initView() {
        getToolBar();
        userInformation = UserInfoManager.getUserInfo();
        String fiat_unit = userInformation.getFiatUnit();
        switch (fiat_unit) {
            case Constants.FIAT_CNY:
                rbSelectCny.setChecked(true);
                break;
            case Constants.FIAT_USD:
                rbSelectUsd.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
//        rbSelectUsd.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                userInformation.setFiatUnit(Constants.FIAT_USD);
//                UserInfoManager.insertOrUpdate(userInformation);
//            }
//        });
//        rbSelectCny.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                userInformation.setFiatUnit(Constants.FIAT_CNY);
//                UserInfoManager.insertOrUpdate(userInformation);
//            }
//        });
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.coin_unit_tittle);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnFinish.setVisibility(View.VISIBLE);
        btnFinish.setText(getString(R.string.btn_confirm_text));
        btnFinish.setTextColor(getResources().getColor(R.color.text_light_blue));
        btnFinish.setTextSize(16);
    }


    @OnClick(R.id.btn_finish)
    public void onViewClicked() {
        if (rbSelectCny.isChecked()){
            userInformation.setFiatUnit(Constants.FIAT_CNY);
            UserInfoManager.insertOrUpdate(userInformation);
        }else {
            userInformation.setFiatUnit(Constants.FIAT_USD);
            UserInfoManager.insertOrUpdate(userInformation);
        }
        restartAct();
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
}