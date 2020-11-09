package com.tianqi.baselib.utils.display;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tianqi.baselib.R;


public class ToastUtil {
    public static Toast mToast;
    private static Toast mToast2;


    public static void showToast(Context context, String text){
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.mytoast_layout, null);
//        ImageView toast_image = (ImageView) layout.findViewById(R.id.toast_image);
//        //图片可以自己从外面传递进来换;
//        toast_image.setBackgroundResource(resId);
        TextView textV = layout.findViewById(R.id.mytoast_text);
        textV.setText(text);
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.TOP, 0, 100);
            mToast.setView(layout);
        } else {
            mToast.cancel();//关闭吐司显示
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            //设置Toast位置的,如若不设置将会是系统默认Toast位置
            mToast.setGravity(Gravity.TOP, 0, 100);
            mToast.setView(layout);
        }
        mToast.show();
    }

//    public static void showToast(Context context, String text){
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View layout = inflater.inflate(R.layout.mytoast_layout, null);
////        ImageView toast_image = (ImageView) layout.findViewById(R.id.toast_image);
////        //图片可以自己从外面传递进来换;
////        toast_image.setBackgroundResource(resId);
//        TextView textV = layout.findViewById(R.id.mytoast_text);
//        textV.setText(text);
//        if (mToast == null) {
//            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//            mToast.setGravity(Gravity.TOP, 0, 100);
//            mToast.setView(layout);
//        } else {
//            mToast.cancel();//关闭吐司显示
//            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//            //设置Toast位置的,如若不设置将会是系统默认Toast位置
//            mToast.setGravity(Gravity.TOP, 0, 100);
//            mToast.setView(layout);
//        }
//        mToast.show();
//    }

    public static void showSimpleToast(Context context, String text){
        if (mToast2 == null) {
            mToast2 = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast2.cancel();//关闭吐司显示
            mToast2 = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        }
        mToast2.show();
    }

//    public static void showToast(String text){
//        if (mToast2 == null) {
//            mToast2 = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//        } else {
//            mToast2.cancel();//关闭吐司显示
//            mToast2 = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//        }
//        mToast2.show();
//    }
}
