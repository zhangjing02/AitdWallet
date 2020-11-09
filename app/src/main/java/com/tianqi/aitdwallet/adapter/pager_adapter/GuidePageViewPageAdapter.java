package com.tianqi.aitdwallet.adapter.pager_adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.bean.CurrencyCardBean;
import com.tianqi.aitdwallet.ui.activity.wallet.property.CoinAddressQrActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ToastUtil;

import org.web3j.abi.datatypes.Int;

import java.util.List;

public class GuidePageViewPageAdapter extends PagerAdapter {
    ImageView ivCoin;

    private List<Integer> mList;
    private Context mContext;
    private OnViewPagerItemClickListener onViewPagerItemClickListener;
    private Typeface typeFace;

    public GuidePageViewPageAdapter(Context mContext,List<Integer> mList) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_guide_viewpager, container, false);
        container.addView(view);

        ivCoin=view.findViewById(R.id.iv_guide_logo);

        GlideUtils.loadResourceImage(mContext, Integer.valueOf(mList.get(position).toString()),ivCoin);
        typeFace = Typeface.createFromAsset(mContext.getAssets(), Constant.FONT_PATH);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnViewPagerItemClickListener {
        void onViewPagerItemClick(int position);
    }

    public void setOnShowItemClickListener(OnViewPagerItemClickListener onViewPagerItemClickListener) {
        this.onViewPagerItemClickListener = onViewPagerItemClickListener;
    }

}
