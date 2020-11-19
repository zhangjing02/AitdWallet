package com.tianqi.aitdwallet.adapter.list_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianqi.aitdwallet.R;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhangjing on 2020/8/26.
 * Describe :
 */
public class MnemonicWordAdapter extends BaseAdapter {
    private List<String> wordBeanList;
    private LayoutInflater layoutInflater;
    private Context context;
    private DecimalFormat df4;
    private int type;
    private int hide_index = -1;
    private int show_index = -1;

    public MnemonicWordAdapter(Context context, List<String> wordBeanList, int type) {
        if (type==1){
            Collections.shuffle(wordBeanList);
        }
        this.wordBeanList = wordBeanList;
        this.context = context;
        df4 = new DecimalFormat("00");
        this.type = type;
    }

    public void refreshData(List<String> wordBeanList) {
        hide_index=-1;
        show_index=-1;
        if (type==1){
            Collections.shuffle(wordBeanList);
        }
        this.wordBeanList = wordBeanList;
        notifyDataSetChanged();
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
            if (type == 3) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_mnemonic_word_import, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_mnemonic_word, null);
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMnemonicIndex.setText(df4.format(i + 1));
        holder.tvMnemonicContent.setText(wordBeanList.get(i));
        if (type == 1) {
            holder.tvMnemonicIndex.setVisibility(View.GONE);
        } else {
            holder.tvMnemonicIndex.setVisibility(View.VISIBLE);
        }

        if (hide_index==-1&&show_index==-1){
            holder.tvMnemonicContent.setVisibility(View.VISIBLE);
            holder.tvSelectCover.setVisibility(View.GONE);
        }

        if (show_index >= 0) {
            if (show_index == i) {
                holder.tvMnemonicContent.setVisibility(View.VISIBLE);
                holder.tvSelectCover.setVisibility(View.GONE);
            }
        }
        if (hide_index >= 0) {
            if (hide_index == i) {
                holder.tvMnemonicContent.setVisibility(View.INVISIBLE);
                holder.tvSelectCover.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }


    public void hideWords(int position) {
        hide_index = position;
        notifyDataSetChanged();
    }
    public int getHideIndex() {
       return hide_index;
    }

    public void showWords(int position) {
        show_index = position;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.tv_mnemonic_index)
        TextView tvMnemonicIndex;
        @BindView(R.id.tv_mnemonic_content)
        TextView tvMnemonicContent;
        @BindView(R.id.layout_memory_words)
        RelativeLayout layoutMemoryWords;
        @BindView(R.id.tv_select_cover)
        TextView tvSelectCover;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
