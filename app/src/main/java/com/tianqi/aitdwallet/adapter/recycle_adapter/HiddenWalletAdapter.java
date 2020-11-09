package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @描述  账单列表
 */
public class HiddenWalletAdapter extends BaseQuickAdapter<WalletInfo, BaseViewHolder> {
    public HiddenWalletAdapter(int layoutResId, @Nullable List<WalletInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletInfo listBean) {
        ImageView iv_coin=helper.getView(R.id.iv_currency);
        TextView tv_coin_name=helper.getView(R.id.tv_currency_name);
        SwitchButton switchButton=helper.getView(R.id.switch_hide_wallet);
        LinearLayout linearLayout=helper.getView(R.id.layout_switch_hide_wallet);
        if (listBean.getIsHide()){
            switchButton.setCheckState(false);
        }else {
            switchButton.setCheckState(true);
        }

        linearLayout.setOnClickListener(view -> {
            if (listBean.getIsHide()){
                listBean.setIsHide(false);
                switchButton.setCheckState(true);
            }else {
                switchButton.setCheckState(false);
                listBean.setIsHide(true);
            }
            WalletInfoManager.insertOrUpdate(listBean);
            EventMessage eventMessage=new EventMessage();
            eventMessage.setType(EventMessage.HIDDEN_WALLET_UPDATE);
            EventBus.getDefault().post(eventMessage);
        });
        GlideUtils.loadResourceImage(mContext,listBean.getResource_id(),iv_coin);
        tv_coin_name.setText(listBean.getWalletName());
    }
}
