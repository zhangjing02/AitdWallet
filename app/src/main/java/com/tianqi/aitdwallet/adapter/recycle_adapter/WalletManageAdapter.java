package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;


/**
 * @描述 HD钱包，创建的钱包下的币种。
 */
public class WalletManageAdapter extends BaseQuickAdapter<CoinInfo, BaseViewHolder> {
    public WalletManageAdapter(int layoutResId, @Nullable List<CoinInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinInfo listBean) {

        ImageView ivWallet = helper.getView(R.id.iv_wallet);
        TextView tvWalletName = helper.getView(R.id.tv_wallet_name);
        ConstraintLayout layoutVirtualCurrency = helper.getView(R.id.layout_virtual_currency);
        TextView tvWalletAddress = helper.getView(R.id.tv_wallet_address);
        TextView tv_add_coin = helper.getView(R.id.tv_add_coin);
        RelativeLayout layout_add_coin = helper.getView(R.id.layout_add_coin);

        LogUtil.i(TAG, "convert: 我们看每一条数据是？"+listBean.toString());
        if (listBean.getWallet_id()!=null&&!listBean.getWallet_id().equals(Constants.COIN_NULL)){
            ivWallet.setVisibility(View.VISIBLE);
            tvWalletName.setVisibility(View.VISIBLE);
            tvWalletAddress.setVisibility(View.VISIBLE);
            GlideUtils.loadResourceImage(mContext, listBean.getResourceId(), ivWallet);
            tvWalletName.setText(listBean.getAlias_name());
            tvWalletAddress.setText(listBean.getCoin_address());
            tv_add_coin.setVisibility(View.GONE);
            layoutVirtualCurrency.setVisibility(View.VISIBLE);
            layout_add_coin.setVisibility(View.GONE);
        }

        switch (listBean.getWallet_id()){
            case Constant.TRANSACTION_COIN_NAME_BTC:
                layoutVirtualCurrency.setBackground(mContext.getResources().getDrawable(R.drawable.bg_wallet_manage_btc));
                break;
            case Constant.TRANSACTION_COIN_NAME_ETH:
                layoutVirtualCurrency.setBackground(mContext.getResources().getDrawable(R.drawable.bg_wallet_manage_eth));
                break;
            case Constant.TRANSACTION_COIN_NAME_USDT_OMNI:
            case Constant.TRANSACTION_COIN_NAME_USDT_ERC20:
                layoutVirtualCurrency.setBackground(mContext.getResources().getDrawable(R.drawable.bg_wallet_manage_usdt));
                break;
            case Constants.COIN_NULL:
                ivWallet.setVisibility(View.GONE);
                tvWalletName.setVisibility(View.GONE);
                tvWalletAddress.setVisibility(View.GONE);
                tv_add_coin.setVisibility(View.VISIBLE);
                layout_add_coin.setVisibility(View.VISIBLE);
               // layoutVirtualCurrency.setBackground(mContext.getResources().getDrawable(R.drawable.bg_wallet_manage_import));
                layoutVirtualCurrency.setVisibility(View.GONE);
                break;
        }
    }
}
