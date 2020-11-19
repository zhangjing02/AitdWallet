package com.tianqi.aitdwallet.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.CoinSelectAdapter;
import com.tianqi.aitdwallet.ui.activity.address.CreateNewAddressActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.CreateWalletActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.widget.WrapListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCoinDialog extends Dialog {

    @BindView(R.id.lv_coin_select)
    WrapListView lvCoinSelect;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    private Context mContext;
    private CoinSelectAdapter coinSelectAdapter;

    public SelectCoinDialog(@NonNull Context context, String item1) {
        super(context, R.style.MyDialog2);
        init(context, item1, "", "", "");
    }

    public SelectCoinDialog(@NonNull Context context, String item1, String item2) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, "", "");
    }

    public SelectCoinDialog(@NonNull Context context, String item1, String item2, String item3) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, item3, "");
    }

    public SelectCoinDialog(@NonNull Context context, String item1, String item2, String item3, String item4) {
        super(context, R.style.MyDialog2);
        init(context, item1, item2, item3, item4);
    }

    private void init(Context context, String item1, String item2, String item3, String item4) {
        View view = View.inflate(context, R.layout.dialog_coin_select, null);
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
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
//        DialogSizeUtli.dialogSize(this, 1.0, "width");
//        DialogSizeUtli.dialogSize(this, 0.6, "height");

        List<CoinInfo>coinInfoList= new ArrayList<>();
        CoinInfo coinInfo=new CoinInfo();
        coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_BTC);
        coinInfo.setResourceId(R.mipmap.ic_circle_btc);
        coinInfoList.add(coinInfo);

        CoinInfo coinInfo2=new CoinInfo();
        coinInfo2.setCoin_name(Constant.TRANSACTION_COIN_NAME_ETH);
        coinInfo2.setResourceId(R.mipmap.ic_circle_eth);
        coinInfoList.add(coinInfo2);

        CoinInfo coinInfo3=new CoinInfo();
        coinInfo3.setCoin_name(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
        coinInfo3.setResourceId(R.mipmap.ic_circle_usdt_omni);
        coinInfoList.add(coinInfo3);

        CoinInfo coinInfo4=new CoinInfo();
        coinInfo4.setCoin_name(Constant.TRANSACTION_COIN_NAME_USDT_ERC20);
        coinInfo4.setResourceId(R.mipmap.ic_circle_usdt_erc20);
        coinInfoList.add(coinInfo4);

        coinSelectAdapter=new CoinSelectAdapter(context,coinInfoList);

        lvCoinSelect.setAdapter(coinSelectAdapter);
        
        lvCoinSelect.setOnItemClickListener((adapterView, view12, i, l) -> {
            Intent intent=new Intent(mContext, CreateNewAddressActivity.class);
            intent.putExtra(Constants.INTENT_PUT_TAG,coinInfoList.get(i).getCoin_name());
            intent.putExtra(Constants.INTENT_PUT_RESOURCE,coinInfoList.get(i).getResourceId());
            mContext.startActivity(intent);
            dismiss();
        });

        btnCancel.setOnClickListener(view1 -> {
            dismiss();
        });
    }


    private DialogFirstClickListener listener;
    private DialogTwoClickListener listenerTwo;
    private DialogThreeClickListener listenerThree;


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
