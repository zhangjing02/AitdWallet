package com.tianqi.aitdwallet.ui.activity.wallet.initwallet;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.wallet.importwallet.ImportWalletActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.PrivacyTermsWebActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.rxhttp.base.RxHelper;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.Constant;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.digital.AESCipher;
import com.tianqi.baselib.utils.display.ToastUtil;
import com.tianqi.baselib.utils.rxtool.RxToolUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * Create by zhangjing on 2020/8/26.
 * Describe ：设置安全密码
 */

public class SetSecurityPsdActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_psd_notice)
    TextView tvPsdNotice;
    @BindView(R.id.iv_psd_notice)
    ImageView ivPsdNotice;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.iv_psd_show)
    ImageView ivPsdShow;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    //    @BindView(R.id.iv_confirm_psd_show)
//    ImageView ivConfirmPsdShow;
    @BindView(R.id.et_password_reminder)
    EditText etPasswordReminder;
    @BindView(R.id.checkbox_read_term)
    CheckBox checkboxReadTerm;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;

    private static final String TAG = "SetSecurityPsdActivity";
    @BindView(R.id.tv_input_notice_tittle)
    TextView tvInputNoticeTittle;
    @BindView(R.id.tv_input_up_letter_notice)
    TextView tvInputUpLetterNotice;
    @BindView(R.id.tv_input_low_letter_notice)
    TextView tvInputLowLetterNotice;
    @BindView(R.id.tv_input_num_notice)
    TextView tvInputNumNotice;
    @BindView(R.id.tv_input_length_notice)
    TextView tvInputLengthNotice;
    private String build_wallet_type;
    private boolean isUpLetter, isLowLetter, isDigit, isLength, isConfirm;

    @Override
    protected int getContentView() {
        return R.layout.activity_set_security_psd;
    }

    @Override
    protected void initView() {
        getToolBar();
    }

    private void getToolBar() {
        build_wallet_type = getIntent().getStringExtra(Constants.INTENT_PUT_TAG);
        toolbarTitle.setText(build_wallet_type);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() {
        etInputPassword.setOnFocusChangeListener(focusChangeListener);
        etInputPassword.addTextChangedListener(watcher);
        etConfirmPassword.addTextChangedListener(watcher);


        checkboxReadTerm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isUpLetter & isLowLetter & isDigit & isLength && isConfirm && b) {
                    btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                } else {
                    btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                }
            }
        });

    }


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String content = editable.toString();
            isUpLetter = content.matches(Constant.CONTAIN_UP_LETTER_REGEX);
            if (isUpLetter) {
                tvInputUpLetterNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputUpLetterNotice.setCompoundDrawables(drawable, null, null, null);
            } else {
                tvInputUpLetterNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputUpLetterNotice.setCompoundDrawables(drawable, null, null, null);
            }

            isLowLetter = content.matches(Constant.CONTAIN_LOW_LETTER_REGEX);
            if (isLowLetter) {
                tvInputLowLetterNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputLowLetterNotice.setCompoundDrawables(drawable, null, null, null);
            } else {
                tvInputLowLetterNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputLowLetterNotice.setCompoundDrawables(drawable, null, null, null);
            }

            isDigit = content.matches(Constant.DIGIT_REGEX);
            if (isDigit) {
                tvInputNumNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputNumNotice.setCompoundDrawables(drawable, null, null, null);
            } else {
                tvInputNumNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputNumNotice.setCompoundDrawables(drawable, null, null, null);
            }

            isLength = content.length() >= 8 && content.length() <= 32;
            if (isLength) {
                tvInputLengthNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputLengthNotice.setCompoundDrawables(drawable, null, null, null);
            } else {
                tvInputLengthNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                tvInputLengthNotice.setCompoundDrawables(drawable, null, null, null);
            }
            //isUpLetter,isLowLetter,isDigit,isLength
            String psd_str, confirm_str;
            psd_str = etInputPassword.getText().toString();
            confirm_str = etConfirmPassword.getText().toString();
            if (psd_str != null && confirm_str != null) {
                isConfirm = psd_str.equals(confirm_str);
            }
            if (isUpLetter & isLowLetter & isDigit & isLength && isConfirm && checkboxReadTerm.isChecked()) {
                btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
            } else {
                btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
            }
        }
    };


    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            Log.i(TAG, view.getId() + "-------onFocusChange: 001我们看看焦点变化" + b);
            switch (view.getId()) {
                case R.id.et_input_password:
                    if (b) {
                        Log.i(TAG, view.getId() + "-------onFocusChange: 002我们看看焦点变化" + b);
                        tvInputNoticeTittle.setVisibility(View.VISIBLE);
                        tvInputUpLetterNotice.setVisibility(View.VISIBLE);
                        tvInputLowLetterNotice.setVisibility(View.VISIBLE);
                        tvInputNumNotice.setVisibility(View.VISIBLE);
                        tvInputLengthNotice.setVisibility(View.VISIBLE);
                        if (isUpLetter) {
                            tvInputUpLetterNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputUpLetterNotice.setCompoundDrawables(drawable, null, null, null);
                        } else {
                            tvInputUpLetterNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputUpLetterNotice.setCompoundDrawables(drawable, null, null, null);
                        }

                        if (isLowLetter) {
                            tvInputLowLetterNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputLowLetterNotice.setCompoundDrawables(drawable, null, null, null);
                        } else {
                            tvInputLowLetterNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputLowLetterNotice.setCompoundDrawables(drawable, null, null, null);
                        }
                        if (isDigit) {
                            tvInputNumNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputNumNotice.setCompoundDrawables(drawable, null, null, null);
                        } else {
                            tvInputNumNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputNumNotice.setCompoundDrawables(drawable, null, null, null);
                        }

                        if (isLength) {
                            tvInputLengthNotice.setTextColor(getResources().getColor(R.color.text_main_blue));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_ok);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputLengthNotice.setCompoundDrawables(drawable, null, null, null);
                        } else {
                            tvInputLengthNotice.setTextColor(getResources().getColor(R.color.text_main_black));
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_point);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                            tvInputLengthNotice.setCompoundDrawables(drawable, null, null, null);
                        }
                    } else {
                        if (isUpLetter && isLowLetter && isDigit && isLength) {
                            tvInputNoticeTittle.setVisibility(View.GONE);
                            tvInputUpLetterNotice.setVisibility(View.GONE);
                            tvInputLowLetterNotice.setVisibility(View.GONE);
                            tvInputNumNotice.setVisibility(View.GONE);
                            tvInputLengthNotice.setVisibility(View.GONE);
                        } else {
                            if (!isUpLetter) {
                                tvInputUpLetterNotice.setTextColor(getResources().getColor(R.color.color_transaction_fail));
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_red_point);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                                tvInputUpLetterNotice.setCompoundDrawables(drawable, null, null, null);
                            }
                            if (!isLowLetter) {
                                tvInputLowLetterNotice.setTextColor(getResources().getColor(R.color.color_transaction_fail));
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_red_point);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                                tvInputLowLetterNotice.setCompoundDrawables(drawable, null, null, null);
                            }
                            if (!isDigit) {
                                tvInputNumNotice.setTextColor(getResources().getColor(R.color.color_transaction_fail));
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_red_point);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                                tvInputNumNotice.setCompoundDrawables(drawable, null, null, null);
                            }

                            if (!isLength) {
                                tvInputLengthNotice.setTextColor(getResources().getColor(R.color.color_transaction_fail));
                                Drawable drawable = getResources().getDrawable(R.mipmap.ic_input_psd_notice_red_point);
                                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); //必须设置图片大小，否则不显示
                                tvInputLengthNotice.setCompoundDrawables(drawable, null, null, null);
                            }
                        }
                    }
                    break;
                case R.id.et_confirm_password:

                    break;
            }
        }
    };

    @SuppressLint("CheckResult")
    @OnClick({R.id.iv_psd_show, R.id.btn_create_wallet,R.id.checkbox_read_term, R.id.tv_service_privacy_terms})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_psd_show:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                showOrHidePsd(etInputPassword, etConfirmPassword, ivPsdShow);
                break;
