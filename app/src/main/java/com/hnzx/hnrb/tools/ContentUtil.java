package com.hnzx.hnrb.tools;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hnzx.hnrb.App;

/**
 * Created by FoAng on 16/10/24 下午1:36；
 */
public class ContentUtil {

    public static boolean checkContentAndTips( String[] tips, View... views) {
        return checkContentAndTips(App.getInstance(),tips,views);
    }

    public static boolean checkContentAndTips(Context mContext,String[] tips, View... views) {
        if (tips == null || views == null || tips.length != views.length) return false;
        for (int i = 0; i < views.length; i++) {
            View mView = views[i];
            if (mView != null && mView instanceof TextView) {
                final String content = ((TextView) mView).getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(mContext, tips[i] + "不能为空", Toast.LENGTH_SHORT);
                    return false;
                }
            }
        }
        return true;
    }

    public static String getFilterContent(String content){
        if (TextUtils.isEmpty(content)) {
            return "";
        } else {
            return content;
        }
    }
}
