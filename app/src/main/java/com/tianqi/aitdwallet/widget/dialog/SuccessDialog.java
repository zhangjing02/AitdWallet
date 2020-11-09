package com.tianqi.aitdwallet.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.tianqi.aitdwallet.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuccessDialog extends Dialog {
    @BindView(R.id.ic_success)
    ImageView ic_success;
    @BindView(R.id.tv_tile)
    TextView tv_tile;
    public SuccessDialog(@NonNull Context context) {
        super(context, R.style.MyDialog3);
        init(context,-1,"");
    }

    public SuccessDialog(@NonNull Context context,int resourceId,String text) {
        super(context, R.style.MyDialog3);
        init(context,resourceId,text);
    }

    private void init(Context context,int resourceId,String text) {
        View view = View.inflate(context, R.layout.dialog_success, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        WindowManager.LayoutParams lp = Objects.requireNonNull(getWindow()).getAttributes();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        if (resourceId!=-1){
            ic_success.setImageResource(resourceId);
        }
        if (!TextUtils.isEmpty(text)){
            tv_tile.setText(text);
        }

        if (!((Activity) context).isFinishing() && !isShowing()) {
            show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                  //  ((Activity) context).finish();
                }
            },2000);
        }
    }

    public void setMessageTxt(String messageTxt){
        tv_tile.setText(messageTxt);
    }


    private OnConfirmLitener onConfirmLitener;

    public interface OnConfirmLitener {
        void onItemClick(View view);
    }

    public void setOnConfirmLitener(OnConfirmLitener onConfirmLitener) {
        this.onConfirmLitener = onConfirmLitener;
    }
}
