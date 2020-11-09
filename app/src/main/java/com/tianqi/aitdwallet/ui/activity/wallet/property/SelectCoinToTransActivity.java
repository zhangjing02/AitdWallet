package com.tianqi.aitdwallet.ui.activity.wallet.property;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.SelectCoinAdapter;
import com.tianqi.aitdwallet.ui.activity.wallet.btc.BitcoinTransactionActivity002;
import com.tianqi.aitdwallet.ui.activity.wallet.usdt.UsdtTransactionActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.usdt.UsdtTransactionActivity002;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.Constant;

import java.util.List;

import butterknife.BindView;

public class SelectCoinToTransActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcv_select_coin)
    RecyclerView rcvSelectCoin;
    private List<CoinInfo> mMessageBeans;
    private SelectCoinAdapter selectCoinAdapter;
    int tx_type = -1;

    @Override
    protected int getContentView() {
        return R.layout.activity_select_coin_to_trans;
    }

    @Override
    protected void initView() {
        getToolBar();
        tx_type = getIntent().getIntExtra(Constants.INTENT_PUT_TRANSACTION_TYPE, -1);

        mMessageBeans = CoinInfoManager.getCoinInfo();
        rcvSelectCoin.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        //  recyWallet.addItemDecoration(new RecyclerViewDivider(getActivity(), HORIZONTAL, 12, getResources().getColor(R.color.transparent2)));
        selectCoinAdapter = new SelectCoinAdapter(R.layout.layout_adapter_select_coin_for_shadow, mMessageBeans);
        rcvSelectCoin.setAdapter(selectCoinAdapter);
    }

    private void getToolBar() {
        //根据传过来的币种，决定tittle
        toolbarTitle.setText(R.string.tittle_select_coin_text);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnCollect.setVisibility(View.GONE);
        btnCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_coin_card_setting));
    }

    @Override
    protected void initData() {
        selectCoinAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (tx_type == Constant.TRANSACTION_TYPE_SEND) {
                if (mMessageBeans.get(position).getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
                    Intent intent = new Intent(this, BitcoinTransactionActivity002.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, mMessageBeans.get(position).getCoin_address());
                    startActivity(intent);
                    finish();
                } else if (mMessageBeans.get(position).getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT)) {
                    Intent intent = new Intent(this, UsdtTransactionActivity002.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, mMessageBeans.get(position).getCoin_address());
                    startActivity(intent);
                    finish();
                }
            } else if (tx_type == Constant.TRANSACTION_TYPE_RECEIVE) {
                Intent intent = new Intent(this, CoinAddressQrActivity.class);
                intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, mMessageBeans.get(position).getCoin_address());
                intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                startActivity(intent);
                finish();
            }
        });


    }

}