package com.tianqi.aitdwallet.ui.activity.wallet.record;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tianqi.aitdwallet.R;
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
import com.tianqi.baselib.rxhttp.bean.GetTxDetailBean;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.digital.DataReshape;
import com.tianqi.baselib.utils.display.GlideUtils;
import com.tianqi.baselib.utils.display.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class TransactionDetailActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_transaction_state)
    ImageView ivTransactionState;
    @BindView(R.id.tv_transaction_state)
    TextView tvTransactionState;
    @BindView(R.id.tv_transaction_amount)
    TextView tvTransactionAmount;
    //    @BindView(R.id.tv_transaction_amount_unit)
//    TextView tvTransactionAmountUnit;
    @BindView(R.id.tv_transaction_fiat)
    TextView tvTransactionFiat;
    @BindView(R.id.tv_payment_address_tag)
    TextView tvPaymentAddressTag;
    @BindView(R.id.tv_payment_address)
    TextView tvPaymentAddress;
    @BindView(R.id.tv_payment_address_copy)
    ImageView tvPaymentAddressCopy;
    @BindView(R.id.tv_receive_address_tag)
    TextView tvReceiveAddressTag;
    @BindView(R.id.tv_receive_address)
    TextView tvReceiveAddress;
    @BindView(R.id.tv_receive_address_copy)
    ImageView tvReceiveAddressCopy;
    @BindView(R.id.tv_miner_tag)
    TextView tvMinerTag;
    @BindView(R.id.tv_miner_fee)
    TextView tvMinerFee;
    @BindView(R.id.tv_chain_record_tag)
    TextView tvChainRecordTag;
    @BindView(R.id.tv_chain_record)
    TextView tvChainRecord;
    @BindView(R.id.tv_chain_record_copy)
    ImageView tvChainRecordCopy;
    @BindView(R.id.tv_arrive_time_tag)
    TextView tvArriveTimeTag;
    @BindView(R.id.tv_arrive_time)
    TextView tvArriveTime;
    @BindView(R.id.tv_transaction_note)
    TextView tvTransactionNote;
    @BindView(R.id.tv_block_height_tag)
    TextView tvBlockHeightTag;
    @BindView(R.id.tv_block_height)
    TextView tvBlockHeight;
    private Calendar calendar;
    private UserInformation userInformation;
    private TransactionRecord txFrId;

    @Override
    protected int getContentView() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    protected void initView() {
        getToolBar();
    }


    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_transaction_detail);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.text_main_blue));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_white_back);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        calendar=Calendar.getInstance();
        userInformation= UserInfoManager.getUserInfo();
        String tx_id = getIntent().getStringExtra(Constants.INTENT_PUT_TRANSACTION_ID);
        String coin_id = getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
        Log.i("ttttttttt", "initData: 我们看一下这个交易id是？"+tx_id);
        txFrId = TransactionRecordManager.getTxFrId(tx_id);
        if (!TextUtils.isEmpty(txFrId.getRemark())){
            tvTransactionNote.setText(txFrId.getRemark()+"");
        }
        String tx_address = getIntent().getStringExtra(Constants.TRANSACTION_COIN_ADDRESS);
        int tx_type = getIntent().getIntExtra(Constants.INTENT_PUT_TRANSACTION_TYPE,-1);

        CoinInfo mainCoinFrCoinId = CoinInfoManager.getMainCoinFrCoinId(coin_id);
        WalletInfo wallet_btc = WalletInfoManager.getWalletFrName(mainCoinFrCoinId.getWallet_id());

        String coin_type_params=null;
        if (mainCoinFrCoinId.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_BTC)) {
            coin_type_params = "btc";
        } else if (mainCoinFrCoinId.getCoin_name().equals(Constant.TRANSACTION_COIN_NAME_USDT_OMNI)) {
            coin_type_params = "usdt";
        }
        if (!TextUtils.isEmpty(coin_type_params)){
            Map<String, Object> map = new HashMap<>();
            map.put("apikey", "AnqHS6Rs2WX0hwFXlrv");
            RetrofitFactory.getInstence(this).API()
                    .getTxDetail(coin_type_params,tx_id,map).compose(RxHelper.io_main())
                    .subscribe(new BaseObserver<GetTxDetailBean>(this) {
                        @Override
                        public void onSuccess(GetTxDetailBean data, String msg) {
                            Log.i("ttttttttttttttt", "onSuccess: "+tx_type);
                            if (data!=null){
                                tvMinerFee.setText( data.getFee());
                                if (tx_type== Constant.TRANSACTION_TYPE_SEND){
                                    tvReceiveAddress.setText(data.getOutputs().get(0).getAddress());
                                    tvPaymentAddress.setText(mainCoinFrCoinId.getCoin_address());
                                    double receive_value=0;
                                    for (int i = 0; i <data.getOutputs().size() ; i++) {
                                        if (!data.getOutputs().get(i).getAddress().equals(mainCoinFrCoinId.getCoin_address())){
                                            receive_value=receive_value+Double.valueOf(data.getOutputs().get(i).getValue());
                                        }
                                    }
                                    //如果是转出，但是在output中没找到不是自己的地址，则应该是自己给自己转账，我们就拿第一比输出作为转金额。
                                    if (receive_value==0){
                                        if (data.getOutputs().size()>0){
                                            receive_value=Double.valueOf(data.getOutputs().get(0).getValue());
                                        }
                                    }

                                    SpannableString spannableString = new SpannableString(DataReshape.doubleIntBig(receive_value,8) +" "+ txFrId.getUnit());
                                    //  SpannableString spannableString = new SpannableString(34.256478222555+" BTC");
                                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
                                    spannableString.setSpan(new AbsoluteSizeSpan(DensityUtil.dp2px(10.0f)), spannableString.toString().indexOf(txFrId.getUnit()), spannableString.length(),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    tvTransactionAmount.setText(spannableString);

                                    if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
                                        tvTransactionFiat.setText("≈ $ " + DataReshape.double2int(wallet_btc.getCoin_USDPrice() * receive_value,2));
                                    } else {
                                        tvTransactionFiat.setText("≈ ¥ " + DataReshape.double2int(wallet_btc.getCoin_CNYPrice() * receive_value,2));
                                    }

                                    tvChainRecord.setText("https://btc.com/"+mainCoinFrCoinId.getCoin_address());
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

                                    calendar.setTimeInMillis(data.getTime()*1000l);
                                    tvArriveTime.setText(format.format(calendar.getTime()));
                                    if ( data.getConfirmations()<=2){
                                        tvTransactionState.setText(R.string.text_transaction_state_waiting);
                                    }else {
                                        tvTransactionState.setText(R.string.text_transaction_state_success);
                                    }
                                    tvBlockHeight.setText(data.getHeight()+"");

                                }else if (tx_type== Constant.TRANSACTION_TYPE_RECEIVE){
                                    tvPaymentAddress.setText(data.getInputs().get(0).getAddress());
                                    tvReceiveAddress.setText(mainCoinFrCoinId.getCoin_address());
                                    //  double receive_value=Double.valueOf(data.getInputs().get(0).getValue());
                                    double receive_value=0;
                                    for (int i = 0; i <data.getOutputs().size() ; i++) {
                                        if (data.getOutputs().get(i).getAddress()!=null){
                                            if (data.getOutputs().get(i).getAddress().equals(mainCoinFrCoinId.getCoin_address())){
                                                receive_value=receive_value+Double.valueOf(data.getOutputs().get(i).getValue());
                                            }
                                        }

                                    }
                                    SpannableString spannableString = new SpannableString(DataReshape.doubleIntBig(receive_value,8) +" "+ txFrId.getUnit());
                                    //  SpannableString spannableString = new SpannableString(34.256478222555+" BTC");
                                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.textLightGrey));
                                    spannableString.setSpan(new AbsoluteSizeSpan(DensityUtil.dp2px(10.0f)), spannableString.toString().indexOf(txFrId.getUnit()), spannableString.length(),
                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    tvTransactionAmount.setText(spannableString);

                                    if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
                                        tvTransactionFiat.setText("≈ ¥ " + DataReshape.double2int(wallet_btc.getCoin_USDPrice() * receive_value,2));
                                    } else {
                                        tvTransactionFiat.setText("≈ ¥ " + DataReshape.double2int(wallet_btc.getCoin_CNYPrice() * receive_value,2));
                                    }

                                    tvChainRecord.setText("https://btc.com/"+mainCoinFrCoinId.getCoin_address());
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

                                    calendar.setTimeInMillis(data.getTime()*1000l);
                                    tvArriveTime.setText(format.format(calendar.getTime()));
                                    if ( data.getConfirmations()<=2){
                                        tvTransactionState.setText(R.string.text_transaction_state_waiting);
                                    }else {
                                        tvTransactionState.setText(R.string.text_receive_money);
                                    }
                                    tvBlockHeight.setText(data.getHeight()+"");
                                }
                            }
                            //fdfd
                        }
                        @Override
                        protected void onFailure(int code, String msg) {
                            ToastUtil.showToast(TransactionDetailActivity.this,msg);
                        }
                    });
        }else {  //如果不是btc和usdt-omni就不掉接口了，自己数据库的用来呈现。
            switch (txFrId.getStatus()) {
                case 1:
                    tvTransactionState.setText(R.string.text_transaction_state_fail);
                    break;
                case 2:
                    tvTransactionState.setText(R.string.text_transaction_state_waiting);
                    break;
                case 0:
                default:
                    tvTransactionState.setText(R.string.text_transaction_state_success);
                    break;
            }

            tvTransactionAmount.setText(DataReshape.doubleIntBig(txFrId.getAmount(),8));
            if (userInformation.getFiatUnit().equals(Constants.FIAT_USD)) {
                tvTransactionFiat.setText("≈ ¥ " + DataReshape.double2int(wallet_btc.getCoin_USDPrice() * txFrId.getAmount(),2));
            } else {
                tvTransactionFiat.setText("≈ ¥ " + DataReshape.double2int(wallet_btc.getCoin_CNYPrice() * txFrId.getAmount(),2));
            }

            if (txFrId.getTransType()==Constant.TRANSACTION_TYPE_SEND){
                tvPaymentAddress.setText(txFrId.getAddress());
                tvReceiveAddress.setText(txFrId.getTargetAddress());
            }else if (txFrId.getTransType()==Constant.TRANSACTION_TYPE_RECEIVE){
                tvPaymentAddress.setText(txFrId.getTargetAddress());
                tvReceiveAddress.setText(txFrId.getAddress());
            }

            tvMinerFee.setText(DataReshape.doubleBig(txFrId.getMiner_fee(),8));
            tvChainRecord.setText("https://btc.com/"+mainCoinFrCoinId.getCoin_address());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

            tvArriveTime.setText(txFrId.getTimeStr());
            tvTransactionNote.setText(txFrId.getRemark());
            tvBlockHeight.setText(txFrId.getBlock_no());
        }
    }

    @OnClick({R.id.tv_payment_address_copy, R.id.tv_receive_address_copy, R.id.tv_chain_record_copy,R.id.tv_chain_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_payment_address_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvPaymentAddress.getText().toString());
                ToastUtil.showToast(this,getString(R.string.notice_copy_success));
                break;
            case R.id.tv_receive_address_copy:
                cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvReceiveAddress.getText().toString());
                ToastUtil.showToast(this,getString(R.string.notice_copy_success));
                break;
            case R.id.tv_chain_record_copy:
                cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tvChainRecord.getText().toString());
                ToastUtil.showToast(this,getString(R.string.notice_copy_success));
                break;
            case R.id.tv_chain_record:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(tvChainRecord.getText().toString());
                intent.setData(content_url);
                startActivity(intent);
                break;
        }
    }
}