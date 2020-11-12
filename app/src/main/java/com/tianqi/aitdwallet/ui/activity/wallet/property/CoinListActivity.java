package com.tianqi.aitdwallet.ui.activity.wallet.property;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.pager_adapter.ViewPagerCardAdapter;
import com.tianqi.aitdwallet.adapter.recycle_adapter.SingleCoinAdapter;
import com.tianqi.aitdwallet.bean.CurrencyCardBean;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.CoinHiddenActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.WalletSettingActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CoinListActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcv_home_coin)
    RecyclerView rcvHomeCoin;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.iv_coin_card_add)
    ImageView ivCoinCardAdd;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private SingleCoinAdapter mAdapter;
    private List<CoinInfo> mMessageBeans;
    private LinearLayoutManager mllManager;
    // private View header;
    private List<CurrencyCardBean> mList;
    private ViewPagerCardAdapter mPagerAdapter;
    private static final String TAG = "CoinListActivity";
    private int select_index;
    private String coin_name;
    private UserInformation userInformation;

    @Override
    protected int getContentView() {
        return R.layout.activity_coin_list;
    }

    @Override
    protected void initView() {
        userInformation= UserInfoManager.getUserInfo();
        coin_name = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);
        getToolBar();
        mMessageBeans = new ArrayList<>();
        ivCoinCardAdd.setVisibility(View.GONE);

        //String coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        List<CoinInfo> specCoinInfo = CoinInfoManager.getNoHiddenSpecHdCoinInfo(coin_name);
        //Log.i(TAG, "initView: 我们这个币种的余额时多少？"+specCoinInfo.get(0).toString());
        mMessageBeans.addAll(specCoinInfo);
        rcvHomeCoin.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        mAdapter = new SingleCoinAdapter(R.layout.layout_adapter_coin_card_for_shadow, mMessageBeans);
        rcvHomeCoin.setAdapter(mAdapter);

        //刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            List<CoinInfo> coinFrName = CoinInfoManager.getNoHiddenSpecHdCoinInfo(mList.get(select_index).getCoin_name());
            mAdapter.setNewData(coinFrName);
            refreshLayout.finishRefresh();
            // initWallet();
        });

