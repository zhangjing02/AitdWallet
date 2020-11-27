package com.tianqi.aitdwallet.ui.activity.setting;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.PushMessageAdapter;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.rxhttp.bean.GetMessageBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class MessageCenterActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.rcv_message_content)
    RecyclerView rcvMessageContent;

    private String[] titles;
    private PushMessageAdapter messageAdapter;
    private Badge badgeVie;
    private TextView tv_tab_title1,tv_tab_title2;

    @Override
    protected int getContentView() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initView() {
        getToolBar();
        titles = new String[]{getString(R.string.tittle_transfer_message), getString(R.string.tittle_system_message)};
        TabLayout.Tab tab;
        for (int i = 0; i < titles.length; i++) {
            tab = tablayout.newTab();

            tab.setCustomView(R.layout.tab_message_transfer);
            if (i==0){
                tv_tab_title1 = tab.getCustomView().findViewById(R.id.tv_tab_title);
                tv_tab_title1.setText(titles[i]);
            }else if (i==1){
                tv_tab_title2 = tab.getCustomView().findViewById(R.id.tv_tab_title);
                tv_tab_title2.setText(titles[i]);
                tv_tab_title2.setTextColor(getResources().getColor(R.color.text_main_60_black));
            }
            tablayout.addTab(tab);
        }
        tablayout.addOnTabSelectedListener(onTabSelectedListener);

        badgeVie = new QBadgeView(this).bindTarget(tv_tab_title1)
                        .setBadgeTextSize(9, true)
                        .setBadgePadding(0f, true)
                        .setGravityOffset(7f, 15    , false)
               // .setOnDragStateChangedListener(mOnDragStateChangedListener)
                .setBadgeGravity(Gravity.TOP | Gravity.END)
                .setBadgeBackgroundColor(getResources().getColor(R.color.color_transaction_fail))
                .setBadgeNumber(22)
                .setBadgeTextColor(getResources().getColor(R.color.color_transaction_fail));
        badgeVie.hide(true);

        badgeVie.setBadgeNumber(36);
        badgeVie.isShowShadow();
        badgeVie.setShowShadow(true);
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_message_center);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
        btnFinish.setVisibility(View.VISIBLE);
        btnFinish.setText(R.string.btn_all_read_text);
        btnFinish.setTextColor(getResources().getColor(R.color.text_light_blue));
        btnFinish.setTextSize(16);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeVie.hide(true);
            }
        });
    }

    TabLayout.BaseOnTabSelectedListener onTabSelectedListener = new TabLayout.BaseOnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // TODO: 2020/11/19 选中和未选中字体颜色值变化
            TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
            textView.setTextColor(getResources().getColor(R.color.text_main_black));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
            textView.setTextColor(getResources().getColor(R.color.text_main_60_black));
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


    Badge.OnDragStateChangedListener mOnDragStateChangedListener = (dragState, badge, targetView) -> {
    };

    @Override
    protected void initData() {
        List<GetMessageBean> list=new ArrayList<>();
        GetMessageBean messageBean=new GetMessageBean();
        messageBean.setMessageTittle("辅导辅导辅导001");
        messageBean.setMessageContent("风动旛动服的辅导费放到飞洒发的发发大水发到付对方答复洒发的发发大水发到付对方答复洒发的发发大水发到付对方答复");
        list.add(messageBean);
        GetMessageBean messageBean02=new GetMessageBean();
        messageBean02.setMessageTittle("辅导辅导辅导002");
        messageBean02.setMessageContent("使用盗版软件写出来的代码是没有灵魂的使用盗版软件写出来的代码是没有灵魂的使用盗版软件写出来的代码是没有灵魂的");
        list.add(messageBean02);
        GetMessageBean messageBean03=new GetMessageBean();
        messageBean03.setMessageTittle("辅导辅导辅导003");
        messageBean03.setMessageContent("分支master已经存在，并且有一些提交，但在origin/master中不存在。你想重新建立基础或重置它们吗？");
        list.add(messageBean03);

        rcvMessageContent.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        messageAdapter = new PushMessageAdapter(R.layout.layout_adapter_message_push, list);
        rcvMessageContent.setAdapter(messageAdapter);

        messageAdapter.setOnItemClickListener((adapter, view, position) -> {
                Intent intent=new Intent(this,MessageDetailActivity.class);
                startActivity(intent);
        });
    }

}