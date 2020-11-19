package com.tianqi.aitdwallet.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.TransactionRecordAdapter;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.widget.MyArrowRefreshHeader;
import com.tianqi.baselib.base.BaseFragment;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.TransactionRecordManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import java.util.List;

import butterknife.BindView;

public class TransactionRecordFragment extends BaseFragment {
    @BindView(R.id.lrc_transaction_record)
    LRecyclerView lrcTransactionRecord;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private LinearLayoutManager mllManager;
    private List<TransactionRecord> mMessageBeans;
    private TransactionRecordAdapter transactionAdapter;
    private static final String TAG = "TransactionRecordFragme";
    private String transaction_type, coin_address, coin_name, coin_id;
    private CoinInfo walletBtcFrAddress;
    private boolean isHttpErro;
    private double btc_account_balance;

    @Override
    protected void initView() {
        Bundle arguments = getArguments();
        transaction_type = arguments.getString(Constants.TRANSACTION_TYPE);
        coin_address = arguments.getString(Constants.TRANSACTION_COIN_ADDRESS);
        coin_name = arguments.getString(Constants.TRANSACTION_COIN_NAME);
        coin_id = arguments.getString(Constants.TRANSACTION_COIN_ID);
        walletBtcFrAddress = CoinInfoManager.getCoinFrAddress(coin_name, coin_address);

        switch (transaction_type) {
            case Constants.TRANSACTION_ALL:
                mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);

                break;
            case Constants.TRANSACTION_SEND:
                mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoinId(coin_address, coin_id);
                break;
            case Constants.TRANSACTION_RECEIVE:
                mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address, coin_id);
                break;
            case Constants.TRANSACTION_FIALE:
                mMessageBeans = TransactionRecordManager.getWalletBtcFail(coin_address, coin_id);
                break;
            default:
                break;
        }

        if (mMessageBeans.size()<=0){
             ivNoData.setVisibility(View.VISIBLE);
             tvNoData.setVisibility(View.VISIBLE);
        }else {
            ivNoData.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
        }

        lrcTransactionRecord.setRefreshHeader(new MyArrowRefreshHeader(getActivity()));


        transactionAdapter = new TransactionRecordAdapter(getActivity(), mMessageBeans);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(transactionAdapter);
        lrcTransactionRecord.setAdapter(mLRecyclerViewAdapter);

        mllManager = new LinearLayoutManager(getActivity());
        lrcTransactionRecord.setLayoutManager(mllManager);

        lrcTransactionRecord.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        lrcTransactionRecord.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);

        lrcTransactionRecord.setFooterViewColor(R.color.theme_color, R.color.white, R.color.white);
        lrcTransactionRecord.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        String str1 = getResources().getString(R.string.loading_notice2);
        String str2 = getResources().getString(R.string.loading_finish2);
        lrcTransactionRecord.setFooterViewHint(str1, str2, "");

        lrcTransactionRecord.setHasFixedSize(true);
        lrcTransactionRecord.setOnRefreshListener(mOnRefreshListener);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_trasaction_record;
    }
    OnRefreshListener mOnRefreshListener = () -> {
        lrcTransactionRecord.refreshComplete(10);
        switch (coin_name) {
            case Constant.TRANSACTION_COIN_NAME_BTC:
                break;
            case Constant.TRANSACTION_COIN_NAME_USDT_OMNI:
              //  getUsdtBalance(walletBtcFrAddress);
                break;
        }
    };

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.TRANSACTION_RECORD_UPDATE) {
            if (transaction_type.equals(Constants.TRANSACTION_SEND)) {
                mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoin(coin_address,walletBtcFrAddress.getCoin_id());
            } else if (transaction_type.equals(Constants.TRANSACTION_ALL)) {
                mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address,walletBtcFrAddress.getCoin_id());
            }else if (transaction_type.equals(Constants.TRANSACTION_RECEIVE)){
                mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address,walletBtcFrAddress.getCoin_id());
            }
        }else if (event.getType()==EventMessage.TRANSACTION_RECORD_UPDATE_USDT){
            if (transaction_type.equals(Constants.TRANSACTION_SEND)) {
                mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoin(coin_address,Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            } else if (transaction_type.equals(Constants.TRANSACTION_ALL)) {
                mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoin(coin_address,Constant.TRANSACTION_COIN_NAME_USDT_OMNI);
            }
        }
        if (mMessageBeans.size()<=0){
            ivNoData.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.VISIBLE);
        }else {
            ivNoData.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
            transactionAdapter.refreshData(mMessageBeans);
            lrcTransactionRecord.refreshComplete(5);
        }
    }

    @Override
    protected void initData() {
    }
}
