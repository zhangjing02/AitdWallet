package com.tianqi.aitdwallet.ui.activity.wallet.record;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.fragment.TransactionRecordFragment;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.widget.listener.DetailScrollStateListener;
import com.tianqi.baselib.widget.scrollview.BaseDetailFrag;
import com.tianqi.baselib.widget.scrollview.DetailScrollView;
import com.tianqi.baselib.widget.scrollview.MatchViewPager;
import com.tianqi.baselib.widget.scrollview.MyPagerAdapter;
import com.tianqi.baselib.widget.scrollview.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

/**
* 查看说明文档 https://nowyxc.github.io/2019/02/19/Android吸顶效果——Smartrefresh-ScrollView-ViewPager-RecylerView/
**/
public class TransactionRecordScrollActivity extends AppCompatActivity implements OnRefreshListener, ObservableScrollView.ScrollViewListener, DetailScrollStateListener {

    private SmartRefreshLayout mRefreshView;
    private DetailScrollView mSvFirst;
    private View mHeaderView;
    private TabLayout mTabLayout;
    private MatchViewPager mVpMain;
    private List<Fragment> mFragments;
    private String  coin_tittle,coin_id;
    private String  coin_address;
    private WalletInfo walletInfo;
    private CoinInfo walletBtcInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record_scroll);
        coin_tittle = getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);
        coin_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        coin_id = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ID);
        walletBtcInfo = CoinInfoManager.getCoinFrAddress(coin_tittle,coin_address);

        initView();
    }

    private void initView(){
        mRefreshView = findViewById(R.id.main_refreshView);
        mSvFirst = findViewById(R.id.main_SvFirst);
        mHeaderView = findViewById(R.id.tab_viewPager_HeaderView);
        mTabLayout = findViewById(R.id.main_TabLayout);
        mVpMain = findViewById(R.id.main_VpMain);

        initRefreshView();
        initScrollView();
        initTabLayout();
        initViewPager();
    }

    private void initRefreshView(){
        mRefreshView.setEnableLoadMore(false);//禁止加载smartRefresh的加载更多
        mRefreshView.setOnRefreshListener(this);//下拉刷新监听
        mRefreshView.setEnableOverScrollBounce(false);//是否启用越界回弹
    }

    private void initScrollView(){
        mSvFirst.setInterceptTouchEvent(true);
        mSvFirst.setDetailScrollStateListener(this);
        mSvFirst.setScrollViewListener(this);
    }

    private void initTabLayout(){
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.TRANSACTION_ALL));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.TRANSACTION_SEND));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.TRANSACTION_RECEIVE));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constants.TRANSACTION_FIALE));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mVpMain.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initViewPager(){
       mFragments = new ArrayList<>();
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TransactionRecordFragment fragment1 = new TransactionRecordFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TRANSACTION_TYPE, mTabLayout.getTabAt(i).getText().toString());
            bundle.putString(Constants.TRANSACTION_COIN_ADDRESS, coin_address);
            bundle.putString(Constants.TRANSACTION_COIN_NAME, coin_tittle);
            bundle.putString(Constants.TRANSACTION_COIN_ID, coin_id);
            fragment1.setArguments(bundle);
            mFragments.add(fragment1);
        }

        mVpMain.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragments));
    }


    @Override
    public boolean isChildTouchEvent() {
        Fragment fragment =  mFragments.get(mVpMain.getCurrentItem());
        if(fragment instanceof BaseDetailFrag){
            return ((BaseDetailFrag)fragment).canChildTouch();
        }
        return false;
    }


    @Override
    public void updateTouchEvent(boolean isInterceptTouchEvent) {
        if(mSvFirst != null)
            mSvFirst.setInterceptTouchEvent(isInterceptTouchEvent);
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        int[] location = new int[2];
        mVpMain.getLocationOnScreen(location);
        int yPosition = location[1];
//        int visibility = mTopTabLayout.getVisibility();
        if (yPosition < mVpMain.getHeightOffset()) {//移动到顶部，小于顶部偏移量(ViewPager完全显示)
            updateTouchEvent(false);
//            if(visibility != View.VISIBLE)
//                mTopTabLayout.setVisibility(View.VISIBLE);
        } else {
            updateTouchEvent(true);
//            if(visibility != View.GONE)
//                mTopTabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishRefresh(2000);
    }
}
