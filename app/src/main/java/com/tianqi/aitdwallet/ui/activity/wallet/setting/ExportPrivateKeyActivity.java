package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.display.CodeEncoder;
import com.tianqi.baselib.utils.display.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExportPrivateKeyActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.tv_export_key_notice)
    TextView tvExportKeyNotice;
    @BindView(R.id.layout_export_key_notice)
    LinearLayout layoutExportKeyNotice;
    @BindView(R.id.tv_coin_private_key)
    TextView tvCoinPrivateKey;
    @BindView(R.id.layout_export_qr_notice)
    LinearLayout layoutExportQrNotice;
    @BindView(R.id.btn_key_copy)
    TextView btnKeyCopy;
    @BindView(R.id.iv_key_qr)
    ImageView ivKeyQr;
    @BindView(R.id.btn_show_qr_code)
    TextView btnShowQrCode;
    @BindView(R.id.layout_key_qr_code)
    LinearLayout layoutKeyQrCode;
    @BindView(R.id.btn_hide_qr_code)
    TextView btnHideQrCode;
    @BindView(R.id.iv_show_key_qr)
    ImageView ivShowKeyQr;

    private String[] titles;
    private Dialog mLoadDialog;

   // private String select_tittle = TITTLE_PRIVATE_KEY;
    private CoinInfo mainCoinFrCoinId;
    private int select_index;
    private static final int TITTLE_PRIVATE_KEY_INDEX = 0;
    private static final int TITTLE_QR_CODE_INDEX = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_export_private_key;
    }

    @Override
    protected void initView() {
        titles = new String[]{getString(R.string.tittle_private_key_text), getString(R.string.tittle_qr_code_text)};
        getToolBar();
        String coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);

        mainCoinFrCoinId = CoinInfoManager.getMainCoinFrCoinId(coin_id);
        tvCoinPrivateKey.setText(mainCoinFrCoinId.getPrivateKey());
        Bitmap qrCodeBitmap = CodeEncoder.createImage(mainCoinFrCoinId.getPrivateKey(), ivShowKeyQr.getLayoutParams().width, ivShowKeyQr.getLayoutParams().height, null);
        ivShowKeyQr.setImageBitmap(qrCodeBitmap);
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
            tablayout.getTabAt(i).setText(titles[i]);
        }


        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                select_index=tab.getPosition();
               // select_tittle = tab.getText().toString();
                switch (select_index) {
                    case TITTLE_PRIVATE_KEY_INDEX:
                        layoutKeyQrCode.setVisibility(View.GONE);
                        layoutExportQrNotice.setVisibility(View.GONE);
                        btnShowQrCode.setVisibility(View.GONE);

                        layoutExportKeyNotice.setVisibility(View.VISIBLE);
                        tvCoinPrivateKey.setVisibility(View.VISIBLE);
                        btnKeyCopy.setVisibility(View.VISIBLE);
                        // etInputKey.setHint("明文私钥");
                        break;
                    case TITTLE_QR_CODE_INDEX:
                        //  etInputKey.setHint("明文keystore");
                        layoutKeyQrCode.setVisibility(View.VISIBLE);
                        layoutExportQrNotice.setVisibility(View.VISIBLE);
                        btnShowQrCode.setVisibility(View.VISIBLE);

                        layoutExportKeyNotice.setVisibility(View.GONE);
                        tvCoinPrivateKey.setVisibility(View.GONE);
                        btnKeyCopy.setVisibility(View.GONE);

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_export_private_key);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {
    }


    @OnClick({R.id.btn_key_copy, R.id.btn_show_qr_code, R.id.btn_hide_qr_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_key_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvCoinPrivateKey.getText().toString());
                ToastUtil.showToast(this, getString(R.string.notice_copy_success));
                break;
            case R.id.btn_show_qr_code:
                btnShowQrCode.setVisibility(View.GONE);
                btnHideQrCode.setVisibility(View.VISIBLE);
                ivShowKeyQr.setVisibility(View.VISIBLE);
                ivKeyQr.setVisibility(View.GONE);
                break;
            case R.id.btn_hide_qr_code:
                btnShowQrCode.setVisibility(View.VISIBLE);
                btnHideQrCode.setVisibility(View.GONE);
                ivShowKeyQr.setVisibility(View.GONE);
                ivKeyQr.setVisibility(View.VISIBLE);

                break;
        }
    }

    @OnClick(R.id.iv_show_key_qr)
    public void onViewClicked() {
    }
}