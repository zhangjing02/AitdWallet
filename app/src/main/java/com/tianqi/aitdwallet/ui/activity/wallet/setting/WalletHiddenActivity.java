package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.HiddenWalletAdapter;
import com.tianqi.aitdwallet.adapter.recycle_adapter.HomeWalletAdapter;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletHiddenActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_my_wallet_tag)
    TextView tvMyWalletTag;
    @BindView(R.id.rcv_hidden_wallet)
    RecyclerView rcvHiddenWallet;

    private HiddenWalletAdapter hiddenWalletAdapter;
    private List<WalletInfo> mWalletBeans;
    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_hidden;
    }

    @Override
    protected void initView() {
        getToolBar();
        mWalletBeans = WalletInfoManager.getWalletInfo();
        rcvHiddenWallet.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        hiddenWalletAdapter = new HiddenWalletAdapter(R.layout.layout_adapter_hidden_wallet, mWalletBeans);
        rcvHiddenWallet.setAdapter(hiddenWalletAdapter);
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_hidden_coin);
        toolbar.setNavigationOnClickListener(v -> {
            EventMessage eventMessage=new EventMessage();
            eventMessage.setType(EventMessage.HOME_WALLET_BALANCE_UPDATE);
            EventBus.getDefault().post(eventMessage);
            finish();//返回
        });
    }

    @Override
    protected void initData() {

    }

}