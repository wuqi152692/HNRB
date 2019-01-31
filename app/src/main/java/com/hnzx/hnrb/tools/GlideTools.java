package com.hnzx.hnrb.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hnzx.hnrb.loader.GlideApp;

/**
 *Glide 加载图片工具
 */
public class GlideTools {

    public static void onDestory(Context context) {
        if (context != null) {
            GlideApp.get(context).clearMemory();
        }
    }

    /**
     * 正常加载
     *
     * @param context
     * @param url
     * @param view
     * @param resourceId      默认图类型
     */
    public static void GlideGif(Context context, String url, ImageView view, int resourceId) {
        GlideApp.with(context).load(url).placeholder(resourceId).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    public static void GlideNofit(Context context, String imgUrl, ImageView view, int resourceId) {
        GlideGif(context,imgUrl,view,resourceId);
    }

    /**
     * 正常加载(centerCrop)
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void GlideNoId(Context context, String url, ImageView imageView) {
        GlideApp.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 正常加载
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void glide(Context context, String url, ImageView imageView, int type) {
        GlideApp.with(context).load(url).placeholder(type).centerCrop().into(imageView);
    }

    /**
     * 正常加载
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void Glide(Context context, String url, ImageView imageView, int type) {
        GlideApp.with(context).load(url).placeholder(type).centerCrop().into(imageView);
    }

    /**
     * 加载圆形图片(支持)
     *
     * @param context
     * @param url
     * @param view
     * @param resourceId      默认图类型
     */
    public static void GlideRound(Context context, String url, ImageView view, int resourceId) {
        GlideApp.with(context).load(url).placeholder(resourceId).circleCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    /**
     * 加载圆角图片（可支持定义显示角度）
     *
     * @param context
     * @param url
     * @param view
     * @param resourceId      默认图类型
     * @param rounded   圆角角度
     */
    public static void GlideRounded(Context context, String url, ImageView view, int resourceId, int rounded) {
        GlideApp.with(context).load(url).placeholder(resourceId)
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(rounded))).into(view);
    }
}
