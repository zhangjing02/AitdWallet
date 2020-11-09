package com.tianqi.aitdwallet.ui.fragment.importwallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.list_adapter.MnemonicWordAdapter;
import com.tianqi.aitdwallet.ui.activity.wallet.initwallet.ImportWalletFrMnemonicActivity;
import com.tianqi.aitdwallet.utils.Constants;
import com.tianqi.aitdwallet.utils.MnemonicUtils;
import com.tianqi.baselib.base.BaseFragment;
import com.tianqi.baselib.utils.display.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class ImportBulkWalletFragment extends BaseFragment {
    @BindView(R.id.tv_select_notice)
    TextView tvSelectNotice;
    @BindView(R.id.et_input_mnemonic)
    EditText etInputMnemonic;
    @BindView(R.id.btn_create_wallet)
    TextView btnCreateWallet;
    @BindView(R.id.gv_mnemonic_word)
    GridView gvMnemonicWord;
    @BindView(R.id.tv_input_error_notice)
    TextView tvInputErrorNotice;
    private MnemonicWordAdapter mnemonicWordAdapter;

    private static final String TAG = "ImportSingleWalletFragm";
    private List<String> list, select_list,show_list;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_import_wallet_bulk;
    }


    @Override
    protected void initView() {
        list = MnemonicUtils.populateWordList();
        select_list = new ArrayList<>();
        show_list=new ArrayList<>();
        mnemonicWordAdapter = new MnemonicWordAdapter(getActivity(), select_list, 3);
        gvMnemonicWord.setAdapter(mnemonicWordAdapter);

        etInputMnemonic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("CheckResult")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //创建被观察者
                select_list.clear();
                Observable.fromIterable(list).filter(s -> {    //对集合数据进行过滤,只发送符合条件的事件
                    String find_key_word = charSequence.toString().substring(charSequence.toString().lastIndexOf(" ") + 1);
                    Log.i(TAG, "onTextChanged: 我們需要筛选的字段是？" + find_key_word);
                    return s.startsWith(find_key_word);
                }).take(5).doOnComplete(() -> {
                    if (select_list.size() > 0) {
                        tvInputErrorNotice.setVisibility(View.GONE);
                        mnemonicWordAdapter.refreshData(select_list);
                        show_list=select_list;
                    } else {
                        tvInputErrorNotice.setVisibility(View.VISIBLE);
                    }
                }).subscribe(s -> {
                    if (!TextUtils.isEmpty(s)){
                        select_list.add(s);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "----我们看输入改变后" + editable.toString());
                String[] mn_words = editable.toString().split("\\s+");
                Log.i(TAG, mn_words.length+"----我们看输入改变后" + editable.toString());
                if (judeMnwordsCorrect(mn_words)){
                    btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_blue_round_button));
                }else {
                    btnCreateWallet.setBackground(getResources().getDrawable(R.drawable.bg_grey_round_button));
                }
            }
        });
    }


    private boolean judeMnwordsCorrect(String[] mn_words) {
        if (mn_words.length == 12) {
            for (int i = 0; i < mn_words.length; i++) {
                if (!list.contains(mn_words[i])) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void initData() {
        gvMnemonicWord.setOnItemClickListener((adapterView, view, i, l) -> {
            String before_str=etInputMnemonic.getText().toString();
            String tem_str="";
            if (before_str.contains(" ")&&before_str.length()>1){
                tem_str = before_str.substring(0, etInputMnemonic.getText().toString().lastIndexOf(" "));
                etInputMnemonic.setText(tem_str+" "+show_list.get(i)+" ");
            }else {
                etInputMnemonic.setText(show_list.get(i)+" ");
            }
            etInputMnemonic.setSelection(etInputMnemonic.getText().toString().length());
            show_list.clear();
            mnemonicWordAdapter.refreshData(show_list);
        });
    }

    @OnClick(R.id.btn_create_wallet)
    public void onViewClicked() {
        String[] mn_words = etInputMnemonic.getText().toString().split("\\s+");
        if (judeMnwordsCorrect(mn_words)){
            Intent intent =new Intent(getActivity(), ImportWalletFrMnemonicActivity.class);
            intent.putExtra(Constants.INTENT_PUT_MNEMONIC,etInputMnemonic.getText().toString());
            startActivity(intent);
        }else {
            ToastUtil.showToast(getActivity(),getString(R.string.notice_mnemonic_word_error));
        }
    }
}
