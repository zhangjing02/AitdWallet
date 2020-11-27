package com.tianqi.aitdwallet.ui.activity.address;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.tool.ScanActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.ContactsInfo;
import com.tianqi.baselib.dbManager.ContactsInfoManager;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.eventbus.EventMessage;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateNewAddressActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_coin_address_name_tag)
    TextView tvCoinAddressNameTag;
    @BindView(R.id.et_input_address_name)
    EditText etInputAddressName;
    @BindView(R.id.tv_coin_address_tag)
    TextView tvCoinAddressTag;
    @BindView(R.id.et_input_address)
    EditText etInputAddress;
    @BindView(R.id.btn_transaction_send)
    TextView btnTransactionSend;
    private String coinName;
    private int resource_id;

    @Override
    protected int getContentView() {
        return R.layout.activity_create_new_address;
    }

    @Override
    protected void initView() {
        getToolBar();
        coinName= getIntent().getStringExtra(Constants.INTENT_PUT_TAG);
        resource_id= getIntent().getIntExtra(Constants.INTENT_PUT_RESOURCE,-1);
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_create_new_address);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnCollect.setVisibility(View.VISIBLE);
        btnCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_scan));
    }

    @Override
    protected void initData() {
        etInputAddressName.addTextChangedListener(textWatcher);
        etInputAddress.addTextChangedListener(textWatcher);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(etInputAddressName.getText().toString()) && !TextUtils.isEmpty(etInputAddress.getText().toString())) {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
            } else {
                btnTransactionSend.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
            }
        }
    };

    @Override
    public void onDataSynEvent(EventMessage event) {
        if (event.getType() == EventMessage.SCAN_EVENT) {
            etInputAddress.setText(event.getMsg());
            etInputAddress.setSelection(event.getMsg().length());
        }
    }

    @OnClick({R.id.btn_collect, R.id.btn_transaction_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivityForResult(intent, 25);
                break;
            case R.id.btn_transaction_send:
                if (judgeSelectInput()){
                    switch (coinName){
                        case Constant.TRANSACTION_COIN_NAME_BTC:
                        case Constant.TRANSACTION_COIN_NAME_USDT_OMNI:
                            if (etInputAddress.getText().toString().trim().length()!=34){
                                ToastUtil.showToast(this,getString(R.string.notice_address_length_error));
                                return;
                            }else if (!isBTCValidAddress(etInputAddress.getText().toString())){
                                ToastUtil.showToast(this,getString(R.string.notice_address_format_error));
                                return;
                            }
                            break;
                        case Constant.TRANSACTION_COIN_NAME_ETH:
                        case Constant.TRANSACTION_COIN_NAME_USDT_ERC20:
                            if (etInputAddress.getText().toString().trim().length()!=42){
                                ToastUtil.showToast(this,getString(R.string.notice_address_length_error));
                                return;
                            }if (!isValidAddress(etInputAddress.getText().toString())||!isETHValidAddress(etInputAddress.getText().toString())){
                                ToastUtil.showToast(this,getString(R.string.notice_address_format_error));
                                return;
                            }
                            break;
                        default:
                            break;
                    }
                    ContactsInfo contactsInfo=new ContactsInfo();
                    contactsInfo.setContactsID(coinName+etInputAddressName.getText().toString().trim()+etInputAddress.getText().toString().trim());
                    contactsInfo.setContactsName(etInputAddressName.getText().toString().trim());
                    contactsInfo.setContactsCoinName(coinName) ;
                    contactsInfo.setContactsCoinAddress(etInputAddress.getText().toString().trim());
                    contactsInfo.setCoinResourceId(resource_id);
                    ContactsInfoManager.insertOrUpdate(contactsInfo);
                    ToastUtil.showToast(this,getString(R.string.notice_create_success));

                    EventMessage eventMessage=new EventMessage();
                    eventMessage.setType(EventMessage.ADD_ADDRESS_UPDATE);
                    EventBus.getDefault().post(eventMessage);
                    finish();
                }
                break;
        }
    }

    /**
     * @return 判断输入是否合法
     */
    private boolean judgeSelectInput() {
        List<ContactsInfo> coinFrIdInfo = ContactsInfoManager.getCoinFrIdInfo(coinName + etInputAddressName.getText().toString().trim() + etInputAddress.getText().toString().trim());
        if (TextUtils.isEmpty(etInputAddressName.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_input_address_name));
            return false;
        } else if (TextUtils.isEmpty(etInputAddress.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_input_address_content));
            return false;
        }else if (coinFrIdInfo!=null&&coinFrIdInfo.size()>0){
            ToastUtil.showToast(this,getString(R.string.notice_do_not_repeat));
            return false;
        }
        return true;
    }


    public static boolean isETHValidAddress(String input) {
        if (!input.startsWith("0x"))
            return false;
        return WalletUtils.isValidAddress(input);
    }


    public static boolean isValidAddress(String input){
        String cleanHexInput = Numeric.cleanHexPrefix(input);
        try{
            Numeric.toBigIntNoPrefix(cleanHexInput);
        }catch(NumberFormatException e){
            return false;
        }

        return cleanHexInput.length() == 40;
    }


    public static boolean isBTCValidAddress(String input) {
        try {
            NetworkParameters networkParameters = MainNetParams.get();
            Address address = Address.fromBase58(networkParameters, input);
            if (address != null)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }
}