package com.tianqi.aitdwallet.adapter.recycle_adapter;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.record.TransactionRecordActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.bean.GetMessageBean;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;

import java.util.List;


/**
 * @描述  HD钱包，创建的钱包下的币种。
 */
public class PushMessageAdapter extends BaseQuickAdapter<GetMessageBean, BaseViewHolder> {
    public PushMessageAdapter(int layoutResId, @Nullable List<GetMessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetMessageBean listBean) {
       // ImageView ivCurrency=helper.getView(R.id.iv_currency);
        TextView tv_message_tittle=helper.getView(R.id.tv_message_tittle);
        TextView tv_message_content=helper.getView(R.id.tv_message_content);

        tv_message_tittle.setText(listBean.getMessageTittle());
        tv_message_content.setText(listBean.getMessageContent());

    }
}
