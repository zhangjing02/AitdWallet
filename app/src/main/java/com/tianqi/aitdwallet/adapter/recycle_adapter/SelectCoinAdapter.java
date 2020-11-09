package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;

/**
 * @描述  账单列表
 */
public class SelectCoinAdapter extends BaseQuickAdapter<CoinInfo, BaseViewHolder> {
   // private ExpandableLayout expand_coin;

    public SelectCoinAdapter(int layoutResId, @Nullable List<CoinInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinInfo listBean) {
        ImageView iv_coin=helper.getView(R.id.iv_currency);
        TextView tv_coin_name=helper.getView(R.id.tv_currency_name);
        TextView tv_currency_address=helper.getView(R.id.tv_currency_address);

        GlideUtils.loadResourceImage(mContext,listBean.getResourceId(),iv_coin);
        tv_coin_name.setText(listBean.getAlias_name());
        tv_currency_address.setText(listBean.getCoin_address());

        String wallet_id =listBean.getWallet_id();
        List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(wallet_id);

    }

    public  void expandLayout(){
//        if (expand_coin!=null&&expand_coin.isExpanded()){
//            expand_coin.collapse();
//        }
    }

}
