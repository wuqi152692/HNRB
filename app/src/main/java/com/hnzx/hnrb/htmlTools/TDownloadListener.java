package com.hnzx.hnrb.htmlTools;

import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

import com.hnzx.hnrb.App;


/**
 * Created by Administrator on 2017/3/8 0008.
 */

public class TDownloadListener  implements DownloadListener{
    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.getInstance().getApplicationContext().startActivity(intent);
    }
}
