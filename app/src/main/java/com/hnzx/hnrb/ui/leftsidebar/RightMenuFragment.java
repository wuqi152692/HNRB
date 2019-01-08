package com.hnzx.hnrb.ui.leftsidebar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.requestbean.GetCheckUserMessageReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.twocode.CaptureActivity;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.SearchActivity;
import com.hnzx.hnrb.ui.dialog.InvitedCodeDialog;
import com.hnzx.hnrb.ui.me.MyActivityActivity;
import com.hnzx.hnrb.ui.me.MyCollectActivity;
import com.hnzx.hnrb.ui.me.MyMessageActivity;
import com.hnzx.hnrb.ui.me.PersonInfoActivity;
import com.hnzx.hnrb.ui.me.SettingActivity;
import com.hnzx.hnrb.ui.me.UserScanActivity;
import com.hnzx.hnrb.ui.me.UserSignActivity;
import com.hnzx.hnrb.ui.me.UserTaskActivity;
import com.hnzx.hnrb.ui.news.NewsFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 首页右侧个人中心页面
 * Created by FoAng on 17/4/28 下午3:17;
 */
public class RightMenuFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mImageViewScan;
    private ImageView mImageViewHeadView;
    private TextView mTextViewUserName;
    private TextView mTextViewVip;
    private TextView mTextViewMessageNum;

    private FrameLayout mFrameLayoutSetting;

    private int messageNum = 0;

    private InvitedCodeDialog dialog;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_right_menu, container, false);
    }

    @Override
    protected boolean isOpenTouchEventIntercept() {
        return true;
    }

    @Override
    protected void initViews(View contentView) {
        mImageViewHeadView = getViewById(R.id.right_menu_headView);
        mTextViewUserName = getViewById(R.id.right_menu_userName);
        mTextViewVip = getViewById(R.id.right_menu_userStatus);
        mImageViewScan = getViewById(R.id.right_menu_scan);

        mFrameLayoutSetting = getViewById(R.id.right_menu_setting);
        mTextViewMessageNum = getViewById(R.id.messageNum);

        getContext().registerReceiver(myBroadcastReceiver, new IntentFilter(PersonInfoActivity.ACTION_STRING_CHANGE_AVATAR));
    }

    @Override
    protected void onLoginStatusInvalidate(boolean status) {
        super.onLoginStatusInvalidate(status);
        updateUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (myBroadcastReceiver != null) {
                getContext().unregisterReceiver(myBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(PersonInfoActivity.ACTION_STRING_CHANGE_AVATAR)) {
                updateUserInfo();
            }
        }
    };

    private void updateUserInfo() {
        UserInfoRsp loginInfo = App.getInstance().getLoginInfo();
        if (loginInfo != null && App.getInstance().isLogin()) {
            mTextViewUserName.setVisibility(TextUtils.isEmpty(loginInfo.nickname) ? View.GONE : View.VISIBLE);
            mTextViewUserName.setText(TextUtils.isEmpty(loginInfo.nickname) ? "" : loginInfo.nickname);
            final String headUrl = loginInfo.avatar;
            GlideTools.GlideRound(getContext(), headUrl, mImageViewHeadView, R.drawable.icon_default_round_head);
            mTextViewVip.setText(loginInfo.is_vip == 1 ? "已认证" : "未认证");
        } else {
            GlideTools.GlideRound(getContext(), null, mImageViewHeadView, R.drawable.icon_default_round_head);
            mTextViewUserName.setText("未登录");
            mTextViewVip.setText("未认证");
        }
    }

    @Override
    protected void initListeners() {
        mImageViewHeadView.setOnClickListener(this);
        mFrameLayoutSetting.setOnClickListener(this);
        mImageViewScan.setOnClickListener(this);

        getViewById(R.id.right_menu_userScan).setOnClickListener(this);
        getViewById(R.id.right_menu_userTask).setOnClickListener(this);
        getViewById(R.id.right_menu_userSign).setOnClickListener(this);
        getViewById(R.id.right_menu_collect).setOnClickListener(this);
        getViewById(R.id.right_menu_message).setOnClickListener(this);
        getViewById(R.id.right_menu_activity).setOnClickListener(this);
        getViewById(R.id.right_menu_subject).setOnClickListener(this);
        getViewById(R.id.right_menu_invited_code).setOnClickListener(this);

        getViewById(R.id.right_menu_search).setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        GetCheckUserMessageReq req = new GetCheckUserMessageReq();
        req.timestamp = SharePreferenceTool.get(mActivity, MyMessageActivity.MESSAGE_TIMESTAMP, "0");

        App.getInstance().requestJsonDataGet(req, new CheckUserMessageListener(), null);
        final UserInfoRsp mUserInfo = App.getInstance().getLoginInfo();
        GlideTools.GlideRound(getContext(), mUserInfo == null || !App.getInstance().isLogin()
                ? null : mUserInfo.avatar, mImageViewHeadView, R.drawable.icon_default_round_head);

        updateUserInfo();
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.right_menu_search:
                IntentUtil.startActivity(mActivity, SearchActivity.class);
                break;
            case R.id.right_menu_scan:
                startActivity(CaptureActivity.newIntent(getContext(), CaptureActivity.class));
                break;
            case R.id.right_menu_headView:
                if (App.getInstance().isLogin()) {
                    startActivity(PersonInfoActivity.newIntent(getContext(), PersonInfoActivity.class));
                } else {
                    startActivity(LoginActivity.newIntent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.right_menu_setting:
                startActivity(SettingActivity.newIntent(getContext(), SettingActivity.class));
                break;
            case R.id.right_menu_userScan:
                if (App.getInstance().isLogin())
                    IntentUtil.startActivity(mActivity, UserScanActivity.class);
                else {
                    startActivity(LoginActivity.newIntent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.right_menu_userTask:
                if (App.getInstance().isLogin())
                    IntentUtil.startActivity(mActivity, UserTaskActivity.class);
                else {
                    startActivity(LoginActivity.newIntent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.right_menu_userSign:
                if (App.getInstance().isLogin())
                    IntentUtil.startActivity(mActivity, UserSignActivity.class);
                else {
                    startActivity(LoginActivity.newIntent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.right_menu_collect:
                if (App.getInstance().isLogin())
                    IntentUtil.startActivity(mActivity, MyCollectActivity.class);
                else
                    startActivity(LoginActivity.newIntent(getContext(), LoginActivity.class));
                break;
            case R.id.right_menu_message:
                if (App.getInstance().isLogin()) {
                    startActivityForResult(MyMessageActivity.newIntent(mActivity, messageNum), 0);
                    mTextViewMessageNum.setVisibility(View.GONE);
                } else {
                    startActivity(LoginActivity.newIntent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.right_menu_activity:
                if (App.getInstance().isLogin()) {
                    IntentUtil.startActivity(mActivity, MyActivityActivity.class);
                } else {
                    startActivity(LoginActivity.newIntent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.right_menu_subject:
                ((MainActivity) mActivity).drawerLayout.closeDrawer(GravityCompat.END);
                ((NewsFragment) ((MainActivity) mActivity).getSupportFragmentManager().
                        findFragmentByTag(String.valueOf(R.id.home))).showSubjectFragment();
                break;
            case R.id.right_menu_invited_code:
                if (dialog == null) {
                    dialog = new InvitedCodeDialog();
                }
                dialog.show(mActivity.getFragmentManager(), getClass().getName());
                break;
        }
    }

    private class CheckUserMessageListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                try {
                    String num = (new JSONObject(response.Info)).getString("result");
                    if (num != null && num.length() > 0 && Integer.parseInt(num) > 0) {
                        messageNum = Integer.parseInt(num);
                        mTextViewMessageNum.setText((new JSONObject(response.Info)).getString("result"));
                        mTextViewMessageNum.setVisibility(View.VISIBLE);
                    } else mTextViewMessageNum.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0x02) {
            mTextViewMessageNum.setVisibility(View.GONE);
        }
    }
}
