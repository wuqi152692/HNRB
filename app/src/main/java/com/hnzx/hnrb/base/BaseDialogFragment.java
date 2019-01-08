package com.hnzx.hnrb.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hnzx.hnrb.view.TopSnackBar;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/11 0011.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    protected Activity mActivity;
    protected View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
        mActivity = getActivity();
        contentView = inflater.inflate(getContentView(), container, false);
        AutoUtils.auto(contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initDatas();
        initListeners();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * set content view
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * init view
     *
     * @param contentView
     * @return
     */
    protected abstract void initViews(View contentView);

    /***
     * set listeners
     */
    protected abstract void initListeners();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     * show toast
     */
    protected void showToast(String msg) {
        if (msg != null) {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示顶部ToastBar弹出提示
     *
     * @param content
     */
    public void showTopToast(String content, boolean isPositive) {
        TopSnackBar.make(mActivity.findViewById(android.R.id.content), content, TopSnackBar.LENGTH_SHORT, isPositive).show();
    }

    /**
     * 显示顶部ToastBar 弹出提示
     *
     * @param resId
     */
    public void showTopToast(@StringRes int resId, boolean isPositive) {
        String content = getResources().getString(resId);
        showTopToast(content, isPositive);
    }

    /**
     * 处理微信分享多次点击出现IllegalStateException
     *
     * @param manager
     * @param tag
     */
    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded() && !isVisible() && !isRemoving())
            super.show(manager, tag);
    }
}
