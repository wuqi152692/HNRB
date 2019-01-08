package com.hnzx.hnrb.htmlTools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hnzx.hnrb.ui.web.WebActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.hnzx.hnrb.view.TopHeadView;

import java.net.URI;
import java.net.URL;
import java.util.List;

public class TWebViewClient extends WebViewClient {

    WebView webView;
    MultiStateView stateView;
    TopHeadView headView;
    Context context;
    CnbetaHtmlParser parser;
    boolean otherOpen = true;

    public TWebViewClient(Context context, WebView webView) {
        this.webView = webView;
        this.context = context;
    }

    public TWebViewClient(Context context, WebView webView, MultiStateView stateView, boolean otherOpen) {
        this.webView = webView;
        this.context = context;
        this.stateView = stateView;
        this.otherOpen = otherOpen;
    }

    public TWebViewClient(Context context, WebView webView, boolean otherOpen) {
        this.webView = webView;
        this.context = context;
        this.otherOpen = otherOpen;
    }

    public TWebViewClient(Context context, WebView webView, boolean otherOpen, TopHeadView headView) {
        this.webView = webView;
        this.context = context;
        this.otherOpen = otherOpen;
        this.headView = headView;
    }

    public void setData(String data) {
        parser = new CnbetaHtmlParser(webView, data, context);
        parser.execute((Void) null);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (headView != null)
            headView.setHeadTitle(webView.getTitle());
        if (stateView != null) {
            if (stateView.getViewState() != MultiStateView.VIEW_STATE_CONTENT) {
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setRepeatCount(0);
                animation.setDuration(1000);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        stateView.clearAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                stateView.startAnimation(animation);
            }
        }
        doSomething();
    }

    public void doSomething() {
        DownloadWebImgTask downloadTask = new DownloadWebImgTask(webView);

        if (parser == null)
            return;

        List<String> urlStrs = parser.getImgUrls();

        String urlStrArray[] = new String[urlStrs.size() + 1];

        urlStrs.toArray(urlStrArray);

        downloadTask.execute(urlStrArray);
    }


//    @Override
//    public void onLoadResource(WebView view, String url) {
//        super.onLoadResource(view, url);
//        String path = Uri.parse(url.toLowerCase()).getPath();
//        if (path.endsWith(".mp4") || path.endsWith(".avi") || path.endsWith(".3gp") || path.endsWith(".mov") || path.endsWith(".mpg ") || path.endsWith(".mpeg"))
//            System.err.println("videopath=" + url);
//    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse(url));
            context.startActivity(intent);
            return true;
        } else if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://")) {
            if (otherOpen) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(WebActivity.WEB_URL_KEY, url);
                context.startActivity(intent);
            } else {
                webView.loadUrl(url);
            }
            return true;
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (request.getUrl().toString().startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse(request.getUrl().toString()));
            context.startActivity(intent);
            return true;
        } else {
            if (otherOpen) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(WebActivity.WEB_URL_KEY, request.getUrl().toString());
                context.startActivity(intent);
            } else {
                webView.loadUrl(request.getUrl().toString());
            }
            return true;
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }
}
