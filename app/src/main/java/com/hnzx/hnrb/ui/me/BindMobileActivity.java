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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.BindMobileReq;
import com.hnzx.hnrb.requestbean.RegisterReq;
import com.hnzx.hnrb.requestbean.SendSmsCodeReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.tools.RegularUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FoAng on 17/4/24 下午9:24;
 * 绑定手机页面
 */
public class BindMobileActivity extends BaseActivity implements View.OnClickListener {

    public static final String BUNDLE_KEY_BIND_MOBILE = "BUNDLE_KEY_BIND_MOBILE";

    public static final String BUNDLE_KEY_MESSAGE_TIME = "BUNDLE_KEY_MESSAGE_TIME";

    public static final String OTHER_LOGIN_PARAMS = "OTHER_LOGIN_PARAMS";

    public static final int MESSAGE_CODE_REFRESH_COUNT = 0x01 << 1;

    public static final int MESSAGE_CODE_DONE = 0x01 << 2;

    private static final int MAX_TIME = 90;

    private EditText mEditTextMobile;

    private EditText mEditTextSmsCode;

    private TextView mTextViewSendCode;

    private Button mButtonSubmit;

    private boolean isShowCount;

    private boolean isOtherLoginBind;

    private String oleMobile;

    private HashMap<String, String> otherLoginParams;


    public static Intent newIntent(Context mContext, String oldMobile) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, BindMobileActivity.class);
        mIntent.putExtra(BUNDLE_KEY_BIND_MOBILE, oldMobile);
        return mIntent;
    }

    public static Intent newIntent(Context mContext, HashMap<String, String> params) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, BindMobileActivity.class);
        mIntent.putExtra(OTHER_LOGIN_PARAMS, params);
        return mIntent;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bind_mobile;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mEditTextMobile = getViewById(R.id.bind_mobile_mobile);
        mEditTextSmsCode = getViewById(R.id.bind_mobile_smsCode);
        mTextViewSendCode = getViewById(R.id.bind_mobile_send_smsCode);
        mButtonSubmit = getViewById(R.id.bind_mobile_submit);
    }

    @Override
    protected void initData() {
        otherLoginParams = (HashMap<String, String>) getIntent().getSerializableExtra(OTHER_LOGIN_PARAMS);
        if (otherLoginParams != null) {//不为null表示是来自第三方登录的手机号绑定
            isOtherLoginBind = true;
            return;
        }
        oleMobile = getIntent().getStringExtra(BUNDLE_KEY_BIND_MOBILE);
        if (TextUtils.isEmpty(oleMobile)) this.finish();
    }

    @Override
    protected void initListeners() {
        mButtonSubmit.setOnClickListener(this);
        mTextViewSendCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.bind_mobile_send_smsCode:
                sendFindPwdSmsCode();
                break;
            case R.id.bind_mobile_submit:
                submitChangeMobile();
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
            mTextViewSendCode.setText("获取验证码");
        } else {
            mTextViewSendCode.setText(String.format("%ss", count));
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
        sendSmsCodeReq.forward = isOtherLoginBind ? SendSmsCodeReq.FORWARD_REGISTER : SendSmsCodeReq.FORWARD_CHANGE_MOBILE;
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
     * 绑定手机号
     */
    private void submitChangeMobile() {
        final String mobile = mEditTextMobile.getEditableText().toString().trim();
        final String smsCode = mEditTextSmsCode.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showTopToast("手机号不能为空", false);
            return;
        } else if (TextUtils.isEmpty(smsCode)) {
            showTopToast("验证码不能为空", false);
            return;
        }

        if (isOtherLoginBind) {

            otherLoginParams.put("username", mobile);
            otherLoginParams.put("password", "123465a");
            otherLoginParams.put("mobile_verify", smsCode);

        } else {

            BindMobileReq bindMobileReq = new BindMobileReq();

            Map<String, String> bindParams = new HashMap<String, String>() {{
                put("mobile", mobile);
                put("mobile_verify", smsCode);
            }};

            App.getInstance().requestJsonDataPost(bindParams, bindMobileReq, new Response.Listener<BaseBeanRsp<String>>() {
                @Override
                public void onResponse(BaseBeanRsp<String> response) {
                    if (response != null && response.Status == 1) {
                        showTopToast("绑定成功", true);
                    } else {
                        showTopToast(response != null && !TextUtils.isEmpty(response.Message) ? response.Message : "", false);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showTopToast("绑定失败：" + error.getMessage(), false);
                }
            });

        }
    }

}
