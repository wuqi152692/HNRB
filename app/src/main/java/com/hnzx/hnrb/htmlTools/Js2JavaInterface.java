package com.hnzx.hnrb.htmlTools;

import android.content.Context;
import android.content.Intent;

import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.ui.news.NewsScanBigImageActivity;

import java.util.ArrayList;

public class Js2JavaInterface {

    private Context context;

    public Js2JavaInterface(Context context) {
        this.context = context;
    }


    @android.webkit.JavascriptInterface
    public void setImgSrc(String imgSrc) {
        ArrayList<String> imagesList = new ArrayList<>();
        imagesList.add(imgSrc);
        Intent intent = new Intent(context, NewsScanBigImageActivity.class);
        intent.putStringArrayListExtra(Constant.BEAN, imagesList);
        intent.putExtra(NewsScanBigImageActivity.SHOW_POSITION, 0);
        context.startActivity(intent);
    }
}