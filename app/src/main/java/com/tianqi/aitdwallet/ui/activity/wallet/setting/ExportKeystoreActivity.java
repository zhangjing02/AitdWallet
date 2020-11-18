package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.ethereum.EthECKeyPair;
import com.quincysx.crypto.ethereum.keystore.CipherException;
import com.quincysx.crypto.ethereum.keystore.KeyStore;
import com.quincysx.crypto.ethereum.keystore.KeyStoreFile;
import com.quincysx.crypto.utils.HexUtils;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.rxhttp.HttpClientUtil;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetUnspentTxBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.CodeEncoder;
import com.tianqi.baselib.utils.display.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    private String[] titles;
    private Dialog mLoadDialog;

   // private String select_tittle = TITTLE_PRIVATE_KEY;
    private CoinInfo mainCoinFrCoinId;
    private int select_index;
    private static final int TITTLE_PRIVATE_KEY_INDEX = 0;
    private static final int TITTLE_QR_CODE_INDEX = 1;
    private  String wallet_psd;

    @Override
    protected int getContentView() {
        return R.layout.activity_export_keystroe;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        titles = new String[]{getString(R.string.tittle_keystore_text), getString(R.string.tittle_qr_code_text)};
        getToolBar();
        String coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
        wallet_psd = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_PASSWORD);

        mainCoinFrCoinId = CoinInfoManager.getMainCoinFrCoinId(coin_id);
        //一般在创建钱包的时候，都已经存储了keystore，如果没有keystore就自己再生成一遍。
        if (mainCoinFrCoinId!=null){
            if (mainCoinFrCoinId.getKeystoreStr()==null||TextUtils.isEmpty(mainCoinFrCoinId.getKeystoreStr())){
                Observable.create((ObservableOnSubscribe<String>) emitter -> {
                    emitter.onNext(mainCoinFrCoinId.getPrivateKey());
                }).map(response -> {
                    try {
                        EthECKeyPair ethECKeyPair=new EthECKeyPair(HexUtils.fromHex(mainCoinFrCoinId.getPrivateKey()));
                        KeyStoreFile light = KeyStore.createLight(wallet_psd,  ethECKeyPair);
                        String keystore_str=light.toString();
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
                            tvCoinPrivateKey.setText(baseEntity);
                        });
            }else {
                tvCoinPrivateKey.setText(mainCoinFrCoinId.getKeystoreStr());
            }
        }

        Bitmap qrCodeBitmap = CodeEncoder.createImage(mainCoinFrCoinId.getKeystoreStr(), ivShowKeyQr.getLayoutParams().width, ivShowKeyQr.getLayoutParams().height, null);
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