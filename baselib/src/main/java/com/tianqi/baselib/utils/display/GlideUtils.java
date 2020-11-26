package com.tianqi.baselib.utils.display;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;


import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tianqi.baselib.R;
import com.tianqi.baselib.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @创建者 Cai
 * @创时间 2018/10/1714:04
 * @描述
 * @版本 Rapidzpay
 * @更新者 rapidpay.tjchain.com.rapidzpay.utils
 * @更新时间 2018/10/17
 * @更新描述 TODO
 */
public class GlideUtils {
    /**
     * Glide 正常网络图片加载
     */

    //本图。
    public static void loadImageView(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.head_default).
                        error(R.mipmap.head_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL) ;//有缓存，不利于调试，调试成功后，可打开缓存。
        Glide.with(context).load(url).apply(requestOptions)
                .into(imageView);
    }


    //圆角矩形
    public static void loadRoundImageView(Context context, String url, ImageView imageView) {
        //设置图片圆角角度
        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(),new GlideRoundImage(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL) //有缓存，不利于调试，调试成功后，可打开缓存。
                .placeholder(R.mipmap.head_default)
                .error(R.mipmap.head_default);

        Glide.with(context).load(url).apply(requestOptions).dontAnimate()
                .into(imageView);
    }

    //圆形图片
    public static void loadCircleImageView(Context context, String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.head_default).
                        error(R.mipmap.head_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //有缓存，不利于调试，调试成功后，可打开缓存。
                .circleCrop();//圆形图片;

        Glide.with(context).load(url).apply(requestOptions)
                .into(imageView);
    }

    /**
     * 从资源文件加载图片
     */
    public static void loadResourceImage(Context context, int resourceId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(imageView.getDrawable() == null ? context.getApplicationContext().getResources().getDrawable(R.mipmap.head_default) : imageView.getDrawable()).
                        error(R.mipmap.head_default);

        Glide.with(context).load(resourceId).apply(requestOptions).dontAnimate().into(imageView);
    }

    /**
     * 从资源文件加载图片
     */
    public static void loadGiftResourceImage(Context context, int resourceId, ImageView imageView) {
        Glide.with(context).asGif().load(resourceId).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource instanceof GifDrawable) {
                    resource.setLoopCount(1);//只播放一次
                }
                return false;
            }
        }).into(imageView);
    }

    public static void loadResourceImageForGuide(Context context, int resourceId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(imageView.getDrawable() == null ? context.getApplicationContext().getResources().getDrawable(R.mipmap.ic_guide_page1) : imageView.getDrawable()).
                        error(R.mipmap.ic_guide_page1);

        Glide.with(context).load(resourceId).apply(requestOptions).dontAnimate().into(imageView);
    }

    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                ToastUtil.showToast(context, context.getString(R.string.save_successful));
                return true;
            } else {
                ToastUtil.showToast(context, context.getString(R.string.save_failed));
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /** * 将图片存到本地 */
    public static Uri saveBitmap(Context context,Bitmap bm, String picName) {
        try {
            String dir=Environment.getExternalStorageDirectory().getAbsolutePath()+"/aitd_wallet/"+picName+".jpg";
            File pictureFile = new File(dir);

            if (!pictureFile.exists()) {
                pictureFile.getParentFile().mkdirs();
                pictureFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(pictureFile);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Uri pictureUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
               // intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, pictureFile.getAbsolutePath());
                pictureUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
            }else {
               // intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureUri = Uri.fromFile(pictureFile);
            }
            return pictureUri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();    }
        return null;
    }


    public static void share(Activity activity){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri u = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), takeScreenShot(activity), "IMG_" + System.currentTimeMillis(),null));//将截图bitmap存系统相册
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, ""));
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
