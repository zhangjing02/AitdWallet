package com.tianqi.aitdwallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianqi.aitdwallet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomPaymentDialog extends Dialog {
    @BindView(R.id.tv_tittle)
    TextView tvTittle;
    @BindView(R.id.tv_transaction_amount)
    TextView tvTransactionAmount;
    @BindView(R.id.tv_transaction_address)
    TextView tvTransactionAddress;
    @BindView(R.id.tv_transaction_miner)
    TextView tvTransactionMiner;
    @BindView(R.id.btn_transaction)
    TextView btnTransaction;

    public BottomPaymentDialog(@NonNull Context context, String item1) {
        super(context, R.style.MyDialog2);
        init(context, item1, "", "");
    }

    public BottomPaymentDialog(@NonNull Context context, String item1, String item2) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, "");
    }

    public BottomPaymentDialog(@NonNull Context context, String item1, String item2, String item3) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, item3);
    }

    private void init(Context context, String item1, String item2, String item3) {
        View view = View.inflate(context, R.layout.dialog_buttom_select, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        tvTransactionAmount.setText(item1);
        tvTransactionAddress.setText(item2);
        tvTransactionMiner.setText(item3);
        //dialog显示在底部
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(layoutParams);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DialogSizeUtli.dialogSize(this, 1.0, "width");
        DialogSizeUtli.dialogSize(this, 0.6, "height");
    }

    private DialogFirstClickListener listener;
    private DialogTwoClickListener listenerTwo;
    private DialogThreeClickListener listenerThree;

    @OnClick(R.id.btn_transaction)
    public void onViewClicked() {
        listener.onDialogFirstClick();
    }


    public interface DialogFirstClickListener {
        void onDialogFirstClick();
    }

    public interface DialogTwoClickListener {
        void onDialogTwoClick();
    }

    public interface DialogThreeClickListener {
        void onDialogThreeClick();
    }

    /**
     * 监听
     *
     * @param listener
     */
    public void setDialogFirstClickListener(DialogFirstClickListener listener) {
        this.listener = listener;
    }

    public void setDialogTwoClickListener(DialogTwoClickListener listener) {
        this.listenerTwo = listener;
    }

    public void setDialogThreeClickListener(DialogThreeClickListener listener) {
        this.listenerThree = listener;
    }
}
