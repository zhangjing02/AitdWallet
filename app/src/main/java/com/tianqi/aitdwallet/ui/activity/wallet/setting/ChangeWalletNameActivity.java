package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangeWalletNameActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.tv_change_name_notice)
    TextView tvChangeNameNotice;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;
    private String wallet_name,coin_id;
    private CoinInfo coinInfo;
    @Override
    protected int getContentView() {
        return R.layout.activity_change_wallet_name;
    }

    @Override
    protected void initView() {
        getToolBar();
        wallet_name=getIntent().getStringExtra(Constants.TRANSACTION_COIN_NAME);
        coin_id=getIntent().getStringExtra(Constants.INTENT_PUT_COIN_ID);
        coinInfo = CoinInfoManager.getMainCoinFrCoinId(coin_id);
        etInputPassword.setText(coinInfo.getAlias_name());
        etInputPassword.setSelection(coinInfo.getAlias_name().length());
        etInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()>=1&&editable.toString().length()<=12){
                    btnConfirm.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                }else {
                    btnConfirm.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                }
            }
        });
    }


    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_change_wallet_name);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {
    }

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (etInputPassword.getText().toString().length()>=1&&etInputPassword.getText().toString().length()<=12){
            coinInfo.setAlias_name(etInputPassword.getText().toString());
            CoinInfoManager.insertOrUpdate(coinInfo);
            ToastUtil.showToast(this,getString(R.string.notice_change_name_success));
            EventMessage eventMessage=new EventMessage();
            eventMessage.setType(EventMessage.COIN_NAME_UPDATE);
            eventMessage.setMsg(coinInfo.getCoin_name());
            EventBus.getDefault().post(eventMessage);
            finish();
        }else if (TextUtils.isEmpty(etInputPassword.getText().toString())){
            ToastUtil.showToast(this,getString(R.string.notice_name_null));
        }else {
            ToastUtil.showToast(this,getString(R.string.notice_name_length_error));
        }
    }
}