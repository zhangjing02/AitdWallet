package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.ChildCoinAdapter;
import com.tianqi.aitdwallet.ui.activity.wallet.property.CoinListActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionRecordActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.widget.ExpandableLayout;
import com.tianqi.baselib.widget.WrapListView;

import java.util.List;
/**
 * @描述  账单列表
 */
public class HomeWalletAdapter extends BaseQuickAdapter<WalletInfo, BaseViewHolder> {
   // private ExpandableLayout expand_coin;

    public HomeWalletAdapter(int layoutResId, @Nullable List<WalletInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletInfo listBean) {
        ImageView iv_coin=helper.getView(R.id.iv_currency);
        ImageView iv_coin_indicator=helper.getView(R.id.iv_coin_indicator);
        TextView tv_coin_name=helper.getView(R.id.tv_currency_name);
        TextView tv_coin_balance=helper.getView(R.id.tv_coin_balance);
        TextView tv_fiat_balance=helper.getView(R.id.tv_fiat_balance);
        ExpandableLayout expand_coin=helper.getView(R.id.expand_coin);
        WrapListView lv_coin_detail=helper.getView(R.id.lv_coin_detail);
        ConstraintLayout layout_virtual_currency=helper.getView(R.id.layout_virtual_currency);

        GlideUtils.loadResourceImage(mContext,listBean.getResource_id(),iv_coin);
        tv_coin_name.setText(listBean.getWalletName());

        if (listBean.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)){
            tv_coin_balance.setText(DataReshape.doubleBig(listBean.getWalletBalance(), 4,4));
        }else if (listBean.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_ETH)){
            tv_coin_balance.setText(DataReshape.doubleBig(listBean.getWalletBalance(), 6,6));
        }else if (listBean.getWallet_id().equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)){
            tv_coin_balance.setText(DataReshape.doubleBig(listBean.getWalletBalance(), 4,4));
        } else {
            tv_coin_balance.setText(DataReshape.doubleBig(listBean.getWalletBalance(), 8,8));
        }

        UserInformation userInformation= UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
            tv_fiat_balance.setText("≈ $" + DataReshape.doubleBig(listBean.getWalletBalance() * listBean.getCoin_USDPrice(), 2));
        }else {
            tv_fiat_balance.setText("≈ ￥" + DataReshape.doubleBig(listBean.getWalletBalance() * listBean.getCoin_CNYPrice(), 2));
        }

        String wallet_id =listBean.getWallet_id();
        List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(wallet_id);

        if (coinFrWalletIds.size()>1){
            iv_coin_indicator.setVisibility(View.VISIBLE);
        }else {
            iv_coin_indicator.setVisibility(View.GONE);
        }

        layout_virtual_currency.setOnClickListener(view -> {
            String wallet_id02 = listBean.getWallet_id();
            List<CoinInfo> coinFrWalletIds02 = CoinInfoManager.getCoinFrWalletId(wallet_id02);
            List<CoinInfo> walletBtcInfo03 = CoinInfoManager.getCoinInfo();
            Log.i(TAG, walletBtcInfo03.size()+"----convert: 我们看这个币种是多少？"+coinFrWalletIds02.size()+"我們查询的wallet_id是？"+wallet_id02);
            if (coinFrWalletIds02.size() > 1) {  //如果大于一个币种，就展开显示。
                if (expand_coin.isExpanded()) {
                    expand_coin.collapse();
                } else {
                    ChildCoinAdapter mAdapter = new ChildCoinAdapter(mContext, coinFrWalletIds02);
                    lv_coin_detail.setAdapter(mAdapter);
                    expand_coin.expand();
                }
            } else if (coinFrWalletIds02.size()==1){
                if (coinFrWalletIds02.get(0).getCoin_ComeType()==0){  //如果是HD钱包，自己的助记词生成的，就进入卡片页面
                    Intent intent = new Intent(mContext, CoinListActivity.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_NAME, listBean.getWalletName());
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coinFrWalletIds02.get(0).getCoin_address());
                    intent.putExtra(Constants.TRANSACTION_COIN_ID, coinFrWalletIds02.get(0).getCoin_id());
                    mContext.startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, TransactionRecordActivity.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_NAME, coinFrWalletIds02.get(0).getCoin_name());
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coinFrWalletIds02.get(0).getCoin_address());
                    intent.putExtra(Constants.TRANSACTION_COIN_ID, coinFrWalletIds02.get(0).getCoin_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public  void expandLayout(){
//        if (expand_coin!=null&&expand_coin.isExpanded()){
//            expand_coin.collapse();
//        }
    }

}
