package com.hnzx.hnrb.ui.me;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.SendSmsCodeReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.tools.RegularUtil;

/**
 * 注册账号
 * Created by FoAng on 17/4/26 下午2:10;
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener{

    public static final String BUNDLE_KEY_MESSAGE_TIME = "BUNDLE_KEY_MESSAGE_TIME";

    public static final int MESSAGE_CODE_REFRESH_COUNT = 0x01 << 1;

    public static final int MESSAGE_CODE_DONE = 0x01 << 2;

    private static final int MAX_TIME = 90;

    private LinearLayout mLinearLayoutSendCode;
    private TextView mTextViewSendCode;
    private RelativeLayout mRelativeLayoutSmsCode;
    private TextView mTextViewTime;
    private EditText mEditTextMobile;
    private EditText mEditTextCode;
    private Button mButtonRegister;
    private boolean isShowCount;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        mLinearLayoutSendCode = getViewById(R.id.register_linea_smsCode);
        mTextViewSendCode = getViewById(R.id.register_text_sendCode);
        mRelativeLayoutSmsCode = getViewById(R.id.register_relative_smsCode);
        mTextViewTime = getViewById(R.id.register_text_time);
        mEditTextMobile = getViewById(R.id.register_edit_mobile);
        mEditTextCode = getViewById(R.id.register_edit_smsCode);
        mButtonRegister = getViewById(R.id.register_button_register);
    }

    @Override
    protected void initListeners() {
        mTextViewSendCode.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch(viewId) {
            case R.id.register_text_sendCode:
                sendRegisterSmsCode();
                break;
            case R.id.register_text_time:
                break;
            case R.id.register_button_register:
                configLoginPassWord();
                break;
        }
    }

    private void configLoginPassWord() {
        final String mobileString = mEditTextMobile.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mobileString)) {
            showTopToast("手机号不能为空",false);
            return;
        } else if (!RegularUtil.isMobile(mobileString)) {
            showTopToast("请输入正确手机号",false);
            return;
        }

        final String smsCode = mEditTextCode.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mobileString)) {
            showTopToast("验证码不能为空",false);
            return;
        }
        startActivity(ModifyPwdActivity.newIntent(getContext(), true, smsCode, mobileString));
    }

    /**
     * 发送注册验证码
     */
    private void sendRegisterSmsCode() {
        final String mobileString = mEditTextMobile.getEditableText().toString().trim();
        if (mEditTextCode != null) mEditTextCode.setText("");
        if (TextUtils.isEmpty(mobileString)) {
            showTopToast("手机号不能为空",false);
            return;
        } else if (!RegularUtil.isMobile(mobileString)) {
            showTopToast("请输入正确手机号",false);
            return;
        }
        SendSmsCodeReq sendSmsCodeReq = new SendSmsCodeReq();
        sendSmsCodeReq.forward = SendSmsCodeReq.FORWARD_REGISTER;
        sendSmsCodeReq.mobile = mobileString;
        App.getInstance().requestJsonDataGet(sendSmsCodeReq, new SendSmsCodeDataListener(), new MyErrorListener("发送验证码错误"));
    }

    /**
     * 发送注册短信验证码回调
     */
    private class SendSmsCodeDataListener implements Response.Listener<BaseBeanRsp<Object>> {

        @Override
        public void onResponse(BaseBeanRsp response) {
            if (response != null && response.Status == 1) {
                showTopToast("发送成功",true);
                changeViewStatus(false);
            } else {
                showTopToast("发送失败",false);
            }
        }
    }

    /**
     * 改变当前验证码输入状态
     * @param isCanSend
     */
    private void changeViewStatus(boolean isCanSend) {
        mTextViewSendCode.setVisibility(isCanSend ? View.VISIBLE : View.GONE);
        mRelativeLayoutSmsCode.setVisibility(isCanSend ? View.GONE : View.VISIBLE);
        mCountHandler.removeCallbacks(countRunnable);
        this.isShowCount = !isCanSend;
        if (isShowCount) new Thread(countRunnable).start();
    }


    private Handler mCountHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int msgCode = msg.what;
            switch(msgCode) {
                case MESSAGE_CODE_DONE:
                    changeViewStatus(true);
                    break;
                case MESSAGE_CODE_REFRESH_COUNT:
                    final String timeCount = msg.getData().getString(BUNDLE_KEY_MESSAGE_TIME);
                    configTimeCountTips(timeCount);
                    break;
            }
        }
    };

    private void configTimeCountTips(String timeCount) {
        mTextViewTime.setVisibility(View.VISIBLE);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(timeCount);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#ff545b")), 0, timeCount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("秒后刷新");
        mTextViewTime.setText(builder);
    }

    private Runnable countRunnable = new Runnable() {
        @Override
        public void run() {
            int count = MAX_TIME;
            while(isShowCount) {
                synchronized(this) {
                    count --;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountHandler.removeCallbacks(countRunnable);
    }
}
