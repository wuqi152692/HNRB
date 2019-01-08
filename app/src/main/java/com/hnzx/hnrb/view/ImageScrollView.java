package com.hnzx.hnrb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author: mingancai
 * @Time: 2017/6/13 0013.
 */

public class ImageScrollView extends ScrollView {
    public ImageScrollView(Context context) {
        super(context);
    }

    public ImageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t - oldt > 10) {
            fullScroll(ScrollView.FOCUS_DOWN);
        } else super.onScrollChanged(l, t, oldl, oldt);
    }
}
