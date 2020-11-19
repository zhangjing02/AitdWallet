package com.tianqi.aitdwallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.utils.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomDialog extends Dialog {
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
    @BindView(R.id.iv_dialog_cancel)
    ImageView ivDialogCancel;
    @BindView(R.id.tv_transaction_amount_tag)
    TextView tvTransactionAmountTag;
    @BindView(R.id.tv_transaction_amount_unit)
    TextView tvTransactionAmountUnit;
    @BindView(R.id.tv_transaction_msg)
    TextView tvTransactionMsg;
    @BindView(R.id.tv_transaction_address_tag)
    TextView tvTransactionAddressTag;
    @BindView(R.id.tv_payment_address)
    TextView tvPaymentAddress;
    private Context mContext;

    public BottomDialog(@NonNull Context context, String item1) {
        super(context, R.style.MyDialog2);
        init(context, item1, "", "","");
    }

    public BottomDialog(@NonNull Context context, String item1, String item2) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, "","");
    }

    public BottomDialog(@NonNull Context context, String item1, String item2, String item3) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, item3,"");
    }
    public BottomDialog(@NonNull Context context, String item1, String item2, String item3,String item4) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, item3,item4);
    }

    private void init(Context context, String item1, String item2, String item3,String item4) {
        View view = View.inflate(context, R.layout.dialog_buttom_select, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        mContext=context;
        tvTransactionAmount.setText(item1);
        tvTransactionAddress.setText(item2);
        tvTransactionMiner.setText(item3);
        tvPaymentAddress.setText(item4);
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
//        DialogSizeUtli.dialogSize(this, 1.0, "width");
//        DialogSizeUtli.dialogSize(this, 0.6, "height");

        ivDialogCancel.setOnClickListener(view1 -> {
            dismiss();
        });
    }

    public void setTransactionCoinType(int type){
        if (type==1){
            tvTransactionAmountUnit.setText(Constant.COIN_UNIT_USDT);
            tvTransactionMsg.setText(mContext.getString(R.string.text_usdt_transaction_type));
        }else if (type==2){
            tvTransactionAmountUnit.setText(Constant.COIN_UNIT_ETH);
            tvTransactionMsg.setText(mContext.getString(R.string.text_eth_transaction_type));
        }else if (type==3){
            tvTransactionAmountUnit.setText(Constant.COIN_UNIT_USDT);
            tvTransactionMsg.setText(mContext.getString(R.string.text_usdt_erc20_transaction_type));
        }else{
            tvTransactionAmountUnit.setText(Constant.COIN_UNIT_USDT);
            tvTransactionMsg.setText(mContext.getString(R.string.text_btc_transaction_type));
        }
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
