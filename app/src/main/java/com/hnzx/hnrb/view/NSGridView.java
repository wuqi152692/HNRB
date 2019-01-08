package com.hnzx.hnrb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class NSGridView extends GridView {
    public NSGridView(Context context) {
        super(context);
    }

    public NSGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NSGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
