package com.tianqi.baselib.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.tianqi.baselib.R;
import com.tianqi.baselib.utils.display.ToastUtil;


/**
 * Created by long on 2018/5/16.
 *
 */

public class ButtonUtils {
    private static long lastClickTime = 0;
    private static long DIFF = 1000;
    private static int lastButtonId = -1;

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        return isFastDoubleClick(-1, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId) {
        return isFastDoubleClick(buttonId, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

    /**
     * 复制到剪贴板
     * @return
     */
    public static void copyClipboard(Context context,TextView textView) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("", textView.getText().toString());
        // 将ClipData内容放到系统剪贴板里。
        if (mClipData != null) {
            cm.setPrimaryClip(mClipData);
          //  ToastUtil.showToast(context.getString(R.string.copy_success));
            ToastUtil.showToast(context,context.getString(R.string.copy_success));
        }
    }
}
