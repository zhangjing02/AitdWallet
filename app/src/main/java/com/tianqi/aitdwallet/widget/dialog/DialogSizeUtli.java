package com.tianqi.aitdwallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.tianqi.baselib.utils.display.ScreenUtils;


public class DialogSizeUtli {
    private static Window                     dialogWindow;
    private static WindowManager              m;
    private static Display                    d;
    private static WindowManager.LayoutParams p;

    /**
     * 设置Dialog的宽高的比例
     */
    @SuppressWarnings("deprecation")
    public static void dialogSize(Dialog dialog, double widthRatio, double heightRatio) {
        dialogWindow = dialog.getWindow();
        m = dialogWindow.getWindowManager();
        d = m.getDefaultDisplay(); // 获取屏幕宽、高用;
        p = dialogWindow.getAttributes(); // 获取对话框当前的参数值;
        p.width = (int) (d.getWidth() * widthRatio);
        p.height = (int) (d.getHeight() * heightRatio);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 设置Dialog的宽高的比例
     */
    @SuppressWarnings("deprecation")
    public static void dialogSize2(Context context, Dialog dialog, double widthRatio, double heightRatio) {
        dialogWindow = dialog.getWindow();
        assert dialogWindow != null;
        m = dialogWindow.getWindowManager();
        d = m.getDefaultDisplay(); // 获取屏幕宽、高用;
        p = dialogWindow.getAttributes(); // 获取对话框当前的参数值;
        p.width = (int) (d.getWidth() * widthRatio);
        p.height = (int) (d.getHeight() * heightRatio) - ScreenUtils.getStatusHeight(context);
        dialog.getWindow().setAttributes(p);
    }

    /**
     * 设置Dialog的宽或高的比例
     */
    @SuppressWarnings("deprecation")
    public static void dialogSize(Dialog dialog, double ratio, String widthOrHeight) {
        dialogWindow = dialog.getWindow();
        m = dialogWindow.getWindowManager();
        d = m.getDefaultDisplay(); // 获取屏幕宽、高用;
        p = dialogWindow.getAttributes(); // 获取对话框当前的参数值;
        if (widthOrHeight.equals("width")) {
            p.width = (int) (d.getWidth() * ratio);
        } else if (widthOrHeight.equals("height")) {
            p.height = (int) (d.getHeight() * ratio);
        }
        dialog.getWindow().setAttributes(p);
    }

    /**
     * LEFT,p.x表示相对左边的偏移,负值忽略. RIGHT时,p.x表示相对右边的偏移,负值忽略. TOP时,p.y表示相对上边的偏移,负值忽略.
     * BOTTOM,p.y表示相对下边的偏移,负值忽略
     */
    public static void dialogLocation(Dialog dialog, int gravity, int x, int y) {
        dialogWindow = dialog.getWindow();
        p = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        p.x = x; // 新位置X坐标
        p.y = y; // 新位置Y坐标
        dialogWindow.setAttributes(p);
    }

//    public static void dialogLocation(Dialog dialog) {
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(dm);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = dm.widthPixels;
//        window.setAttributes(lp);
//    }
}
