package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionDetailActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;

import butterknife.BindView;


/**
 * @描述 HD钱包，创建的钱包下的币种。
 */
public class TransRecordAdapter extends BaseQuickAdapter<TransactionRecord, BaseViewHolder> {
    public TransRecordAdapter(int layoutResId, @Nullable List<TransactionRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransactionRecord listBean) {

        ImageView ivTransactionState = helper.getView(R.id.iv_transaction_state);
        TextView tvTransactionAddress = helper.getView(R.id.tv_transaction_address);
        TextView tvTransactionTime = helper.getView(R.id.tv_transaction_time);
        TextView tvCurrencyBalance = helper.getView(R.id.tv_currency_balance);
        TextView tvTransactionState = helper.getView(R.id.tv_transaction_state);
        ConstraintLayout layoutVirtualCurrency = helper.getView(R.id.layout_virtual_currency);

        tvTransactionAddress.setText(listBean.getAddress());
        tvTransactionTime.setText(listBean.getTimeStr());
        tvCurrencyBalance.setText(DataReshape.doubleBig(listBean.getAmount(), 8));
        switch (listBean.getStatus()) {
            case 1:
                tvTransactionState.setText(R.string.text_transaction_state_fail);
                tvTransactionState.setTextColor(mContext.getResources().getColor(R.color.color_transaction_fail));
                GlideUtils.loadResourceImage(mContext, R.mipmap.ic_transaction_fail, ivTransactionState);

                break;
            case 2:
                tvTransactionState.setTextColor(mContext.getResources().getColor(R.color.text_light_blue));
                GlideUtils.loadResourceImage(mContext, R.mipmap.ic_transaction_loading, ivTransactionState);
                tvTransactionState.setText(R.string.text_transaction_state_waiting);
                break;
            case 0:
            default:
                tvTransactionState.setTextColor(mContext.getResources().getColor(R.color.color_transaction_success));
                GlideUtils.loadResourceImage(mContext, R.mipmap.ic_transcation_success, ivTransactionState);
                tvTransactionState.setText(R.string.text_transaction_state_success);
                break;
        }

        layoutVirtualCurrency.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, TransactionDetailActivity.class);
            String tx_id = listBean.getTxid();
            intent.putExtra(Constants.INTENT_PUT_TRANSACTION_ID, tx_id);
            intent.putExtra(Constants.INTENT_PUT_TRANSACTION_TYPE, listBean.getTransType());
            intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, listBean.getAddress());
            intent.putExtra(Constants.INTENT_PUT_COIN_ID, listBean.getCoin_id());

            mContext.startActivity(intent);
        });

    }
}



