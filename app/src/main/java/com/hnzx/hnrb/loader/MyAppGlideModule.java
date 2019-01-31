package com.hnzx.hnrb.loader;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author: mingancai
 * @Time: 2018/2/23 0023.
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    private boolean isOpenLowMemoryMode = true;

    public boolean isOpenLowMemoryMode() {
        return isOpenLowMemoryMode;
    }

    public void setOpenLowMemoryMode(boolean openLowMemoryMode) {
        isOpenLowMemoryMode = openLowMemoryMode;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        if (isOpenLowMemoryMode) {
            // 对应的低内存模式，但是也存在对于一些图片存在画质的问题，如包括条纹(banding)以及着色(tinting)问题
            builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
        } else {
            super.applyOptions(context, builder);
        }
    }
}
