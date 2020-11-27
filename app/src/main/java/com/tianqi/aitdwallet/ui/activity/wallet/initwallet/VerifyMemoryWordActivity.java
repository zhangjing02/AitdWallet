package com.tianqi.aitdwallet.ui.activity.wallet.initwallet;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.VerifyMnemonicWordAdapter;
import com.tianqi.aitdwallet.bean.MnemonicWordBean;
import com.tianqi.aitdwallet.ui.activity.BaseActivity;
import com.tianqi.baselib.dao.UserInformation;
import com.tianqi.baselib.dbManager.UserInfoManager;
import com.tianqi.baselib.utils.ButtonUtils;
import com.tianqi.baselib.utils.LogUtil;
import com.tianqi.baselib.utils.display.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VerifyMemoryWordActivity extends BaseActivity {

    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_verify_word_notice)
    TextView tvVerifyWordNotice;
    @BindView(R.id.tv_select_word1_tag)
    TextView tvSelectWord1Tag;
    @BindView(R.id.tv_select_word1)
    TextView tvSelectWord1;
    @BindView(R.id.tv_select_word2_tag)
    TextView tvSelectWord2Tag;
    @BindView(R.id.tv_select_word2)
    TextView tvSelectWord2;
    @BindView(R.id.tv_select_word3_tag)
    TextView tvSelectWord3Tag;
    @BindView(R.id.tv_select_word3)
    TextView tvSelectWord3;
    @BindView(R.id.gv_mnemonic_word)
    GridView gvMnemonicWord;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;

    private UserInformation userInformation;
    private VerifyMnemonicWordAdapter mnemonicWordAdapter;
    private List<String> list;
    private int grid_select_index1, grid_select_index2, grid_select_index3;
    private List<Integer> randomNums;
    private  Gson gson;
    private Type listType;

    private List<MnemonicWordBean>wordBeanList;


    private static final String TAG = "VerifyMemoryWordActivit";

    @Override
    protected void initView() {
        getToolBar();
        userInformation = UserInfoManager.getUserInfo();
        randomNums = getRandomNum(1, 12, 3);

        LogUtil.i(TAG, "initView: 我们看取到的三个随机数。" + randomNums.toString());


        tvSelectWord1Tag.setText(String.format(getString(R.string.text_select_which_word),randomNums.get(0)));
        tvSelectWord2Tag.setText(String.format(getString(R.string.text_select_which_word),randomNums.get(1)));
        tvSelectWord3Tag.setText(String.format(getString(R.string.text_select_which_word),randomNums.get(2)));

        gson = new Gson();
        listType = new TypeToken<List<String>>() {
        }.getType();
        list = gson.fromJson(userInformation.getMnemonicWord(), listType);
        wordBeanList=new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            MnemonicWordBean wordBean=new MnemonicWordBean();
            wordBean.setWord(list.get(i));
            wordBeanList.add(wordBean);
        }

        mnemonicWordAdapter = new VerifyMnemonicWordAdapter(this, wordBeanList, 1);
        gvMnemonicWord.setAdapter(mnemonicWordAdapter);
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_verify_mnemonic);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }
    int select_count=0;
    int []select_index=new int[]{-1,-1,-1};
    @Override
    protected void initData() {
        gvMnemonicWord.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i!=select_index[0]&&i!=select_index[1]&&i!=select_index[2]&&select_count<3){
                if (TextUtils.isEmpty(tvSelectWord1.getText().toString())) {
                    grid_select_index1 = i;
                    tvSelectWord1.setText(wordBeanList.get(i).getWord());
                    select_index[0]=i;
                    select_count++;
                } else if (TextUtils.isEmpty(tvSelectWord2.getText().toString())) {
                    grid_select_index2 = i;
                    tvSelectWord2.setText(wordBeanList.get(i).getWord());
                    select_index[1]=i;
                    select_count++;
                } else if (TextUtils.isEmpty(tvSelectWord3.getText().toString())) {
                    grid_select_index3 = i;
                    tvSelectWord3.setText(wordBeanList.get(i).getWord());
                    select_index[2]=i;
                    select_count++;
                }
                if (wordBeanList.get(i).isHidden()){
                    wordBeanList.get(i).setHidden(false);
                }else {
                    wordBeanList.get(i).setHidden(true);
                }
                mnemonicWordAdapter.refresSingleData(wordBeanList);
            }

            if (!TextUtils.isEmpty(tvSelectWord1.getText()) && !TextUtils.isEmpty(tvSelectWord2.getText()) && !TextUtils.isEmpty(tvSelectWord3.getText())) {
                btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verify_memory_word;
    }

    @OnClick({R.id.tv_select_word1, R.id.tv_select_word2, R.id.tv_select_word3, R.id.btn_create_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select_word1:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                if (!TextUtils.isEmpty(tvSelectWord1.getText().toString())) {
                    tvSelectWord1.setText(null);
                    btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));

                    wordBeanList.get(select_index[0]).setHidden(false);
                    mnemonicWordAdapter.refresSingleData(wordBeanList);
                    select_index[0]=-1;
                    select_count--;
                }
                break;
            case R.id.tv_select_word2:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                if (!TextUtils.isEmpty(tvSelectWord2.getText().toString())) {
                    tvSelectWord2.setText(null);
                    btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    wordBeanList.get(select_index[1]).setHidden(false);
                    mnemonicWordAdapter.refresSingleData(wordBeanList);
                    select_count--;
                    select_index[1]=-1;
                }
                break;
            case R.id.tv_select_word3:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                if (!TextUtils.isEmpty(tvSelectWord3.getText().toString())) {
                    tvSelectWord3.setText(null);
                    btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                    wordBeanList.get(select_index[2]).setHidden(false);
                    mnemonicWordAdapter.refresSingleData(wordBeanList);
                    select_count--;
                    select_index[2]=-1;
                }
                break;
            case R.id.btn_create_wallet:
                if (ButtonUtils.isFastDoubleClick()) {
                    return;
                }
                if (judgeSelectInput()) {
                    Intent intent = new Intent(this, CreateWalletActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    /**
     * @return 判断输入是否合法
     */
    private boolean judgeSelectInput() {
        List<String> mm_words = gson.fromJson(userInformation.getMnemonicWord(), listType);
        if (TextUtils.isEmpty(tvSelectWord1.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_select_first_word));
            return false;
        } else if (TextUtils.isEmpty(tvSelectWord2.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_select_second_word));
            return false;
        } else if (TextUtils.isEmpty(tvSelectWord3.getText().toString())) {
            ToastUtil.showToast(this, getString(R.string.notice_select_third_word));
            return false;
        } else if (!tvSelectWord1.getText().toString().equals(mm_words.get(randomNums.get(0)-1))) {
            tvSelectWord1.setText("");
            tvSelectWord2.setText("");
            tvSelectWord3.setText("");

            mnemonicWordAdapter.refreshData(wordBeanList);
            select_count=0;
            select_index=new int[]{-1,-1,-1};

            ToastUtil.showToast(this, getString(R.string.notice_first_word_error));
            return false;
        } else if (!tvSelectWord2.getText().toString().equals(mm_words.get(randomNums.get(1)-1))) {
            tvSelectWord1.setText("");
            tvSelectWord2.setText("");
            tvSelectWord3.setText("");
            mnemonicWordAdapter.refreshData(wordBeanList);
            select_count=0;
            select_index=new int[]{-1,-1,-1};
            ToastUtil.showToast(this, getString(R.string.notice_second_word_error));
            return false;
        } else if (!tvSelectWord3.getText().toString().equals(mm_words.get(randomNums.get(2)-1))) {
            tvSelectWord1.setText("");
            tvSelectWord2.setText("");
            tvSelectWord3.setText("");
            mnemonicWordAdapter.refreshData(wordBeanList);
            select_count=0;
            select_index=new int[]{-1,-1,-1};
            ToastUtil.showToast(this, getString(R.string.notice_third_word_error));
            return false;
        }
        return true;
    }


    /**
     * @param requMin      最小值
     * @param requMax      最大值
     * @param targetLength 获取随机数个数
     * @return
     */
    public List<Integer> getRandomNum(int requMin, int requMax, int targetLength) {
        if (requMax - requMin < 1) {
            System.out.print("最小值和最大值数据有误");
            return null;
        } else if (requMax - requMin < targetLength) {
            System.out.print("指定随机个数超过范围");
            return null;
        }
        int target = targetLength;
        List<Integer> list = new ArrayList<>();

        List<Integer> requList = new ArrayList<>();
        for (int i = requMin; i <= requMax; i++) {
            requList.add(i);
        }

        for (int i = 0; i < targetLength; i++) {
            // 取出一个随机角标
            int r = (int) (Math.random() * requList.size());
            list.add(requList.get(r));

            // 移除已经取过的值
            requList.remove(r);
        }

        return list;
    }
}