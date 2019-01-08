package com.hnzx.hnrb.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.FindPwdReq;
import com.hnzx.hnrb.requestbean.SendSmsCodeReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.tools.RegularUtil;
import com.hnzx.hnrb.view.TopHeadView;

/**
 * Created by FoAng on 17/4/24 下午9:22;
 * 找回密码页面
 */
public class FindPwdActivity extends BaseActivity implements View.OnClickListener {

    public static final String BUNDLE_KEY_IS_BIND_MOBILE = "BUNDLE_KEY_IS_BIND_MOBILE";

    public static final String BUNDLE_KEY_MESSAGE_TIME = "BUNDLE_KEY_MESSAGE_TIME";

    public static final int MESSAGE_CODE_REFRESH_COUNT = 0x01 << 1;

    public static final int MESSAGE_CODE_DONE = 0x01 << 2;

    private static final int MAX_TIME = 90;

    private TopHeadView mTopHeadView;

    private EditText mEditTextMobile;

    private EditText mEditTextSmsCode;

    private EditText mEditTextNewPwd;

    private TextView mTextViewSendSmsCode;

    private LinearLayout mLinearLayoutPwdContent;

    private Button mButtonSubmit;

    private boolean isShowCount;

    private boolean isBindMobile;

    public static Intent newIntent(Context mContext) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, FindPwdActivity.class);
        return mIntent;
    }

    /**
     * 是否为绑定手机
     *
     * @param mContext
     * @param isBindMobile
     * @return
     */
    public static Intent newIntent(Context mContext, boolean isBindMobile) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, FindPwdActivity.class);
        mIntent.putExtra(BUNDLE_KEY_IS_BIND_MOBILE, isBindMobile);
        return mIntent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_pwd;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTopHeadView = getViewById(R.id.find_pwd_topView);
        mEditTextMobile = getViewById(R.id.find_pwd_mobile);
        mEditTextSmsCode = getViewById(R.id.find_pwd_smsCode);
        mTextViewSendSmsCode = getViewById(R.id.find_pwd_sendSmsCode);
        mButtonSubmit = getViewById(R.id.find_pwd_submit);
        mEditTextNewPwd = getViewById(R.id.find_pwd_newPwd);
        mLinearLayoutPwdContent = getViewById(R.id.find_pwd_newPwdContent);
    }

    @Override
    protected void initData() {
        isBindMobile = getIntent().getBooleanExtra(BUNDLE_KEY_IS_BIND_MOBILE, false);
        mLinearLayoutPwdContent.setVisibility(isBindMobile ? View.GONE : View.VISIBLE);
        mTopHeadView.setHeadTitle(isBindMobile ? "绑定手机" : "忘记密码");
        mButtonSubmit.setText(isBindMobile ? "绑定" : "重置");
    }

    @Override
    protected void initListeners() {
        mButtonSubmit.setOnClickListener(this);
        mTextViewSendSmsCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.find_pwd_submit:
                submitFindPwd();
                break;
            case R.id.find_pwd_sendSmsCode:
                sendFindPwdSmsCode();
                break;
        }
    }

    /**
     * 发送验证码计时Handler
     */
    private Handler mCountHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int msgCode = msg.what;
            switch (msgCode) {
                case MESSAGE_CODE_DONE:
                    configCountTips(null);
                    break;
                case MESSAGE_CODE_REFRESH_COUNT:
                    final String count = msg.getData().getString(BUNDLE_KEY_MESSAGE_TIME);
                    configCountTips(count);
                    break;
            }
        }
    };

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            int count = MAX_TIME;
            while (isShowCount) {
                synchronized (this) {
                    count--;
                    Message message = Message.obtain();
                    message.getData().putString(BUNDLE_KEY_MESSAGE_TIME, String.valueOf(count));
                    if (count == 0) {
                        mCountHandler.sendEmptyMessage(MESSAGE_CODE_DONE);
                        isShowCount = false;
                    } else {
                        message.what = MESSAGE_CODE_REFRESH_COUNT;
                        mCountHandler.sendMessage(message);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void configCountTips(String count) {
        if (!isShowCount) {
            mTextViewSendSmsCode.setText("获取验证码");
        } else {
            mTextViewSendSmsCode.setText(String.format("%ss", count));
        }
    }

    /**
     * 发送找回密码短信验证码
     */
    private void sendFindPwdSmsCode() {
        if (isShowCount) return;
        final String mobile = mEditTextMobile.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showTopToast("手机号不能为空", false);
            return;
        } else if (!RegularUtil.isMobile(mobile)) {
            showTopToast("请输入正确手机号码", false);
            return;
        }
        SendSmsCodeReq sendSmsCodeReq = new SendSmsCodeReq();
        sendSmsCodeReq.forward = SendSmsCodeReq.FORWARD_FIND_PWD;
        sendSmsCodeReq.mobile = mobile;

        App.getInstance().requestJsonDataGet(sendSmsCodeReq, new Response.Listener<BaseBeanRsp<Object>>() {
            @Override
            public void onResponse(BaseBeanRsp<Object> response) {
                if (response != null && response.Status == 1) {
                    showTopToast("发送成功", true);
                    isShowCount = true;
                    new Thread(myRunnable).start();
                } else {
                    showTopToast("发送失败", false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast("发送失败：" + error.getMessage(), false);
            }
        });
    }

    /**
     * 提交找回新密码
     */
    private void submitFindPwd() {
        final String mobile = mEditTextMobile.getEditableText().toString().trim();
        final String smsCode = mEditTextSmsCode.getEditableText().toString().trim();
        final String newPwd = mEditTextNewPwd.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showTopToast("手机号不能为空", false);
            return;
        } else if (TextUtils.isEmpty(smsCode)) {
            showTopToast("验证码不能为空", false);
            return;
        } else if (TextUtils.isEmpty(newPwd)) {
            showTopToast("请输入新密码", true);
            return;
        } else if (!TextUtils.isEmpty(newPwd) && !RegularUtil.isPassWordValid(newPwd)) {
            showTopToast("密码必须为6位以上数字字母组合", false);
            return;
        }
        FindPwdReq mFindPwdReq = new FindPwdReq(mobile, newPwd, smsCode);
        App.getInstance().requestJsonDataGet(mFindPwdReq, new Response.Listener<BaseBeanRsp<String>>() {
            @Override
            public void onResponse(BaseBeanRsp<String> response) {
                if (response != null && response.Status == 1) {
                    showTopToast("重置密码成功，请重新登录",true);
                    mEditTextNewPwd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FindPwdActivity.this.finish();
                        }
                    }, 600);
                } else {
                    showTopToast("重置密码失败，请重试",false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast("重置密码失败：" + error.getMessage(),false);
            }
        });

    }
}
