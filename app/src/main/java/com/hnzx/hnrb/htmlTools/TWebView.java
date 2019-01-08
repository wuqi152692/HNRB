package com.hnzx.hnrb.htmlTools;

import android.content.Context;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TWebView extends WebView {
    TWebViewClient client;

    public TWebView(Context context) {
        super(context);
    }

    public TWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        client.setData(data);
//        loadDataWithBaseURL(null, data, mimeType, encoding, null);
    }

    public void loadData(String data) {
        loadData(data, "text/html", "utf-8");
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
        this.client = (TWebViewClient) client;
    }
}
