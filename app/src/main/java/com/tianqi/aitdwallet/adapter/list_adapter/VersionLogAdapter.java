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
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.ContactsInfo;
import com.tianqi.baselib.dbManager.ContactsInfoManager;
import com.tianqi.baselib.rxhttp.bean.GetVersionLogBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.widget.ExpandableLayout;

import java.util.List;


public class VersionLogAdapter extends BaseAdapter {

    private Context mContext;
    private List<GetVersionLogBean.DataBean> maps;
    private OnActionClickLitener mOnActionClickLitener;
    private Typeface typeFace;

    private String gate_way_id;
    private static final int TRUST_GATE_WAY = 2;
    private static final int UN_TRUST_GATE_WAY = 1;
    private int function_type;

    public VersionLogAdapter(Context context, List<GetVersionLogBean.DataBean> beanList) {
        mContext = context;
        maps = beanList;
        typeFace = Typeface.createFromAsset(context.getAssets(), Constant.FONT_PATH);
    }

    public void refreshData(List<GetVersionLogBean.DataBean> couponTypeBeans) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_version_log, null);

            viewHolder.tv_version_log_tittle=view.findViewById(R.id.tv_version_log_tittle);
            viewHolder.tv_version_log_content=view.findViewById(R.id.tv_version_log_content);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_version_log_tittle.setText(maps.get(position).getVersion());
        if (mContext.getResources().getConfiguration().locale.getCountry().equals("US")){
            viewHolder.tv_version_log_content.setText(maps.get(position).getContentEn().replace("\\n", "\n"));
        }else {
            viewHolder.tv_version_log_content.setText(maps.get(position).getContent().replace("\\n", "\n"));
        }
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
        TextView tv_version_log_tittle,tv_version_log_content;
    }
}
