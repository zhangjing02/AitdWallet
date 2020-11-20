package com.tianqi.aitdwallet.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.display.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewGuideEndDialog extends Dialog {

    @BindView(R.id.tv_shot_warning_tittle01)
    TextView tvShotWarningTittle01;
    @BindView(R.id.tv_shot_warning_tittle02)
    TextView tvShotWarningTittle02;
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

    public NewGuideEndDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        View view = View.inflate(context, R.layout.dialog_new_guide_end, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        mContext = context;
        DialogSizeUtli.dialogSize(this, 1, "width");
        DialogSizeUtli.dialogSize(this, 1, "height");
        if (!context.isFinishing()){
          UserInformation userInformation= UserInfoManager.getUserInfo();
            if(userInformation.getLanguageId()== Constants.LANGUAGE_ENGLISH){
                GlideUtils.loadResourceImage(context,R.drawable.ic_new_guide_end_en,ivShotWarning);
            }else {
                GlideUtils.loadResourceImage(context,R.drawable.ic_new_guide_end,ivShotWarning);
            }
//            if (context.getResources().getConfiguration().locale.getCountry().equals("US")){
//                GlideUtils.loadResourceImage(context,R.drawable.ic_new_guide_end_en,ivShotWarning);
//            }else {
//                GlideUtils.loadResourceImage(context,R.drawable.ic_new_guide_end,ivShotWarning);
//            }
        }
        btnINew.setOnClickListener(view1 -> {
            // mOnDialogClickListener.onItemClick(view1, "", DIALOG_CONFIRM);
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
