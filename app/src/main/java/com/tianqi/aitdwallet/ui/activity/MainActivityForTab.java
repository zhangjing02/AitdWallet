package com.tianqi.aitdwallet.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.fragment.Fragment1;
import com.tianqi.aitdwallet.ui.fragment.SettingFragment;
import com.tianqi.aitdwallet.ui.fragment.WalletFragment002;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.utils.statusbar.StatusBarCompat;
import com.tianqi.aitdwallet.widget.dialog.FunctionNotOpenDialog;
import com.tianqi.aitdwallet.widget.dialog.VersionUpdateDialog;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.PrefUtils;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetNewVersionBean;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivityForTab extends BaseActivity {
    @BindView(R.id.secondViewPager)
    NoScrollViewPager secondViewPager;

    @BindView(R.id.relativeLayout2)
    LinearLayout relativeLayout2;
    @BindView(R.id.rb_property)
    LinearLayout rbProperty;
    @BindView(R.id.rb_exchange)
    LinearLayout rbExchange;
    @BindView(R.id.rb_financial)
    LinearLayout rbFinancial;
    @BindView(R.id.rb_setting)
    LinearLayout rbSetting;
    @BindView(R.id.iv_property)
    ImageView ivProperty;
    @BindView(R.id.tv_property)
    TextView tvProperty;
    @BindView(R.id.iv_financial)
    ImageView ivFinancial;
    @BindView(R.id.tv_financial)
    TextView tvFinancial;
    @BindView(R.id.iv_exchange)
    ImageView ivExchange;
    @BindView(R.id.tv_exchange)
    TextView tvExchange;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;

    private String[] titles;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private WalletFragment002 fragment1;
    public static boolean isForeground = false;

    @Override
    protected int getContentView() {
        return R.layout.activity_main_for_tab;
    }

    @Override
    protected void initView() {
        titles = new String[]{getString(R.string.tittle_property), getString(R.string.tittle_financial), getString(R.string.tittle_browse), getString(R.string.tittle_setting)};
       // StatusBarCompat.translucentStatusBar(MainActivityForTab.this, true);
        fragment1 = new WalletFragment002();
        fragments.add(fragment1);
        for (int i = 0; i < titles.length - 2; i++) {
            Fragment1 fragment2 = new Fragment1();
            Bundle bundle = new Bundle();
            bundle.putString("content", titles[i]);
            fragment1.setArguments(bundle);
            fragments.add(fragment2);
        }
        SettingFragment fragment3 = new SettingFragment();
        fragments.add(fragment3);
        secondViewPager.setOffscreenPageLimit(3);
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        secondViewPager.setAdapter(adapter);

    }

    private String getVersionCode() {
        // 包管理器 可以获取清单文件信息
        PackageManager packageManager = getPackageManager();
        try {
            // 获取包信息
            // 参1 包名 参2 获取额外信息的flag 不需要的话 写0
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
//        VersionUpdateDialog shotNoticeDialog = new VersionUpdateDialog(MainActivity.this, R.style.MyDialog2,false);
//        shotNoticeDialog.show();
//        shotNoticeDialog.setOnDialogClickListener(new VersionUpdateDialog.OnDialogClickListener() {
//            @Override
//            public void onItemClick(View view, String password, int type) {
//
//            }
//        });
        RetrofitFactory.getInstence(this).API()
                .getNewVersion().compose(RxHelper.io_main())
                .subscribe(new BaseObserver<GetNewVersionBean>(this) {
                    @Override
                    public void onSuccess(GetNewVersionBean data, String msg) {
                        String version_str = data.getVersion();
                        String local_version_str = getVersionCode();
                        String version_replace = version_str.replace(".", "");
                        String local_version_replace = local_version_str.replace(".", "");
                        int version_code = Integer.valueOf(version_replace);
                        int local_version_code = Integer.valueOf(local_version_replace);
                        int first_open = PrefUtils.getInt(MainActivityForTab.this, PrefUtils.FIRST_START_APP, -1);
                        if (first_open > 0) {
                            if (version_code > local_version_code) {
                                Calendar calendar = Calendar.getInstance();
                                long time_update = PrefUtils.getLong(MainActivityForTab.this, PrefUtils.APP_UPDATE_TIME, 0);
                                long time_during = calendar.getTimeInMillis() / 1000 - time_update / 1000 - 86400;
                                if (data.isForceFlag()) {
                                    VersionUpdateDialog shotNoticeDialog = new VersionUpdateDialog(MainActivityForTab.this, R.style.MyDialog2, data.isForceFlag());
                                    shotNoticeDialog.setTittle(getString(R.string.tittle_version_update_tag) + data.getVersion());
                                    shotNoticeDialog.setContent(data.getMessage());
                                    shotNoticeDialog.setOnDialogClickListener((view, password, type) -> {
                                        Intent intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse(data.getUrl());
                                        intent.setData(content_url);
                                        startActivity(intent);
                                    });
                                    shotNoticeDialog.show();
                                } else {
                                    if ((time_update > 0 && time_during > 0) || time_update == 0) {
                                        //如果是非强制更新，如果时间记录点不是第一次，如果时间记录点已经过去一天了，就弹窗。
                                        VersionUpdateDialog shotNoticeDialog = new VersionUpdateDialog(MainActivityForTab.this, R.style.MyDialog2, data.isForceFlag());
                                        shotNoticeDialog.setTittle(getString(R.string.tittle_version_update_tag) + data.getVersion());
                                        shotNoticeDialog.setContent(data.getMessage());
                                        shotNoticeDialog.setOnDialogClickListener((view, password, type) -> {
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(data.getUrl());
                                            intent.setData(content_url);
                                            startActivity(intent);
                                        });
                                        shotNoticeDialog.show();
                                        PrefUtils.setLong(MainActivityForTab.this, PrefUtils.APP_UPDATE_TIME, calendar.getTimeInMillis());
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                    }
                });
    }

    @Override
    protected void initData() {
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @OnClick({R.id.rb_property, R.id.rb_exchange, R.id.rb_financial, R.id.rb_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_property:
                setCheck(0);
                secondViewPager.setCurrentItem(0, false);
                //   StatusBarCompat.translucentStatusBar(MainActivity.this, true);
                break;
            case R.id.rb_exchange:
            case R.id.rb_financial:
                // StatusBarCompat.translucentStatusBar(MainActivity.this, true);
                int currentItem = secondViewPager.getCurrentItem();
//                if (currentItem == 0) {
//                    setCheck(0);
//                } else if (currentItem == 3) {
//                    setCheck(3);
//                }
                FunctionNotOpenDialog shotNoticeDialog = new FunctionNotOpenDialog(this, R.style.MyDialog2);
                shotNoticeDialog.show();

                UserInformation userInformation = UserInfoManager.getUserInfo();
                if (userInformation.getLanguageId() == Constants.LANGUAGE_ENGLISH) {
                    shotNoticeDialog.setImageView(R.mipmap.ic_function_not_open_en);
                }

//                if (getResources().getConfiguration().locale.getCountry().equals("US")){
//                    shotNoticeDialog.setImageView(R.mipmap.ic_function_not_open_en);
//                }

//                StatusBarCompat.translucentStatusBar(MainActivity.this, false);
//                StatusBarCompat.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.white_transparent));
                //   ToastUtil.showSimpleToast(this,getString(R.string.notice_function_not_open));
                break;
            case R.id.rb_setting:
                setCheck(3);
                secondViewPager.setCurrentItem(3, false);
                //  StatusBarCompat.translucentStatusBar(MainActivity.this, false);
                //  StatusBarCompat.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.white_transparent));
                break;
        }
    }

    private void setCheck(int position) {
        switch (position) {
            case 0:
                GlideUtils.loadGiftResourceImage(this,R.mipmap.ainmator_main_tab_property,ivProperty);
                tvProperty.setTextColor(getResources().getColor(R.color.text_main_blue));
                GlideUtils.loadResourceImage(this,R.mipmap.ic_main_tab_setting_grey,ivSetting);
                tvSetting.setTextColor(getResources().getColor(R.color.text_main2_black));

                break;
            case 1:
//                rbProperty.setChecked(false);
//                rbExchange.setChecked(true);
//                rbFinancial.setChecked(false);
//                rbSetting.setChecked(false);
                break;
            case 2:
//                rbProperty.setChecked(false);
//                rbExchange.setChecked(false);
//                rbFinancial.setChecked(true);
//                rbSetting.setChecked(false);
                break;
            case 3:
//                rbProperty.setChecked(false);
//                rbExchange.setChecked(false);
//                rbFinancial.setChecked(false);
//                rbSetting.setChecked(true);
                GlideUtils.loadGiftResourceImage(this,R.mipmap.animator_main_tab_setting,ivSetting);
                tvSetting.setTextColor(getResources().getColor(R.color.text_main_blue));

                GlideUtils.loadResourceImage(this,R.mipmap.ic_main_tab_property_grey,ivProperty);
                tvProperty.setTextColor(getResources().getColor(R.color.text_main2_black));
                break;
            default:
                break;
        }
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragment1 != null) {
                fragment1.hiddenPopWindow();
            }
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast(this, getString(R.string.exist_procedure));
                mExitTime = System.currentTimeMillis();
            } else {
                //JPushInterface.clearAllNotifications(WalletActivity2.this);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (fragment1 != null) {
            fragment1.hiddenPopWindow();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (fragment1 != null) {
            fragment1.hiddenPopWindow();
        }
        return super.dispatchTouchEvent(ev);
    }


}