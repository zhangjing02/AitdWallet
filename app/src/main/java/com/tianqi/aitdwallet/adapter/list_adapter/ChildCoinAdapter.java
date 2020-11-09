package com.tianqi.aitdwallet.adapter.list_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.property.CoinListActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionRecordActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;

import java.util.List;


public class ChildCoinAdapter extends BaseAdapter {

    private Context mContext;
    private List<CoinInfo> maps;
    private OnActionClickLitener mOnActionClickLitener;
    private Typeface typeFace;

    private String gate_way_id;
    private static final int TRUST_GATE_WAY = 2;
    private static final int UN_TRUST_GATE_WAY = 1;
    private WalletInfo walletInfo;

    public ChildCoinAdapter(Context context, List<CoinInfo> beanList) {
        mContext = context;
        maps = beanList;
        typeFace = Typeface.createFromAsset(context.getAssets(), Constant.FONT_PATH);
        walletInfo= WalletInfoManager.getHdWalletInfo();
    }

    public void refreshData(List<CoinInfo> couponTypeBeans) {
        maps = couponTypeBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public Object getItem(int position) {
        return maps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_child_coin, null);

            viewHolder.tvCoinName=view.findViewById(R.id.tv_coin_name);
            viewHolder.tvCoinAddress=view.findViewById(R.id.tv_coin_address);
            viewHolder.tvCurrencyBalance=view.findViewById(R.id.tv_currency_balance);
            viewHolder.tvFiatBalance=view.findViewById(R.id.tv_fiat_balance);
            viewHolder.layoutCurrency=view.findViewById(R.id.layout_currency);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (maps.get(position).getCoin_ComeType()==Constant.COIN_SOURCE_CREATE){
            viewHolder.tvCoinName.setText(String.format(mContext.getString(R.string.text_create_coin_alias_name),maps.get(position).getAlias_name()));
        }else if (maps.get(position).getCoin_ComeType()==Constant.COIN_SOURCE_IMPORT){
            viewHolder.tvCoinName.setText(String.format(mContext.getString(R.string.text_import_coin_alias_name),maps.get(position).getAlias_name())+position);
        }

        viewHolder.tvCoinAddress.setText(maps.get(position).getCoin_address());
        viewHolder.tvCurrencyBalance.setText(DataReshape.doubleBig(maps.get(position).getCoin_totalAmount(),8) +"");

        UserInformation userInformation= UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
            viewHolder.tvFiatBalance.setText("≈ $"+DataReshape.double2int((maps.get(position).getCoin_totalAmount()*walletInfo.getCoin_USDPrice()),2));
        }else {
            viewHolder.tvFiatBalance.setText("≈ ￥"+DataReshape.double2int((maps.get(position).getCoin_totalAmount()*walletInfo.getCoin_CNYPrice()),2));
        }

        viewHolder.layoutCurrency.setOnClickListener(view1 -> {
            if (maps.get(position).getCoin_ComeType()==0){
                Intent intent = new Intent(mContext, CoinListActivity.class);
                intent.putExtra(Constants.TRANSACTION_COIN_NAME, maps.get(position).getWallet_id());
                intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, maps.get(position).getCoin_address());
                intent.putExtra(Constants.TRANSACTION_COIN_ID, maps.get(position).getCoin_id());
                mContext.startActivity(intent);
            }else {
//                Intent intent = new Intent(mContext, TransactionRecordActivity.class);
//                intent.putExtra(Constants.TRANSACTION_COIN_NAME, maps.get(position).getCoin_name());
//                intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, maps.get(position).getCoin_address());
//                intent.putExtra(Constants.TRANSACTION_COIN_ID, maps.get(position).getCoin_id());
//                mContext.startActivity(intent);
                Intent intent = new Intent(mContext, TransactionRecordActivity.class);
                intent.putExtra(Constants.TRANSACTION_COIN_NAME, maps.get(position).getCoin_name());
                intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, maps.get(position).getCoin_address());
                intent.putExtra(Constants.TRANSACTION_COIN_ID, maps.get(position).getCoin_id());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    //设置分享点击接口
    public interface OnActionClickLitener {
        void onItemClick(View view, int position, boolean switch_on);
    }

    public void setOnActionClickLitener(OnActionClickLitener onActionClickLitener) {
        this.mOnActionClickLitener = onActionClickLitener;
    }
    
    static public class ViewHolder {
        TextView tvCoinName;
        TextView tvCoinAddress;
        TextView tvCurrencyBalance;
        TextView tvFiatBalance;
        ConstraintLayout layoutCurrency;
    }
}
