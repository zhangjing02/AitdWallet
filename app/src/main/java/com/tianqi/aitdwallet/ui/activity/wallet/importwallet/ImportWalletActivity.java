package com.tianqi.aitdwallet.ui.activity.wallet.importwallet;


import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.pager_adapter.FragmentAdapter;
import com.tianqi.aitdwallet.ui.fragment.importwallet.ImportBulkWalletFragment;
import com.tianqi.aitdwallet.ui.fragment.importwallet.ImportSingleWalletFragment;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.utils.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ImportWalletActivity extends BaseActivity {

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
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;

    List<String> titles;
    List<Fragment> fragments;

    @Override
    protected int getContentView() {
        return R.layout.activity_import_wallet_main;
    }

    @Override
    protected void initView() {
        getToolBar();
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        titles.add(getString(R.string.tittle_bulk_import_wallet));
        titles.add(getString(R.string.tittle_single_import_wallet));

        fragments.add(new ImportBulkWalletFragment());
        fragments.add(new ImportSingleWalletFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(1);

    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_import_wallet);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {

    }

}