package com.tianqi.aitdwallet.ui.activity.wallet.record;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.TransRecordAdapter;
import com.tianqi.aitdwallet.ui.activity.wallet.btc.BitcoinTransactionActivity002;
import com.tianqi.aitdwallet.ui.activity.wallet.eth.EthTransactionActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.property.CoinAddressQrActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.usdt.UsdtErc20TransactionActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.usdt.UsdtTransactionActivity002;
import com.tianqi.aitdwallet.ui.service.DataManageService;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.TransactionRecord;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.TransactionRecordManager;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetErc20TxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetEthTxRecordBean;
import com.tianqi.baselib.rxhttp.bean.GetListUnspentBean;
import com.tianqi.baselib.rxhttp.bean.GetLoadingTxBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

/**
 * @author zhangjing
 * @date 2020/11/9
 * @description  交易记录的类，此处还缺少分页加载的逻辑。
 */

public class TransactionRecordActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout_out)
    TabLayout tablayoutOut;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String[] titles;
    List<Fragment> fragments;
    @BindView(R.id.rcv_transaction_record)
    RecyclerView rcvTransactionRecord;

    private String coin_tittle, coin_id;
    private String coin_address;
    private WalletInfo walletInfo;
    private CoinInfo walletBtcInfo;
    private UserInformation userInformation;
    private boolean isHttpErro;
    private double btc_account_balance;
    private View record_header, record_header02, record_empty;
    private TransRecordAdapter recordAdapter;
    private List<TransactionRecord> mMessageBeans, mMessageBeans02;
    private int select_index;
    private TabLayout tablayout;
    private LinearLayoutManager mLayoutManager;

    private TextView btnTransactionSend, btnTransactionReceive;
    private  int mCurrentCounter;
    private boolean isErr;
    private DataManageService service = null;
    private boolean isBind = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_transcation_record002;
    }

    @Override
    protected void initView() {
        getToolBar();
        coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        coin_id = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ID);
        walletBtcInfo = CoinInfoManager.getCoinFrAddress(coin_tittle, coin_address);
        walletInfo = WalletInfoManager.getWalletFrName(walletBtcInfo.getCoin_name());

        mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
        mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rcvTransactionRecord.setLayoutManager(mLayoutManager);

        recordAdapter = new TransRecordAdapter(R.layout.adapter_transaction_record, mMessageBeans);
        rcvTransactionRecord.setAdapter(recordAdapter);

        record_header = getLayoutInflater().inflate(R.layout.activity_transcation_record_header, (ViewGroup) rcvTransactionRecord.getParent(), false);
        record_header02 = getLayoutInflater().inflate(R.layout.activity_transcation_record_header02, (ViewGroup) rcvTransactionRecord.getParent(), false);
        record_empty = getLayoutInflater().inflate(R.layout.adapter_empty_view, (ViewGroup) rcvTransactionRecord.getParent(), false);
        ImageView iv_no_data=record_empty.findViewById(R.id.iv_no_data);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_no_data.getLayoutParams();

        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        if (height<=1920){
            layoutParams.setMargins(0,40,0,0);
            iv_no_data.setLayoutParams(layoutParams);
        }
        recordAdapter.addHeaderView(record_header);
        recordAdapter.addHeaderView(record_header02);

        if (mMessageBeans.size()<=0){
            recordAdapter.addHeaderView(record_empty);
        }
        ImageView ivWalletCoin = record_header.findViewById(R.id.iv_wallet_coin);
        TextView tvCurrencyBalance = record_header.findViewById(R.id.tv_currency_balance);
        TextView tvFiatBalance = record_header.findViewById(R.id.tv_fiat_balance);
        btnTransactionSend = record_header.findViewById(R.id.btn_transaction_send);
        btnTransactionReceive = record_header.findViewById(R.id.btn_transaction_receive);
        View lineMiddle = record_header.findViewById(R.id.line_middle);
        tablayout = record_header02.findViewById(R.id.tablayout);
        titles = new String[]{getString(R.string.titttle_record_all), getString(R.string.tittle_record_send), getString(R.string.tittle_record_receive), getString(R.string.tittle_record_failure)};

        for (int i = 0; i < titles.length; i++) {
            tablayout.addTab(tablayout.newTab());
            tablayout.getTabAt(i).setText(titles[i]);
            tablayoutOut.addTab(tablayoutOut.newTab());
            tablayoutOut.getTabAt(i).setText(titles[i]);
        }

        tablayout.addOnTabSelectedListener(onTabSelectedListener);
        tablayoutOut.addOnTabSelectedListener(onTabSelectedListener);

        GlideUtils.loadResourceImage(this, walletBtcInfo.getResourceId(), ivWalletCoin);

        if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 8 ));
        } else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 4));
        }else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_ETH)) {
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 6));
        }else if (walletBtcInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_ERC20)){
            tvCurrencyBalance.setText(DataReshape.doubleAll(walletBtcInfo.getCoin_totalAmount(), 6));
        }
        UserInformation userInformation = UserInfoManager.getUserInfo();
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
            tvFiatBalance.setText("≈$" + DataReshape.doubleBig(walletBtcInfo.getCoin_totalAmount() * walletInfo.getCoin_USDPrice(), 2));
        } else {
            tvFiatBalance.setText("≈￥" + DataReshape.doubleBig(walletBtcInfo.getCoin_totalAmount() * walletInfo.getCoin_CNYPrice(), 2));
        }
        setListener();
        //刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
          //  initData();
            service.getTxRecordData(coin_id);
        });
        if (mMessageBeans.size()<=0){
            refreshLayout.autoRefresh();
        }
        Intent intent = new Intent(this, DataManageService.class);
        intent.putExtra(Constants.TRANSACTION_COIN_ID, coin_id);
        bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private void getToolBar() {
        coin_tittle = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);
        toolbarTitle.setText(coin_tittle);

        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    TabLayout.BaseOnTabSelectedListener onTabSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            select_index = tab.getPosition();
            //  select_tittle = tab.getText().toString();
            switch (select_index) {
                case 1:
                    mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 2:
                    mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 3:
                    mMessageBeans = TransactionRecordManager.getWalletBtcFail(coin_address, coin_id);
                    break;
                case 0:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                default:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                    break;
            }

            tablayout.getTabAt(select_index).select();
            tablayoutOut.getTabAt(select_index).select();
            moveToPosition(0);
            // refreshLayout.autoRefresh();
            // rcvTransactionRecord.setAdapter(recordAdapter);
            //   fdfdfdfd
            recordAdapter.setNewData(mMessageBeans);

            if (mMessageBeans.size() <= 0) {
                recordAdapter.removeAllHeaderView();
                recordAdapter.addHeaderView(record_header);
                recordAdapter.addHeaderView(record_header02);
                recordAdapter.addHeaderView(record_empty);
            } else {
                recordAdapter.removeHeaderView(record_empty);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void moveToPosition(int position) {
        if (position != -1) {
            rcvTransactionRecord.scrollToPosition(position);
            LinearLayoutManager mLayoutManager =
                    (LinearLayoutManager) rcvTransactionRecord.getLayoutManager();
            tablayoutOut.setVisibility(View.GONE);
            mLayoutManager.scrollToPositionWithOffset(position, 00);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            DataManageService.MyBinder myBinder = (DataManageService.MyBinder) binder;
            service = myBinder.getService();
            service.getTxRecordData(coin_id);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    protected void initData() {

    }

    /**
     * 设置RecyclerView的滚动监听
     */
    private int mSuspensionHeight, mCurrentFirstVisibleIndex;

    private void setListener() {
        rcvTransactionRecord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 获取头部View的高度
                mSuspensionHeight = tablayout.getHeight();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurrentFirstVisibleIndex != mLayoutManager.findFirstVisibleItemPosition()) {
                    // 假如屏幕中显示的第一个条目索引与当前获取到的不一致, 就更新第一条目索引值
                    mCurrentFirstVisibleIndex = mLayoutManager.findFirstVisibleItemPosition();
                }
                // 找到当前屏幕中显示的第二条目
                View view = mLayoutManager.findViewByPosition(mCurrentFirstVisibleIndex + 1);
                if (view != null) {
                    if (view.getTop() <= mSuspensionHeight) {
                        tablayoutOut.setVisibility(View.VISIBLE);
                        // 当前屏幕中第二Item离控件顶部距离如果小于悬挂条高度.
                        // 则悬挂条随着RecyclerView的滚动移动, 这个悬挂条一直遮挡着Item条目的头.
                        //tablayout.setY(-(mSuspensionHeight - view.getTop()));
                        if (mCurrentFirstVisibleIndex < 1) {
                            tablayoutOut.setVisibility(View.GONE);
                        }
                    } else {
                    }
                }
                //Log.e(TAG, "dy  " + dy);
            }
        });

        btnTransactionSend.setOnClickListener(view -> {
            switch (coin_tittle) {
                case Constant.TRANSACTION_COIN_NAME_BTC:
                    // Intent intent=new Intent(this, BitcoinTransactionNativeActivity.class);
                    Intent intent = new Intent(this, BitcoinTransactionActivity002.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
                case Constant.TRANSACTION_COIN_NAME_USDT_OMNI:
                    intent = new Intent(this, UsdtTransactionActivity002.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
                case Constant.TRANSACTION_COIN_NAME_ETH:
                    intent = new Intent(this, EthTransactionActivity.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
                case Constant.TRANSACTION_COIN_NAME_USDT_ERC20:
                    intent = new Intent(this, UsdtErc20TransactionActivity.class);
                    intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
                    startActivity(intent);
                    break;
            }
        });
        btnTransactionReceive.setOnClickListener(view -> {
            Intent intent = new Intent(this, CoinAddressQrActivity.class);
            intent.putExtra(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
            intent.putExtra(Constants.TRANSACTION_COIN_NAME, walletBtcInfo.getCoin_name());
            startActivity(intent);
        });

//        recordAdapter.setOnLoadMoreListener(() -> rcvTransactionRecord.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mCurrentCounter >= 3) {
//                    //数据全部加载完毕
//                    recordAdapter.loadMoreEnd();
//                } else {
//                    if (isErr) {
//                        //成功获取更多数据
//                       // recordAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
//                       // mCurrentCounter = mQuickAdapter.getData().size();
//                        recordAdapter.loadMoreComplete();
//                    } else {
//                        //获取更多数据失败
//                        isErr = true;
//                        ToastUtil.showToast(TransactionRecordActivity.this,getString(R.string.notice_request_error));
//                        recordAdapter.loadMoreFail();
//                    }
//                }
//            }
//
//        }, 500), rcvTransactionRecord);
    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.TRANSACTION_RECORD_UPDATE) {
            switch (select_index) {
                case 1:
                    mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 2:
                    mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 3:
                    mMessageBeans = TransactionRecordManager.getWalletBtcFail(coin_address, coin_id);
                    break;
                case 0:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                default:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                    break;
            }
//            mMessageBeans.addAll(mMessageBeans02);
//            mMessageBeans.addAll(mMessageBeans02);
//            mMessageBeans.addAll(mMessageBeans02);
//            mMessageBeans.addAll(mMessageBeans02);
            recordAdapter.setNewData(mMessageBeans);
            refreshLayout.finishRefresh();
        } else if (event.getType() == EventMessage.TRANSACTION_RECORD_UPDATE_USDT) {
            switch (select_index) {
                case 1:
                    mMessageBeans = TransactionRecordManager.getTxSendFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 2:
                    mMessageBeans = TransactionRecordManager.getTxReceiveFrAddressAndCoinId(coin_address, coin_id);
                    break;
                case 3:
                    mMessageBeans = TransactionRecordManager.getWalletBtcFail(coin_address, coin_id);
                    break;
                case 0:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                default:
                    mMessageBeans = TransactionRecordManager.getTxFrAddressAndCoinId(coin_address, coin_id);
                    break;
            }
            recordAdapter.setNewData(mMessageBeans);
            refreshLayout.finishRefresh();
        }
        if (mMessageBeans.size() <= 0) {
            recordAdapter.addHeaderView(record_empty);
        } else {
            recordAdapter.removeHeaderView(record_empty);
        }
    }
}