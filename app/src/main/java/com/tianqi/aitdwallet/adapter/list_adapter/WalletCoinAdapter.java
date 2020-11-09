package com.tianqi.aitdwallet.adapter.list_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhangjing on 2020/8/26.
 * Describe :
 */
public class WalletCoinAdapter extends BaseAdapter {
    private  List<Map<String,Object>> wordBeanList;
    private LayoutInflater layoutInflater;
    private Context context;
    private DecimalFormat df4;
    private int type;
    private int loading_index = -1;
    private int show_index = -1;

    public WalletCoinAdapter(Context context,  List<Map<String,Object>> wordBeanList) {
        this.wordBeanList = wordBeanList;
        this.context = context;
        df4 = new DecimalFormat("00");
        this.type = type;
    }

    @Override
    public int getCount() {
        return wordBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return wordBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_wallet_coin, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivCoin.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_circle_btc));

        GlideUtils.loadResourceImage(context, (Integer) wordBeanList.get(i).get(Constant.ACTION_IMAGE), holder.ivCoin);
        if (i == loading_index) {  //正在加载
         //   holder.cardCoin.setVisibility(View.VISIBLE);
            holder.progressBar1.setVisibility(View.VISIBLE);
        }else if (i>loading_index){  //未加载
        //    holder.cardCoin.setVisibility(View.VISIBLE);
            holder.progressBar1.setVisibility(View.GONE);
        }else {  //加载完成，无论成功还是失败。
            switch (i){
                case 0:
                    Map<String, Object> map = wordBeanList.get(i);
                    map.put(Constant.ACTION_IMAGE,R.mipmap.ic_circle_btc);
                    wordBeanList.set(i,map);
                    GlideUtils.loadResourceImage(context, (Integer) wordBeanList.get(i).get(Constant.ACTION_IMAGE), holder.ivCoin);
                    break;
                case 1:
                    map = wordBeanList.get(i);
                   // map.put(Constant.ACTION_IMAGE,R.mipmap.ic_circle_eth);
                    map.put(Constant.ACTION_IMAGE,R.mipmap.ic_circle_usdt);
                    wordBeanList.set(i,map);
                    GlideUtils.loadResourceImage(context, (Integer) wordBeanList.get(i).get(Constant.ACTION_IMAGE), holder.ivCoin);
                    break;
//                case 2:
//                    map = wordBeanList.get(i);
//                    map.put(Constant.ACTION_IMAGE,R.mipmap.ic_circle_usdt);
//                    wordBeanList.set(i,map);
//                    GlideUtils.loadResourceImage(context, (Integer) wordBeanList.get(i).get(Constant.ACTION_IMAGE), holder.ivCoin);
//                    break;
//                case 3:
//                    map = wordBeanList.get(i);
//                    map.put(Constant.ACTION_IMAGE,R.mipmap.ic_circle_aitd);
//                    wordBeanList.set(i,map);
//                    GlideUtils.loadResourceImage(context, (Integer) wordBeanList.get(i).get(Constant.ACTION_IMAGE), holder.ivCoin);
//                    break;
            }
          //  holder.cardCoin.setVisibility(View.GONE);
            holder.progressBar1.setVisibility(View.GONE);
        }
        return convertView;
    }


    public void loadingIndex(int position) {
        loading_index = position;
        notifyDataSetChanged();
    }

    public void showWords(int position) {
        show_index = position;
        notifyDataSetChanged();
    }


    class ViewHolder {
        @BindView(R.id.iv_coin)
        ImageView ivCoin;
//        @BindView(R.id.card_coin)
//        CardView cardCoin;
        @BindView(R.id.progressBar1)
        ProgressBar progressBar1;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
