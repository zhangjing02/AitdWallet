package com.tianqi.aitdwallet.ui.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.statusbar.StatusBarCompat;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.utils.display.CodeEncoder;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InviteFriendActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.layout_toolbar)
    RelativeLayout layoutToolbar;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_up_colon)
    ImageView ivUpColon;
    @BindView(R.id.tv_aitd_slogan)
    TextView tvAitdSlogan;
    @BindView(R.id.iv_down_colon)
    ImageView ivDownColon;
    @BindView(R.id.tv_open_beta_content)
    TextView tvOpenBetaContent;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.tv_aitd_describe)
    TextView tvAitdDescribe;
    @BindView(R.id.tv_app_name)
    TextView tvAppName;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_scan_to_download)
    TextView tvScanToDownload;

    private Bitmap qrCodeBitmap;

    @Override
    protected int getContentView() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected void initView() {
        StatusBarCompat.translucentStatusBar(this, true);

    }

    @Override
    protected void initData() {
        qrCodeBitmap = CodeEncoder.createImage("https://app-download.ttguminle.com/android/AITD_Wallet-1.0.0-2.apk", ivQrCode.getLayoutParams().width, ivQrCode.getLayoutParams().height, null);
        //qrCodeBitmap =convertViewToBitmap();
        ivQrCode.setImageBitmap(qrCodeBitmap);

    }

    @OnClick({R.id.iv_back, R.id.iv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                shareAddressImg();
                break;
        }
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
                        qrCodeBitmap =takeScreenShot(InviteFriendActivity.this);
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
                            u = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), qrCodeBitmap, "IMG_" + System.currentTimeMillis(),"456"));
                        } catch (Exception e) {
                            u= GlideUtils.saveBitmap(InviteFriendActivity.this,qrCodeBitmap,"IMG_" + System.currentTimeMillis());
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
}