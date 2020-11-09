package com.tianqi.aitdwallet.ui.activity.tool;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.leeiidesu.permission.PermissionHelper;
import com.leeiidesu.permission.callback.OnPermissionResultListener;
import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huke on 2019/11/5
 * 扫码
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {


    private final String TAG = this.getClass().getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    @BindView(R.id.zx_scan)
    ZXingView mZXingView;


    private String type = "";

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        //        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    /**
     * 识别完成振动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    private String usersn;

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.e("----------------", "type:" + type);
        Log.e("----------------", "結果result:" + result);
        //---------: 結果result:groupid:109
        vibrate();
        if (!TextUtils.isEmpty(result)) {
            EventMessage message = new EventMessage();
            message.setType(EventMessage.SCAN_EVENT);
            message.setMsg(result);
            EventBus.getDefault().post(message);
            finish();
        }
        // mZXingView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = getString(R.string.shanguandeng);
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
//        Log.e(TAG, "打开相机出错");

        Toast.makeText(this, getResources().getString(R.string.toastdakai), Toast.LENGTH_SHORT).show();
        mZXingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            // 系统选择二维码图片识别
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    if (uri != null) {
                        final Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                        if (mBitmap != null) {
//                            mZXingView.decodeQRCode(mBitmap); // 解析效果差
                            decodeQRCode(mBitmap);
                        }

//                        if (mBitmap != null) {
//                            mBitmap.recycle();
//                        }
                    }
                } catch (Exception e) {
//                    Log.e(TAG, "onActivityResult：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 子线程解析二维码图片
     *
     * @param mBitmap
     */
    private void decodeQRCode(final Bitmap mBitmap) {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(QRCodeDecoder.syncDecodeQRCode(mBitmap));
                emitter.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String result) {
//                        Log.e(TAG, "custom decodeQRCode: result = " + result);
                        onScanQRCodeSuccess(result);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.e(TAG, "custom decodeQRCode: result err = " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (mBitmap != null) {
                            mBitmap.recycle();
                        }
                    }
                });
    }


    private void permission() {
        PermissionHelper.with(this)
                .permissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .showOnDenied(getResources().getString(R.string.settingdakai), getResources().getString(R.string.seal_select_chat_bg_cancel), getResources().getString(R.string.goseeting))
                .listener(new OnPermissionResultListener() {
                    @Override
                    public void onGranted() {
                    }

                    @Override
                    public void onFailed(ArrayList<String> deniedPermissions) {
                        ToastUtil.showToast(ScanActivity.this, getString(R.string.getfailse));
                    }
                })
                .request();
    }


    @Override
    protected void initView() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        permission();
        mZXingView.setDelegate(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_scan;
    }



}
