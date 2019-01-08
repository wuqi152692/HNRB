package com.hnzx.hnrb.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.tools.LogUtil;

/**
 * 顶部通用导航栏
 * Created by FoAng on 17/5/4 下午4:22;
 */
public class TopHeadView extends LinearLayout implements View.OnClickListener {

    private View contentView;

    private View mImageViewBack;

    private TextView mTextViewTitle;

    private View mImageViewShare;

    public TopHeadView(Context context) {
        super(context);
        initView();
    }

    public TopHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TopHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_top_head_view, null);
        mImageViewBack = contentView.findViewById(R.id.top_head_back);
        mImageViewShare = contentView.findViewById(R.id.share);
        mTextViewTitle = (TextView) contentView.findViewById(R.id.top_head_title);
        addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mImageViewBack.setOnClickListener(this);
    }

    public void configViewLayout(@LayoutRes int layoutId) {
        if (layoutId != 0) this.removeAllViews();
        contentView = LayoutInflater.from(getContext()).inflate(layoutId, null);
        addView(contentView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mImageViewBack.setOnClickListener(this);
    }

    public View getContentView() {
        return contentView;
    }

    public void setHeadTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTextViewTitle.setText(title);
        }
    }

    public void setTopBackListener(View.OnClickListener listener) {
        if (listener == null) return;
        mImageViewBack.setOnClickListener(listener);
    }

    public void setTopShareListener(View.OnClickListener listener) {
        if (listener == null) return;
        mImageViewShare.setOnClickListener(listener);
        mImageViewShare.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.top_head_back:
                try {
                    Activity mActivity = (Activity) getContext();
                    mActivity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e(e.getMessage());
                }
                break;
        }
    }
}
