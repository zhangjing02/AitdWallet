package com.tianqi.aitdwallet.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianqi.aitdwallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExplainTxMinerFeeDialog extends Dialog {


    @BindView(R.id.iv_shot_warning)
    ImageView ivShotWarning;
    @BindView(R.id.tv_shot_warning_tittle)
    TextView tvShotWarningTittle;
    @BindView(R.id.iv_shot_warning_content1)
    TextView ivShotWarningContent1;

    @BindView(R.id.btn_confirm)
    TextView btnConfirm;
    private OnDialogClickListener mOnDialogClickListener;

    public static final int DIALOG_CANCEL = 0;
    public static final int DIALOG_CONFIRM = 1;
    private Context mContext;

    public ExplainTxMinerFeeDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        View view = View.inflate(context, R.layout.dialog_explain_tx_miner_fee, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        mContext = context;
//        DialogSizeUtli.dialogSize(this, 0.8, "width");
//        DialogSizeUtli.dialogSize(this, 0.45, "height");

        btnConfirm.setOnClickListener(v -> {
           // mOnDialogClickListener.onItemClick(v, etPayPassword.getText().toString(), DIALOG_CONFIRM);
            dismiss();
        });
    }


    //设置点击回调
    public interface OnDialogClickListener {
        void onItemClick(View view, String password, int type);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.mOnDialogClickListener = onDialogClickListener;
    }

}
