package com.tianqi.aitdwallet.ui.activity.wallet.setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tianqi.aitdwallet.R;
import com.tianqi.baselib.rxhttp.config.HttpConfig;
import com.tianqi.baselib.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyTermsWebActivity extends AppCompatActivity {


    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    @BindView(R.id.btn_collect)
    ImageView btnCollect;
    @BindView(R.id.btn_finish)
    TextView btnFinish;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_home_shop)
    WebView webHomeShop;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_terms_web);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        getToolBar();

        WebSettings mWebSettings = webHomeShop.getSettings();
        //设置缓存模式
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setDomStorageEnabled(true);//给权限

        webHomeShop.loadUrl(HttpConfig.PRIVACY_TERMS_URL);
        mWebSettings.setJavaScriptEnabled(true);
        //设置缓存模式
        webViewSettings();
    }

    private void getToolBar() {
        toolbarTitle.setText(R.string.tittle_service_privacy_terms);
        toolbar.setNavigationOnClickListener(v -> {
            finish();//返回
        });
    }

    private void webViewSettings() {
        webHomeShop.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                LogUtil.e("verbose", request.getUrl().toString() + "view.getUrl()==========================" + view.getUrl());
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        webHomeShop.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }

                super.onProgressChanged(view, newProgress);
            }
        });
    }
}
