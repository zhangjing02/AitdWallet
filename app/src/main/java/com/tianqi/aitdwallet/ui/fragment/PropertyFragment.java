package com.tianqi.aitdwallet.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.MainActivityForTab;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.VerifySecurityPsdActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.SelectWalletTypeActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.SetSecurityPsdActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseFragment;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;

import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.WalletProtobufSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class PropertyFragment extends BaseFragment {
    @BindView(R.id.property_tittle)
    TextView propertyTittle;
    @BindView(R.id.property_slogan)
    TextView propertySlogan;
    @BindView(R.id.property_slogan2)
    TextView propertySlogan2;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;
    @BindView(R.id.btn_import_wallet)
    TextView btnImportWallet;

    @Override
    protected void initView() {
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_property;
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.btn_create_wallet, R.id.btn_import_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_wallet:
                if (WalletInfoManager.getWalletInfo().size()>0){
                    WalletInfo walletInfo = WalletInfoManager.getWalletInfo().get(0);
                    if (walletInfo!=null&&walletInfo.getWalletType()>=0){
                        Intent intent=new Intent(getActivity(), MainActivityForTab.class);
                        startActivity(intent);
                    }
                }else {
                    Intent intent=new Intent(getActivity(), SelectWalletTypeActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_import_wallet:
                UserInformation userInfo = UserInfoManager.getUserInfo();
                if (userInfo!=null&& !TextUtils.isEmpty(userInfo.getPasswordStr())){
                    // TODO: 2020/10/14 进入验证密码的页面。
                    Intent intent=new Intent(getActivity(), VerifySecurityPsdActivity.class);
                                 intent.putExtra(Constants.INTENT_PUT_TAG,getString(R.string.tittle_import_wallet));
                    startActivity(intent);

                }else {
                    Intent intent=new Intent(getActivity(), SetSecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG,getString(R.string.tittle_import_wallet));
                    startActivity(intent);
                }
                break;
        }
    }

    private Wallet readWallet(File walletFile) throws FileNotFoundException, UnreadableWalletException {
        InputStream inputStream = new FileInputStream(walletFile);
        Wallet wallet = new WalletProtobufSerializer().readWallet(inputStream);
        wallet.autosaveToFile(walletFile, 3 * 1000, TimeUnit.MILLISECONDS, null);
        wallet.cleanup();
        return wallet;
    }
}
