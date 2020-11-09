package com.tianqi.aitdwallet.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tianqi.aitdwallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VersionUpdateDialog extends Dialog {

    @BindView(R.id.tv_shot_warning_tittle01)
    TextView tvShotWarningTittle01;
    @BindView(R.id.tv_shot_warning_tittle02)
    TextView tvShotWarningTittle02;
    @BindView(R.id.btn_version_update)
    AppCompatButton btnINew;
    @BindView(R.id.layout_content)
    ConstraintLayout layoutContent;
    @BindView(R.id.iv_shot_warning)
    ImageView ivShotWarning;
    @BindView(R.id.layout_version_update)
    ConstraintLayout layoutVersionUpdate;
    private OnDialogClickListener mOnDialogClickListener;

    public static final int DIALOG_CANCEL = 0;
    public static final int DIALOG_CONFIRM = 1;
    private Context mContext;

    public VersionUpdateDialog(@NonNull Activity context, int themeResId, boolean isForceUpdate) {
        super(context, themeResId);
        View view = View.inflate(context, R.layout.dialog_version_update, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        mContext = context;
        DialogSizeUtli.dialogSize(this, 1, "width");
        DialogSizeUtli.dialogSize(this, 1, "height");
//        if (context.getResources().getConfiguration().locale.getCountry().equals("US")){
//            GlideUtils.loadResourceImage(context,R.drawable.ic_new_guide_end_en,ivShotWarning);
//        }else {
//            GlideUtils.loadResourceImage(context,R.drawable.ic_new_guide_end,ivShotWarning);
//        }
        btnINew.setOnClickListener(view1 -> {
            mOnDialogClickListener.onItemClick(view1, "", DIALOG_CONFIRM);
            dismiss();
        });

        if (!isForceUpdate) {
            layoutVersionUpdate.setOnClickListener(view12 -> {
                // mOnDialogClickListener.onItemClick(view1, "", DIALOG_CONFIRM);
                dismiss();
            });
        }
    }

    public void setTittle(String version_tittle){
        tvShotWarningTittle01.setText(version_tittle);
    }

    public void setContent(String version_content){
        tvShotWarningTittle02.setText(version_content);
    }

    //设置点击回调
    public interface OnDialogClickListener {
        void onItemClick(View view, String password, int type);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.mOnDialogClickListener = onDialogClickListener;
    }

}
