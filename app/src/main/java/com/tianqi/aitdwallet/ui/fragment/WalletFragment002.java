package com.tianqi.aitdwallet.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.HomeWalletAdapter;
import com.tianqi.aitdwallet.ui.activity.wallet.property.SelectCoinToTransActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.VerifySecurityPsdActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.WalletHiddenActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.WalletManageActivity;
import com.tianqi.aitdwallet.ui.service.DataManageService;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.utils.HighlightOptionsUtils;
import com.tianqi.aitdwallet.utils.statusbar.StatusBarCompat;
import com.tianqi.aitdwallet.widget.dialog.NewGuideEndDialog;
import com.tianqi.aitdwallet.widget.dialog.NewGuideStartDialog;
import com.tianqi.baselib.base.BaseFragment;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dao.WalletInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.dbManager.PrefUtils;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.dbManager.WalletInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ScreenUtils;
import com.tianqi.baselib.utils.eventbus.EventMessage;
import com.tianqi.baselib.widget.RoundRectImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.content.Context.BIND_AUTO_CREATE;

public class WalletFragment002 extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rcv_home_wallet)
    RecyclerView rcvHomeWallet;

    ImageView ivAddWallet;
    ImageView ivNewGuideStep2;
    TextView tvCurrencySavingTag;
    ImageView ivBalanceHide;
    TextView tvFiatBalance;
    RoundRectImageView ivNewGuideStep;
    TextView tvFiatHide;
    TextView btnTransactionSend;
    ImageView ivTransactionSend;
    TextView btnTransactionReceive;
    ImageView ivTransactionReceive;
    ImageView ivHideWallet;
    @BindView(R.id.iv_new_guide_step4)
    ImageView ivNewGuideStep4;
    @BindView(R.id.iv_new_guide_step3)
    ImageView ivNewGuideStep3;

    private LinearLayoutManager mllManager;
    private HomeWalletAdapter homeWalletAdapter;
    private static final String TAG = "WalletFragment";
    private List<WalletInfo> mWalletBeans,mGuideWalletBeans;
    private WalletInfo walletInfo;
    private Typeface typeFace;
    private boolean isHideBalance;

    private PopupWindow mPopWindow,mPopWindowTop;
    private int new_guide_step_index;
    private View fragment_wallet_header;
    private UserInformation userInformation;
    private DataManageService service = null;
    private boolean isBind = false;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_wallet_info002;
    }

    @Override
    protected void initView() {
        walletInfo = WalletInfoManager.getHdWalletInfo();
        if (walletInfo == null) {
            walletInfo = WalletInfoManager.getWalletInfo().get(0);
        }
        initRecycleView();
        StatusBarCompat.translucentStatusBar(getActivity(), true);

        int first_open = PrefUtils.getInt(getActivity(), PrefUtils.FIRST_START_APP, -1);
        if (first_open < 0) {
            NewGuideStartDialog shotNoticeDialog = new NewGuideStartDialog(getActivity(), R.style.MyDialog2);
            shotNoticeDialog.setOnDialogClickListener((view1, password, type) -> {
                showGuide();
            });
            shotNoticeDialog.setOnDismissListener(dialogInterface -> {
                PrefUtils.setInt(getActivity(), PrefUtils.FIRST_START_APP, 2);
            });
            shotNoticeDialog.show();
        }

        Intent intent = new Intent(getActivity(), DataManageService.class);
        getActivity().bindService(intent, conn, BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            isBind = true;
            DataManageService.MyBinder myBinder = (DataManageService.MyBinder) binder;
            service = myBinder.getService();
            service.getWalletBalance();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    /**
     * 新手引导层 只有第一次使用 APP 才显示
     */

    public void showGuide() {
        //设置需要高亮的布局
        //为没个布局配置需要显示的信息 这里自定义xml即可
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LogUtil.i(TAG, "showGuide: 我们看屏幕的高度是？" + height);

        NewbieGuide.with(this)
                .setLabel("guide1")
                .setShowCounts(1)//控制次数
                .alwaysShow(true)//总是显示，调试时可以打开
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                    }
                    @Override
                    public void onRemoved(Controller controller) {
                        LogUtil.i(TAG, new_guide_step_index + "-----onShowed: 002我们看到了啥？" + controller.toString());
                        ivNewGuideStep.setVisibility(View.GONE);
                        tvFiatBalance.setVisibility(View.VISIBLE);
                        ivNewGuideStep4.setVisibility(View.GONE);
                        ivNewGuideStep2.setVisibility(View.GONE);
                        rcvHomeWallet.setVisibility(View.VISIBLE);
                        
                        homeWalletAdapter.setNewData(mWalletBeans);
                        if (new_guide_step_index == 3) {
                            ivNewGuideStep4.setVisibility(View.GONE);
                            new_guide_step_index = 0;
                            NewGuideEndDialog shotNoticeDialog = new NewGuideEndDialog(getActivity(), R.style.MyDialog2);
                            shotNoticeDialog.show();
                        }
                    }
                }).setOnPageChangedListener(page -> {
            new_guide_step_index = page;
            if (page == 0) {            //代表当前显示的是第几页、。
                ivNewGuideStep.setVisibility(View.VISIBLE);
                tvFiatBalance.setVisibility(View.INVISIBLE);
            } else if (page == 1) {  //如果是第二页，就展示他的弹框。
                ivNewGuideStep.setVisibility(View.GONE);
                ivNewGuideStep2.setVisibility(View.VISIBLE);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;//设置阴影透明度
                getActivity().getWindow().setAttributes(lp);
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_share_select, null);
                mPopWindow = new PopupWindow(contentView);
                //130和112是设计尺寸。
                mPopWindow.setWidth(ScreenUtils.dip2px(getActivity(), 130));
                mPopWindow.setHeight(ScreenUtils.dip2px(getActivity(), 112));
                mPopWindow.setOutsideTouchable(true);
                //130是pop的宽度，10是整体右边距，15是view控件的宽度。
                mPopWindow.showAsDropDown(ivAddWallet, -ScreenUtils.dip2px(getActivity(), 130 - 10 - 15), 50);
                mPopWindow.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                    lp1.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp1);
                });
            } else if (page == 2) {
                if (mPopWindow != null && mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                }
                ivNewGuideStep3.setVisibility(View.VISIBLE);
            } else if (page == 3) {
               // rcvHomeWallet.setVisibility(View.GONE);
                // TODO: 2020/11/21 把数据更新为无，方便展示新手指引
                mGuideWalletBeans=new ArrayList<>();
                homeWalletAdapter.setNewData(mGuideWalletBeans);


                ivNewGuideStep4.setVisibility(View.VISIBLE);
                ivNewGuideStep2.setVisibility(View.GONE);
                ivNewGuideStep3.setVisibility(View.GONE);
                //  ivNewGuideStep4.setVisibility(View.VISIBLE);
            }
        })
                .addGuidePage(GuidePage.newInstance()
                        // .addHighLight(ivNewGuideStep)
                        .addHighLightWithOptions(ivNewGuideStep, HighLight.Shape.ROUND_RECTANGLE, 600,
                                0, HighlightOptionsUtils.createTransparentOptions())
                        .setLayoutRes(R.layout.view_guide1, R.id.textView4).setOnLayoutInflatedListener((view, controller) -> {
                            ImageView imageView = view.findViewById(R.id.iv_indicator);
                            TextView textView3 = view.findViewById(R.id.textView3);
                            // TextView textView2 = view.findViewById(R.id.textView2);
                            TextView textView4 = view.findViewById(R.id.textView4);
                            SpannableString spannableString = new SpannableString(textView3.getText().toString());
                            int end=3;

                            userInformation=UserInfoManager.getUserInfo();
                            if (userInformation.getLanguageId()==Constants.LANGUAGE_ENGLISH){
                                end=12;
                            }
//                                if (getResources().getConfiguration().locale.getCountry().equals("US")){
//                                    end=12;
//                                }
                            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_main_yellow)), 0, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            textView3.setText(spannableString);
                            if (height <= 1920) {  // TODO: 2020/11/20 此处还需要用几款小手机做一下适配。

                                double ratio = height * 1000 / width / 1000f;
                                if (ratio<1.7f){  //只针对小手机中的特别宽和矮的手机（华为）做了微调。
                                    LinearLayout.LayoutParams layoutParams001 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                                    layoutParams001.setMargins(0, 0, 0, DensityUtil.dp2px(10f));
                                    imageView.setLayoutParams(layoutParams001);

                                    LinearLayout.LayoutParams layoutParams002 = (LinearLayout.LayoutParams) textView3.getLayoutParams();
                                    layoutParams002.setMargins(0, 0, 0, DensityUtil.dp2px(30f));
                                    textView3.setLayoutParams(layoutParams002);
//                                    LinearLayout.LayoutParams layoutParams003 = (LinearLayout.LayoutParams) textView4.getLayoutParams();
//                                    layoutParams003.setMargins(0, 0, 0, -DensityUtil.dp2px(30f));
//                                    textView4.setLayoutParams(layoutParams003);
                                }else {
                                    int xx = (1920 - height) / 3;
                                    LinearLayout.LayoutParams layoutParams001 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                                    layoutParams001.setMargins(0, 0, 0, 131 - xx);
                                    textView3.setLayoutParams(layoutParams001);

                                    LinearLayout.LayoutParams layoutParams002 = (LinearLayout.LayoutParams) textView3.getLayoutParams();
                                    layoutParams002.setMargins(0, 0, 0, 150 - xx);
                                    textView3.setLayoutParams(layoutParams002);

                                    LinearLayout.LayoutParams layoutParams003 = (LinearLayout.LayoutParams) textView4.getLayoutParams();
                                    layoutParams003.setMargins(0, 0, 0, 500 - xx);
                                    textView4.setLayoutParams(layoutParams003);
                                }
                            }
                        }))
                .addGuidePage(GuidePage.newInstance()
                        //.addHighLight(ivAddWallet)
                        .addHighLightWithOptions(ivNewGuideStep2, HighLight.Shape.CIRCLE, HighlightOptionsUtils.createTransparentOptions())
                        .setEverywhereCancelable(false)//是否点击任意位置消失引导页
                        .setLayoutRes(R.layout.view_guide2, R.id.textView9)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                TextView textView = view.findViewById(R.id.textView2);
                                SpannableString spannableString = new SpannableString(textView.getText().toString());
                                int start1 = textView.getText().toString().indexOf(getString(R.string.text_new_guide_cut1_start));
                                int end1 = textView.getText().toString().indexOf(getString(R.string.text_new_guide_cut1_end));
                                int start2 = textView.getText().toString().indexOf(getString(R.string.text_new_guide_cut2_start));
                                int end2 = textView.getText().toString().indexOf(getString(R.string.text_new_guide_cut2_end));
                                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_main_yellow)), start1, end1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_main_yellow)), start2, end2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                textView.setText(spannableString);
                            }
                        }))
                .addGuidePage(GuidePage.newInstance()
                        //.addHighLight(ivNewGuideStep3)
                        .addHighLightWithOptions(ivNewGuideStep3, HighLight.Shape.CIRCLE, HighlightOptionsUtils.createTransparentOptions())
                        .setEverywhereCancelable(false)//是否点击任意位置消失引导页
                        .setLayoutRes(R.layout.view_guide3, R.id.textView9)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                TextView textView2 = view.findViewById(R.id.textView2);
                                TextView textView9 = view.findViewById(R.id.textView9);
                                SpannableString spannableString = new SpannableString(textView2.getText().toString());
                                int end1 = textView2.getText().toString().indexOf(getString(R.string.text_new_guide_cut3_end));
                                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_main_yellow)), 0, end1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                textView2.setText(spannableString);
                                if (height <= 1920) {
                                    int xx =0;
                                    if (height<=1600){
                                        xx = (1920 - height) /30;
                                    }else {
                                        xx = (1920 - height) / 2;
                                    }

                                    LinearLayout.LayoutParams layoutParams002 = (LinearLayout.LayoutParams) textView9.getLayoutParams();
                                    layoutParams002.setMargins(0, 0, 0, 160 - xx);
                                    textView9.setLayoutParams(layoutParams002);
                                    LinearLayout.LayoutParams layoutParams003 = (LinearLayout.LayoutParams) textView2.getLayoutParams();
                                    layoutParams003.setMargins(0, 0, 0, 130 - xx);
                                    textView2.setLayoutParams(layoutParams003);
//                                    LinearLayout.LayoutParams layoutParams003 = (LinearLayout.LayoutParams) textView4.getLayoutParams();
//                                    layoutParams003.setMargins(0,0,0,500);
//                                    textView4.setLayoutParams(layoutParams003);
                                }
                            }
                        }))
                .addGuidePage(GuidePage.newInstance()
                        //.addHighLight(ivNewGuideStep3)
                        //.addHighLight(new RectF(30, height / 3 + 156, width - 30, (int) (height * 112 / 261 + 162.4)), HighLight.Shape.ROUND_RECTANGLE, 20)
                        .addHighLightWithOptions(ivNewGuideStep4, HighLight.Shape.ROUND_RECTANGLE, 20,
                                -10, HighlightOptionsUtils.createTransparentOptions())
                        .setEverywhereCancelable(false)//是否点击任意位置消失引导页
                        .setLayoutRes(R.layout.view_guide4, R.id.textView9)
                        .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                TextView textView2 = view.findViewById(R.id.textView2);
                                TextView textView9 = view.findViewById(R.id.textView9);
                                ImageView iv_indicator = view.findViewById(R.id.iv_indicator);
                                SpannableString spannableString = new SpannableString(textView2.getText().toString());
                                int end1 = textView2.getText().toString().indexOf(getString(R.string.text_new_guide_cut4_end));
                                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_main_yellow)), 0, end1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                textView2.setText(spannableString);
                                if (height <= 1920) {
                                    int xx =0;
                                    if (height<=1600){
                                        xx = (1920 - height) /30;
                                    }else {
                                        xx = (1920 - height) / 2;
                                    }
                                    LinearLayout.LayoutParams layoutParams002 = (LinearLayout.LayoutParams) textView9.getLayoutParams();
                                    layoutParams002.setMargins(0, 0, 0, 100);
                                    textView9.setLayoutParams(layoutParams002);
                                    LinearLayout.LayoutParams layoutParams003 = (LinearLayout.LayoutParams) textView2.getLayoutParams();
                                    layoutParams003.setMargins(0, 0, 0, 130 - xx);
                                    textView2.setLayoutParams(layoutParams003);
                                    LinearLayout.LayoutParams layoutParams004 = (LinearLayout.LayoutParams) iv_indicator.getLayoutParams();
                                    layoutParams004.setMargins(0, 0, 0, 50 - xx);
                                    iv_indicator.setLayoutParams(layoutParams004);
                                }
                            }
                        }))
                .show();

        ViewGroup.LayoutParams layoutParams = ivNewGuideStep4.getLayoutParams();
        layoutParams.height = 253;
        ivNewGuideStep4.setLayoutParams(layoutParams);
        LogUtil.i(TAG, (height / 3 + 156) + "---------------showGuide: 我们看计算的高度是？" + (height / 3 + 356) + "原始高度是：" + height);
    }

    private void initRecycleView() {
        mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);//不隐藏的。
