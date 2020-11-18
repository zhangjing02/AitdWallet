package com.tianqi.aitdwallet.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianqi.aitdwallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @创建者 Cai
 * @创时间 2018/12/1914:25
 * @描述
 * @版本 Rapidzpay
 * @更新者 rapidpay.tjchain.com.rapidzpay.dialog
 * @更新时间 2018/12/19
 * @更新描述 TODO
 */
public class DelAddressDialog extends Dialog {

    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.dialog_button_cancel)
    TextView dialogButtonCancel;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.dialog_button_ok)
    TextView dialogButtonOk;
    private OnExitClickLitener mOnExitClickLitener;

    public DelAddressDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        View view = View.inflate(context, R.layout.dialog_del_address, null);
        setContentView(view);
        ButterKnife.bind(this, view);
//        DialogSizeUtli.dialogSize(this, 0.8, "width");
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnExitClickLitener.onItemClick(v);
                dismiss();
            }
        });
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

//    public DelAddressDialog(@NonNull Activity context, int themeResId, String tittle, String confrim) {
//        super(context, themeResId);
//        View view = View.inflate(context, R.layout.dialog_del_address, null);
//        setContentView(view);
//        ButterKnife.bind(this, view);
//        DialogSizeUtli.dialogSize(this, 0.8, "width");
//
//        mTvMessage.setText(tittle + "");
//        dialog_button_ok.setText(confrim + "");
//        dialog_button_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnExitClickLitener.onItemClick(v);
//                dismiss();
//            }
//        });
//        dialog_button_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//    }

    public DelAddressDialog(@NonNull Context context) {
        super(context);
    }

    /**
     * 设置内容
     *
     * @param text
     */
    public void setText(String text) {
        tvMessage.setText(text);
    }

    //设置评论回调接口
    public interface OnExitClickLitener {
        void onItemClick(View view);
    }

    public void setOnExitClickLitener(OnExitClickLitener onExitClickLitener) {
        this.mOnExitClickLitener = onExitClickLitener;
    }

}
