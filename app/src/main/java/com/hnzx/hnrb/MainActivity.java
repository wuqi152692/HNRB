package com.hnzx.hnrb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.network.          GetData;
import com.hnzx.hnrb.requestbean.GetAdsPopupReq;
import com.hnzx.hnrb.requestbean.GetCheckUpdateReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAdsRsp;
import com.hnzx.hnrb.responsebean.GetCheckUpdateRsp;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.tools.TimeType;
import com.hnzx.hnrb.ui.audio.AudioFragment;
import com.hnzx.hnrb.ui.dialog.ADPopupDialog;
import com.hnzx.hnrb.ui.dialog.ExitAppDialog;
import com.hnzx.hnrb.ui.dialog.UpdateAppDialog;
import com.hnzx.hnrb.ui.government.GovernmentFragment;
import com.hnzx.hnrb.ui.interact.InteractFragment;
import com.hnzx.hnrb.ui.leftsidebar.LeftMenuFragment;
import com.hnzx.hnrb.ui.leftsidebar.RightMenuFragment;
import com.hnzx.hnrb.ui.live.LiveFragment;
import com.hnzx.hnrb.ui.news.NewsFragment;
import com.hnzx.hnrb.ui.web.WebActivity;
import com.zhy.autolayout.utils.AutoUtils;

