package com.hnzx.hnrb.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.ModifyPwdReq;
import com.hnzx.hnrb.requestbean.RegisterReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.tools.RegularUtil;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.view.TopHeadView;

import java.util.HashMap;

/**
 * Created by FoAng on 17/4/24 下午9:21;
 * 修改密码页面
 */
public class ModifyPwdActivity extends BaseActivity {

    //注册账号
    public static final String BUNDLE_KEY_IS_REGISTER = "BUNDLE_KEY_IS_REGISTER";
    //注册账号短信验证码
    public static final String BUNDLE_KEY_SME_CODE = "BUNDLE_KEY_SME_CODE";

    public static final String BUNDLE_KEY_REGISTER_MOBILE = "BUNDLE_KEY_REGISTER_MOBILE";

    private LinearLayout mLinearLayoutOldPwd;
    private EditText mEditTextOldPwd;
    private EditText mEditTextNewPwd;
    private EditText mEditTextSure;
    private TopHeadView mTopHeadView;
    private Button mButtonSubmit;
    private boolean isRegisterPwd;
    private String mobile;
    private String smsCode;

    public static Intent newIntent(Context mContext, boolean isRegister, String smsCode, String mobile) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, ModifyPwdActivity.class);
        mIntent.putExtra(BUNDLE_KEY_IS_REGISTER, isRegister);
        mIntent.putExtra(BUNDLE_KEY_SME_CODE, smsCode);
        mIntent.putExtra(BUNDLE_KEY_REGISTER_MOBILE, mobile);
        return mIntent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_modify_pwd;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mEditTextOldPwd = getViewById(R.id.modify_old_pwd);
        mEditTextNewPwd = getViewById(R.id.modify_new_pwd);
        mEditTextSure = getViewById(R.id.modify_new_pwd_sure);
        mButtonSubmit = getViewById(R.id.modify_pwd_submit);
        mLinearLayoutOldPwd = getViewById(R.id.modify_old_pwd_content);
        mTopHeadView = getViewById(R.id.modify_old_pwd_head);

        isRegisterPwd = getIntent().getBooleanExtra(BUNDLE_KEY_IS_REGISTER, false);
        mLinearLayoutOldPwd.setVisibility(isRegisterPwd ? View.GONE : View.VISIBLE);
        mobile = getIntent().getStringExtra(BUNDLE_KEY_REGISTER_MOBILE);
        smsCode = getIntent().getStringExtra(BUNDLE_KEY_SME_CODE);

        mTopHeadView.setHeadTitle(isRegisterPwd ? "设置密码" : "修改密码");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRegisterPwd) {
                    registerAccount(mobile, smsCode);
                } else {
                    modifyNewPwd();
                }
            }
        });
    }

    private void registerAccount(final String mobile, final String smsCode) {
        final String newPwd = mEditTextNewPwd.getEditableText().toString().trim();
        final String newPwdSure = mEditTextSure.getEditableText().toString().trim();
        if (TextUtils.isEmpty(newPwd)) {
            showTopToast("请输入新密码", true);
            return;
        } else if (TextUtils.isEmpty(newPwdSure)) {
            showTopToast("请再次输入新密码", false);
            return;
        } else if (!newPwd.equals(newPwdSure)) {
            showTopToast("密码设置不一致", true);
            return;
        } else if (!RegularUtil.isPassWordValid(newPwd) || !RegularUtil.isPassWordValid(newPwdSure)) {
            showTopToast("密码输入无效，请重新输入", true);
            return;
        }

        RegisterReq registerReq = new RegisterReq();

        HashMap<String, String> params = new HashMap<String, String>() {{
            put("username", mobile);
            put("mobile_verify", smsCode);
            put("password", newPwdSure);
        }};

        App.getInstance().requestJsonDataPost(params, registerReq, new Response.Listener<BaseBeanRsp<UserInfoRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<UserInfoRsp> response) {
                if (response != null && response.Status == 1) {
                    finish();
                } else {
                    showTopToast(response.Message, false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast("注册用户失败，请重试", false);
            }
        });
    }

    /**
     * 修改新密码
     */
    private void modifyNewPwd() {
        final String originPwd = mEditTextOldPwd.getEditableText().toString().trim();
        final String newPwd = mEditTextNewPwd.getEditableText().toString().trim();
        final String newPwdSure = mEditTextSure.getEditableText().toString().trim();
        if (TextUtils.isEmpty(originPwd)) {
            showTopToast("请输入原密码", true);
            return;
        } else if (TextUtils.isEmpty(newPwd)) {
            showTopToast("请输入新密码", true);
            return;
        } else if (TextUtils.isEmpty(newPwdSure)) {
            showTopToast("请再次输入新密码", true);
            return;
        } else if (!newPwd.equals(newPwdSure)) {
            showTopToast("密码设置不一致", true);
            return;
        } else if (!RegularUtil.isPassWordValid(newPwd) || !RegularUtil.isPassWordValid(newPwdSure)) {
            showTopToast("密码输入无效，请重新输入", true);
            return;
        }

        ModifyPwdReq registerReq = new ModifyPwdReq();

        HashMap<String, String> params = new HashMap<String, String>() {{
            put("password", originPwd);
            put("new_password", newPwd);
            put("confirm_password", newPwdSure);
        }};

        App.getInstance().requestJsonDataPost(params, registerReq, new Response.Listener<BaseBeanRsp<String>>() {
            @Override
            public void onResponse(BaseBeanRsp<String> response) {
                if (response != null && response.Status == 1) {
                    showTopToast("修改密码成功，请重新登录", true);
                    mEditTextNewPwd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 修改密码成功后，跳转到主页面
                             */
                            startActivity(MainActivity.newIntent(ModifyPwdActivity.this, true));
                        }
                    }, 500);
                } else {
                    showTopToast("修改密码失败:" + (TextUtils.isEmpty(response.Message) ? "" : response.Message), false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast("修改密码失败，请重试", false);
            }
        });
    }


}
