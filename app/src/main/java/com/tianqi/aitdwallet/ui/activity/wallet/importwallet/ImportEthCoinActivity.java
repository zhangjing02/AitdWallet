package com.tianqi.aitdwallet.ui.activity.wallet.importwallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.ethereum.keystore.CipherException;
import com.quincysx.crypto.ethereum.keystore.KeyStore;
import com.quincysx.crypto.ethereum.keystore.KeyStoreFile;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.MnemonicWordAdapter;
import com.tianqi.aitdwallet.ui.activity.MainActivity;
import com.tianqi.aitdwallet.ui.activity.tool.ScanActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.PrivacyTermsWebActivity;
import com.tianqi.aitdwallet.ui.service.DataManageService;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.utils.MnemonicUtils;
import com.tianqi.aitdwallet.utils.WalletUtils;
import com.tianqi.aitdwallet.widget.dialog.ExplainPrivateKeyDialog;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.CoinRateInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.CoinRateInfoManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.LoadingDialogUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class ImportEthCoinActivity extends BaseActivity {

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
    @BindView(R.id.et_input_key)
    EditText etInputKey;
    @BindView(R.id.checkbox_read_term)
    CheckBox checkboxReadTerm;
    @BindView(R.id.btn_import_wallet)
    TextView btnImportWallet;
    @BindView(R.id.gv_mnemonic_word)
    GridView gvMnemonicWord;
    @BindView(R.id.tv_explain_private_key)
    TextView tvExplainPrivateKey;
    @BindView(R.id.tv_input_error_notice)
    TextView tvInputErrorNotice;
    @BindView(R.id.tv_import_keystore_notice)
    TextView tvImportKeystoreNotice;
    @BindView(R.id.layout_import_keystore_notice)
    LinearLayout layoutImportKeystoreNotice;
    @BindView(R.id.et_keystore_psd)
    EditText etKeystorePsd;
    @BindView(R.id.iv_psd_show)
    ImageView ivPsdShow;
    @BindView(R.id.tv_service_privacy_terms)
    TextView tvServicePrivacyTerms;
    @BindView(R.id.layout_privacy_term)
    LinearLayout layoutPrivacyTerm;

    private String[] titles;

    // private String select_tittle = TITTLE_PRIVATE_KEY;
    private Dialog mLoadDialog;
    private List<String> list, select_list, show_list;
    private MnemonicWordAdapter mnemonicWordAdapter;

    private int select_index;
    private static final int TITTLE_PRIVATE_KEY_INDEX = 0;
    private static final int TITTLE_MNEMONIC_WORD_INDEX = 2;
    private static final int TITTLE_KEYSTORE = 1;
    private int height, width;
    private double ratio;
    private ECKeyPair decrypt;
    private static final int KEYSTORE_SAME_ERROR = 1;
    private static final int KEYSTORE_FORMAT_ERROR = 2;
    private static final int KEYSTORE_PASSWORD_ERROR = 3;
    private static final int KEYSTORE_PASSWORD_OK = 5;

    private DataManageService service = null;
    private boolean isBind = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_import_single_coin;
    }

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
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutPrivacyTerm.getLayoutParams();
            layoutParams.setMargins(0, DensityUtil.dp2px(160f), 0, 0);
            layoutPrivacyTerm.setLayoutParams(layoutParams);
        }

        titles = new String[]{getString(R.string.tittle_private_key_text), getString(R.string.tittle_keystore_text), getString(R.string.tittle_mnemonic_word_text)};
        getToolBar();
        list = MnemonicUtils.populateWordList();
        select_list = new ArrayList<>();
        show_list = new ArrayList<>();
        mnemonicWordAdapter = new MnemonicWordAdapter(this, select_list, 3);
        gvMnemonicWord.setAdapter(mnemonicWordAdapter);

        etInputKey.setHint(R.string.input_private_key_hint);
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
            tablayout.getTabAt(i).setText(titles[i]);
        }
        tablayout.addOnTabSelectedListener(onTabSelectedListener);
        etInputKey.addTextChangedListener(textWatcher);
        checkboxReadTerm.setOnCheckedChangeListener(onCheckedChangeListener);

        Intent intent = new Intent(this, DataManageService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }


    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            DataManageService.MyBinder myBinder = (DataManageService.MyBinder) binder;
            service = myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };


    TabLayout.BaseOnTabSelectedListener onTabSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            select_index = tab.getPosition();
            //  select_tittle = tab.getText().toString();
            switch (select_index) {
                case TITTLE_PRIVATE_KEY_INDEX:
                    gvMnemonicWord.setVisibility(View.GONE);
                    etInputKey.setHint(R.string.input_private_key_hint);
                    etInputKey.setText("");
                    etKeystorePsd.setText("");
                    layoutImportKeystoreNotice.setVisibility(View.GONE);
                    tvImportKeystoreNotice.setVisibility(View.GONE);
                    etKeystorePsd.setVisibility(View.GONE);
                    ivPsdShow.setVisibility(View.GONE);

                    LogUtil.i("tttttttttttt", ratio + "我们看转换的尺寸" + DensityUtil.dp2px(10f));
                    if (height <= 1920 && ratio < 1.7f) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutPrivacyTerm.getLayoutParams();
                        layoutParams.setMargins(0, DensityUtil.dp2px(160f), 0, 0);
                        layoutPrivacyTerm.setLayoutParams(layoutParams);
                    } else {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutPrivacyTerm.getLayoutParams();
                        layoutParams.setMargins(0, DensityUtil.dp2px(260f), 0, 0);
                        layoutPrivacyTerm.setLayoutParams(layoutParams);
                    }
                    break;
                case TITTLE_KEYSTORE:
                    gvMnemonicWord.setVisibility(View.GONE);
                    etInputKey.setHint(R.string.input_keystore_hint);
                    etInputKey.setText("");

                    layoutImportKeystoreNotice.setVisibility(View.VISIBLE);
                    tvImportKeystoreNotice.setVisibility(View.VISIBLE);
                    etKeystorePsd.setVisibility(View.VISIBLE);
                    ivPsdShow.setVisibility(View.VISIBLE);

                    if (height <= 1920 && ratio < 1.7f) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutPrivacyTerm.getLayoutParams();
                        layoutParams.setMargins(0, DensityUtil.dp2px(50f), 0, 0);
                        layoutPrivacyTerm.setLayoutParams(layoutParams);
                    } else {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutPrivacyTerm.getLayoutParams();
                        layoutParams.setMargins(0, DensityUtil.dp2px(210f), 0, 0);
                        layoutPrivacyTerm.setLayoutParams(layoutParams);
                    }
                    break;
                case TITTLE_MNEMONIC_WORD_INDEX:
                    gvMnemonicWord.setVisibility(View.VISIBLE);
                    etInputKey.setHint(R.string.input_mnemonic_word_hint);
                    etInputKey.setText("");
                    etKeystorePsd.setText("");

                    layoutImportKeystoreNotice.setVisibility(View.GONE);
                    tvImportKeystoreNotice.setVisibility(View.GONE);
                    etKeystorePsd.setVisibility(View.GONE);
                    ivPsdShow.setVisibility(View.GONE);
                    if (height <= 1920 && ratio < 1.7f) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutPrivacyTerm.getLayoutParams();
                        layoutParams.setMargins(0, DensityUtil.dp2px(150f), 0, 0);
                        layoutPrivacyTerm.setLayoutParams(layoutParams);
                    } else {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layoutPrivacyTerm.getLayoutParams();
                        layoutParams.setMargins(0, DensityUtil.dp2px(260f), 0, 0);
                        layoutPrivacyTerm.setLayoutParams(layoutParams);
                    }
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @SuppressLint("CheckResult")
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (select_index == TITTLE_MNEMONIC_WORD_INDEX) {
                //创建被观察者
                select_list.clear();
                Observable.fromIterable(list).filter(s -> {    //对集合数据进行过滤,只发送符合条件的事件
                    String find_key_word = charSequence.toString().substring(charSequence.toString().lastIndexOf(" ") + 1);
                    return s.startsWith(find_key_word);
                }).take(5).doOnComplete(() -> {
                    if (select_list.size() > 0) {
                        tvInputErrorNotice.setVisibility(View.GONE);
                        mnemonicWordAdapter.refreshData(select_list);
                        show_list = select_list;
                    } else {
                        tvInputErrorNotice.setVisibility(View.VISIBLE);
                    }
                }).subscribe(s -> {
                    if (!TextUtils.isEmpty(s)) {
                        select_list.add(s);
                    }
                });
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (select_index) {
                case TITTLE_PRIVATE_KEY_INDEX:
                    if (!TextUtils.isEmpty(editable.toString()) && checkboxReadTerm.isChecked()) {

                        if (etInputKey.getText().toString().trim().startsWith("0x")){
                            if (etInputKey.getText().toString().length() == 66){
                                btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                            }else {
                                btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                            }
                        }else {
                            if (etInputKey.getText().toString().length() == 64){
                                btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                            }else {
                                btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                            }
                        }
                    } else {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    }
                    break;
                case TITTLE_MNEMONIC_WORD_INDEX:
                    String[] mn_words = editable.toString().split("\\s+");
                    // String[] mn_words2 = editable.toString().split(" ");
                    if (judeMnwordsCorrect(mn_words) && checkboxReadTerm.isChecked()) {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                    } else {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    }
                    break;
                case TITTLE_KEYSTORE:
                    if (!TextUtils.isEmpty(etInputKey.getText().toString()) && checkboxReadTerm.isChecked()) {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                    } else {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    }
                    break;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (select_index) {
                case TITTLE_PRIVATE_KEY_INDEX:
                    if (!TextUtils.isEmpty(etInputKey.getText().toString()) && checkboxReadTerm.isChecked()) {
                        if (etInputKey.getText().toString().trim().startsWith("0x")){
                           if (etInputKey.getText().toString().length() == 66){
                               btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                           }else {
                               btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                           }
                        }else {
                            if (etInputKey.getText().toString().length() == 64){
                                btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                            }else {
                                btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                            }
                        }
                    } else {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    }
                    break;
                case TITTLE_MNEMONIC_WORD_INDEX:
                    String[] mn_words = etInputKey.getText().toString().split("\\s+");
                    if (judeMnwordsCorrect(mn_words) && checkboxReadTerm.isChecked()) {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                    } else {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    }
                    break;
                case TITTLE_KEYSTORE:
                    if (!TextUtils.isEmpty(etInputKey.getText().toString()) && checkboxReadTerm.isChecked()) {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                    } else {
                        btnImportWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    }
                    break;
            }
        }
    };

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_import_wallet);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnCollect.setVisibility(View.VISIBLE);
        btnCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_scan));
    }

    @Override
    protected void initData() {
        gvMnemonicWord.setOnItemClickListener((adapterView, view, i, l) -> {
            String before_str = etInputKey.getText().toString();
            String tem_str = "";
            if (before_str.contains(" ") && before_str.length() > 1) {
                tem_str = before_str.substring(0, etInputKey.getText().toString().lastIndexOf(" "));
                etInputKey.setText(tem_str + " " + show_list.get(i) + " ");
            } else {
                etInputKey.setText(show_list.get(i) + " ");
            }
            etInputKey.setSelection(etInputKey.getText().toString().length());
            show_list.clear();
            mnemonicWordAdapter.refreshData(show_list);
        });
    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.SCAN_EVENT) {
            etInputKey.setText(event.getMsg());
            etInputKey.setSelection(event.getMsg().length());
        }
    }

    @SuppressLint("CheckResult")
    @OnClick({R.id.btn_collect, R.id.btn_import_wallet, R.id.tv_explain_private_key, R.id.tv_service_privacy_terms, R.id.iv_psd_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent, 25);
                break;
            case R.id.tv_service_privacy_terms:
                intent = new Intent(this, PrivacyTermsWebActivity.class);
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse("http://mine-pool.aitdcoin.com/aitd-coin.html ");
//                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.tv_explain_private_key:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                ExplainPrivateKeyDialog shotNoticeDialog = new ExplainPrivateKeyDialog(this, R.style.MyDialog2);
                shotNoticeDialog.show();
                break;
            case R.id.iv_psd_show:
                showOrHidePsd(etKeystorePsd, ivPsdShow);
                break;
            case R.id.btn_import_wallet:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                switch (select_index) {
                    case TITTLE_PRIVATE_KEY_INDEX:
                        if (judgeSelectInput()) {
                            mLoadDialog = LoadingDialogUtils.createLoadingDialog(this, "");
                            ECKeyPair ecKeyPair = WalletUtils.importCoinMaser(CoinTypes.Ethereum,
                                    etInputKey.getText().toString().trim().startsWith("0x")?etInputKey.getText().toString().trim().substring(2):
                                            etInputKey.getText().toString().trim());
                            importSingleCoin(ecKeyPair);
                        }
                        break;
                    case TITTLE_MNEMONIC_WORD_INDEX:
                        String[] mn_words = etInputKey.getText().toString().split("\\s+");
                        if (judeMnwordsCorrect(mn_words) && checkboxReadTerm.isChecked()) {
                            mLoadDialog = LoadingDialogUtils.createLoadingDialog(this, "");
                            ECKeyPair ecKeyPair02 = WalletUtils.importCoinMaser(CoinTypes.Ethereum, Arrays.asList(mn_words));
                            importSingleCoin(ecKeyPair02);
                        } else {
                            if (mLoadDialog != null) {
                                mLoadDialog.dismiss();
                            }
                            ToastUtil.showToast(this, getString(R.string.notice_mnemonic_wore_error));
                        }
                        break;
                    case TITTLE_KEYSTORE:
                        //   通过导入的keystore，生成私钥，公钥，地址。
                        if (judgeKeystoreInput()) {
                            importKeystoreForEth();
                        }
                        break;
                }
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void importKeystoreForEth() {
        mLoadDialog = LoadingDialogUtils.createLoadingDialog(this, "");
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            try {
                String content = etInputKey.getText().toString().toLowerCase().trim().replace(" ","");
                if (content.contains("x-ethers")) {  //兼容ios给的keystore数据，如果有这个就截取掉，不要。
                    int star_index = content.indexOf("{", content.indexOf("x-ethers")) - 11;
                    int end_index;
                    if (content.indexOf(",", content.indexOf("}", content.indexOf("x-ethers"))) > 0) {
                        end_index = content.indexOf("}", content.indexOf("x-ethers")) + 2;
                    } else {
                        star_index = content.indexOf("{", content.indexOf("x-ethers")) - 13;
                        end_index = content.indexOf("}", content.indexOf("x-ethers")) + 1;
                    }
                    String yy = content.substring(star_index, end_index);
                    content = content.replace(yy, "");
                }

                KeyStoreFile keyStoreFile = KeyStoreFile.parse(content);
                decrypt = KeyStore.decrypt(etKeystorePsd.getText().toString(), keyStoreFile);
                if (CoinInfoManager.getCoinFrPrivateKey(Constant.TRANSACTION_COIN_NAME_ETH, decrypt.getPrivateKey()).size() > 0) {
                    emitter.onNext(KEYSTORE_SAME_ERROR);
                } else {
                    emitter.onNext(KEYSTORE_PASSWORD_OK);
                }
            } catch (IOException e) {
                emitter.onNext(KEYSTORE_FORMAT_ERROR);
                e.printStackTrace();
            } catch (CipherException e) {
                emitter.onNext(KEYSTORE_PASSWORD_ERROR);
                e.printStackTrace();
            }
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    switch (baseEntity) {
                        case KEYSTORE_SAME_ERROR:
                            ToastUtil.showToast(this, getString(R.string.notice_same_keystore_text));
                            if (mLoadDialog != null) {
                                mLoadDialog.dismiss();
                            }
                            break;
                        case KEYSTORE_FORMAT_ERROR:
                            if (mLoadDialog != null) {
                                mLoadDialog.dismiss();
                            }
                            ToastUtil.showToast(this, getString(R.string.notice_keystore_error_text));
                            break;
                        case KEYSTORE_PASSWORD_ERROR:
                            if (mLoadDialog != null) {
                                mLoadDialog.dismiss();
                            }
                            ToastUtil.showToast(this, getString(R.string.notice_keystore_psd_error_text));
                            break;
                        case KEYSTORE_PASSWORD_OK:
                            importSingleCoin(decrypt);
                            break;
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void importSingleCoin(ECKeyPair ecKeyPair) {
        Observable.create((ObservableOnSubscribe<ECKeyPair>) emitter -> {
            //此处的文件夹只做临时存储，便于后两个页面获取wallet，正式的wallet存储文件夹，不用这个。
            emitter.onNext(ecKeyPair);
        }).map(master -> {  //解析网络请求，并且拼接交易对象，再把交易对象签名成hex。
            //钱包数据库,目前先只拿名字是BTC的，钱包。
            WalletInfo walletInfo = WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_ETH);
            if (walletInfo == null) {
                walletInfo = createWalletInfo(Constant.TRANSACTION_COIN_NAME_ETH);
            }
            CoinRateInfo btc_rate = CoinRateInfoManager.getWalletBtcFrCoinId(Constant.COIN_RATE_ETH);
            //币种数据库
            CoinInfo coinInfo = new CoinInfo();
            coinInfo.setCoin_address(Constants.HEX_PREFIX + master.getAddress());
            if (btc_rate != null && walletInfo != null) {
                walletInfo.setCoin_CNYPrice(btc_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(btc_rate.getPrice_usd());
            }
            LogUtil.i("WalletFragment", "importSingleCoin: 我们得到的钱包是什么？" + walletInfo.toString());
            coinInfo.setCoin_fullName(Constant.COIN_FULL_NAME_ETH);

            List<CoinInfo> walletBtcInfo = CoinInfoManager.getCoinEthImportInfo();
            coinInfo.setCoin_ComeType(Constant.COIN_SOURCE_IMPORT);
            coinInfo.setCoin_id(Constant.IMPORT_ETH_ID + walletBtcInfo.size());
            // coinInfo.setIsCollect();
            coinInfo.setPrivateKey(master.getPrivateKey());
            coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_ETH);
            coinInfo.setCoin_type(Constant.COIN_BIP_TYPE_ETH);
            if (select_index == TITTLE_KEYSTORE) {
                coinInfo.setKeystoreStr(etInputKey.getText().toString());
            }
            coinInfo.setAlias_name(Constant.TRANSACTION_COIN_NAME_ETH);
            coinInfo.setResourceId(R.mipmap.ic_circle_eth);
            coinInfo.setPublicKey(master.getPublicKey());
            coinInfo.setWallet_id(walletInfo.getWallet_id());
            //  coinInfo.setWalletLimit();  //不需要限制。
            CoinInfoManager.insertOrUpdate(coinInfo);
            //保存一个文件形式，方便加载的时候，能很快加载出钱包。否则每次去生成会很慢。
            if (isBind) {
                service.createEthWallet(coinInfo);
            }
            return coinInfo;
        }).delay(2, TimeUnit.SECONDS).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    if (mLoadDialog != null) {
                        mLoadDialog.dismiss();
                    }
                    ToastUtil.showToast(this, getString(R.string.notice_import_success));
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    EventMessage eventMessage = new EventMessage();
                    eventMessage.setType(EventMessage.NEW_COIN_UPDATE);
                    EventBus.getDefault().post(eventMessage);
                    finish();
                });
    }

    /**
     * @return 判断输入是否合法
     */
    private boolean judgeSelectInput() {
        if (TextUtils.isEmpty(etInputKey.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_input_content));
            return false;
        } else if (!checkboxReadTerm.isChecked()) {
            ToastUtil.showToast(this, getString(R.string.notice_agree_terms));
            return false;
        } else if (select_index == TITTLE_PRIVATE_KEY_INDEX ) {
            if (etInputKey.getText().toString().trim().startsWith("0x")){
                if (etInputKey.getText().toString().trim().length() != 66){
                    ToastUtil.showToast(this, getString(R.string.notice_private_key_error));
                    return false;
                }
            }else {
                if (etInputKey.getText().toString().trim().length() != 64){
                    ToastUtil.showToast(this, getString(R.string.notice_private_key_error));
                    return false;
                }
            }
            return true;
        } else if (CoinInfoManager.getCoinFrPrivateKey(Constant.TRANSACTION_COIN_NAME_ETH, etInputKey.getText().toString()).size() > 0) {
            ToastUtil.showToast(this, getString(R.string.notice_same_private_key));
            return false;
        }
        return true;
    }

    /**
     * @return 判断输入是否合法
     */
    private boolean judgeKeystoreInput() {
        if (TextUtils.isEmpty(etInputKey.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_input_content));
            return false;
        } else if (!checkboxReadTerm.isChecked()) {
            ToastUtil.showToast(this, getString(R.string.notice_agree_terms));
            return false;
        }
        return true;
    }

    private boolean judeMnwordsCorrect(String[] mn_words) {
        if (mn_words.length == 12) {
            for (int i = 0; i < mn_words.length; i++) {
                if (!list.contains(mn_words[i])) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private WalletInfo createWalletInfo(String wallet_id) {
        //钱包数据库。
        CoinRateInfo coinRateInfo = CoinRateInfoManager.getWalletBtcFrCoinId(Constant.TRANSACTION_COIN_NAME_ETH);
        UserInformation userInfo = UserInfoManager.getUserInfo();
        WalletInfo walletInfo = new WalletInfo();
        walletInfo.setUserId(userInfo.getUserId());
        walletInfo.setWalletName(wallet_id);
        walletInfo.setAlias_name(wallet_id);
        walletInfo.setWalletType(Constant.WALLET_TYPE_HD);
        walletInfo.setWallet_id(wallet_id);
        walletInfo.setCoin_CNYPrice(coinRateInfo.getPrice_cny());
        walletInfo.setCoin_USDPrice(coinRateInfo.getPrice_usd());
        walletInfo.setIsImportToCreate(true);
        if (wallet_id.equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            CoinRateInfo bitcoin_rate = CoinRateInfoManager.getWalletBtcFrCoinId(Constant.COIN_RATE_ETH);
            if (bitcoin_rate != null) {
                walletInfo.setCoin_CNYPrice(bitcoin_rate.getPrice_cny());
                walletInfo.setCoin_USDPrice(bitcoin_rate.getPrice_usd());
            } else {
                CoinRateInfo walletBtcFrCoinId = CoinRateInfoManager.getWalletBtcFrCoinId(Constant.TRANSACTION_COIN_NAME_ETH);
                walletInfo.setCoin_CNYPrice(walletBtcFrCoinId.getPrice_cny());
                walletInfo.setCoin_USDPrice(walletBtcFrCoinId.getPrice_usd());

            }
            walletInfo.setResource_id(R.mipmap.ic_circle_eth);
        }
        WalletInfoManager.insertOrUpdate(walletInfo);
        return walletInfo;
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