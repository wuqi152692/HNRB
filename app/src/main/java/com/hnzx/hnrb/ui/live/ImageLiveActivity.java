package com.hnzx.hnrb.ui.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.ImageLiveViewPagerAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetLiveContentReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetLiveContentRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsCommentDialog;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.ui.leftsidebar.LeftMenuFragment;
import com.hnzx.hnrb.ui.leftsidebar.RightMenuFragment;
import com.hnzx.hnrb.ui.news.NewsFragment;
import com.hnzx.hnrb.view.CustomViewpager;
import com.hnzx.hnrb.view.MScrollView;
import com.hnzx.hnrb.view.MultiStateView;
import com.hnzx.hnrb.view.pulltorefresh.PullToRefreshLayout;
import com.hnzx.hnrb.view.pulltorefresh.PullableScrollView;
import com.umeng.socialize.UMShareAPI;

public class ImageLiveActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private TextView title, msg, editMessage, popleNum;
    private CheckBox sortCTV;
    private ImageView other, image;
    private CheckBox showOrHideMsg;
    private PullableScrollView mScrollView;
    private RadioGroup radioGroup;
    private PullToRefreshLayout refreshLayout;

    private String live_id;
    private Bundle bundle;

    private GetLiveContentRsp info;

    private Fragment hallFragment, commentFragment;

    @Override
    protected int getLayoutId() {
        live_id = getIntent().getStringExtra(Constant.BEAN);
        bundle = new Bundle();
        bundle.putString(Constant.BEAN, live_id);
        return R.layout.activity_image_live;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.requestLayout();
        refreshLayout.setOnRefreshListener(new refreshListener());
        editMessage = (TextView) findViewById(R.id.edit_message);
        if (App.getInstance().isLogin() && App.getInstance().getLoginInfo().is_vip != 1) {
            editMessage.setVisibility(View.INVISIBLE);
        }
        sortCTV = (CheckBox) findViewById(R.id.sort);
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        title = (TextView) findViewById(R.id.title);
        msg = (TextView) findViewById(R.id.msg);
        other = (ImageView) findViewById(R.id.other);
        image = (ImageView) findViewById(R.id.image);
        other.setVisibility(View.VISIBLE);

        popleNum = getViewById(R.id.popleNum);

        showOrHideMsg = (CheckBox) findViewById(R.id.showOrHideMsg);

        mScrollView = (PullableScrollView) findViewById(R.id.mScrollView);
        mScrollView.setFocusable(true);
        mScrollView.setFocusableInTouchMode(true);

        mScrollView.requestFocus();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = ImageLiveHallFragment.newInstance(refreshLayout);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.contentLayout, fragment, String.valueOf(R.id.hallRB)).commit();
        }
    }

    @Override
    protected void initData() {
        GetLiveContentReq req = new GetLiveContentReq();
        req.live_id = live_id;

        App.getInstance().requestJsonDataGet(req, new dataListener(), new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.other).setOnClickListener(this);
        findViewById(R.id.return_top).setOnClickListener(this);
        findViewById(R.id.pinglunLayout).setOnClickListener(this);
        editMessage.setOnClickListener(this);
        sortCTV.setOnClickListener(this);
        other.setOnClickListener(this);
        showOrHideMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    msg.setVisibility(View.GONE);
                else
                    msg.setVisibility(View.VISIBLE);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                FragmentManager fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();

                hallFragment = fm.findFragmentByTag(String.valueOf(R.id.hallRB));

                commentFragment = fm.findFragmentByTag(String.valueOf(R.id.commentRB));

                if (hallFragment != null) ft.hide(hallFragment);

                if (commentFragment != null) ft.hide(commentFragment);
                switch (checkedId) {
                    case R.id.hallRB:
                        sortCTV.setVisibility(View.VISIBLE);
                        if (hallFragment == null) {
                            hallFragment = ImageLiveHallFragment.newInstance(refreshLayout);
                            hallFragment.setArguments(bundle);
                            ft.add(R.id.contentLayout, hallFragment, String.valueOf(R.id.hallRB));
                        } else {
                            ft.show(hallFragment);
                        }
                        break;
                    case R.id.commentRB:
                        sortCTV.setVisibility(View.GONE);
                        if (commentFragment == null) {
                            commentFragment = ImageLiveCommentFragment.newInstance(refreshLayout);
                            commentFragment.setArguments(bundle);
                            ft.add(R.id.contentLayout, commentFragment, String.valueOf(R.id.commentRB));
                        } else {
                            ft.show(commentFragment);
                        }
                        break;
                }
                ft.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_et:
            case R.id.pinglunLayout:
                if (!App.getInstance().isLogin()) {
                    startActivity(LoginActivity.newIntent(this, false));
                    return;
                }
                NewsCommentDialog dialog = NewsCommentDialog.newInstance(live_id, "", NewsCommentDialog.LIVE_COMMENT,"");
                dialog.show(getFragmentManager(), getLocalClassName());
                break;
            case R.id.return_top:
                returnTop();
                break;
            case R.id.edit_message:
                if (!App.getInstance().isLogin()) {
                    startActivity(LoginActivity.newIntent(this, false));
                    return;
                }
                startActivity(PublishActivity.newIntent(this, live_id));
                break;
            case R.id.sort:
                sort();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.other:
                share();
                break;
            default:
                break;
        }
    }

    private Intent sortIntent;

    private void returnTop() {
        mScrollView.scrollTo(0, 0);
    }

    private void sort() {
        sortCTV.setChecked(sortCTV.isChecked());
        if (sortIntent == null) {
            sortIntent = new Intent();
            sortIntent.setAction("sort");
        }
        sortIntent.putExtra(Constant.BEAN, sortCTV.isChecked());
        sendBroadcast(sortIntent);
    }

    NewsShareDialog dialog;

    private void share() {
        if (info == null) {
            showToast("分享失败");
            return;
        }
        if (dialog == null || dialog.isAdded())
            dialog = NewsShareDialog.newInstance(info.title, info.title, info.thumb, info.url);
        dialog.show(getFragmentManager(), getLocalClassName());
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetLiveContentRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetLiveContentRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                info = response.Info;
                addData(response.Info);
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }
    }

    private void addData(GetLiveContentRsp rsp) {
        popleNum.setText(rsp.joined + "人");
        GlideTools.Glide(this, rsp.thumb, image, R.drawable.bg_morentu_datumoshi);
        msg.setText(rsp.brief);
        title.setText(rsp.title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private Intent refreshIntent;

    private class refreshListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            if (refreshIntent == null) {
                refreshIntent = new Intent();
                refreshIntent.setAction("refresh");
            }
            refreshIntent.putExtra(Constant.BEAN, true);
            sendBroadcast(refreshIntent);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (refreshIntent == null) {
                refreshIntent = new Intent();
                refreshIntent.setAction("refresh");
            }
            refreshIntent.putExtra(Constant.BEAN, false);
            sendBroadcast(refreshIntent);
        }
    }
}
