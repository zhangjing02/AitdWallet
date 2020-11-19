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
import com.tianqi.aitdwallet.ui.activity.wallet.importwallet.ImportBtcCoinActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.importwallet.ImportEthCoinActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.importwallet.ImportUsdtCoinActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.importwallet.ImportUsdtErc20CoinActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionRecordActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SingleWalletAdapter extends RecyclerView.Adapter<SingleWalletAdapter.HomeWalletAdapterViewHolder> {
    private Context mContext;
    private List<CoinInfo> mMessageBeans;
    private static final String TAG = "HomeWalletAdapter";
    private UserInformation mUserInformation;
    private int type;

    public SingleWalletAdapter(Context context, List<CoinInfo> messageBeans,int type) {
        mContext = context;
        mMessageBeans = messageBeans;
        mUserInformation = UserInfoManager.getUserInfo();
        this.type=type;
    }

    public void refreshData(List<CoinInfo> list) {
        mMessageBeans = list;
        notifyDataSetChanged();
    }

    public List<CoinInfo> getDataList() {
        return mMessageBeans;
    }


    @Override
    public void onBindViewHolder(SingleWalletAdapter.HomeWalletAdapterViewHolder holder, int position, List<Object> payloads) {
        // super.onBindViewHolder(holder, position, payloads);
        if (payloads.get(0).equals(Constants.SINGEL_HOME_FRESH)) {
            //局部刷新
            holder.tvCurrencyName.setText(mMessageBeans.get(position).getCoin_name());
            GlideUtils.loadResourceImage(mContext, mMessageBeans.get(position).getResourceId(), holder.ivCurrency);
        }
    }


    @Override
    public HomeWalletAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (type==1){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_coin_card, viewGroup, false);
        }else {
             view = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_single_wallet, viewGroup, false);
        }

        return new HomeWalletAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeWalletAdapterViewHolder ViewHolder, int position) {
        ViewHolder.tvCurrencyName.setText(mMessageBeans.get(position).getCoin_name());
        GlideUtils.loadResourceImage(mContext, mMessageBeans.get(position).getResourceId(), ViewHolder.ivCurrency);

        ViewHolder.layoutVirtualCurrency.setOnClickListener(view -> {
            if (type==1){
                Intent intent = new Intent(mContext, TransactionRecordActivity.class);
                intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, mMessageBeans.get(position).getCoin_address());
                intent.putExtra(Constants.TRANSACTION_COIN_ID, mMessageBeans.get(position).getCoin_id());
                mContext.startActivity(intent);
            }else {
                switch (mMessageBeans.get(position).getCoin_name()){
                    case Constant.TRANSACTION_COIN_NAME_BTC:
                        Intent intent = new Intent(mContext, ImportBtcCoinActivity.class);
                        intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                        mContext.startActivity(intent);
                        break;
                    case Constant.TRANSACTION_COIN_NAME_USDT_OMNI:
                        intent = new Intent(mContext, ImportUsdtCoinActivity.class);
                        intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                        mContext.startActivity(intent);
                        break;
                    case Constant.TRANSACTION_COIN_NAME_USDT_ERC20:
                        intent = new Intent(mContext, ImportUsdtErc20CoinActivity.class);
                        intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                        mContext.startActivity(intent);
                        break;
                    case Constant.TRANSACTION_COIN_NAME_ETH:
                        intent = new Intent(mContext, ImportEthCoinActivity.class);
                        intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMessageBeans.size();
    }

    public class HomeWalletAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_currency)
        ImageView ivCurrency;
        @BindView(R.id.tv_currency_name)
        TextView tvCurrencyName;
        @BindView(R.id.layout_virtual_currency)
        ConstraintLayout layoutVirtualCurrency;

        public HomeWalletAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
