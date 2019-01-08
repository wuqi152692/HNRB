package com.hnzx.hnrb.htmlTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.tools.WebUtil;
import com.hnzx.hnrb.ui.web.WebActivity;
import com.hnzx.hnrb.view.MultiStateView;

/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class TWebChromeClient extends WebChromeClient {

    // For Android < 3.0 app支持版本已经抛弃3.0以下
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
//            ***
    }

    // For Android  >= 3.0
    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
//        WebActivity.uploadMessage = valueCallback;
//        openImageChooserActivity(App.getInstance().getApplicationContext());
    }

    //For Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> valueCallback,
                                String acceptType, String capture) {
//        WebActivity.uploadMessage = valueCallback;
//        openImageChooserActivity(App.getInstance().getApplicationContext());
    }

    // For Android >= 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        WebActivity.uploadMessageAboveL = filePathCallback;
        openImageChooserActivity(webView.getContext());
        return true;
    }

    private void openImageChooserActivity(Context context) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        ((Activity) context).startActivityForResult(Intent.createChooser(i,
                "Image Chooser"), WebActivity.FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT) {
            Message href = view.getHandler().obtainMessage();
            view.requestFocusNodeHref(href);

            String url = href.getData().getString("url");
            Intent intent = new Intent(view.getContext(), WebActivity.class);
            intent.putExtra(WebActivity.WEB_URL_KEY, url);
            view.getContext().startActivity(intent);
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        } else {
            WebView newWebView = new WebView(view.getContext());
            WebUtil.setWebView(newWebView, view.getContext());
            newWebView.setWebViewClient(new TWebViewClient(view.getContext(), newWebView));
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            return true;
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message,
                             JsResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                view.getContext());
        builder.setTitle("来自应用的信息显示：").setMessage(message)
                .setNeutralButton("我知道了", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        result.cancel();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message,
                               final JsResult result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                view.getContext());
        builder.setTitle("来自应用的信息显示：").setMessage(message)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        }).show();
        return true;
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin,
                                                   GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }
}
