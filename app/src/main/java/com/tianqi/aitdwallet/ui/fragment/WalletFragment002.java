package com.tianqi.aitdwallet.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.HomeWalletAdapter;
import com.tianqi.aitdwallet.ui.activity.wallet.property.SelectCoinToTransActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.VerifySecurityPsdActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.WalletHiddenActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.WalletManageActivity;
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
import com.tianqi.baselib.rxhttp.RetrofitFactory;
import com.tianqi.baselib.rxhttp.base.BaseObserver;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.rxhttp.bean.GetListUnspentBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ScreenUtils;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class WalletFragment002 extends BaseFragment {

//    @BindView(R.id.tv_wallet_tittle)
//    TextView tvWalletTittle;
//    @BindView(R.id.iv_add_wallet)
//    ImageView ivAddWallet;
//    @BindView(R.id.tv_currency_saving_tag)
//    TextView tvCurrencySavingTag;
//    @BindView(R.id.iv_balance_hide)
//    ImageView ivBalanceHide;
//    @BindView(R.id.tv_fiat_balance)
//    TextView tvFiatBalance;
//    @BindView(R.id.tv_btc_balance)
//    TextView tvBtcBalance;
//    //    @BindView(R.id.btn_transaction_receive)
////    TextView tvAvailableCapitalTag;
////    @BindView(R.id.tv_available_capital)
////    TextView tvAvailableCapital;
////    @BindView(R.id.btn_transaction_send)
////    TextView tvFrozenCapitalTag;
//    //    @BindView(R.id.tv_frozen_capital)
////    TextView tvFrozenCapital;
//    @BindView(R.id.tv_fiat_hide)
//    TextView tvFiatHide;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
//    @BindView(R.id.iv_transaction_send)
//    ImageView ivTransactionSend;
//    @BindView(R.id.iv_transaction_receive)
//    ImageView ivTransactionReceive;
//    @BindView(R.id.tv_property_tag)
//    TextView tvPropertyTag;
//    @BindView(R.id.iv_hide_wallet)
//    ImageView ivHideWallet;
//    @BindView(R.id.iv_new_guide_step)
//    ImageView ivNewGuideStep;
//    @BindView(R.id.iv_new_guide_step4)
//    ImageView ivNewGuideStep4;
//    @BindView(R.id.iv_new_guide_step3)
//    ImageView ivNewGuideStep3;
//    @BindView(R.id.iv_new_guide_step2)
//    ImageView ivNewGuideStep2;
    @BindView(R.id.tv_hide_available_capital)
    TextView tvHideAvailableCapital;
    @BindView(R.id.btn_transaction_send)
    TextView btnTransactionSend;
    @BindView(R.id.tv_hide_frozen_capital)
    TextView tvHideFrozenCapital;
    @BindView(R.id.layout_wallet)
    RelativeLayout layoutWallet;

    private LinearLayoutManager mllManager;

    @BindView(R.id.rcv_home_wallet)
    RecyclerView rcvHomeWallet;

    private HomeWalletAdapter homeWalletAdapter;
    private static final String TAG = "WalletFragment";
    private double btc_account_balance = 0, eth_account_balance = 0;
    private boolean isHttpErro;
    private ImageView mIvAddWallet;
    private List<CoinInfo> mMessageBeans;
    private List<WalletInfo> mWalletBeans;
    private WalletInfo walletInfo;
    private int post_usdt_count;
    private Typeface typeFace;
    private boolean isHideBalance;

    private PopupWindow mPopWindow;
    private int new_guide_step_index;
    private int btc_quest_count, usdt_quest_count;
    private List<CoinInfo> allBtccCoinInfos, allusdtCoinInfos;
    private List<Disposable> disposableList;
    private View fragment_wallet_header;
    private TextView tvFiatBalance;
    private ImageView ivAddWallet;
    private TextView tvCurrencySavingTag;
    private TextView tvFiatHide;
    private ImageView ivBalanceHide;
    private ImageView ivNewGuideStep,ivNewGuideStep4,ivNewGuideStep2,ivNewGuideStep3;
   // private boolean is_request_btc,is_request_usdt;

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
            shotNoticeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    PrefUtils.setInt(getActivity(), PrefUtils.FIRST_START_APP, 2);
                }
            });
            shotNoticeDialog.show();
        }
    }

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
        Log.i(TAG, "showGuide: 我们看屏幕的高度是？" + height);

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
                        Log.i(TAG, new_guide_step_index + "-----onShowed: 002我们看到了啥？" + controller.toString());
                        ivNewGuideStep.setVisibility(View.GONE);
                        tvFiatBalance.setVisibility(View.VISIBLE);
                        ivNewGuideStep4.setVisibility(View.GONE);
                        ivNewGuideStep2.setVisibility(View.GONE);
                        rcvHomeWallet.setVisibility(View.VISIBLE);
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
                rcvHomeWallet.setVisibility(View.GONE);
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
                        .setLayoutRes(R.layout.view_guide1, R.id.textView4).setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                            @Override
                            public void onLayoutInflated(View view, Controller controller) {
                                ImageView imageView = view.findViewById(R.id.iv_indicator);
                                TextView textView3 = view.findViewById(R.id.textView3);
                                // TextView textView2 = view.findViewById(R.id.textView2);
                                TextView textView4 = view.findViewById(R.id.textView4);
                                SpannableString spannableString = new SpannableString(textView3.getText().toString());
                                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_main_yellow)), 0, 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                textView3.setText(spannableString);
                                if (height <= 1920) {
//                                    ViewGroup.LayoutParams layoutParams = textView3.getLayoutParams();
//                                    layoutParams.height=60;
//                                    textView3.setLayoutParams(layoutParams);
                                    int xx = (1920 - height) / 3;

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
                                    int xx = (1920 - height) / 2;
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
                        //  .addHighLightWithOptions(ivNewGuideStep4, HighLight.Shape.ROUND_RECTANGLE, 10,10, HighlightOptionsUtils.createTransparentOptions())
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
                                    int xx = (1920 - height) / 2;
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
        Log.i(TAG, (height / 3 + 156) + "---------------showGuide: 我们看计算的高度是？" + (height / 3 + 356) + "原始高度是：" + height);
    }

    private void initRecycleView() {
        mMessageBeans = CoinInfoManager.getCoinInfo();
        mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);//不隐藏的。

