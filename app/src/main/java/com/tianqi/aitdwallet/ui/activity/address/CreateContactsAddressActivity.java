package com.tianqi.aitdwallet.ui.activity.address;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.ContactsAddressAdapter;
import com.tianqi.aitdwallet.widget.dialog.SelectCoinDialog;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dao.ContactsInfo;
import com.tianqi.baselib.dbManager.ContactsInfoManager;
import com.tianqi.baselib.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateContactsAddressActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_create_address)
    TextView btnCreateAddress;
    @BindView(R.id.lv_coin_address)
    ListView lvCoinAddress;
    @BindView(R.id.iv_no_data)
    ImageView ivNoData;
    private List<ContactsInfo> contactsInfos;

    private ContactsAddressAdapter coinSelectAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_create_address;
    }

    @Override
    protected void initView() {
        contactsInfos = ContactsInfoManager.getWalletInfo();
        getToolBar();
    }


    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_address_management);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });

        if (contactsInfos != null && contactsInfos.size() > 0) {
            btnCollect.setVisibility(View.VISIBLE);
            btnCollect.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add_contacts_address));
            btnCreateAddress.setVisibility(View.GONE);
            ivNoData.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        List<CoinInfo> coinInfoList = new ArrayList<>();
        if (contactsInfos!=null&&contactsInfos.size()>0){
            for (int i = 0; i <contactsInfos.size() ; i++) {
                CoinInfo coinInfo = new CoinInfo();
                coinInfo.setCoin_name(contactsInfos.get(i).getContactsCoinName());
                coinInfo.setResourceId(contactsInfos.get(i).getCoinResourceId());
                coinInfoList.add(coinInfo);
            }
        }
        coinSelectAdapter = new ContactsAddressAdapter(this, coinInfoList);

        lvCoinAddress.setAdapter(coinSelectAdapter);
    }

    @OnClick({R.id.btn_collect, R.id.btn_create_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
            case R.id.btn_create_address:
                SelectCoinDialog bottomPaymentDialog = new SelectCoinDialog(this, "这是啥？");
                bottomPaymentDialog.show();
                break;
        }
    }

}