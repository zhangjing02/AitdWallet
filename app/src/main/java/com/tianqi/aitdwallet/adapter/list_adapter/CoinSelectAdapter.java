package com.tianqi.aitdwallet.adapter.list_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;


public class CoinSelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<CoinInfo> maps;
    private OnActionClickLitener mOnActionClickLitener;
    private Typeface typeFace;

    private String gate_way_id;
    private static final int TRUST_GATE_WAY = 2;
    private static final int UN_TRUST_GATE_WAY = 1;

    public CoinSelectAdapter(Context context, List<CoinInfo> beanList) {
        mContext = context;
        maps = beanList;
        typeFace = Typeface.createFromAsset(context.getAssets(), Constant.FONT_PATH);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_select_coin, null);

            viewHolder.tvCoinName=view.findViewById(R.id.tv_coin_name);
            viewHolder.icCoin=view.findViewById(R.id.iv_coin);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvCoinName.setText(String.format(mContext.getString(R.string.text_select_coin_address),maps.get(position).getCoin_name()));

        GlideUtils.loadResourceImage(mContext,maps.get(position).getResourceId(), viewHolder.icCoin);

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
        ImageView icCoin;
    }
}
