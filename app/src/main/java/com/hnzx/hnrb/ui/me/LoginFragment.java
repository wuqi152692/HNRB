package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.requestbean.GetFastLoginReq;
import com.hnzx.hnrb.requestbean.LoginReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.ui.LoginActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录
 * Created by FoAng on 17/4/26 下午2:10;
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {

    public static final String BUNDLE_KEY_IS_CLEAN_TOP = "BUNDLE_KEY_IS_CLEAN_TOP";

    private EditText mEditTextMobile;

    private EditText mEditTextPwd;

    private Button mButtonLogin;

    private TextView mTextViewFindPwd;

    private ImageView loginQQ, loginWechat, loginSina;

    private UMShareAPI mShareAPI = null;
    private String whereOauth;
    private boolean isCleanTop;

    public static LoginFragment newInstance(boolean isCleanTop) {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(BUNDLE_KEY_IS_CLEAN_TOP, isCleanTop);
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setArguments(mBundle);
        return loginFragment;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        mEditTextMobile = getViewById(R.id.login_edit_mobile);
        mEditTextPwd = getViewById(R.id.login_edit_pwd);
        mButtonLogin = getViewById(R.id.login_button_action);
        mTextViewFindPwd = getViewById(R.id.login_textView_findPwd);
        loginQQ = getViewById(R.id.loginQQ);
        loginWechat = getViewById(R.id.loginWechat);
        loginSina = getViewById(R.id.loginSina);
    }

    @Override
    protected void initListeners() {
        mButtonLogin.setOnClickListener(this);
        mTextViewFindPwd.setOnClickListener(this);
        loginQQ.setOnClickListener(this);
        loginWechat.setOnClickListener(this);
        loginSina.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        Bundle mBundle = getArguments();
        isCleanTop = (mBundle != null && mBundle.getBoolean(BUNDLE_KEY_IS_CLEAN_TOP));
        mShareAPI = UMShareAPI.get(mActivity);
        mEditTextPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*final String pwd = mEditTextPwd.getEditableText().toString();
                mTextViewFindPwd.setVisibility(TextUtils.isEmpty(pwd) ? View.GONE : View.VISIBLE);*/
            }
        });
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.login_button_action:
                // 用户登录
                doLoginAction();
                break;
            case R.id.login_textView_findPwd:
                // 找回密码
                startActivity(FindPwdActivity.newIntent(getContext()));
                break;
            case R.id.loginQQ:
                whereOauth = "qq";
                mShareAPI.doOauthVerify(mActivity, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.loginWechat:
                whereOauth = "wechat";
                mShareAPI.doOauthVerify(mActivity, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.loginSina:
                whereOauth = "sina";
                mShareAPI.doOauthVerify(mActivity, SHARE_MEDIA.SINA, umAuthListener);
                break;
        }
    }


    private void doLoginAction() {
        final String mobile = mEditTextMobile.getEditableText().toString().trim();
        final String pwd = mEditTextPwd.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showTopToast("手机号不能为空", false);
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            showTopToast("密码不能为空", false);
            return;
        }
        HashMap<String, String> loginParams = new HashMap<String, String>() {{
            put("username", mobile);
            put("password", pwd);

        }};
        // 登录账号
        App.getInstance().requestJsonDataPost(loginParams, new LoginReq(), new Response.Listener<BaseBeanRsp<UserInfoRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<UserInfoRsp> response) {
                if (response != null && response.Status == 1) {
                    UserInfoRsp userInfo = response.Info;
                    if (userInfo != null) {
                        showTopToast("登录成功", true);
                        App.getInstance().saveLoginUserInfo(userInfo);
                        mEditTextMobile.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isCleanTop) {
                                    getActivity().startActivity(MainActivity.newIntent(getActivity(), true));
                                } else {
                                    getActivity().finish();
                                }
                            }
                        }, 600);
                    } else {
                        showTopToast(response.Message, false);
                    }
                } else {
                    showTopToast(response == null || TextUtils.isEmpty(response.Message) ? "登录失败" : response.Message, false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast("登录失败", false);
            }
        });
    }

    /**
     * 授权登录监听器
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            mShareAPI.getPlatformInfo(mActivity, platform, umGetUserInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("取消授权");
        }
    };

    private UMAuthListener umGetUserInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            GetFastLoginReq fastLogin = new GetFastLoginReq();
            HashMap<String, String> params = new HashMap<>();
            String headImg;
            for (String key : data.keySet()) {
                switch (key) {
                    case "profile_image_url":
                        headImg = data.get(key);
                        params.put("icon", headImg);
                        params.put("avatar", headImg);
                        break;
                    case "screen_name":
                        params.put("nickname", data.get(key));
                        break;
                    case "gender":
                        params.put("sex", data.get(key).equals("男") ? "1" : "0");
                        break;
                    case "openid":
                        fastLogin.openid = data.get(key);
                        break;
                    case "id":
                        fastLogin.openid = data.get(key);
                        break;
                    case "unionid":
                        fastLogin.openid = data.get(key);
                        break;
                    default:
                        break;
                }
            }
            otherLogin(fastLogin, params);
        }

        // 第三方登录
        void otherLogin(GetFastLoginReq fastLogin, HashMap<String, String> params) {
            App.getInstance().requestJsonDataGet(fastLogin, new loginListener(params), null);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showToast("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showToast("取消授权");
        }
    };

    private class loginListener implements Response.Listener<BaseBeanRsp<UserInfoRsp>> {
        private HashMap<String, String> params;

        public loginListener(HashMap<String, String> params) {
            this.params = params;
        }

        @Override
        public void onResponse(BaseBeanRsp<UserInfoRsp> response) {
            if (response != null) {

                if (response.Status == 1) {
                    showTopToast("登录成功", true);
                    App.getInstance().saveLoginUserInfo(response.Info);
                    mEditTextMobile.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().finish();
                        }
                    }, 600);
                } else {
                    BindMobileActivity.newIntent(mActivity, params);
                }
            }
        }
    }
}
