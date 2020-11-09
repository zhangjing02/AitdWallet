package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @描述  账单列表
 */
public class HiddenCoinAdapter extends BaseQuickAdapter<CoinInfo, BaseViewHolder> {
    public HiddenCoinAdapter(int layoutResId, @Nullable List<CoinInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinInfo listBean) {
        ImageView iv_coin=helper.getView(R.id.iv_currency);
        TextView tv_coin_name=helper.getView(R.id.tv_currency_name);
        SwitchButton switchButton=helper.getView(R.id.switch_hide_wallet);
        LinearLayout linearLayout=helper.getView(R.id.layout_switch_hide_wallet);



        TextView tv_currency_full_name=helper.getView(R.id.tv_currency_full_name);
        TextView tv_currency_address=helper.getView(R.id.tv_currency_address);
        ImageView iv_address_copy=helper.getView(R.id.iv_address_copy);

        GlideUtils.loadResourceImage(mContext,listBean.getResourceId(),iv_coin);
        tv_coin_name.setText(listBean.getCoin_name());
        tv_currency_full_name.setText(listBean.getCoin_fullName());
        tv_currency_address.setText(listBean.getCoin_address());


        if (listBean.getIsHidden()){
            switchButton.setCheckState(true);
        }else {
            switchButton.setCheckState(false);
        }

        linearLayout.setOnClickListener(view -> {
            if (listBean.getIsHidden()){
                listBean.setIsHidden(false);
                switchButton.setCheckState(false);
            }else {
                switchButton.setCheckState(true);
                listBean.setIsHidden(true);
            }
            CoinInfoManager.insertOrUpdate(listBean);
            EventMessage eventMessage=new EventMessage();
            eventMessage.setType(EventMessage.HIDDEN_COIN_UPDATE);
            EventBus.getDefault().post(eventMessage);
        });

        iv_address_copy.setOnClickListener(view -> {
            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(listBean.getCoin_address());
            ToastUtil.showToast(mContext,mContext.getString(R.string.copy_success));
        });
    }
}
