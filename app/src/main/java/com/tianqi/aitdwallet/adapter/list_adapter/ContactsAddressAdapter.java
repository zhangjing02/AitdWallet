package com.tianqi.aitdwallet.adapter.list_adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.widget.ExpandableLayout;

import java.util.List;


public class ContactsAddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<CoinInfo> maps;
    private OnActionClickLitener mOnActionClickLitener;
    private Typeface typeFace;

    private String gate_way_id;
    private static final int TRUST_GATE_WAY = 2;
    private static final int UN_TRUST_GATE_WAY = 1;

    public ContactsAddressAdapter(Context context, List<CoinInfo> beanList) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_contacts_address, null);

            viewHolder.tvCoinName=view.findViewById(R.id.tv_coin_name);
            viewHolder.icCoin=view.findViewById(R.id.iv_coin);
            viewHolder.expandableLayout=view.findViewById(R.id.expand_coin);
            viewHolder.lv_coin_detail=view.findViewById(R.id.lv_coin_detail);
            viewHolder.layout_coin=view.findViewById(R.id.layout_coin);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvCoinName.setText(maps.get(position).getCoin_name());

        GlideUtils.loadResourceImage(mContext,maps.get(position).getResourceId(), viewHolder.icCoin);

        viewHolder.layout_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        ImageView icCoin;
        ExpandableLayout expandableLayout;
        ListView lv_coin_detail;
        ConstraintLayout layout_coin;
    }
}
