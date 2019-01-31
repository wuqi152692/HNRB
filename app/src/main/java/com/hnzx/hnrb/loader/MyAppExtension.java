package com.hnzx.hnrb.loader;

import android.annotation.SuppressLint;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.annotation.GlideType;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author: mingancai
 * @Time: 2018/2/23 0023.
 */
@GlideExtension
public class MyAppExtension {
    private static final int MINI_THUMB_SIZE = 100;

    private static final RequestOptions DECODE_TYPE_GIF = RequestOptions.decodeTypeOf(GifDrawable.class).lock();


    private MyAppExtension() {
    } // utility class

    /**
     * 扩展加载ImageView方法
     *
     * @param options
     */
    @SuppressLint("CheckResult")
    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options.fitCenter().override(MINI_THUMB_SIZE);
    }

    /**
     * 扩展加载图片类型
     *
     * @param requestBuilder
     */
    @GlideType(GifDrawable.class)
    public static void asGifType(RequestBuilder<GifDrawable> requestBuilder) {
        requestBuilder
                .transition(new DrawableTransitionOptions())
                .apply(DECODE_TYPE_GIF);
    }
}
