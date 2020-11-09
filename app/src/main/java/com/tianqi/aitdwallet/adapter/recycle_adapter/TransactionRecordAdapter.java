package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionDetailActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TransactionRecordAdapter extends RecyclerView.Adapter<TransactionRecordAdapter.HomeWalletAdapterViewHolder> {

    private Context mContext;
    private List<TransactionRecord> mMessageBeans;
    private static final String TAG = "HomeWalletAdapter";
    private UserInformation mUserInformation;

    public TransactionRecordAdapter(Context context, List<TransactionRecord> messageBeans) {
        mContext = context;
        mMessageBeans = messageBeans;
        mUserInformation = UserInfoManager.getUserInfo();
    }

    public void refreshData(List<TransactionRecord> list) {
        mMessageBeans = list;
        notifyDataSetChanged();
    }

    public List<TransactionRecord> getDataList() {
        return mMessageBeans;
    }


    @Override
    public HomeWalletAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_transaction_record, viewGroup, false);
        return new HomeWalletAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeWalletAdapterViewHolder ViewHolder, int position) {
        ViewHolder.tvTransactionAddress.setText(mMessageBeans.get(position).getAddress());
        ViewHolder.tvTransactionTime.setText(mMessageBeans.get(position).getTimeStr());
        ViewHolder.tvCurrencyBalance.setText(DataReshape.doubleBig(mMessageBeans.get(position).getAmount() ,8) );
        switch (mMessageBeans.get(position).getStatus()) {
            case 1:
                ViewHolder.tvTransactionState.setText(R.string.text_transaction_state_fail);
                ViewHolder.tvTransactionState.setTextColor(mContext.getResources().getColor(R.color.color_transaction_fail));
                GlideUtils.loadResourceImage(mContext,R.mipmap.ic_transaction_fail,ViewHolder.ivTransactionState);

                break;
            case 2:
                ViewHolder.tvTransactionState.setTextColor(mContext.getResources().getColor(R.color.text_light_blue));
                GlideUtils.loadResourceImage(mContext,R.mipmap.ic_transaction_loading,ViewHolder.ivTransactionState);
                ViewHolder.tvTransactionState.setText(R.string.text_transaction_state_waiting);
                break;
            case 0:
            default:
                ViewHolder.tvTransactionState.setTextColor(mContext.getResources().getColor(R.color.color_transaction_success));
                GlideUtils.loadResourceImage(mContext,R.mipmap.ic_transcation_success,ViewHolder.ivTransactionState);
                ViewHolder.tvTransactionState.setText(R.string.text_transaction_state_success);
                break;
        }

        ViewHolder.layoutVirtualCurrency.setOnClickListener(view -> {
            Intent intent=new Intent(mContext, TransactionDetailActivity.class);
            String tx_id=mMessageBeans.get(position).getTxid();
            intent.putExtra(Constants.INTENT_PUT_TRANSACTION_ID,tx_id);
            intent.putExtra(Constants.INTENT_PUT_TRANSACTION_TYPE,mMessageBeans.get(position).getTransType());
            intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS,mMessageBeans.get(position).getAddress());
            intent.putExtra(Constants.INTENT_PUT_COIN_ID,mMessageBeans.get(position).getCoin_id());

            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mMessageBeans.size();
    }

    public class HomeWalletAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_transaction_state)
        ImageView ivTransactionState;
        @BindView(R.id.tv_transaction_address)
        TextView tvTransactionAddress;
        @BindView(R.id.tv_transaction_time)
        TextView tvTransactionTime;
        @BindView(R.id.tv_currency_balance)
        TextView tvCurrencyBalance;
        @BindView(R.id.tv_transaction_state)
        TextView tvTransactionState;
        @BindView(R.id.layout_virtual_currency)
        ConstraintLayout layoutVirtualCurrency;

        public HomeWalletAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
