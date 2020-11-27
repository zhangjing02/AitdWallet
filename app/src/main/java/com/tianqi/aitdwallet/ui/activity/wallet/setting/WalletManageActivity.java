package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.WalletManageAdapter;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletManageActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.tv_wallet_delete)
    TextView tvWalletDelete;
    @BindView(R.id.rcv_manage_wallet)
    RecyclerView rcvManageWallet;
    @BindView(R.id.layout_add_import_wallet)
    ConstraintLayout layoutAddImportWallet;
    @BindView(R.id.tv_wallet_address)
    TextView tvWalletAddress;
    @BindView(R.id.line_bottom)
    View lineBottom;
//    @BindView(R.id.tv_wallet_delete)
//    TextView tvWalletDelete;


    private List<CoinInfo> mMessageBeans;
    private WalletManageAdapter mAdapter;
    private String[] titles;
    //private String select_tittle = TITTLE_CREATE;
    private int select_index;
    private static final int TITTLE_CREATE_INDEX = 0;
    private static final int TITTLE_IMPORT_INDEX = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet_manage;
    }

    @Override
    protected void initView() {
        getToolBar();
        titles = new String[]{getString(R.string.tittle_create_wallet_text), getString(R.string.tittle_import_wallet_text)};
        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
            tablayout.getTabAt(i).setText(titles[i]);
        }

        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                select_index = tab.getPosition();
                //select_tittle = tab.getText().toString();
                switch (select_index) {
                    case TITTLE_CREATE_INDEX:
                        rcvManageWallet.setVisibility(View.VISIBLE);
                        layoutAddImportWallet.setVisibility(View.GONE);
                        mMessageBeans = CoinInfoManager.getCreateCoinInfo();
                        if (mMessageBeans.size() <= 0) {
                            rcvManageWallet.setVisibility(View.GONE);
                            layoutAddImportWallet.setVisibility(View.VISIBLE);
                            tvWalletAddress.setText(R.string.btn_create_hd_wallet_text);
                            tvWalletDelete.setVisibility(View.GONE);
                        } else {
                            rcvManageWallet.setVisibility(View.VISIBLE);
                            layoutAddImportWallet.setVisibility(View.GONE);
                            mAdapter.setNewData(mMessageBeans);
                            tvWalletDelete.setVisibility(View.VISIBLE);
                            lineBottom.setVisibility(View.VISIBLE);
                        }
                        break;
                    case TITTLE_IMPORT_INDEX:
                        tvWalletDelete.setVisibility(View.GONE);
                        lineBottom.setVisibility(View.GONE);
                        mMessageBeans = CoinInfoManager.getImportCoinInfo();
//                        if (mMessageBeans.size() <= 0){
//                            tvWalletDelete.setVisibility(View.GONE);
//                        }else {
//                            tvWalletDelete.setVisibility(View.VISIBLE);
//                        }
                        CoinInfo coinInfo = new CoinInfo();
                        coinInfo.setWallet_id(Constants.COIN_NULL);
                        mMessageBeans.add(coinInfo);
                        if (mMessageBeans.size() <= 0) {
                            rcvManageWallet.setVisibility(View.GONE);
                            layoutAddImportWallet.setVisibility(View.VISIBLE);
                            tvWalletAddress.setText(R.string.text_import_wallet);
                        } else {
                            rcvManageWallet.setVisibility(View.VISIBLE);
                            layoutAddImportWallet.setVisibility(View.GONE);
                            mAdapter.setNewData(mMessageBeans);
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mMessageBeans = CoinInfoManager.getCreateCoinInfo();
        rcvManageWallet.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new WalletManageAdapter(R.layout.layout_adapter_wallet_manage, mMessageBeans);
        rcvManageWallet.setAdapter(mAdapter);

        if (mMessageBeans.size() <= 0) {
            rcvManageWallet.setVisibility(View.GONE);
            layoutAddImportWallet.setVisibility(View.VISIBLE);
            tvWalletAddress.setText(R.string.btn_create_hd_wallet_text);
            tvWalletDelete.setVisibility(View.GONE);
        } else {
            tvWalletDelete.setVisibility(View.VISIBLE);
            rcvManageWallet.setVisibility(View.VISIBLE);
            layoutAddImportWallet.setVisibility(View.GONE);
        }

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (select_index) {
                case TITTLE_CREATE_INDEX:
                    Intent intent = new Intent(this, WalletSettingActivity.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                    intent.putExtra(Constants.INTENT_PUT_COIN_ID, mMessageBeans.get(position).getCoin_id());

                    startActivity(intent);
                    break;
                case TITTLE_IMPORT_INDEX:
                    //WalletSettingFrImportActivity
                    if (mMessageBeans.get(position).getWallet_id().equals(Constants.COIN_NULL)) {
                        intent = new Intent(this, VerifySecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_IMPORT_WALLET);
                        startActivity(intent);
                    } else {
                        intent = new Intent(this, WalletSettingFrImportActivity.class);
                        intent.putExtra(Constants.TRANSACTION_COIN_NAME, mMessageBeans.get(position).getCoin_name());
                        intent.putExtra(Constants.INTENT_PUT_COIN_ID, mMessageBeans.get(position).getCoin_id());
                        startActivity(intent);
                    }
                    break;
            }
        });
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_wallet_manage);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.DELETE_CREATE_COIN_UPDATE) {
            // TODO: 2020/10/23 如果是删除创建币种的话。
            mMessageBeans = CoinInfoManager.getCreateCoinInfo();
            if (mMessageBeans.size() <= 0) {
                rcvManageWallet.setVisibility(View.GONE);
                layoutAddImportWallet.setVisibility(View.VISIBLE);
                tvWalletAddress.setText(R.string.btn_create_hd_wallet_text);
                tvWalletDelete.setVisibility(View.GONE);
            } else {
                tvWalletDelete.setVisibility(View.VISIBLE);
                rcvManageWallet.setVisibility(View.VISIBLE);
                layoutAddImportWallet.setVisibility(View.GONE);
                mAdapter.setNewData(mMessageBeans);
            }

        } else if (event.getType() == EventMessage.DELETE_IMPORT_COIN_UPDATE) {
            mMessageBeans = CoinInfoManager.getImportCoinInfo();
            if (mMessageBeans.size() <= 0) {
                tvWalletDelete.setVisibility(View.GONE);
            } else {
                tvWalletDelete.setVisibility(View.VISIBLE);
            }
            CoinInfo coinInfo = new CoinInfo();
            coinInfo.setWallet_id(Constants.COIN_NULL);
            mMessageBeans.add(coinInfo);
            mAdapter.setNewData(mMessageBeans);
            if (mMessageBeans.size() <= 0) {
                //  rcvManageWallet.setVisibility(View.GONE);
                layoutAddImportWallet.setVisibility(View.VISIBLE);
            }
        } else if (event.getType() == EventMessage.COIN_NAME_UPDATE) {
            switch (select_index) {
                case TITTLE_CREATE_INDEX:
                    mMessageBeans = CoinInfoManager.getCreateCoinInfo();
                    mAdapter.setNewData(mMessageBeans);
                    break;
                case TITTLE_IMPORT_INDEX:
                    mMessageBeans = CoinInfoManager.getImportCoinInfo();
                    mAdapter.setNewData(mMessageBeans);
                    break;
            }
        }
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.layout_add_import_wallet, R.id.tv_wallet_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_add_import_wallet:
                switch (select_index) {
                    case TITTLE_CREATE_INDEX:
                        Intent intent = new Intent(this, VerifySecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_create_wallet));
                        startActivity(intent);

                        break;
                    case TITTLE_IMPORT_INDEX:
                        intent = new Intent(this, VerifySecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_import_wallet));
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.tv_wallet_delete:
                switch (select_index) {
                    case TITTLE_CREATE_INDEX:
                        Intent intent = new Intent(this, VerifySecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_DELETE_CREATE_WALLET);
                        startActivity(intent);
                        break;
                    case TITTLE_IMPORT_INDEX:
                        intent = new Intent(this, VerifySecurityPsdActivity.class);
                        intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_DELETE_IMPORT_WALLET);
                        startActivity(intent);
                        break;
                }
                break;
        }
    }

}