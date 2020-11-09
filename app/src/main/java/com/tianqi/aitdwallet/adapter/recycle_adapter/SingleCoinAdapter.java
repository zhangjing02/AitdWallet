package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionRecordActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;


/**
 * @描述  HD钱包，创建的钱包下的币种。
 */
public class SingleCoinAdapter extends BaseQuickAdapter<CoinInfo, BaseViewHolder> {
    public SingleCoinAdapter(int layoutResId, @Nullable List<CoinInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinInfo listBean) {
        ImageView ivCurrency=helper.getView(R.id.iv_currency);
        TextView tvCurrencyName=helper.getView(R.id.tv_currency_name);
        TextView tv_coin_balance=helper.getView(R.id.tv_coin_balance);
        TextView tv_coin_fiat_value=helper.getView(R.id.tv_coin_fiat_value);
        ConstraintLayout layoutVirtualCurrency=helper.getView(R.id.layout_virtual_currency);

        tvCurrencyName.setText(listBean.getCoin_name());
        tv_coin_balance.setText(DataReshape.doubleBig(listBean.getCoin_totalAmount(),8));
        GlideUtils.loadResourceImage(mContext, listBean.getResourceId(), ivCurrency);
        WalletInfo walletInfo = WalletInfoManager.getWalletFrName(listBean.getCoin_name());
        UserInformation userInformation= UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
            tv_coin_fiat_value.setText("≈ $"+DataReshape.doubleBig(listBean.getCoin_totalAmount()*walletInfo.getCoin_USDPrice(),2));
        }else {
            tv_coin_fiat_value.setText("≈ ￥"+DataReshape.doubleBig(listBean.getCoin_totalAmount()*walletInfo.getCoin_CNYPrice(),2));
        }

        layoutVirtualCurrency.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, TransactionRecordActivity.class);
            intent.putExtra(Constants.TRANSACTION_COIN_NAME, listBean.getCoin_name());
            intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, listBean.getCoin_address());
            intent.putExtra(Constants.TRANSACTION_COIN_ID, listBean.getCoin_id());
            mContext.startActivity(intent);
        });
    }
}
