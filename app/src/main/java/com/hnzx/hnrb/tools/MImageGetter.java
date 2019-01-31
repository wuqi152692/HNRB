package com.hnzx.hnrb.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hnzx.hnrb.loader.GlideApp;

/**
 * @author: mingancai
 * @Time: 2017/7/13 0013.
 */

public class MImageGetter implements Html.ImageGetter {
    Context c;
    TextView container;

    public MImageGetter(TextView text, Context c) {
        this.c = c;
        this.container = text;
    }

    @SuppressLint("CheckResult")
    @Override
    public Drawable getDrawable(String source) {

        final Drawable[] drawable = new Drawable[1];

        GlideApp.with(c).load(source).fitCenter().into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource != null) {
                    drawable[0] = resource;
                    drawable[0].setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                    container.invalidate();
                    container.setText(container.getText());
                }
            }
        });
        return drawable[0];
    }
}
