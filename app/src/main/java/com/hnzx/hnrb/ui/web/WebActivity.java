package com.hnzx.hnrb.ui.web;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.htmlTools.TDownloadListener;
import com.hnzx.hnrb.htmlTools.TWebChromeClient;
import com.hnzx.hnrb.htmlTools.TWebView;
import com.hnzx.hnrb.htmlTools.TWebViewClient;
import com.hnzx.hnrb.tools.LogUtil;
import com.hnzx.hnrb.tools.WebUtil;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.view.TopHeadView;
import com.umeng.socialize.UMShareAPI;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;

public class WebActivity extends BaseActivity {
    private WebView webView;
    /**
     * 需要上级页面传递的web url
     */
    private String url;

    private boolean isOtherOpen, isAbleShare = true;

    public static final String WEB_URL_KEY = "UrlKey";

    public static final String BUNDLE_KEY_IS_OTHER_OPEN = "BUNDLE_KEY_IS_OTHER_OPEN";

    public static final String BUNDLE_KEY_IS_ABLE_SHARE = "BUNDLE_KEY_IS_ABLE_SHARE";

    private TopHeadView headView;

    public static Intent newIntent(Context context, String url,  boolean isOtherOpen) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(BUNDLE_KEY_IS_OTHER_OPEN, isOtherOpen);
        intent.putExtra(WEB_URL_KEY, url);
        return intent;
    }

    public static Intent newIntent(Context context, String url, boolean isOtherOpen, boolean isAbleShare) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(BUNDLE_KEY_IS_OTHER_OPEN, isOtherOpen);
        intent.putExtra(BUNDLE_KEY_IS_ABLE_SHARE, isAbleShare);
        intent.putExtra(WEB_URL_KEY, url);
        return intent;
    }

    @Override
    public boolean supportSlideBack() {

        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void initViews(Bundle savedInstanceState) {
        isOtherOpen = getIntent().getBooleanExtra(BUNDLE_KEY_IS_OTHER_OPEN, false);
        isAbleShare = getIntent().getBooleanExtra(BUNDLE_KEY_IS_ABLE_SHARE, true);
        headView = (TopHeadView) findViewById(R.id.webview_topHeadView);
        webView = (WebView) findViewById(R.id.webView);
        WebUtil.setWebView(webView, this);
        webView.setDownloadListener(new TDownloadListener());
        webView.setWebChromeClient(new TWebChromeClient());
        webView.setWebViewClient(new TWebViewClient(this, webView, isOtherOpen, headView));
    }

    @Override
    protected void initData() {
        url = getIntent().getStringExtra(WEB_URL_KEY);
        webView.postDelayed(new Runnable() {

            @Override
            public void run() {
                webView.loadUrl(url);
            }
        }, 500);
    }

    private NewsShareDialog dialog;

    @Override
    protected void initListeners() {
        if (isAbleShare)
            headView.setTopShareListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog = NewsShareDialog.newInstance(webView.getTitle(), webView.getTitle(), "", webView.getUrl());

                    dialog.show(getFragmentManager(), getLocalClassName());
                }
            });
    }

    public static ValueCallback<Uri> uploadMessage;
    public static ValueCallback<Uri[]> uploadMessageAboveL;
    public final static int FILE_CHOOSER_RESULT_CODE = 10000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            webView.getClass().getMethod("onResume").invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
