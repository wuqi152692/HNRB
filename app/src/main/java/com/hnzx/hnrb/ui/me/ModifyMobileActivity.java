package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.view.TopHeadView;

/**
 * Created by FoAng on 17/4/24 下午9:24;
 * 修改绑定手机号
 */
public class ModifyMobileActivity extends BaseActivity {

    private EditText mEditTextMobile;

    private Button mButtonSubmit;

    private TopHeadView mTopHeadView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_modify_mobile;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mEditTextMobile = getViewById(R.id.modify_mobile_phone);
        mButtonSubmit = getViewById(R.id.modify_mobile_submit);
        mTopHeadView = getViewById(R.id.modify_mobile_headView);
    }

    @Override
    protected void initData() {
        UserInfoRsp userInfoRsp = App.getInstance().getLoginInfo();
        if (userInfoRsp != null) {
            final String userName = userInfoRsp.username;
            mEditTextMobile.setHint(getSubMobileString(userName));
        }
        mTopHeadView.setHeadTitle("修改手机号");
    }

    private String getSubMobileString(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        } else {
            return mobile.substring(0, 3) + "*****" + mobile.substring(mobile.length() - 3, mobile.length());
        }
    }

    @Override
    protected void initListeners() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMobileNext();
            }
        });
    }

    private void submitMobileNext() {
        final String mobile = mEditTextMobile.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showTopToast("请输入手机号", true);
            return;
        }
        UserInfoRsp userInfoRsp = App.getInstance().getLoginInfo();
        if (userInfoRsp != null && !TextUtils.isEmpty(userInfoRsp.username) && mobile.equals(userInfoRsp.username)) {
            startActivity(BindMobileActivity.newIntent(this, BindMobileActivity.class));
            finish();
        } else {
            showTopToast("手机号验证错误", false);
            return;
        }
    }


}