//        for (int i = 0; i < 10; i++) {
//            mWalletBeans.addAll(WalletInfoManager.getWalletInfoNoHidden(false));
//        }
        rcvHomeWallet.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        homeWalletAdapter = new HomeWalletAdapter(R.layout.layout_adapter_home_wallet_for_shadow, mWalletBeans);
        rcvHomeWallet.setAdapter(homeWalletAdapter);
        adapterAddHeader();
        //刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
           // initWallet();
            service.getWalletBalance();
        });

        mllManager = new LinearLayoutManager(getActivity());
        rcvHomeWallet.setLayoutManager(mllManager);
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), Constant.FONT_PATH);
        showHeadData();
    }

    private void adapterAddHeader() {
        fragment_wallet_header = getLayoutInflater().inflate(R.layout.fragment_wallet_info_header, (ViewGroup) rcvHomeWallet.getParent(), false);
        homeWalletAdapter.addHeaderView(fragment_wallet_header);
        tvFiatBalance = fragment_wallet_header.findViewById(R.id.tv_fiat_balance);
        ivAddWallet = fragment_wallet_header.findViewById(R.id.iv_add_wallet);
        tvCurrencySavingTag = fragment_wallet_header.findViewById(R.id.tv_currency_saving_tag);
        tvFiatHide = fragment_wallet_header.findViewById(R.id.tv_fiat_hide);
        ivBalanceHide = fragment_wallet_header.findViewById(R.id.iv_balance_hide);

        ivAddWallet = fragment_wallet_header.findViewById(R.id.iv_add_wallet);
        btnTransactionSend = fragment_wallet_header.findViewById(R.id.btn_transaction_send);
        ivTransactionSend = fragment_wallet_header.findViewById(R.id.iv_transaction_send);
        btnTransactionReceive = fragment_wallet_header.findViewById(R.id.btn_transaction_receive);
        ivTransactionReceive = fragment_wallet_header.findViewById(R.id.iv_transaction_receive);
        ivHideWallet = fragment_wallet_header.findViewById(R.id.iv_hide_wallet);
        ivNewGuideStep = fragment_wallet_header.findViewById(R.id.iv_new_guide_step);
        ivNewGuideStep2 = fragment_wallet_header.findViewById(R.id.iv_new_guide_step2);

        ivAddWallet.setOnClickListener(this);
        ivBalanceHide.setOnClickListener(this);

        btnTransactionSend.setOnClickListener(this);
        ivTransactionSend.setOnClickListener(this);
        btnTransactionReceive.setOnClickListener(this);
        ivTransactionReceive.setOnClickListener(this);
        ivHideWallet.setOnClickListener(this);
    }

    private void showHeadData() {
        double wallet_balance = 0;
        double fiat_rate = walletInfo.getCoin_CNYPrice();
        double fait_balance = 0;
        UserInformation userInformation = UserInfoManager.getUserInfo();

        for (int i = 0; i < mWalletBeans.size(); i++) {
            WalletInfo walletInfo = mWalletBeans.get(i);
            String wallet_id = walletInfo.getWallet_id();
            List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(wallet_id);
            for (int j = 0; j < coinFrWalletIds.size(); j++) {
                wallet_balance = wallet_balance + coinFrWalletIds.get(j).getCoin_totalAmount();
                if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
                    fait_balance = coinFrWalletIds.get(j).getCoin_totalAmount() * walletInfo.getCoin_USDPrice() + fait_balance;
                } else {
                    fait_balance = coinFrWalletIds.get(j).getCoin_totalAmount() * walletInfo.getCoin_CNYPrice() + fait_balance;
                }
            }
        }
        //tvBtcBalance.setText("≈" + DataReshape.doubleBig(wallet_balance, 8) + " BTC");

        LogUtil.i(TAG, walletInfo.toString() + "-----showHeadData: 我们计算出来的余额时？" + fait_balance + "有几条数据:" + mWalletBeans.size());
        if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
            fiat_rate = walletInfo.getCoin_USDPrice();
            tvFiatBalance.setText(DataReshape.doubleBig(fait_balance, 2));
            tvFiatBalance.setTypeface(typeFace);
            double froze_num = 0;
            String fiat_str = DataReshape.doubleBig(fait_balance, 2);
            //tvAvailableCapital.setText("$≈" + (Double.valueOf(fiat_str.replace("$", "")) - froze_num));
            //  tvFrozenCapital.setText("$≈" + DataReshape.doubleBig(froze_num, 2));
            tvCurrencySavingTag.setText(getString(R.string.text_total_property) + "($)");
        } else {
            tvFiatBalance.setText(DataReshape.doubleBig(fait_balance, 2));
            tvFiatBalance.setTypeface(typeFace);
            double froze_num = 0;
            String fiat_str = DataReshape.doubleBig(fait_balance, 2);
            //  tvAvailableCapital.setText("￥≈" + (Double.valueOf(fiat_str.replace("$", "")) - froze_num));
            //   tvFrozenCapital.setText("￥≈" + DataReshape.doubleBig(froze_num, 2));
            tvCurrencySavingTag.setText(getString(R.string.text_total_property) + "(￥)");
        }
    }

    @Override
    protected void initData() {
      //  initWallet();
    }

    boolean isFresh;
    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.DELETE_CREATE_COIN_UPDATE) {
            if (!isFresh){
                isFresh=true;
                mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);
                homeWalletAdapter.setNewData(mWalletBeans);
            }
        } else if (event.getType() == EventMessage.HIDDEN_WALLET_UPDATE) {
            if (!isFresh){
                isFresh=true;
                mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);
                homeWalletAdapter.setNewData(mWalletBeans);
            }
        } else if (event.getType() == EventMessage.DELETE_IMPORT_COIN_UPDATE) {
            if (!isFresh){
                isFresh=true;
                mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);
                homeWalletAdapter.setNewData(mWalletBeans);
            }
        } else if (event.getType() == EventMessage.NEW_COIN_UPDATE) {
            if (!isFresh){
                isFresh=true;
                mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);
                homeWalletAdapter.setNewData(mWalletBeans);
            }
        }else if (event.getType()==EventMessage.HOME_WALLET_BALANCE_UPDATE){
            if (!isFresh){
                isFresh=true;
                mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);
                homeWalletAdapter.setNewData(mWalletBeans);
            }
            refreshLayout.finishRefresh();
        }
        new Handler().postDelayed(() -> {
            isFresh=false;
        },2000);
        showHeadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        service.unbindService(conn);
    }

    public void hiddenPopWindow() {
        if (mPopWindowTop != null) {
            mPopWindowTop.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_wallet:
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;//设置阴影透明度
                getActivity().getWindow().setAttributes(lp);

                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_share_select, null);
                if (mPopWindowTop!=null&&mPopWindowTop.isShowing()){
                    mPopWindowTop.dismiss();
                }
                mPopWindowTop = new PopupWindow(contentView);
                //130和112是设计尺寸。
                mPopWindowTop.setWidth(ScreenUtils.dip2px(getActivity(), 130));
                mPopWindowTop.setHeight(ScreenUtils.dip2px(getActivity(), 112));

                TextView tv_share_facebook = contentView.findViewById(R.id.tv_import_wallet);
                TextView tv_share_other = contentView.findViewById(R.id.tv_wallet_manage);

                tv_share_facebook.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), VerifySecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG, getString(R.string.tittle_import_wallet));
                    startActivity(intent);
                    mPopWindowTop.dismiss();
                });
                tv_share_other.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), WalletManageActivity.class);
                    startActivity(intent);
                    mPopWindowTop.dismiss();
                });
                //130是pop的宽度，10是整体右边距，15是view控件的宽度。
                mPopWindowTop.showAsDropDown(view, -ScreenUtils.dip2px(getActivity(), 130 - 10 - 15), 0);

                mPopWindowTop.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                    lp1.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp1);
                });

                mPopWindowTop.setOutsideTouchable(true);

                break;
            case R.id.iv_balance_hide:
                if (isHideBalance) { //显示
                    //tvBtcBalance.setVisibility(View.VISIBLE);
                    tvFiatBalance.setVisibility(View.VISIBLE);
                    // tvAvailableCapital.setVisibility(View.VISIBLE);
                    //tvFrozenCapital.setVisibility(View.VISIBLE);

                    tvFiatHide.setVisibility(View.GONE);

                    GlideUtils.loadResourceImage(getActivity(), R.mipmap.ic_open_white_trans_eye, ivBalanceHide);
                } else {  //隐藏
                    // tvBtcBalance.setVisibility(View.INVISIBLE);
                    tvFiatBalance.setVisibility(View.INVISIBLE);
                    // tvAvailableCapital.setVisibility(View.INVISIBLE);
                    // tvFrozenCapital.setVisibility(View.INVISIBLE);

                    tvFiatHide.setVisibility(View.VISIBLE);
                    GlideUtils.loadResourceImage(getActivity(), R.mipmap.ic_close_white_trans_eye, ivBalanceHide);
                }
                isHideBalance = !isHideBalance;
                break;
            case R.id.iv_hide_wallet:
                Intent intent = new Intent(getActivity(), WalletHiddenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_transaction_send:
            case R.id.iv_transaction_send:
                intent = new Intent(getActivity(), SelectCoinToTransActivity.class);
                intent.putExtra(Constants.INTENT_PUT_TRANSACTION_TYPE, Constant.TRANSACTION_TYPE_SEND);
                startActivity(intent);

                break;
            case R.id.btn_transaction_receive:
            case R.id.iv_transaction_receive:
                intent = new Intent(getActivity(), SelectCoinToTransActivity.class);
                intent.putExtra(Constants.INTENT_PUT_TRANSACTION_TYPE, Constant.TRANSACTION_TYPE_RECEIVE);
                startActivity(intent);
                break;
        }
    }
}

