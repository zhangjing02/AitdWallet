package com.tianqi.aitdwallet.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.app.hubert.guide.listener.OnHighlightDrewListener;
import com.app.hubert.guide.model.HighlightOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quincysx.crypto.CoinTypes;
import com.quincysx.crypto.ECKeyPair;
import com.quincysx.crypto.bip32.ExtendedKey;
import com.quincysx.crypto.bip32.ValidationException;
import com.quincysx.crypto.bip39.SeedCalculator;
import com.quincysx.crypto.bip44.AddressIndex;
import com.quincysx.crypto.bip44.BIP44;
import com.quincysx.crypto.bip44.CoinPairDerive;
import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Create by zhangjing on 2020/11/3.
 * Describe :
 */
public class HighlightOptionsUtils {

    public static HighlightOptions createTransparentOptions() {
        HighlightOptions options = new HighlightOptions.Builder()
                .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onHighlightDrew(Canvas canvas, RectF rectF) {
                        Paint paint = new Paint();
                        paint.setColor(Color.TRANSPARENT);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(10);
                        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                        // canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 30, paint);
                        canvas.drawRoundRect(0, 0, 200, 200, 600, 600, paint);
                        // canvas.drawRect(150, 75, 250, 120, paint);
                    }
                }).build();
        return options;
};
    public static HighlightOptions createYellowOptions(Context context) {
        HighlightOptions options002 = new HighlightOptions.Builder()
                .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onHighlightDrew(Canvas canvas, RectF rectF) {
                        Paint paint = new Paint();
                        paint.setColor(context.getResources().getColor(R.color.text_main2_yellow));
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(2);
                        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
                        // canvas.drawRoundRect(0,0,200,200,600,600,paint);
                        // canvas.drawRect(150, 75, 250, 120, paint);
                    }
                })
                .build();
        return options002;
};
    public static HighlightOptions createYellowOptions002(Context context) {
        HighlightOptions options002 = new HighlightOptions.Builder()
                .setOnHighlightDrewListener(new OnHighlightDrewListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onHighlightDrew(Canvas canvas, RectF rectF) {
                        Paint paint = new Paint();
                        paint.setColor(context.getResources().getColor(R.color.text_main2_yellow));
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(2);
                        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 -50, paint);
                        // canvas.drawRoundRect(0,0,200,200,600,600,paint);
                        // canvas.drawRect(150, 75, 250, 120, paint);
                    }
                })
                .build();
        return options002;
};




}



