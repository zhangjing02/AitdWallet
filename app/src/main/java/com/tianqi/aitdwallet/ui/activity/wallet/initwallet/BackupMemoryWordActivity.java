package com.tianqi.aitdwallet.ui.activity.wallet.initwallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.bip32.ExtendedKey;
import com.quincysx.crypto.bip39.MnemonicGenerator;
import com.quincysx.crypto.bip39.RandomSeed;
import com.quincysx.crypto.bip39.SeedCalculator;
import com.quincysx.crypto.bip39.WordCount;
import com.quincysx.crypto.bip39.wordlists.English;
import com.quincysx.crypto.bip44.AddressIndex;
import com.quincysx.crypto.bip44.BIP44;
import com.quincysx.crypto.bip44.CoinPairDerive;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.MnemonicWordAdapter;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.widget.dialog.ScreenShotNoticeDialog;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.LogUtil;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * Create by zhangjing on 2020/8/26.
 * Describe ：备份助记词页面
 */
public class BackupMemoryWordActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_back_up_notice)
    TextView tvBackUpNotice;
    @BindView(R.id.gv_mnemonic_word)
    GridView gvMnemonicWord;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;
    private UserInformation userInformation;
    private MnemonicWordAdapter mnemonicWordAdapter;
    private static final SecureRandom secureRandom = new SecureRandom();
    private ECKeyPair master = null;

    private List<String> strings;
    private static final String TAG = "BackupMemoryWordActivit";

    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    };

    /** 读取媒体数据库时需要读取的列 */
    private static final String[] MEDIA_PROJECTIONS =  {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
    };

    /** 内部存储器内容观察者 */
    private ContentObserver mInternalObserver;

    /** 外部存储器内容观察者 */
    private ContentObserver mExternalObserver;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        getToolBar();
        //禁止屏幕截屏
    //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        String already_mnemonic= getIntent().getStringExtra(Constants.INTENT_PUT_TAG);

        byte[] random = RandomSeed.random(WordCount.TWELVE);
        Observable.just(random).map(s -> {
            if (TextUtils.isEmpty(already_mnemonic)){
                strings = new MnemonicGenerator(English.INSTANCE).createMnemonic(s);
            }else if (Constants.INTENT_PUT_ALREADY_MNEMONIC.equals(already_mnemonic)){
                btnCreateWallet.setVisibility(View.GONE);
                UserInformation userInfo = UserInfoManager.getUserInfo();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<String>>() {
                }.getType();
                strings = gson.fromJson(userInfo.getMnemonicWord(), listType);
            }
            byte[] seed = new SeedCalculator().calculateSeed(strings, "");
            ExtendedKey extendedKey = ExtendedKey.create(seed);
            AddressIndex address = BIP44.m().purpose44()
                    .coinType(CoinTypes.Bitcoin)
                    .account(0)
                    .external()
                    .address(0);
            CoinPairDerive coinKeyPair = new CoinPairDerive(extendedKey);
            ECKeyPair master = coinKeyPair.derive(address);

            return master;
        }).compose(RxHelper.compute_main()).subscribe(ecKeyPair -> {
            String xx=strings.toString().replace(","," ");
            LogUtil.i(TAG, "initView: 我们看助记词"+xx);
            mnemonicWordAdapter = new MnemonicWordAdapter(BackupMemoryWordActivity.this, strings,0);
            gvMnemonicWord.setAdapter(mnemonicWordAdapter);
        });
    }
    
    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_back_up_mnemonic);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {
        userInformation = UserInfoManager.getUserInfo();

        mHandlerThread = new HandlerThread("Screenshot_Observer");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        // 初始化
        mInternalObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, mHandler);
        mExternalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mHandler);

        // 添加监听
        this.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver
        );
        this.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver
        );


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_backup_memory_word;
    }


    @OnClick({ R.id.btn_create_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_create_wallet:
                Gson gson = new Gson();
                String data = gson.toJson(strings);
                userInformation.setMnemonicWord(data);
                UserInfoManager.insertOrUpdate(userInformation);

                Intent intent=new Intent(this, VerifyMemoryWordActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            cursor = this.getContentResolver().query(
                    contentUri,
                    MEDIA_PROJECTIONS,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            );

            if (cursor == null) {
                return;
            }
            if (!cursor.moveToFirst()) {
                return;
            }

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);

            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);

            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    /**
     * 处理监听到的资源
     */
    private void handleMediaRowData(String data, long dateTaken) {
        if (checkScreenShot(data, dateTaken)) {
            Log.d(TAG, data + " 我们监听到了截图么？" + dateTaken);
            // TODO: 2020/10/21 弹框提示用户，截图有风险。
            ScreenShotNoticeDialog shotNoticeDialog=new ScreenShotNoticeDialog(this,R.style.MyDialog2);

            shotNoticeDialog.show();
        } else {
            Log.d(TAG, "Not screenshot event");
        }
    }

    /**
     * 判断是否是截屏
     */
    private boolean checkScreenShot(String data, long dateTaken) {

        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private class MediaContentObserver extends ContentObserver {

        private Uri mContentUri;

        public MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            mContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, mContentUri.toString());
            handleMediaContentChange(mContentUri);
        }
    }


}