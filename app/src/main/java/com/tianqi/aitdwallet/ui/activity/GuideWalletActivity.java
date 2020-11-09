package com.tianqi.aitdwallet.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.VerifySecurityPsdActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.BackupMemoryWordActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.SetSecurityPsdActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideWalletActivity extends BaseActivity {
    UserInformation userInformation;
    List<WalletInfo> walletInfos;
    @BindView(R.id.tv_guide_tittle)
    TextView tvGuideTittle;
    @BindView(R.id.tv_guide_notice1)
    TextView tvGuideNotice1;
    @BindView(R.id.tv_guide_notice2)
    TextView tvGuideNotice2;
    @BindView(R.id.iv_guide_logo)
    ImageView ivGuideLogo;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;
    @BindView(R.id.btn_import_wallet)
    TextView btnImportWallet;
    private UserInformation userInfo;

    @Override
    protected int getContentView() {
        return R.layout.activity_guide_wallet;
    }

    @Override
    protected void initView() {
        //setSteepStatusBar(false);
         userInfo = UserInfoManager.getUserInfo();
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.btn_create_wallet, R.id.btn_import_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_wallet:
//                Intent intent2=new Intent(this, CreateWalletActivity.class);
//                startActivity(intent2);

                if (WalletInfoManager.getWalletInfo().size()>0){
                    WalletInfo walletInfo = WalletInfoManager.getWalletInfo().get(0);
                    if (walletInfo!=null&&walletInfo.getWalletType()>=0&&!walletInfo.getIsImportToCreate()){
                        Intent intent=new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }else if ( userInfo!=null&& !TextUtils.isEmpty(userInfo.getPasswordStr())){
                        Intent intent = new Intent(this, BackupMemoryWordActivity.class);
                        startActivity(intent);
                    }else {
                        Intent  intent = new Intent(this, SetSecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG,Constants.INTENT_PUT_CREATE_WALLET);
                        //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
                        startActivity(intent);
                    }
                }else if ( userInfo!=null&& !TextUtils.isEmpty(userInfo.getPasswordStr())){
                    Intent intent = new Intent(this, BackupMemoryWordActivity.class);
                    startActivity(intent);
                }else {
                    Intent  intent = new Intent(this, SetSecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG,Constants.INTENT_PUT_CREATE_WALLET);
                    //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_import_wallet:
                if (userInfo!=null&& !TextUtils.isEmpty(userInfo.getPasswordStr())){
                    // TODO: 2020/10/14 进入验证密码的页面。
                    Intent intent=new Intent(this, VerifySecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG,Constants.INTENT_PUT_IMPORT_WALLET);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(this, SetSecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG,Constants.INTENT_PUT_IMPORT_WALLET);
                    startActivity(intent);
                }
                break;
        }
    }
}