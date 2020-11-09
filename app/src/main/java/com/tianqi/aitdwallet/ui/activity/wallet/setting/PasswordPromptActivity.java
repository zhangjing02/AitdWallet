package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordPromptActivity extends BaseActivity {

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
    @BindView(R.id.iv_psd_show)
    ImageView ivPsdShow;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;

    private UserInformation userInfo;

    @Override
    protected int getContentView() {
        return R.layout.activity_password_prompt;
    }

    @Override
    protected void initView() {
        getToolBar();
        userInfo=UserInfoManager.getUserInfo();
        etInputPassword.setText(userInfo.getPasswordTip()+"");
        if (!TextUtils.isEmpty(userInfo.getPasswordTip())){
            etInputPassword.setSelection(userInfo.getPasswordTip().length());
        }
    }


    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_psd_notice_msg);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    @Override
    protected void initData() { }


    @OnClick({R.id.iv_psd_show, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_psd_show:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                showOrHidePsd(etInputPassword, ivPsdShow);
                break;
            case R.id.btn_confirm:
                String psd_tip=etInputPassword.getText().toString();
                userInfo.setPasswordTip(psd_tip);
                UserInfoManager.insertOrUpdate(userInfo);
                finish();
                break;
        }
    }

    /**
     * 显示隐藏密码
     *
     * @param editText  需要显示的edittext控件，
     * @param imageView 需要点击的眼睛图标。
     */
    public void showOrHidePsd(EditText editText, ImageView imageView) {
        LogUtil.d("ttttttt", InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD + "--showOrHidePsd: 键盘" + editText.getInputType());
        if (editText.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            imageView.setImageResource(R.mipmap.ic_open_eye);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            imageView.setImageResource(R.mipmap.ic_close_eye);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().toString().trim().length());
    }
}