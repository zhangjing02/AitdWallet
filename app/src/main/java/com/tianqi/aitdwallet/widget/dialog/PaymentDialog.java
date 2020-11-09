package com.tianqi.aitdwallet.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PaymentDialog extends Dialog {

    @BindView(R.id.tv_tile)
    TextView tvTile;
    @BindView(R.id.et_pay_password)
    EditText etPayPassword;
    @BindView(R.id.btn_transaction)
    TextView btnTransaction;
    @BindView(R.id.tv_forget_psd)
    TextView tvForgetPsd;
    @BindView(R.id.iv_dialog_cancel)
    ImageView ivDialogCancel;
    @BindView(R.id.iv_open_eye)
    ImageView ivOpenEye;
    private OnDialogClickListener mOnDialogClickListener;

    public static final int DIALOG_CANCEL = 0;
    public static final int DIALOG_CONFIRM = 1;
    private Context mContext;

    public PaymentDialog(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        View view = View.inflate(context, R.layout.dialog_payment, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        mContext = context;

        //dialog显示在底部
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);

        DialogSizeUtli.dialogSize(this, 1, "width");
        DialogSizeUtli.dialogSize(this, 0.5, "height");
        btnTransaction.setOnClickListener(v -> {
            mOnDialogClickListener.onItemClick(v, etPayPassword.getText().toString(), DIALOG_CONFIRM);
            dismiss();
        });

        ivDialogCancel.setOnClickListener(view1 -> {
            dismiss();
        });

        ivOpenEye.setOnClickListener(view12 -> {
            showOrHidePsd(etPayPassword,ivOpenEye);
        });

//        mDialogButtonCancel.setOnClickListener(v -> {
//            mOnDialogClickListener.onItemClick(view, null, DIALOG_CANCEL);
//            dismiss();
//        });
//        mDialogButtonCancel.setTextColor(Color.parseColor("#2d82c2"));
//        mDialogButtonOk.setTextColor(Color.parseColor("#2d82c2"));

        // ScreenUtils.showKeyboard(view);
        tvForgetPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPsdNoticeBottomDialog paymentDialog = new ForgetPsdNoticeBottomDialog((Activity) mContext, R.style.MyDialog2);
                paymentDialog.show();
            }
        });

    }

    public void setDialog_button_okTxt(String txt) {
        btnTransaction.setText(txt);
    }


    //设置点击回调
    public interface OnDialogClickListener {
        void onItemClick(View view, String password, int type);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.mOnDialogClickListener = onDialogClickListener;
    }


    /**
     * 显示隐藏密码
     *
     * @param editText  需要显示的edittext控件，
     * @param imageView 需要点击的眼睛图标。
     */
    public void showOrHidePsd(EditText editText,ImageView imageView) {
        LogUtil.d("ttttttt", InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD + "--showOrHidePsd: 键盘" + editText.getInputType());
        if (editText.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            imageView.setImageResource(R.mipmap.ic_open_eye);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            imageView.setImageResource(R.mipmap.ic_close_eye);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().toString().trim().length());
    }

}
