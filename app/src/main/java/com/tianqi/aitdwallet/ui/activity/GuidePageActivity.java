package com.tianqi.aitdwallet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.pager_adapter.GuidePageViewPageAdapter;
import com.tianqi.aitdwallet.adapter.pager_adapter.GuidePageViewPageAdapter02;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.BackupMemoryWordActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.SetSecurityPsdActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.VerifySecurityPsdActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.zhpan.indicator.IndicatorView;
import com.zhpan.indicator.enums.IndicatorSlideMode;
import com.zhpan.indicator.enums.IndicatorStyle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuidePageActivity extends BaseActivity {
    @BindView(R.id.tv_guide_tittle)
    TextView tvGuideTittle;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;
    @BindView(R.id.btn_import_wallet)
    TextView btnImportWallet;
    @BindView(R.id.iv_guide_logo)
    ViewPager2 ivGuideLogo;
    @BindView(R.id.indicator_view)
    IndicatorView indicatorView;
    private UserInformation userInfo;
    private GuidePageViewPageAdapter02 pageAdapter;
    private List<Integer> mList;

    @Override
    protected int getContentView() {
        return R.layout.activity_guide_page;
    }

    @Override
    protected void initView() {
        //setSteepStatusBar(false);
        userInfo = UserInfoManager.getUserInfo();
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        mList.add(R.drawable.ic_guide_page2);
        mList.add(R.drawable.ic_guide_page1);
        userInfo = UserInfoManager.getUserInfo();
        if (userInfo != null) {
            if (userInfo.getLanguageId() == Constants.LANGUAGE_ENGLISH) {
                mList.add(R.drawable.ic_guide_page3_en);
            } else {
                mList.add(R.drawable.ic_guide_page3_cn);
            }
        } else {
            if (getResources().getConfiguration().locale.getCountry().equals("US")) {
                mList.add(R.drawable.ic_guide_page3_en);
            } else {
                mList.add(R.drawable.ic_guide_page3_cn);
            }
        }

        pageAdapter = new GuidePageViewPageAdapter02(this, mList, ivGuideLogo);

        ivGuideLogo.setAdapter(pageAdapter);

        indicatorView.setSliderColor(getResources().getColor(R.color.bg_button_xxx_grey), getResources().getColor(R.color.color_progress_blue))
                .setSliderWidth(17)
                .setSliderHeight(5)
                .setSlideMode(IndicatorSlideMode.COLOR)
                .setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                .setupWithViewPager(ivGuideLogo);

        ivGuideLogo.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        tvGuideTittle.setText(getString(R.string.tittle_guide_page_text1));
                        break;
                    case 1:
                        tvGuideTittle.setText(getString(R.string.tittle_guide_page_text2));
                        break;
                    case 2:
                        tvGuideTittle.setText(getString(R.string.tittle_guide_page_text3));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @OnClick({R.id.btn_create_wallet, R.id.btn_import_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_wallet:
//                Intent intent2=new Intent(this, CreateWalletActivity.class);
//                startActivity(intent2);
                if (WalletInfoManager.getWalletInfo().size() > 0) {
                    WalletInfo walletInfo = WalletInfoManager.getWalletInfo().get(0);
                    if (walletInfo != null && walletInfo.getWalletType() >= 0 && !walletInfo.getIsImportToCreate()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
                        Intent intent = new Intent(this, BackupMemoryWordActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, SetSecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_create_wallet));
                        //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
                        startActivity(intent);
                    }
                } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
                    Intent intent = new Intent(this, BackupMemoryWordActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, SetSecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_create_wallet));
                    //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.btn_import_wallet:
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
                    // 进入验证密码的页面。
                    Intent intent = new Intent(this, VerifySecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_import_wallet));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, SetSecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_import_wallet));
                    startActivity(intent);
                }
                finish();
                break;
        }
    }
}