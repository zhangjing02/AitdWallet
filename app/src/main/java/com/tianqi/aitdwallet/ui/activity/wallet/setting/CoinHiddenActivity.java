package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianqi.aitdwallet.R;
import com.tianqi.aitdwallet.adapter.recycle_adapter.HiddenCoinAdapter;
import com.tianqi.baselib.base.BaseActivity;
import com.tianqi.baselib.dao.CoinInfo;
import com.tianqi.baselib.dbManager.CoinInfoManager;
import com.tianqi.baselib.widget.CustomClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class CoinHiddenActivity extends BaseActivity {

    @BindView(R.id.tv_coin_search)
    CustomClearEditText tvCoinSearch;
    @BindView(R.id.tv_search_cancel)
    TextView tvSearchCancel;
    @BindView(R.id.rcv_coin_hidden)
    RecyclerView rcvCoinHidden;

    private HiddenCoinAdapter hiddenWalletAdapter;
    private List<CoinInfo> mCoinBeans, mSearchCoinBeans;

    private PublishSubject<String> mPublishSubject;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected int getContentView() {
        return R.layout.activity_coin_hidden;
    }

    @Override
    protected void initView() {
        mSearchCoinBeans = new ArrayList<>();
        mCoinBeans = CoinInfoManager.getCreateCoinInfo();
        rcvCoinHidden.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        hiddenWalletAdapter = new HiddenCoinAdapter(R.layout.layout_adapter_hidden_coin, mCoinBeans);
        rcvCoinHidden.setAdapter(hiddenWalletAdapter);
    }

    @Override
    protected void initData() {
        tvCoinSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString().trim())) {
                    hiddenWalletAdapter.setNewData(mCoinBeans);
                } else {
                    // startSearch(editable.toString());
                    mSearchCoinBeans.clear();
                    for (int i = 0; i < mCoinBeans.size(); i++) {
                        String up_word = mCoinBeans.get(i).getCoin_name().toLowerCase();
                        if (mCoinBeans.get(i).getCoin_name().contains(editable.toString()) || up_word.contains(editable.toString())) {
                            mSearchCoinBeans.add(mCoinBeans.get(i));
                        }
                    }
                    hiddenWalletAdapter.setNewData(mSearchCoinBeans);
                }
            }
        });
    }

    //开始搜索
    private void startSearch(String query) {
        mPublishSubject.onNext(query);
    }


    private Observable<List<CoinInfo>> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<List<CoinInfo>>() {

            @Override
            public void subscribe(ObservableEmitter<List<CoinInfo>> observableEmitter) throws Exception {
                // TODO: 2020/10/27 把关键词，用作数据库的搜索。
                for (int i = 0; i < mCoinBeans.size(); i++) {
                    if (mCoinBeans.get(i).getCoin_fullName().contains(query)) {
                        mSearchCoinBeans.add(mCoinBeans.get(i));
                    }
                }
                observableEmitter.onNext(mSearchCoinBeans);
                observableEmitter.onComplete();

            }
        }).subscribeOn(Schedulers.io());
    }


    @OnClick({R.id.tv_coin_search, R.id.tv_search_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_coin_search:

                break;
            case R.id.tv_search_cancel:
                finish();
                break;
        }
    }
}