package com.tianqi.aitdwallet.ui.activity.wallet.property;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.CodeEncoder;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ToastUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CoinAddressQrActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_coin)
    ImageView ivCoin;
    @BindView(R.id.iv_address_qr)
    ImageView ivAddressQr;
    @BindView(R.id.tv_coin_address)
    TextView tvCoinAddress;
    @BindView(R.id.btn_address_copy)
    TextView btnAddressCopy;

    private Uri saveBitmap;
    private Bitmap qrCodeBitmap;
    private String stringExtra_address;
    private String stringExtra_name;

    @Override
    protected int getContentView() {
        return R.layout.activity_coin_address_qr;
    }

    @Override
    protected void initView() {
         stringExtra_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
         stringExtra_name = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);

        getToolBar();

        qrCodeBitmap = CodeEncoder.createImage(stringExtra_address, ivAddressQr.getLayoutParams().width, ivAddressQr.getLayoutParams().height, null);
        //qrCodeBitmap =convertViewToBitmap();
        ivAddressQr.setImageBitmap(qrCodeBitmap);
        tvCoinAddress.setText(stringExtra_address);
    }

    private void getToolBar() {
        //根据传过来的币种，决定tittle
        if (stringExtra_name.equals(Constant.TRANSACTION_COIN_NAME_BTC)){
            toolbarTitle.setText(R.string.tittle_btc_receive);
        }else if (stringExtra_name.equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)){
            toolbarTitle.setText(R.string.tittle_usdt_omni_receive);
            ivCoin.setImageDrawable(getResources().getDrawable(R.mipmap.ic_circle_usdt_omni));
        }else if (stringExtra_name.equals(Constant.TRANSACTION_COIN_NAME_ETH)){
            ivCoin.setImageDrawable(getResources().getDrawable(R.mipmap.ic_circle_eth));
            toolbarTitle.setText(R.string.tittle_eth_receive);
        }else if (stringExtra_name.equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)){
            ivCoin.setImageDrawable(getResources().getDrawable(R.mipmap.ic_circle_usdt_erc20));
            toolbarTitle.setText(R.string.tittle_usdt_erc20_receive);
        }

        toolbar.setBackgroundColor(getResources().getColor(R.color.bg_pager_grey));
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnCollect.setVisibility(View.VISIBLE);
        btnCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_black_share));
        btnCollect.setOnClickListener(view -> {
           // shareAddressImg();
           // GlideUtils.share(CoinAddressQrActivity.this);
            shareAddressImg();
        });
    }

    /**
     * Activity截屏
     * */
    public static Bitmap takeScreenShot(Activity pActivity) {
        View view = pActivity.getWindow().getDecorView();
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        Bitmap bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        // 测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = frame.top;
        Point point = new Point();
        pActivity.getWindowManager().getDefaultDisplay().getSize(point);
        int width = point.x ;
        int height = point.y;
        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height - stautsHeight);
        return bitmap;
    }


    private static Bitmap convertViewToBitmap(View tempView) {
        /**
         * 创建一个bitmap放于画布之上进行绘制 （简直如有神助）
         */
        Bitmap bitmap = Bitmap.createBitmap(tempView.getWidth(),
                tempView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tempView.draw(canvas);
        return bitmap;
    }


    private void shareAddressImg() {
        Observable<String> observable = Observable.create(emitter -> {
            emitter.onNext("123");
            emitter.onComplete();
        });
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(String result) {
//                        Log.e(TAG, "custom decodeQRCode: result = " + result);
                        Calendar calendar=Calendar.getInstance();
                      //  saveBitmap = GlideUtils.saveBitmap(CoinAddressQrActivity.this,qrCodeBitmap, "btc_addres"+calendar.getTimeInMillis());
                        qrCodeBitmap =takeScreenShot(CoinAddressQrActivity.this);
                    }
                    @Override
                    public void onError(Throwable e) {
//                        Log.e(TAG, "custom decodeQRCode: result err = " + e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        Uri u = null;//将截图bitmap存系统相册
                        try {
                            u = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), qrCodeBitmap, "IMG_" + System.currentTimeMillis(),"123"));
                        } catch (Exception e) {
                            u= GlideUtils.saveBitmap(CoinAddressQrActivity.this,qrCodeBitmap,"IMG_" + System.currentTimeMillis());
                            e.printStackTrace();
                        }
                        intent.putExtra(Intent.EXTRA_STREAM, u);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getString(R.string.tittle_share_sth)));
//                        if (saveBitmap!=null){
//                            Intent imageIntent = new Intent(Intent.ACTION_SEND);
//                            imageIntent.setType("image/jpeg");
//                            imageIntent.putExtra(Intent.EXTRA_STREAM, saveBitmap);
//                            startActivity(Intent.createChooser(imageIntent, getString(R.string.tittle_share_sth)));
//                        }
                    }
                });
    }


    @Override
    protected void initData() {
    }


    @OnClick(R.id.btn_address_copy)
    public void onViewClicked() {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(tvCoinAddress.getText().toString());
        ToastUtil.showToast(this,getString(R.string.notice_copy_success));
    }
}