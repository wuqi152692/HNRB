package com.hnzx.hnrb.ui.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.ModifyNickReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.view.TopHeadView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FoAng on 17/4/24 下午9:25;
 * 修改性别页面
 */

public class ModifyNickActivity extends BaseActivity {

    private static final String BUNDLE_KEY_NICK_NAME = "BUNDLE_KEY_NICK_NAME";

    private TopHeadView mTopHeadView;

    private EditText mEditTextNickName;

    private Button mButtonSubmit;

    private String originNickName;

    public static final Intent newIntent(Context mContext, String nickName) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, ModifyNickActivity.class);
        mIntent.putExtra(BUNDLE_KEY_NICK_NAME, nickName);
        return mIntent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_modify_nick;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTopHeadView = getViewById(R.id.modify_nick_headView);
        mEditTextNickName = getViewById(R.id.modify_nick_name);
        mButtonSubmit = getViewById(R.id.modify_nick_submit);
    }

    @Override
    protected void initData() {
        mTopHeadView.setHeadTitle("修改昵称");
        originNickName = getIntent().getStringExtra(BUNDLE_KEY_NICK_NAME);
        if (!TextUtils.isEmpty(originNickName)) mEditTextNickName.setText(originNickName);
        mEditTextNickName.setSelection(mEditTextNickName.getEditableText().toString().length());
    }

    @Override
    protected void initListeners() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyNickName();
            }
        });
    }

    private void modifyNickName() {
        final String nickName = mEditTextNickName.getEditableText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            showTopToast("手机号不能为空",false);
            return;
        } else if (!TextUtils.isEmpty(originNickName) && originNickName.equals(nickName)) {
            showTopToast("昵称相同无需修改",false);
            return;
        }

        final UserInfoRsp userInfoRsp = App.getInstance().getLoginInfo();
        ModifyNickReq nickReq = new ModifyNickReq();

        Map<String, String> modifyReq = new HashMap<String, String>(){{
            put("nickname", nickName);
        }};
        App.getInstance().requestJsonDataPost(modifyReq, nickReq, new Response.Listener<BaseBeanRsp<String>>() {
            @Override
            public void onResponse(BaseBeanRsp<String> response) {
                if (response != null && response.Status == 1) {
                    showTopToast("修改成功",true);
                    userInfoRsp.nickname = nickName;
                    App.getInstance().saveLoginUserInfo(userInfoRsp);

                    mEditTextNickName.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(Activity.RESULT_OK);
                            ModifyNickActivity.this.finish();
                        }
                    }, 500);

                } else {
                    showTopToast(response != null && !TextUtils.isEmpty(response.Message) ? response.Message :
                        "修改失败",false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast("修改失败：" + error.getMessage(),false);
            }
        });

    }

}
