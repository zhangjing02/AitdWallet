package com.tianqi.aitdwallet.ui.activity.wallet.setting;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.GuidePageActivity;
import com.tianqi.aitdwallet.ui.activity.MainActivityForTab;
import com.tianqi.aitdwallet.ui.activity.wallet.importwallet.ImportWalletActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.BackupMemoryWordActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.SetSecurityPsdActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.widget.dialog.ForgetPsdNoticeDialog;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.digital.AESCipher;
import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Create by zhangjing on 2020/8/26.
 * Describe ：设置安全密码
 */

public class VerifySecurityPsdActivity extends BaseActivity {

    private static final String TAG = "SetSecurityPsdActivity";
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_input_notice)
    TextView tvInputNotice;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.iv_psd_show)
    ImageView ivPsdShow;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;

    private UserInformation userInfoManager;
    private String intent_target;
    private Dialog mLoadDialog;

    @Override
    protected void initView() {
        getToolBar();
        // intent.putExtra(Constants.INTENT_PUT_TAG,Constants.INTENT_PUT_IMPORT_WALLET);
        userInfoManager = UserInfoManager.getUserInfo();
        // StatusBarCompat.translucentStatusBar(this, true);

        etInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // TODO: 2020/10/16 随后根据密码规则来判断是否置灰
                if (editable.toString().length() >= 8) {
                    btnConfirm.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                } else {
                    btnConfirm.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                }
            }
        });
    }

    private void getToolBar() {
        intent_target = getIntent().getStringExtra(Constants.INTENT_PUT_TAG);
        toolbarTitle.setText(intent_target);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verify_security_psd;
    }


    @SuppressLint("CheckResult")
    @OnClick({R.id.iv_psd_show, R.id.btn_confirm, R.id.tv_forget_psd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_psd_show:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                showOrHidePsd(etInputPassword, ivPsdShow);
                break;
            case R.id.tv_forget_psd:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                ForgetPsdNoticeDialog shotNoticeDialog = new ForgetPsdNoticeDialog(this, R.style.MyDialog2);

                shotNoticeDialog.show();
                break;
            case R.id.btn_confirm:
                if (judgeSelectInput()) {
                    if (intent_target.equals(getString(R.string.tittle_import_wallet))) {
                        Intent intent = new Intent(this, ImportWalletActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_IMPORT_WALLET);
                        startActivity(intent);
                    } else if (intent_target.equals(getString(R.string.tittle_export_private_key))) {
                        Intent intent = new Intent(this, ExportPrivateKeyActivity.class);
                       // String wallet_name = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);
                        String coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
                        intent.putExtra(Constants.INTENT_PUT_COIN_ID, coin_id);
                        startActivity(intent);
                    } else if (intent_target.equals(getString(R.string.tittle_export_keystore))) {
                        Intent intent = new Intent(this, ExportKeystoreActivity.class);
                        String coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
                        intent.putExtra(Constants.INTENT_PUT_COIN_ID, coin_id);
                        intent.putExtra(Constants.INTENT_PUT_COIN_PASSWORD, etInputPassword.getText().toString());
                        startActivity(intent);
                    } else if (intent_target.equals(getString(R.string.tittle_back_up_mnemonic))) {
                        Intent intent = new Intent(this, BackupMemoryWordActivity.class);
//                        String wallet_name = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);
                        intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_ALREADY_MNEMONIC);
                        startActivity(intent);
                    } else if (intent_target.equals(Constants.INTENT_PUT_DELETE_CREATE_WALLET)) { //删除创建的钱包
                        // TODO: 2020/10/23 添加了loading，让用户等待一下。
                        mLoadDialog = LoadingDialogUtils.createLoadingDialog(this, "");
                        List<CoinInfo> coinInfoList_create = CoinInfoManager.getCreateCoinInfo();
                        List<CoinInfo> coinInfoList_import = CoinInfoManager.getImportCoinInfo();
                        delCoinInfo(coinInfoList_create, coinInfoList_import, 0);

                    } else if (intent_target.equals(Constants.INTENT_PUT_DELETE_IMPORT_WALLET)) { //删除导入的钱包
                        List<CoinInfo> coinInfoList_create = CoinInfoManager.getCreateCoinInfo();
                        List<CoinInfo> coinInfoList_import = CoinInfoManager.getImportCoinInfo();
                        delCoinInfo(coinInfoList_create, coinInfoList_import, 1);
                    } else if (intent_target.equals(getString(R.string.tittle_delete_wallet))) {
                        String coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
                        CoinInfo mainCoinFrCoinId = CoinInfoManager.getMainCoinFrCoinId(coin_id);
                        List<CoinInfo> coinFrWalletId = CoinInfoManager.getCoinFrWalletId(mainCoinFrCoinId.getWallet_id());
                        if (coinFrWalletId.size() <= 1) {
                            WalletInfo hdWalletInfoFrId = WalletInfoManager.getHdWalletInfoFrId(mainCoinFrCoinId.getWallet_id());
                            WalletInfoManager.deleteScaleRecord(hdWalletInfoFrId);
                        }
                        CoinInfoManager.deleteScaleRecord(mainCoinFrCoinId);
                        EventMessage eventMessage = new EventMessage();
                        eventMessage.setType(EventMessage.DELETE_CREATE_COIN_UPDATE);
                        EventBus.getDefault().post(eventMessage);
                        WalletInfo walletInfo = WalletInfoManager.getHdWalletInfo();
                        if (walletInfo != null) {
                            Intent intent = new Intent(this, MainActivityForTab.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(this, GuidePageActivity.class);
                            startActivity(intent);
                        }
                        ToastUtil.showToast(this, getString(R.string.notice_delete_wallet_success));
                    } else if (intent_target.equals(getString(R.string.tittle_create_wallet))) {
                        UserInformation userInfo = UserInfoManager.getUserInfo();
                        if (WalletInfoManager.getWalletInfo().size() > 0) {
                            WalletInfo walletInfo = WalletInfoManager.getWalletInfo().get(0);
                            LogUtil.i(TAG, walletInfo.getWalletType() + "+------onViewClicked: 我们看这次的钱包是？" + walletInfo.getIsImportToCreate());
                            if (walletInfo != null && walletInfo.getWalletType() >= 0 && !walletInfo.getIsImportToCreate()) {
                                Intent intent = new Intent(this, MainActivityForTab.class);
                                startActivity(intent);
                            } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
                                Intent intent = new Intent(this, BackupMemoryWordActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(this, SetSecurityPsdActivity.class);
                                intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_CREATE_WALLET);
                                //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
                                startActivity(intent);
                            }
                        } else if (userInfo != null && !TextUtils.isEmpty(userInfo.getPasswordStr())) {
                            Intent intent = new Intent(this, BackupMemoryWordActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(this, SetSecurityPsdActivity.class);
                            intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_CREATE_WALLET);
                            //Intent intent=new Intent(this, SelectWalletTypeActivity.class);
                            startActivity(intent);
                        }
                    } else if (intent_target.equals(getString(R.string.tittle_change_safe_psd))) {
                        Intent intent = new Intent(this, SetSecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_set_new_psd));
                        startActivity(intent);
                    }
                    finish();
                }
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void delCoinInfo(List<CoinInfo> coinInfoList_create, List<CoinInfo> coinInfoList_import, int type) {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            if (type == 0) {  //删除创建的。
                CoinInfoManager.deleteScaleRecords(coinInfoList_create);
                List<String> wallet_id_str = new ArrayList<>();
                if (coinInfoList_import.size() > 0) {
                    List<WalletInfo> walletInfos = WalletInfoManager.getWalletInfo();
                    for (int i = 0; i < walletInfos.size(); i++) {
                        wallet_id_str.add(walletInfos.get(i).getWallet_id());
                    }
                    for (int i = 0; i < coinInfoList_import.size(); i++) {
                        if (wallet_id_str.contains(coinInfoList_import.get(i).getWallet_id())) {
                            wallet_id_str.remove(coinInfoList_import.get(i).getWallet_id());
                        }
                    }
                    for (int i = 0; i < wallet_id_str.size(); i++) {
                        LogUtil.i(TAG, i + "------onViewClicked: 我们看删除了谁？" + wallet_id_str.get(i));
                        WalletInfoManager.deleteScaleRecord(WalletInfoManager.getHdWalletInfoFrId(wallet_id_str.get(i)));
                    }
                    emitter.onNext(0);
                } else {
                    emitter.onNext(1);
                    WalletInfoManager.clearScaleRecord();
                }
            } else {   //删除导入的。
                CoinInfoManager.deleteScaleRecords(coinInfoList_import);
                List<String> wallet_id_str = new ArrayList<>();
                if (coinInfoList_create.size() > 0) {
                    List<WalletInfo> walletInfos = WalletInfoManager.getWalletInfo();
                    for (int i = 0; i < walletInfos.size(); i++) {
                        wallet_id_str.add(walletInfos.get(i).getWallet_id());
                    }
                    for (int i = 0; i < coinInfoList_create.size(); i++) {
                        if (wallet_id_str.contains(coinInfoList_create.get(i).getWallet_id())) {
                            wallet_id_str.remove(coinInfoList_create.get(i).getWallet_id());
                        }
                    }
                    for (int i = 0; i < wallet_id_str.size(); i++) {
                        LogUtil.i(TAG, i + "------onViewClicked: 我们看删除了谁？" + wallet_id_str.get(i));
                        WalletInfoManager.deleteScaleRecord(WalletInfoManager.getHdWalletInfoFrId(wallet_id_str.get(i)));
                    }
                    emitter.onNext(2);
                } else {
                    emitter.onNext(3);
                    WalletInfoManager.clearScaleRecord();
                }
            }
        }).compose(RxHelper.pool_main()).subscribe(baseEntity -> {
            if (mLoadDialog != null) {
                mLoadDialog.dismiss();
            }
            if (baseEntity == 1 || baseEntity == 3) {  //
                Intent intent = new Intent(this, GuidePageActivity.class);
                startActivity(intent);
            } else if (baseEntity == 0) {//如果是还有导入的币种，就返回上以页面，并且刷新页面。
                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.DELETE_CREATE_COIN_UPDATE);
                EventBus.getDefault().post(eventMessage);
            } else if (baseEntity == 2) {
                EventMessage eventMessage = new EventMessage();
                eventMessage.setType(EventMessage.DELETE_IMPORT_COIN_UPDATE);
                EventBus.getDefault().post(eventMessage);
            }
            ToastUtil.showToast(this, getString(R.string.notice_delete_wallet_success));
            finish();
        });
    }

    /**
     * @return 判断输入是否合法
     */
    private boolean judgeSelectInput() {
        String aes_decode_str = AESCipher.decrypt(Constant.PSD_KEY, userInfoManager.getPasswordStr());
        if (TextUtils.isEmpty(etInputPassword.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_input_psd));
            return false;
        } else if (!aes_decode_str.equals(etInputPassword.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_psd_error));
            return false;
        }
        return true;
    }

    /**
     * 显示隐藏密码
     *
     * @param editText  需要显示的edittext控件，
     * @param imageView 需要点击的眼睛图标。
     */
    public void showOrHidePsd(EditText editText, ImageView imageView) {
        if (editText.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            imageView.setImageResource(R.mipmap.ic_open_eye);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            imageView.setImageResource(R.mipmap.ic_close_eye);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().toString().trim().length());
    }

}