//        header = LayoutInflater.from(this).inflate(R.layout.header_coin_card, findViewById(android.R.id.content), false);
//        ViewPager mViewPager = header.findViewById(R.id.viewpager);
        mList = new ArrayList<>();
        List<CoinInfo> specCoinInfo1 = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_BTC);

        if (specCoinInfo1.size()>0){
            CoinInfo coinFrAddress = specCoinInfo1.get(0);
            WalletInfo wallet_btc = WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_BTC);
            CurrencyCardBean mCurrencyCardBean;
            if (wallet_btc != null) {
                mCurrencyCardBean = new CurrencyCardBean();
                mCurrencyCardBean.setCoin_name(coinFrAddress.getCoin_name());
                mCurrencyCardBean.setCoin_alias_name(coinFrAddress.getAlias_name());
                mCurrencyCardBean.setCoin_address(coinFrAddress.getCoin_address());
                mCurrencyCardBean.setCoin_icon_id(coinFrAddress.getCoin_id());
                if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
                    mCurrencyCardBean.setCurrency_to_fiat_num("≈ $"+ DataReshape.doubleBig(coinFrAddress.getCoin_totalAmount() * wallet_btc.getCoin_USDPrice(),2));
                }else {
                    mCurrencyCardBean.setCurrency_to_fiat_num("≈ ￥"+DataReshape.doubleBig(coinFrAddress.getCoin_totalAmount() * wallet_btc.getCoin_CNYPrice(),2));
                }
                mCurrencyCardBean.setCurrency_bg_id(R.mipmap.bg_coin_card_btc);
                mCurrencyCardBean.setCoin_icon_white_id(R.mipmap.ic_circle_white_btc);
                mList.add(mCurrencyCardBean);
            }
        }

        List<CoinInfo> specCoinInfo2 = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_ETH);
        if (specCoinInfo2.size()>0){
            CoinInfo coin_eth = specCoinInfo2.get(0);
            WalletInfo wallet_eth = WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_ETH);
            if (wallet_eth != null) {
                CurrencyCardBean mCurrencyCardBean = new CurrencyCardBean();
                mCurrencyCardBean.setCoin_name(coin_eth.getCoin_name());
                mCurrencyCardBean.setCoin_alias_name(coin_eth.getAlias_name());
                mCurrencyCardBean.setCoin_address(coin_eth.getCoin_address());
                mCurrencyCardBean.setCoin_icon_id(coin_eth.getCoin_id());
                if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
                    mCurrencyCardBean.setCurrency_to_fiat_num("≈ $"+DataReshape.doubleBig(coin_eth.getCoin_totalAmount() * wallet_eth.getCoin_USDPrice(),2));
                }else {
                    mCurrencyCardBean.setCurrency_to_fiat_num("≈ ￥"+DataReshape.doubleBig(coin_eth.getCoin_totalAmount() * wallet_eth.getCoin_CNYPrice(),2));
                }
                mCurrencyCardBean.setCurrency_bg_id(R.mipmap.bg_coin_card_eth);
                mCurrencyCardBean.setCoin_icon_white_id(R.mipmap.ic_circle_white_eth);
                mList.add(mCurrencyCardBean);
            }
        }

        List<CoinInfo> specCoinInfo3 = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
        if (specCoinInfo3.size()>0){
            CoinInfo coin_usdt = specCoinInfo3.get(0);
            WalletInfo wallet_usdt = WalletInfoManager.getWalletFrName(Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            if (wallet_usdt != null) {
                CurrencyCardBean  mCurrencyCardBean = new CurrencyCardBean();
                mCurrencyCardBean.setCoin_name(coin_usdt.getCoin_name());
                mCurrencyCardBean.setCoin_alias_name(coin_usdt.getAlias_name());
                mCurrencyCardBean.setCoin_address(coin_usdt.getCoin_address());
                mCurrencyCardBean.setCoin_icon_id(coin_usdt.getCoin_id());

                if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
                    mCurrencyCardBean.setCurrency_to_fiat_num("≈ $"+DataReshape.doubleBig(coin_usdt.getCoin_totalAmount() * wallet_usdt.getCoin_USDPrice(),2));
                }else {
                    mCurrencyCardBean.setCurrency_to_fiat_num("≈ ￥"+DataReshape.doubleBig(coin_usdt.getCoin_totalAmount() * wallet_usdt.getCoin_CNYPrice(),2));
                }

                mCurrencyCardBean.setCurrency_bg_id(R.mipmap.bg_coin_card_usdt);
                mCurrencyCardBean.setCoin_icon_white_id(R.mipmap.ic_circle_white_usdt);
                mList.add(mCurrencyCardBean);
            }
        }

        mPagerAdapter = new ViewPagerCardAdapter(mList, this);
        viewpager.setAdapter(mPagerAdapter);
        // viewpager.setOffscreenPageLimit(0);
        // TODO: 2020/10/21 根据传进来的币种，来设置默认显示的卡片
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getCoin_name().equals(coin_name)) {
                viewpager.setCurrentItem(i);
                select_index=i;
            }
        }

        viewpager.setPageTransformer(true,
                new ScaleInTransformer(new ScaleInTransformer(new ScaleInTransformer())));

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                List<CoinInfo> coinFrName = CoinInfoManager.getNoHiddenSpecHdCoinInfo(mList.get(position).getCoin_name());
                Log.i(TAG, mList.get(position).getCoin_name()+"001initView:" + coinFrName.size());
                select_index = position;
                toolbarTitle.setText(mList.get(position).getCoin_name());

                mMessageBeans.clear();
                mMessageBeans.addAll(coinFrName);
                mAdapter.setNewData(mMessageBeans);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "我们看点击了哪个？" + state);
            }
        });

        mPagerAdapter.setOnShowItemClickListener(position -> {
        });
        // requestData();
    }

    private void getToolBar() {
        //根据传过来的币种，决定tittle
        toolbarTitle.setText(coin_name);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnCollect.setVisibility(View.VISIBLE);
        btnCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_coin_card_setting));
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType()==EventMessage.HIDDEN_COIN_UPDATE){
            List<CoinInfo> coinFrName = CoinInfoManager.getNoHiddenSpecHdCoinInfo(mList.get(select_index).getCoin_name());
            mAdapter.setNewData(coinFrName);
        }else if (event.getType()==EventMessage.COIN_NAME_UPDATE){
            List<CoinInfo> specCoinInfo1 = CoinInfoManager.getSpecCoinInfo(event.getMsg());
            if (specCoinInfo1.size()>0){
                CoinInfo coinFrAddress = specCoinInfo1.get(0);
                WalletInfo wallet_btc = WalletInfoManager.getWalletFrName(event.getMsg());
                CurrencyCardBean mCurrencyCardBean;
                if (wallet_btc != null) {
                    mCurrencyCardBean = new CurrencyCardBean();
                    mCurrencyCardBean.setCoin_name(coinFrAddress.getCoin_name());
                    mCurrencyCardBean.setCoin_alias_name(coinFrAddress.getAlias_name());
                    mCurrencyCardBean.setCoin_address(coinFrAddress.getCoin_address());
                    mCurrencyCardBean.setCoin_icon_id(coinFrAddress.getCoin_id());

                    if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)){
                        mCurrencyCardBean.setCurrency_to_fiat_num("≈ $"+coinFrAddress.getCoin_totalAmount() * wallet_btc.getCoin_USDPrice());
                    }else {
                        mCurrencyCardBean.setCurrency_to_fiat_num("≈ ￥"+coinFrAddress.getCoin_totalAmount() * wallet_btc.getCoin_CNYPrice());
                    }
                   switch (event.getMsg()){
                       case Constant.TRANSACTION_COIN_NAME_BTC:
                           mCurrencyCardBean.setCurrency_bg_id(R.mipmap.bg_coin_card_btc);
                           break;
                       case Constant.TRANSACTION_COIN_NAME_ETH:
                           mCurrencyCardBean.setCurrency_bg_id(R.mipmap.bg_coin_card_eth);
                           break;
                       case Constant.TRANSACTION_COIN_NAME_USDT_OMNI:
                           mCurrencyCardBean.setCurrency_bg_id(R.mipmap.bg_coin_card_usdt);
                           break;
                   }
                    mCurrencyCardBean.setCoin_icon_white_id(wallet_btc.getResource_id());
                    mList.set(select_index,mCurrencyCardBean);
                }
            }
            mPagerAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.btn_collect, R.id.iv_coin_card_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
                Intent intent = new Intent(this, WalletSettingActivity.class);
                intent.putExtra(Constants.TRANSACTION_COIN_NAME, toolbarTitle.getText());
                intent.putExtra(Constants.INTENT_PUT_COIN_ID, mList.get(select_index).getCoin_icon_id());
                startActivity(intent);
                break;
            case R.id.iv_coin_card_add:
                Intent intent1=new Intent(this, CoinHiddenActivity.class);
                startActivity(intent1);
                break;
        }
    }
}