package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.ethereum.EthECKeyPair;
import com.quincysx.crypto.ethereum.keystore.CipherException;
import com.quincysx.crypto.ethereum.keystore.KeyStore;
import com.quincysx.crypto.ethereum.keystore.KeyStoreFile;
import com.quincysx.crypto.utils.HexUtils;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.CodeEncoder;
import com.tianqi.baselib.utils.display.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class ExportKeystoreActivity extends BaseActivity {

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
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    private String[] titles;
    private Dialog mLoadDialog;

    // private String select_tittle = TITTLE_PRIVATE_KEY;
    private CoinInfo mainCoinFrCoinId;
    private int select_index;
    private static final int TITTLE_PRIVATE_KEY_INDEX = 0;
    private static final int TITTLE_QR_CODE_INDEX = 1;
    private String wallet_psd;
    private int height, width;
    private double ratio;

    @Override
    protected int getContentView() {
        return R.layout.activity_export_keystroe;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        ratio = height * 1000 / width / 1000f;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int densityDpi = metrics.densityDpi;
        LogUtil.i("ttttttttttttt", width + "showGuide: 我们看屏幕的高度是？" + height + "屏幕密度" + densityDpi);
        if (height <= 1920 && ratio < 1.7f) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnKeyCopy.getLayoutParams();
            layoutParams.setMargins( DensityUtil.dp2px(15f), DensityUtil.dp2px(10f),  DensityUtil.dp2px(15f), 0);
            btnKeyCopy.setLayoutParams(layoutParams);
        }

        titles = new String[]{getString(R.string.tittle_keystore_text), getString(R.string.tittle_qr_code_text)};
        getToolBar();
        String coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
        wallet_psd = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_PASSWORD);

        mainCoinFrCoinId = CoinInfoManager.getMainCoinFrCoinId(coin_id);
        //一般在创建钱包的时候，都已经存储了keystore，如果没有keystore就自己再生成一遍。
        if (mainCoinFrCoinId != null) {
            if (mainCoinFrCoinId.getKeystoreStr() == null || TextUtils.isEmpty(mainCoinFrCoinId.getKeystoreStr())) {
                progressBar1.setVisibility(View.VISIBLE);
                Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    emitter.onNext(mainCoinFrCoinId.getPrivateKey());
                }).map(response -> {
                    try {
                        EthECKeyPair ethECKeyPair = new EthECKeyPair(HexUtils.fromHex(mainCoinFrCoinId.getPrivateKey()));
                        KeyStoreFile light = KeyStore.createLight(wallet_psd, ethECKeyPair);
                        String keystore_str = light.toString();
                        mainCoinFrCoinId.setKeystoreStr(keystore_str);
                        CoinInfoManager.insertOrUpdate(mainCoinFrCoinId);
                        return keystore_str;
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    } catch (CipherException e) {
                        e.printStackTrace();
                    }
                    return Constant.HTTP_ERROR;
                }).compose(RxHelper.pool_main())
                        .subscribe(baseEntity -> {
                            progressBar1.setVisibility(View.GONE);
                            tvCoinPrivateKey.setText(baseEntity);
                        });
            } else {
                tvCoinPrivateKey.setText(mainCoinFrCoinId.getKeystoreStr());
            }
        }

        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
            tablayout.getTabAt(i).setText(titles[i]);
        }

        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                select_index = tab.getPosition();
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
                        Bitmap qrCodeBitmap = CodeEncoder.createImage(mainCoinFrCoinId.getKeystoreStr(), ivShowKeyQr.getLayoutParams().width, ivShowKeyQr.getLayoutParams().height, null);
                        ivShowKeyQr.setImageBitmap(qrCodeBitmap);
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
        toolbarTitle.setText(R.string.tittle_export_keystore);
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