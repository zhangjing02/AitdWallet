package com.tianqi.aitdwallet.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tianqi.aitdwallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewGuideStartDialog extends Dialog {

//    @BindView(R.id.tv_shot_warning_tittle01)
//    TextView tvShotWarningTittle01;
    @BindView(R.id.tv_shot_warning_tittle02)
    TextView tvShotWarningTittle02;
    @BindView(R.id.btn_i_know)
    TextView btnIKnow;
    @BindView(R.id.btn_i_new)
    TextView btnINew;
    @BindView(R.id.layout_content)
    ConstraintLayout layoutContent;
    @BindView(R.id.iv_shot_warning)
    ImageView ivShotWarning;
    private OnDialogClickListener mOnDialogClickListener;

    public static final int DIALOG_CANCEL = 0;
    public static final int DIALOG_CONFIRM = 1;
    private Context mContext;

    public NewGuideStartDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        View view = View.inflate(context, R.layout.dialog_new_guide_start, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        mContext = context;
        DialogSizeUtli.dialogSize(this, 1, "width");
        DialogSizeUtli.dialogSize(this, 1, "height");

        btnIKnow.setOnClickListener(v -> {
           // mOnDialogClickListener.onItemClick(v, etPayPassword.getText().toString(), DIALOG_CONFIRM);
            dismiss();
        });

        btnINew.setOnClickListener(view1 -> {
             mOnDialogClickListener.onItemClick(view1, "", DIALOG_CONFIRM);
            dismiss();
        });
        SpannableString spannableString = new SpannableString(tvShotWarningTittle02.getText().toString());
        int end1 = tvShotWarningTittle02.getText().toString().indexOf("Hi!");
        spannableString.setSpan(new AbsoluteSizeSpan(30, true), 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvShotWarningTittle02.setText(spannableString);

    }


    //设置点击回调
    public interface OnDialogClickListener {
        void onItemClick(View view, String password, int type);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.mOnDialogClickListener = onDialogClickListener;
    }

}