import cn.jpush.android.api.JPluginPlatformInterface;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton homeRB, governmentRB, liveRB, audioRB, interactRB;
    public DrawerLayout drawerLayout;
    private Fragment fragmentNews, fragmentGoverment, fragmentLive, fragmentAudio, fragmentInteract;
    private View home_titlebar;

    private UpdateAppDialog dialog;
    private ADPopupDialog adDialog;

    private final String ADS_POPUP_SHOWTIME_KEY = "ADS_POPUP_SHOWTIME_KEY";
    private final String ADS_POPUP_SHOWTOTAL_KEY = "ADS_POPUP_SHOWTOTAL_KEY";

    private JPluginPlatformInterface pHuaweiPushInterface;

    public static Intent newIntent(Context mContext, boolean isCleanTop) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, MainActivity.class);
        if (isCleanTop)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return mIntent;
    }

    @Override
    public boolean supportSlideBack() {//不需要侧滑返回
        return false;
    }

    @Override
    protected int getLayoutId() {
        if (App.getInstance().getPushOpen())
            finish();
        pHuaweiPushInterface = new JPluginPlatformInterface(this.getApplicationContext());
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initView();
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new NewsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, fragment, String.valueOf(homeRB.getId())).commit();
            fragmentManager.beginTransaction().replace(R.id.left_drawer, new LeftMenuFragment(),
                    String.valueOf(R.id.left_drawer)).commit();
            fragmentManager.beginTransaction().replace(R.id.right_drawer, new RightMenuFragment(),
                    String.valueOf(R.id.right_drawer)).commit();
        }
    }

    @Override
    protected void initData() {
        AutoUtils.auto(homeRB);
        AutoUtils.auto(governmentRB);
        AutoUtils.auto(liveRB);
        AutoUtils.auto(audioRB);
        AutoUtils.auto(interactRB);
        /**
         * 显示版本检查
         */
        App.getInstance().requestJsonDataGet(new GetCheckUpdateReq(), new Response.Listener<BaseBeanRsp<GetCheckUpdateRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<GetCheckUpdateRsp> response) {
                if (response != null && response.Status == 1 && response.Info != null) {
                    if (response.Info.version > App.getInstance().getVersion()) {
                        if (dialog == null)
                            dialog = UpdateAppDialog.newInstance(response.Info);
                        dialog.show(getFragmentManager(), getLocalClassName() + "update");
                    }
                }
            }
        }, null);

        if (SharePreferenceTool.contains(MainActivity.this, ADS_POPUP_SHOWTIME_KEY)
                && SharePreferenceTool.contains(MainActivity.this, ADS_POPUP_SHOWTIME_KEY)
                && TimeType.getYMD().equals((String) SharePreferenceTool.get(this, ADS_POPUP_SHOWTIME_KEY, ""))
                && ((int) SharePreferenceTool.get(this, ADS_POPUP_SHOWTOTAL_KEY, 1)) == 3)
            return;
        App.getInstance().requestJsonArrayDataGet(new GetAdsPopupReq(), new Response.Listener<BaseBeanArrayRsp<GetAdsRsp>>() {
            @Override
            public void onResponse(BaseBeanArrayRsp<GetAdsRsp> response) {
                if (response != null && response.Status == 1 && response.Info != null && response.Info.size() > 0) {
                    changeAdsTimes();
                    if (adDialog == null)
                        adDialog = ADPopupDialog.newInstance(response.Info);
                    adDialog.show(getFragmentManager(), getLocalClassName() + "ad");
                }
            }
        }, null);
    }

    private void changeAdsTimes() {
        try {
            if (SharePreferenceTool.contains(MainActivity.this, ADS_POPUP_SHOWTIME_KEY)) {
                String temp = (String) SharePreferenceTool.get(MainActivity.this, ADS_POPUP_SHOWTIME_KEY, "");
                String time = TimeType.getYMD();
                int total = (int) SharePreferenceTool.get(MainActivity.this, ADS_POPUP_SHOWTOTAL_KEY, 1);
                if (temp.equals(time))
                    SharePreferenceTool.put(MainActivity.this, ADS_POPUP_SHOWTOTAL_KEY, total + 1);
                else {
                    SharePreferenceTool.put(MainActivity.this, ADS_POPUP_SHOWTIME_KEY, TimeType.getYMD());
                    SharePreferenceTool.put(MainActivity.this, ADS_POPUP_SHOWTOTAL_KEY, 1);
                }
            } else {
                SharePreferenceTool.put(MainActivity.this, ADS_POPUP_SHOWTIME_KEY, TimeType.getYMD());
                SharePreferenceTool.put(MainActivity.this, ADS_POPUP_SHOWTOTAL_KEY, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.home_menu).setOnClickListener(this);
        findViewById(R.id.home_pager).setOnClickListener(this);
        getViewById(R.id.home_person_menu).setOnClickListener(this);
    }

    private void initView() {

        home_titlebar = findViewById(R.id.home_titlebar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ((RadioGroup) findViewById(R.id.radioGroup)).setOnCheckedChangeListener(new CheckedChangeLIstener());

        homeRB = (RadioButton) findViewById(R.id.home);
        governmentRB = (RadioButton) findViewById(R.id.government);
        liveRB = (RadioButton) findViewById(R.id.live);
        audioRB = (RadioButton) findViewById(R.id.audio);
        interactRB = (RadioButton) findViewById(R.id.interact);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.home_person_menu:
                // 个人中心页面
                drawerLayout.openDrawer(GravityCompat.END);
                break;
            case R.id.home_pager:
                // 点击跳转到电子日报页面
                startActivity(WebActivity.newIntent(this, GetData.DAY_NEWS_ONLINE, false, false));
                break;
            default:
                break;
        }
    }

    private class CheckedChangeLIstener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            FragmentManager fm = getSupportFragmentManager();

            FragmentTransaction ft = fm.beginTransaction();

            fragmentNews = fm.findFragmentByTag(String.valueOf(homeRB.getId()));

            fragmentGoverment = fm.findFragmentByTag(String.valueOf(governmentRB.getId()));

            fragmentLive = fm.findFragmentByTag(String.valueOf(liveRB.getId()));

            fragmentAudio = fm.findFragmentByTag(String.valueOf(audioRB.getId()));

            fragmentInteract = fm.findFragmentByTag(String.valueOf(interactRB.getId()));

            if (fragmentNews != null) ft.hide(fragmentNews);

            if (fragmentLive != null) ft.hide(fragmentLive);

            if (fragmentAudio != null) ft.hide(fragmentAudio);

            if (fragmentInteract != null) ft.hide(fragmentInteract);

            if (fragmentGoverment != null) ft.hide(fragmentGoverment);
            switch (checkedId) {
                case R.id.home:
                    playerPause();
                    home_titlebar.setVisibility(View.VISIBLE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    if (fragmentNews == null) {
                        fragmentNews = new NewsFragment();
                        ft.add(R.id.content, fragmentNews, String.valueOf(homeRB.getId()));
                    } else {
                        ft.show(fragmentNews);
                    }
                    break;
                case R.id.government:
                    playerPause();
                    home_titlebar.setVisibility(View.GONE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    if (fragmentGoverment == null) {
                        fragmentGoverment = new GovernmentFragment();
                        ft.add(R.id.content, fragmentGoverment, String.valueOf(governmentRB.getId()));
                    } else {
                        ft.show(fragmentGoverment);
                    }
                    break;
                case R.id.live:
                    playerPause();
                    home_titlebar.setVisibility(View.GONE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    if (fragmentLive == null) {
                        fragmentLive = new LiveFragment();
                        ft.add(R.id.content, fragmentLive, String.valueOf(liveRB.getId()));
                    } else {
                        ft.show(fragmentLive);
                    }
                    break;
                case R.id.audio:
                    home_titlebar.setVisibility(View.GONE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    if (fragmentAudio == null) {
                        fragmentAudio = new AudioFragment();
                        ft.add(R.id.content, fragmentAudio, String.valueOf(audioRB.getId()));
                    } else {
                        ft.show(fragmentAudio);
                    }
                    break;
                case R.id.interact:
                    playerPause();
                    home_titlebar.setVisibility(View.GONE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    if (fragmentInteract == null) {
                        fragmentInteract = new InteractFragment();
                        ft.add(R.id.content, fragmentInteract, String.valueOf(interactRB.getId()));
                    } else {
                        ft.show(fragmentInteract);
                    }
                    break;
            }
            try {
                ft.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void playerPause() {
        try {
            if (JCMediaManager.instance().mediaPlayer.isPlaying()) {
                JCMediaManager.instance().mediaPlayer.pause();
                JCVideoPlayer.backPress();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = getSupportFragmentManager().findFragmentByTag(String.valueOf(homeRB.getId()));
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);
        //适配华为HMS SDK增加的调用
        if (requestCode == JPluginPlatformInterface.JPLUGIN_REQUEST_CODE) {
            pHuaweiPushInterface.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private ExitAppDialog exitAppDialog;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
            return;
        }

        if (JCVideoPlayer.backPress())
            return;

        if (exitAppDialog == null) {
            exitAppDialog = new ExitAppDialog();
        }
        exitAppDialog.show(getFragmentManager(), getLocalClassName() + "exit");
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //适配华为HMS SDK增加的调用
        pHuaweiPushInterface.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //适配华为HMS SDK增加的调用
        pHuaweiPushInterface.onStop(this);
    }
}
