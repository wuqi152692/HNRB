package com.hnzx.hnrb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author: mingancai
 * @Time: 2017/4/24 0024.
 */

public class NSRecyclerView extends RecyclerView {
    public NSRecyclerView(Context context) {
        super(context);
    }

    public NSRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NSRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}
