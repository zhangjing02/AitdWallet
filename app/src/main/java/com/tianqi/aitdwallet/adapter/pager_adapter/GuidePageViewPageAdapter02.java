package com.tianqi.aitdwallet.adapter.pager_adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;

public class GuidePageViewPageAdapter02 extends RecyclerView.Adapter<GuidePageViewPageAdapter02.ViewHolder> {


    private List<Integer> mData;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;
    private Context mContext;


    private int[] colorArray = new int[]{android.R.color.black, android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_red_dark};


    public GuidePageViewPageAdapter02(Context context, List<Integer> data, ViewPager2 viewPager2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.viewPager2 = viewPager2;
        mContext=context;
    }

    @NonNull
    @Override
    public GuidePageViewPageAdapter02.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_guide_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // String animal = mData.get(position);
        GlideUtils.loadResourceImageForGuide(mContext,Integer.valueOf(mData.get(position)),holder.myTextView);
      //  holder.relativeLayout.setBackgroundResource(colorArray[position]);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myTextView;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.iv_guide_logo);
           // relativeLayout = itemView.findViewById(R.id.container);

        }
    }
}
