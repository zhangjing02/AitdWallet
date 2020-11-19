package com.tianqi.aitdwallet.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.ui.activity.address.ContactsAddressManageActivity;
import com.tianqi.aitdwallet.ui.activity.setting.AboutUsActivity;
import com.tianqi.aitdwallet.ui.activity.setting.InviteFriendActivity;
import com.tianqi.aitdwallet.ui.activity.setting.MessageCenterActivity;
import com.tianqi.aitdwallet.ui.activity.setting.SafeCenterActivity;
import com.tianqi.aitdwallet.ui.activity.setting.SystemSettingActivity;
import com.tianqi.aitdwallet.ui.activity.wallet.setting.WalletManageActivity;
import com.tianqi.baselib.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_wallet_manage)
    TextView tvWalletManage;
    @BindView(R.id.tv_address_manage)
    TextView tvAddressManage;
    @BindView(R.id.tv_my_message)
    TextView tvMyMessage;
    @BindView(R.id.tv_invite_friend)
    TextView tvInviteFriend;
    @BindView(R.id.tv_safe_center)
    TextView tvSafeCenter;
    @BindView(R.id.tv_sys_setting)
    TextView tvSysSetting;
    @BindView(R.id.tv_about_me)
    TextView tvAboutMe;

    @Override
    protected void initView() {
        getToolBar();
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_setting);
        toolbar.setNavigationIcon(null);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initData() {
    }


    @OnClick({R.id.tv_wallet_manage, R.id.tv_address_manage, R.id.tv_my_message, R.id.tv_invite_friend, R.id.tv_safe_center, R.id.tv_sys_setting, R.id.tv_about_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_wallet_manage:
                Intent intent = new Intent(getActivity(), WalletManageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_address_manage:
                intent=new Intent(getActivity(), ContactsAddressManageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_my_message:
                intent=new Intent(getActivity(), MessageCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_invite_friend:
                intent=new Intent(getActivity(), InviteFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_safe_center:
                intent=new Intent(getActivity(), SafeCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sys_setting:
                intent=new Intent(getActivity(), SystemSettingActivity.class);
                startActivity(intent);
//                intent=new Intent(getActivity(), FirstActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_about_me:
                intent=new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
