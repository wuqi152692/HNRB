package com.hnzx.hnrb.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 *
 */
public class GlideTools {

    public static void GlideGif(Context context, String imgUrl, ImageView view, int resourceId) {
        if (imgUrl.contains(".gif"))
            Glide.with(context).load(imgUrl).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(resourceId).fitCenter()
                    .into(view);
        else
            Glide(context, imgUrl, view, resourceId);

    }

    public static void GlideNofit(Context context, String imgUrl, ImageView view, int resourceId) {
        if (imgUrl.contains(".gif"))
            Glide.with(context).load(imgUrl).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(resourceId)
                    .into(view);
        else
            Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(resourceId).into(view);

    }

    public static void GlideNoId(Context context, String imgUrl, ImageView view) {
        if (imgUrl.contains(".gif"))
            Glide.with(context).load(imgUrl).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        else
            Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);

    }

    public static void Glide(Context context, String imgUrl, ImageView view, int resourceId) {
        Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(resourceId).dontAnimate().into(view);
    }

    public static void Glide(Context context, String imgUrl, ImageView view, Drawable resource) {
        Glide.with(context).load(imgUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(resource).dontAnimate().into(view);
    }

    /**
     * Glide 加载圆行图片
     *
     * @param imgUrl
     * @param view
     * @param resourceId
     */
    public static void GlideRound(Context context, String imgUrl, ImageView view, int resourceId) {
        Glide.with(context).load(imgUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(resourceId).centerCrop()
                .into(new BitmapImageViewTarget(view) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        view.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * Glide 加载圆角图片
     *
     * @param imgUrl
     * @param view
     * @param resourceId
     */
    public static void GlideRounded(Context context, String imgUrl, ImageView view, int resourceId, final int px) {
        if (!imgUrl.endsWith(".gif"))
            Glide.with(context).load(imgUrl).asBitmap().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(resourceId).centerCrop()
                    .into(new BitmapImageViewTarget(view) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable roundedBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(view.getContext().getResources(), resource);
                            roundedBitmapDrawable.setCornerRadius(px);//设置圆角半径（根据实际需求）
                            roundedBitmapDrawable.setAntiAlias(true);  //设置反走样
                            view.setImageDrawable(roundedBitmapDrawable);
                        }
                    });
        else
            Glide.with(context).load(imgUrl).asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(resourceId).fitCenter()
                    .into(view);
    }


}
