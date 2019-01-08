package com.hnzx.hnrb.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.view.TopSnackBar;

/**
 * @author: mingancai
 * @Time: 2017/3/24 0024.
 */

public abstract class BaseFragment extends Fragment {

    public static final String BUNDLE_KEY_LOGIN_STATUS = "BUNDLE_KEY_LOGIN_STATUS";

    protected Activity mActivity;
    protected View contentView;
    private int lastLoginStatus;

    protected boolean isFirstRun = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = getActivity();
        contentView = getContentView(inflater, container);
        /**
         * fragment未设置背景时存在点击事件透传bug
         */
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true && isOpenTouchEventIntercept();
            }
        });

        return contentView;
    }

    protected <T extends View> T getViewById(@IdRes int viewId) {
        return (T) contentView.findViewById(viewId);
    }

    /**
     * 是否开启拦截点击事件
     *
     * @return
     */
    protected boolean isOpenTouchEventIntercept() {
        return false;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initDatas();
        initListeners();
        lastLoginStatus = App.getInstance().isLogin() ? App.LOGIN_STATUS_ONLINE : App.LOGIN_STATUS_OFFLINE;
    }

    @Override
    public void onResume() {
        super.onResume();
        int loginStatus = App.getInstance().isLogin() ? App.LOGIN_STATUS_ONLINE : App.LOGIN_STATUS_OFFLINE;
        if (loginStatus != lastLoginStatus) onLoginStatusInvalidate(App.getInstance().isLogin());
        lastLoginStatus = loginStatus;
    }

    /**
     * 用户登录状态发生变化，如需要更新用户信息状态，则重写此方法
     *
     * @param status
     */
    protected void onLoginStatusInvalidate(boolean status) {
    }


    /**
     * set content view
     *
     * @param inflater
     * @return
     */
    protected abstract View getContentView(LayoutInflater inflater, ViewGroup container);

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
     * 显示顶部ToastBar弹出提示
     *
     * @param content
     */
    public void showTopToast(String content, boolean isPositive) {
        TopSnackBar.make(this.getActivity().findViewById(android.R.id.content), content, TopSnackBar.LENGTH_SHORT, isPositive).show();
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
     * show toast
     */
    protected void showToast(String msg) {
        if (msg != null) {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
