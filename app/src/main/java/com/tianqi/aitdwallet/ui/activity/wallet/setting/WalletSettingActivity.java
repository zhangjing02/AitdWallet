package com.tianqi.aitdwallet.ui.activity.wallet.setting;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletSettingActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_wallet_coin)
    ImageView ivWalletCoin;
    @BindView(R.id.tv_wallet_coin_name)
    TextView tvWalletCoinName;
    @BindView(R.id.tv_coin_address)
    TextView tvCoinAddress;
    @BindView(R.id.iv_coin_address_copy)
    ImageView ivCoinAddressCopy;
    @BindView(R.id.tv_change_wallet_name)
    TextView tvChangeWalletName;
    @BindView(R.id.tv_psd_notice)
    TextView tvPsdNotice;
    @BindView(R.id.tv_export_private_key)
    TextView tvExportPrivateKey;
    @BindView(R.id.tv_export_keystore)
    TextView tvExportKeystore;
    @BindView(R.id.tv_back_up_mnemonic)
    TextView tvBackUpMnemonic;
    @BindView(R.id.tv_delete_wallet)
    TextView tvDeleteWallet;
    @BindView(R.id.line_down_keystore)
    View lineDownKeystore;
    @BindView(R.id.line_down_mnemonic)
    View lineDownMnemonic;
    private String wallet_name;
    private String coin_id;
    private CoinInfo coinFrWalletId;

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_setting;
    }

    @Override
    protected void initView() {
        // StatusBarCompat.translucentStatusBar(this, true);
        getToolBar();
        wallet_name = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);

        coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
        //WalletInfo wallet_info = WalletInfoManager.getWalletFrName(wallet_name);
        coinFrWalletId = CoinInfoManager.getMainCoinFrCoinId(coin_id);

        GlideUtils.loadResourceImage(this, coinFrWalletId.getResourceId(), ivWalletCoin);

        tvWalletCoinName.setText(coinFrWalletId.getAlias_name() + "");
        tvCoinAddress.setText(coinFrWalletId.getCoin_address());
        if (wallet_name.contains(Constant.TRANSACTION_COIN_NAME_ETH)||wallet_name.contains(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)) {
            tvExportKeystore.setVisibility(View.VISIBLE);
            lineDownKeystore.setVisibility(View.VISIBLE);
        } else {
            tvExportKeystore.setVisibility(View.GONE);
            lineDownKeystore.setVisibility(View.GONE);
        }
    }

    private void getToolBar() {
        //根据传过来的币种，决定tittle
        toolbarTitle.setText("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.white_transparent));
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.COIN_NAME_UPDATE) {
            CoinInfo coinFrWalletId = CoinInfoManager.getMainCoinFrCoinId(coin_id);
            tvWalletCoinName.setText(coinFrWalletId.getAlias_name() + "");
        }
    }

    @OnClick({R.id.iv_coin_address_copy, R.id.tv_change_wallet_name, R.id.tv_psd_notice, R.id.tv_export_private_key, R.id.tv_export_keystore, R.id.tv_back_up_mnemonic, R.id.tv_delete_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_coin_address_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvCoinAddress.getText().toString());
                ToastUtil.showToast(this, getString(R.string.notice_copy_success));
                break;
            case R.id.tv_change_wallet_name:
                Intent intent = new Intent(this, ChangeWalletNameActivity.class);
                intent.putExtra(Constants.INTENT_PUT_COIN_ID, coin_id);
                intent.putExtra(Constants.TRANSACTION_COIN_NAME, wallet_name);
                startActivity(intent);
                break;
            case R.id.tv_psd_notice:
                intent = new Intent(this, PasswordPromptActivity.class);
                // intent.putExtra(Constants.TRANSACTION_COIN_NAME,wallet_name);
                startActivity(intent);
                break;
            case R.id.tv_export_private_key:
                intent = new Intent(this, VerifySecurityPsdActivity.class);
                intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_export_private_key));
               // intent.putExtra(Constants.TRANSACTION_COIN_NAME, wallet_name);
                intent.putExtra(Constants.INTENT_PUT_COIN_ID, coin_id);
                startActivity(intent);
                break;
            case R.id.tv_export_keystore:
                intent = new Intent(this, VerifySecurityPsdActivity.class);
                intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_export_keystore));
                intent.putExtra(Constants.INTENT_PUT_COIN_ID, coin_id);
                startActivity(intent);
                break;
            case R.id.tv_back_up_mnemonic:
                //INTENT_PUT_BACK_UP_MNEMONIC
                intent = new Intent(this, VerifySecurityPsdActivity.class);
                intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_back_up_mnemonic));
                intent.putExtra(Constants.TRANSACTION_COIN_NAME, wallet_name);
                startActivity(intent);
                break;
            case R.id.tv_delete_wallet:
                intent = new Intent(this, VerifySecurityPsdActivity.class);
               // intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_DELETE_COIN);
                intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_delete_wallet));
                intent.putExtra(Constants.INTENT_PUT_COIN_ID, coin_id);
                startActivity(intent);
                break;
        }
    }

}