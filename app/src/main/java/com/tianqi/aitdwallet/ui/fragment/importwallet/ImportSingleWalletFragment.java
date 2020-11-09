package com.tianqi.aitdwallet.ui.fragment.importwallet;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.pager_adapter.ViewPagerCardAdapter;
import com.tianqi.aitdwallet.adapter.recycle_adapter.SingleWalletAdapter;
import com.tianqi.aitdwallet.bean.CurrencyCardBean;
import com.tianqi.aitdwallet.widget.MyArrowRefreshHeader;
import com.tianqi.baselib.base.BaseFragment;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ImportSingleWalletFragment extends BaseFragment {

    @BindView(R.id.rcv_single_wallet)
    LRecyclerView rcvSingleWallet;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private LinearLayoutManager mllManager;
    private List<CoinInfo> mMessageBeans;


    private SingleWalletAdapter homeWalletAdapter;
    private static final String TAG = "ImportSingleWalletFragm";

    @Override
    protected void initView() {
        initRecycleView();
    }

    private void initRecycleView() {
        rcvSingleWallet.setRefreshHeader(new MyArrowRefreshHeader(getActivity()));
        mMessageBeans = new ArrayList<>();
        CoinInfo coinInfo=new CoinInfo();
        coinInfo.setCoin_name(Constant.TRANSACTION_COIN_NAME_BTC);
        coinInfo.setResourceId(R.mipmap.ic_circle_btc);
        mMessageBeans.add(coinInfo);
//        CoinInfo coinInfo2=new CoinInfo();
//        coinInfo2.setCoin_name(Constant.TRANSACTION_COIN_NAME_ETH);
//        coinInfo2.setResourceId(R.mipmap.ic_circle_eth);
//        mMessageBeans.add(coinInfo2);
//        CoinInfo coinInfo3=new CoinInfo();
//        coinInfo3.setCoin_name("AITD");
//        coinInfo3.setResourceId(R.mipmap.ic_circle_aitd);
//        mMessageBeans.add(coinInfo3);
        CoinInfo coinInfo4=new CoinInfo();
        coinInfo4.setCoin_name(Constant.TRANSACTION_COIN_NAME_USDT);
        coinInfo4.setResourceId(R.mipmap.ic_circle_usdt);
        mMessageBeans.add(coinInfo4);

        homeWalletAdapter = new SingleWalletAdapter(getActivity(), mMessageBeans,0);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(homeWalletAdapter);
        rcvSingleWallet.setAdapter(mLRecyclerViewAdapter);

        mllManager = new LinearLayoutManager(getActivity());
        rcvSingleWallet.setLayoutManager(mllManager);

        rcvSingleWallet.setLoadMoreEnabled(false);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_import_wallet_single;
    }

    @Override
    protected void initData() {
    }


}
