package com.tianqi.aitdwallet.ui.activity.wallet.initwallet;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by zhangjing on 2020/8/26.
 * Describe ：选择钱包种类
 */
public class SelectWalletTypeActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_hd_wallet)
    TextView btnHdWallet;

    @Override
    protected void initView() {
        getToolBar();
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_select_wallet);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_select_wallet_type;
    }


    @OnClick({R.id.btn_hd_wallet, R.id.btn_single_wallet, R.id.btn_multi_sign_wallet, R.id.btn_nfc_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_hd_wallet:
                UserInformation userInformation = UserInfoManager.getUserInfo();
                Intent intent;
                if (userInformation != null) {
                    intent = new Intent(this, BackupMemoryWordActivity.class);
                } else {
                    intent = new Intent(this, SetSecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG,Constants.INTENT_PUT_CREATE_WALLET);
                }
                startActivity(intent);
                break;
            case R.id.btn_single_wallet:
                break;
            case R.id.btn_multi_sign_wallet:
                break;
            case R.id.btn_nfc_wallet:
                break;
        }
    }
}