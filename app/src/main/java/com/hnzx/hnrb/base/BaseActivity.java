package com.hnzx.hnrb.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.transition.Explode;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.aitangba.swipeback.SwipeBackHelper;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.jpush.JPushReceiver;
import com.hnzx.hnrb.view.TopSnackBar;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by Administrator on 2017/3/13 0013.
 * 支持侧滑 可关闭
 */

public abstract class BaseActivity extends AutoLayoutActivity implements SwipeBackHelper.SlideBackManager {

    public static final String BUNDLE_KEY_LOGIN_STATUS = "BUNDLE_KEY_LOGIN_STATUS";

    private int lastLoginStatus;

    protected boolean isFromJPush;

    protected boolean isFirstRun = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
            getWindow().setEnterTransition(new Explode().setDuration(800));
        this.setContentView(this.getLayoutId());
        this.initViews(savedInstanceState);
        this.initData();
        this.initListeners();
        lastLoginStatus = App.getInstance().isLogin() ? App.LOGIN_STATUS_ONLINE : App.LOGIN_STATUS_OFFLINE;
        try {
            isFromJPush = getIntent().getBooleanExtra(JPushReceiver.TAG, false);
            App.getInstance().setPushOpen(isFromJPush);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Intent newIntent(Context mContext, Class mClass) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, mClass);
        return mIntent;
    }

    protected <T extends View> T getViewById(@IdRes int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 填充 layout id
     *
     * @return layout id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化UI
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化activity data
     */
    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
        int loginStatus = App.getInstance().isLogin() ? App.LOGIN_STATUS_ONLINE : App.LOGIN_STATUS_OFFLINE;
        if (loginStatus != lastLoginStatus) onLoginStatusInvalidate(App.getInstance().isLogin());
        lastLoginStatus = loginStatus;

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onResume(this);
    }

    /**
     * 用户登录状态发生变化，如需要更新用户信息状态，则重写此方法
     *
     * @param status
     */
    protected void onLoginStatusInvalidate(boolean status) {
    }

    /**
     * 初始化view listenter
     */
    protected abstract void initListeners();

    public void showToast(String msg) {
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示顶部ToastBar弹出提示
     *
     * @param content
     */
    public void showTopToast(String content, boolean isPositive) {
        TopSnackBar.make(findViewById(android.R.id.content), content, TopSnackBar.LENGTH_SHORT, isPositive).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFromJPush) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        App.getInstance().setPushOpen(false);
        finish();
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private static final String TAG = "SwipeBackActivity";

    private SwipeBackHelper mSwipeBackHelper;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        if (mSwipeBackHelper == null) {
            mSwipeBackHelper = new SwipeBackHelper(this);
        }
        return mSwipeBackHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public Activity getSlideActivity() {
        return this;
    }

    @Override
    public boolean supportSlideBack() {
        return true;
    }

    @Override
    public boolean canBeSlideBack() {
        return true;
    }

    @Override
    public void finish() {
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper.finishSwipeImmediately();
            mSwipeBackHelper = null;
        }
        super.finish();
    }
}