//            case R.id.iv_confirm_psd_show:
//                if (ButtonUtils.isFastDoubleClick()) {
//                    return;
//                }
//                showOrHidePsd(etConfirmPassword, ivConfirmPsdShow);
//                break;
//            case R.id.checkbox_read_term:
//                Log.i(TAG, "onViewClicked: 点击了条款去看看。");
//                break;
            case R.id.checkbox_read_term:
                break;
            case R.id.tv_service_privacy_terms:
                Intent intent = new Intent(this, PrivacyTermsWebActivity.class);
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse("http://mine-pool.aitdcoin.com/aitd-coin.html ");
//                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.btn_create_wallet:
                if (judgeSelectInput()) {
                    Observable.just(true).map(s -> {
                        String accountPwd=AESCipher.encrypt(Constant.PSD_KEY, etInputPassword.getText().toString().trim());
                        Log.i(TAG, "onViewClicked: 001存入的密码是？  "+accountPwd);
                        UserInformation userInformation = new UserInformation();
                        userInformation.setUserId(RxToolUtil.readFingerprintFromFile(this));
                      //  userInformation.setPasswordStr(MD5.Md5(etInputPassword.getText().toString().trim()));
                        userInformation.setPasswordStr(accountPwd);

                        //解密
                        String aes_decode_str = AESCipher.decrypt(Constant.PSD_KEY,accountPwd);

                        Log.i(TAG, "onViewClicked: 002存入的密码是？  "+aes_decode_str);

                        userInformation.setNoCenter(true);
                        // TODO: 2020/10/29 此处加入一些判语言和币种单位的逻辑。
                        if (getResources().getConfiguration().locale.getCountry().equals("US")) {
                            userInformation.setFiatUnit(Constants.FIAT_USD);
                        } else {
                            userInformation.setFiatUnit(Constants.FIAT_CNY);
                        }
                        userInformation.setPasswordTip(etPasswordReminder.getText().toString());
                        return userInformation;
                    }).compose(RxHelper.io_main()).subscribe(userInformation -> {
                        UserInfoManager.insertOrUpdate(userInformation);
                        if (build_wallet_type.equals(Constants.INTENT_PUT_CREATE_WALLET)) {
                            Intent intent2 = new Intent(this, BackupMemoryWordActivity.class);
                            startActivity(intent2);
                        } else if (build_wallet_type.equals(Constants.INTENT_PUT_IMPORT_WALLET)) {
                            Intent intent3 = new Intent(this, ImportWalletActivity.class);
                            intent3.putExtra(Constants.INTENT_PUT_TAG, Constants.INTENT_PUT_IMPORT_WALLET);
                            startActivity(intent3);
                        }
                        ToastUtil.showToast(this,getString(R.string.notice_setting_success_text));
                        finish();
                    });
                }
                break;
        }
    }

    /**
     * @return 判断输入是否合法
     */
    private boolean judgeSelectInput() {
        Log.i(TAG, "judgeSelectInput: 我们看用户的选择" + checkboxReadTerm.isChecked());
        if (TextUtils.isEmpty(etInputPassword.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_input_psd));
            return false;
        } else if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_input_psd));
            return false;
        } else if (!etInputPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_twice_psd_diff));
            return false;
        } else if (!checkboxReadTerm.isChecked()) {
            ToastUtil.showToast(this, getString(R.string.notice_agree_terms));
            return false;
        } else if (!etInputPassword.getText().toString().matches(Constant.CONTAIN_UP_LETTER_REGEX)) {
            ToastUtil.showToast(this, getString(R.string.notice_contain_up_word));
            return false;
        } else if (!etInputPassword.getText().toString().matches(Constant.CONTAIN_LOW_LETTER_REGEX)) {
            ToastUtil.showToast(this, getString(R.string.notice_contain_low_word));
            return false;
        } else if (!etInputPassword.getText().toString().matches(Constant.DIGIT_REGEX)) {
            ToastUtil.showToast(this, getString(R.string.notice_contain_num));
            return false;
        } else if (!(etInputPassword.getText().toString().length() >= 8 && etInputPassword.getText().toString().length() <= 32)) {
            ToastUtil.showToast(this, getString(R.string.notice_content_length));
            return false;
        }
        return true;
    }

    /**
     * 显示隐藏密码
     *
     * @param editText  需要显示的edittext控件，
     * @param imageView 需要点击的眼睛图标。
     */
    public void showOrHidePsd(EditText editText, EditText etConfirmPassword, ImageView imageView) {
        LogUtil.d("ttttttt", InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD + "--showOrHidePsd: 键盘" + editText.getInputType());
        if (editText.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            imageView.setImageResource(R.mipmap.ic_open_eye);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            imageView.setImageResource(R.mipmap.ic_close_eye);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().toString().trim().length());
    }


}