//        for (int i = 0; i <20 ; i++) {
//            mWalletBeans.addAll(WalletInfoManager.getWalletInfoNoHidden(false));
//        }
        rcvHomeWallet.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));


        homeWalletAdapter = new HomeWalletAdapter(R.layout.layout_adapter_home_wallet_for_shadow, mWalletBeans);
        rcvHomeWallet.setAdapter(homeWalletAdapter);

        fragment_wallet_header=getLayoutInflater().inflate(R.layout.fragment_wallet_info, (ViewGroup) rcvHomeWallet.getParent(), false);
        homeWalletAdapter.addHeaderView(fragment_wallet_header);
        tvFiatBalance=fragment_wallet_header.findViewById(R.id.tv_fiat_balance);
        ivAddWallet=fragment_wallet_header.findViewById(R.id.iv_add_wallet);
        tvCurrencySavingTag=fragment_wallet_header.findViewById(R.id.tv_currency_saving_tag);
        tvFiatHide=fragment_wallet_header.findViewById(R.id.tv_fiat_hide);
        ivBalanceHide=fragment_wallet_header.findViewById(R.id.iv_balance_hide);


        ivNewGuideStep=fragment_wallet_header.findViewById(R.id.iv_new_guide_step);
        ivNewGuideStep4=fragment_wallet_header.findViewById(R.id.iv_balance_hide);
        ivNewGuideStep2=fragment_wallet_header.findViewById(R.id.iv_balance_hide);
        ivNewGuideStep3=fragment_wallet_header.findViewById(R.id.iv_balance_hide);

        //刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            Log.i(TAG, "onRefresh: 慢慢刷新");
            initWallet();
        });

        mllManager = new LinearLayoutManager(getActivity());
        rcvHomeWallet.setLayoutManager(mllManager);

        typeFace = Typeface.createFromAsset(getActivity().getAssets(), Constant.FONT_PATH);

        showHeadData();
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

        Log.i(TAG, walletInfo.toString() + "-----showHeadData: 我们计算出来的余额时？" + fait_balance + "有几条数据:" + mWalletBeans.size());
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

    }

    @Override
    public void onResume() {
        super.onResume();
        initWallet();
    }

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.DELETE_CREATE_COIN_UPDATE) {
            homeWalletAdapter = new HomeWalletAdapter(R.layout.layout_adapter_home_wallet_for_shadow, mWalletBeans);
            rcvHomeWallet.setAdapter(homeWalletAdapter);
        } else if (event.getType() == EventMessage.HIDDEN_WALLET_UPDATE) {
            mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);
            homeWalletAdapter.setNewData(mWalletBeans);
        } else if (event.getType() == EventMessage.DELETE_IMPORT_COIN_UPDATE) {
            homeWalletAdapter = new HomeWalletAdapter(R.layout.layout_adapter_home_wallet_for_shadow, mWalletBeans);
            rcvHomeWallet.setAdapter(homeWalletAdapter);
        } else if (event.getType() == EventMessage.NEW_COIN_UPDATE) {
            // TODO: 2020/11/2 导入了币种，不知道该更新点啥？
        }
    }

    @SuppressLint("CheckResult")
    private void initWallet() {
        //通过币种的rpc接口信息，给钱包的总金额赋值。
        disposableList = new ArrayList<>();
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            allBtccCoinInfos = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_BTC);
            //先按照顺序，调取btc的余额。如果调取完成，或原数据为空，就走完成逻辑。
            if (allBtccCoinInfos!=null&&allBtccCoinInfos.size()>0){
                for (int i = 0; i < allBtccCoinInfos.size(); i++) {
                    emitter.onNext(i);
                    if (i==allBtccCoinInfos.size()-1){
                        emitter.onComplete(); }
                }
            }else {
                emitter.onComplete();
            }
        }).map(index -> {
             getBtcUtxo(allBtccCoinInfos.get(index));
            return "123";
        }).delay(2, TimeUnit.SECONDS).doOnComplete(() -> {
            //当BTC调完后（但结果未必都返回了，所以要做2S延迟）完成的时候，我们做一个2S的延时，然后去循环调用usdt的余额。
            allusdtCoinInfos = CoinInfoManager.getSpecCoinInfo(Constant.TRANSACTION_COIN_NAME_USDT);
            if (allusdtCoinInfos!=null&&allusdtCoinInfos.size() > 0) {
                usdt_quest_count = 0;
                for (int i = 0; i < allusdtCoinInfos.size(); i++) {
                    // getUsdtBalance(usdtCoinInfo.get(i));
                    getBtcUtxo(allusdtCoinInfos.get(i));
                }
            }
        }).compose(RxHelper.pool_main())
                .subscribe(baseEntity -> {
                    //此处留空，方便后续拓展。
                });
    }

    String coin_type_params = null;

    @SuppressLint("CheckResult")
    private void getBtcUtxo(CoinInfo specCoinInfo) {
        if (specCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            coin_type_params = "btc";
        } else if (specCoinInfo.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT)) {
            coin_type_params = "usdt";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
        RetrofitFactory.getInstence(getActivity()).API()
                .getBtcAddressBalance(coin_type_params, specCoinInfo.getCoin_address(), "1/1", map).compose(RxHelper.io_main())
                .subscribe(new BaseObserver<List<GetListUnspentBean>>(getActivity()) {
                    @Override
                    public void onSuccess(List<GetListUnspentBean> data, String msg) {
                        if (data != null && data.size() > 0) {
                            Log.i(TAG, data.get(0).getReceive() + "-----onSuccess: 001我们看看余额应该是多少？" + data.get(0).getSpend());
                            double xxx = Double.valueOf(data.get(0).getReceive()) + Double.valueOf(data.get(0).getSpend());
                            Log.i(TAG, data.get(0).getReceive() + "-----onSuccess: 002我们看看余额应该是多少？" + xxx);
                            specCoinInfo.setCoin_totalAmount(Double.valueOf(data.get(0).getReceive()) + Double.valueOf(data.get(0).getSpend()));
                            CoinInfoManager.insertOrUpdate(specCoinInfo);

                            if (data.get(0).getNetwork().equals(Constant.COIN_UNIT_BTC)) {
                                btc_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWaletBalance(0);
                            } else if (data.get(0).getNetwork().equals(Constant.COIN_UNIT_USDT)) {
                                usdt_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWaletBalance(1);
                            }
                        } else {
                            if (coin_type_params != null && coin_type_params.equals("btc")) {
                                btc_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWaletBalance(0);
                            } else if (coin_type_params != null && coin_type_params.equals("usdt")) {
                                usdt_quest_count++;
                                //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                                updateWaletBalance(1);
                            }
                        }
                    }
                    @Override
                    protected void onFailure(int code, String msg) {
                        if (coin_type_params != null && coin_type_params.equals("btc")) {
                            btc_quest_count++;
                            //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                            updateWaletBalance(0);
                        } else if (coin_type_params != null && coin_type_params.equals("usdt")) {
                            usdt_quest_count++;
                            //判断是最后一个btc，就去把数据，整合到钱包数据库，并刷新页面。
                            updateWaletBalance(1);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }
                });

    }

    private void updateWaletBalance(int coin_type) {
        if (coin_type == 0) {
            Log.i(TAG, btc_quest_count+"updateWaletBalance: 001更新了btc------");
            if (btc_quest_count >= allBtccCoinInfos.size()) {
                mMessageBeans = CoinInfoManager.getCoinInfo();
                for (int i = 0; i < mWalletBeans.size(); i++) {
                    WalletInfo walletInfo = mWalletBeans.get(i);
                    String wallet_id = walletInfo.getWallet_id();
                    List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(wallet_id);
                    double wallet_balance = 0;
                    for (int j = 0; j < coinFrWalletIds.size(); j++) {
                        wallet_balance = wallet_balance + coinFrWalletIds.get(j).getCoin_totalAmount();
                    }
                    walletInfo.setWalletBalance(wallet_balance);
                    WalletInfoManager.insertOrUpdate(walletInfo);
                }
                Log.i(TAG, "updateWaletBalance: 002更新了btc----");
            }
        } else if (coin_type == 1) {
            Log.i(TAG, usdt_quest_count+"updateWaletBalance: 001更新了usdt-----");
            if (usdt_quest_count >= allusdtCoinInfos.size()) {
                mMessageBeans = CoinInfoManager.getCoinInfo();
                for (int i = 0; i < mWalletBeans.size(); i++) {
                    WalletInfo walletInfo = mWalletBeans.get(i);
                    String wallet_id = walletInfo.getWallet_id();
                    List<CoinInfo> coinFrWalletIds = CoinInfoManager.getCoinFrWalletId(wallet_id);
                    double wallet_balance = 0;
                    for (int j = 0; j < coinFrWalletIds.size(); j++) {
                        wallet_balance = wallet_balance + coinFrWalletIds.get(j).getCoin_totalAmount();
                    }
                    walletInfo.setWalletBalance(wallet_balance);
                    WalletInfoManager.insertOrUpdate(walletInfo);
                }
                Log.i(TAG, "updateWaletBalance: 002更新了usdt----");
            }
        }
        mWalletBeans = WalletInfoManager.getWalletInfoNoHidden(false);
        homeWalletAdapter.setNewData(mWalletBeans);
        refreshLayout.finishRefresh();
        //获取了新的余额，去更新头部的卡片的余额数值。
        showHeadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposableList.size() > 0) {
            for (int i = 0; i < disposableList.size(); i++) {
                disposableList.get(i).dispose();
            }
        }
    }

    @OnClick({R.id.iv_add_wallet, R.id.iv_balance_hide, R.id.iv_hide_wallet, R.id.btn_transaction_send,
            R.id.iv_transaction_send, R.id.btn_transaction_receive, R.id.iv_transaction_receive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_wallet:
                //                Intent intent = new Intent(getActivity(), ImportWalletActivity.class);
//                startActivity(intent);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;//设置阴影透明度
                getActivity().getWindow().setAttributes(lp);

                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_share_select, null);
                PopupWindow mPopWindow = new PopupWindow(contentView);

                //130和112是设计尺寸。
                mPopWindow.setWidth(ScreenUtils.dip2px(getActivity(), 130));
                mPopWindow.setHeight(ScreenUtils.dip2px(getActivity(), 112));
                mPopWindow.setOutsideTouchable(true);
                TextView tv_share_facebook = contentView.findViewById(R.id.tv_import_wallet);
                TextView tv_share_other = contentView.findViewById(R.id.tv_wallet_manage);

                tv_share_facebook.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), VerifySecurityPsdActivity.class);
                    intent.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_IMPORT_WALLET);
                    startActivity(intent);
                    mPopWindow.dismiss();
                });
                tv_share_other.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), WalletManageActivity.class);
                    startActivity(intent);
                    mPopWindow.dismiss();
                });
                //130是pop的宽度，10是整体右边距，15是view控件的宽度。
                mPopWindow.showAsDropDown(view, -ScreenUtils.dip2px(getActivity(), 130 - 10 - 15), 0);

                mPopWindow.setOnDismissListener(() -> {
                    WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                    lp1.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp1);
                });

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

//                VersionUpdateDialog shotNoticeDialog = new VersionUpdateDialog(getActivity(), R.style.MyDialog2);
//                shotNoticeDialog.show();

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
                //Constant.TRANSACTION_TYPE_RECEIVE
                intent = new Intent(getActivity(), SelectCoinToTransActivity.class);
                intent.putExtra(Constants.INTENT_PUT_TRANSACTION_TYPE, Constant.TRANSACTION_TYPE_RECEIVE);
                startActivity(intent);
                break;
        }
    }
}

