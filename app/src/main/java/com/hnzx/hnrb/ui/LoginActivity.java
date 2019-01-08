package com.hnzx.hnrb.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.flyco.tablayout.SlidingTabLayout;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.GetAdsLoginReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAdsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.me.LoginFragment;
import com.hnzx.hnrb.ui.me.RegisterFragment;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by FoAng on 17/4/26 下午1:47;
 */

public class LoginActivity extends BaseActivity {

    public static final String BUNDLE_KEY_IS_CLEAN_TOP = "BUNDLE_KEY_IS_CLEAN_TOP";

    private SlidingTabLayout mTabLayout;

    private ViewPager mViewPager;

    private LoginAdapter mLoginAdapter;

    private ImageView mImageViewBack;

    private ImageView loginAdImage;

    private boolean isCleanTop;

    private GetAdsRsp ads;

    public static Intent newIntent(Context mContext, boolean isCleanTop) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, LoginActivity.class);
        if (isCleanTop)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtra(BUNDLE_KEY_IS_CLEAN_TOP, isCleanTop);
        return mIntent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTabLayout = getViewById(R.id.login_tabLayout);
        mViewPager = getViewById(R.id.login_viewPager);
        mImageViewBack = getViewById(R.id.login_top_back);
        loginAdImage = getViewById(R.id.loginAdImage);
        AutoUtils.auto(mTabLayout);
    }

    @Override
    protected void initData() {
        isCleanTop = getIntent().getBooleanExtra(BUNDLE_KEY_IS_CLEAN_TOP, false);
        mLoginAdapter = new LoginAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mLoginAdapter);
        mTabLayout.setViewPager(mViewPager);

        App.getInstance().requestJsonDataGet(new GetAdsLoginReq(), new Response.Listener<BaseBeanRsp<GetAdsRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<GetAdsRsp> response) {
                if (response != null && response.Status == 1) {
                    GlideTools.Glide(LoginActivity.this, response.Info.thumb, loginAdImage, R.drawable.icon_default_blue);
                    ads = response.Info;
                }
            }
        }, null);
    }

    @Override
    protected void initListeners() {
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ads != null)
                    ADSelect.goWhere(LoginActivity.this, ads,false);
            }
        });
    }

    /**
     * 首页登录以及注册适配器
     */
    private class LoginAdapter extends FragmentPagerAdapter {

        private String[] resource = new String[]{"登录", "注册"};


        public void updateResource(String[] items) {
            this.resource = items;
            this.notifyDataSetChanged();
        }

        public LoginAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new LoginFragment();
            } else if (position == 1) {
                return new RegisterFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return resource == null ? 0 : resource.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position <= resource.length - 1 ? resource[position] : "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
