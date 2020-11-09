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
import com.tianqi.aitdwallet.bean.MnemonicWordBean;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Create by zhangjing on 2020/8/26.
 * Describe :
 */
public class VerifyMnemonicWordAdapter extends BaseAdapter {
    private List<MnemonicWordBean> wordBeanList;
    private LayoutInflater layoutInflater;
    private Context context;
    private DecimalFormat df4;
    private int type;
    private int hide_index = -1;
    private int show_index = -1;

    public VerifyMnemonicWordAdapter(Context context, List<MnemonicWordBean> wordBeanList, int type) {
        if (type==1){
            Collections.shuffle(wordBeanList);
        }
        this.wordBeanList = wordBeanList;
        this.context = context;
        df4 = new DecimalFormat("00");
        this.type = type;
    }

    public void refreshData(List<MnemonicWordBean> wordBeanList) {
        hide_index=-1;
        show_index=-1;
        for (int i = 0; i <wordBeanList.size() ; i++) {
            wordBeanList.get(i).setHidden(false);
        }
        if (type==1){
            Collections.shuffle(wordBeanList);
        }
        this.wordBeanList = wordBeanList;
        notifyDataSetChanged();
    }

    public void refresSingleData(List<MnemonicWordBean> wordBeanList) {

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
        holder.tvMnemonicContent.setText(wordBeanList.get(i).getWord());
        if (type == 1) {
            holder.tvMnemonicIndex.setVisibility(View.GONE);
        } else {
            holder.tvMnemonicIndex.setVisibility(View.VISIBLE);
        }

        if (hide_index==-1&&show_index==-1){
            holder.tvMnemonicContent.setVisibility(View.VISIBLE);
            holder.tvSelectCover.setVisibility(View.GONE);
        }

        if (wordBeanList.get(i).isHidden()){
            holder.tvMnemonicContent.setVisibility(View.INVISIBLE);
            holder.tvSelectCover.setVisibility(View.VISIBLE);
        }else {
            holder.tvMnemonicContent.setVisibility(View.VISIBLE);
            holder.tvSelectCover.setVisibility(View.GONE);
        }

        return convertView;
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
