package com.hnzx.hnrb.ui.live;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetLiveContentReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetLiveContentRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.ScreenUtil;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsCommentDialog;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.view.MScrollView;
import com.hnzx.hnrb.view.MultiStateView;
import com.hnzx.hnrb.view.pulltorefresh.PullToRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoLiveActivity extends BaseActivity implements View.OnClickListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener {

    private MultiStateView stateView;
    private TextView title, msg, editMessage, popleNum;
    private CheckBox sortCTV;
    private ImageView other, thumbImageView, videoBack;
    private CheckBox showOrHideMsg;
    private MScrollView mScrollView;
    private RadioGroup radioGroup;

    private View titleLayout;
    private View liveLayout;
    private View pinglunLayout;
    private View controlLayout;

    private String live_id;
    private Bundle bundle;

    private VideoView player;
    private GetLiveContentRsp info;
    private CheckBox mediacontroller_scale;

    private PullToRefreshLayout refreshLayout;

    private Fragment hallFragment, commentFragment;

    private MediaController controller;

    private JCVideoPlayerStandard standard;

    private int screenWidth;

    @Override
    protected int getLayoutId() {
        live_id = getIntent().getStringExtra(Constant.BEAN);
        bundle = new Bundle();
        bundle.putString(Constant.BEAN, live_id);
        return R.layout.activity_video_live;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        screenWidth = ScreenUtil.getScreenWidth(this);
        refreshLayout = (PullToRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.requestLayout();
        refreshLayout.setOnRefreshListener(new refreshListener());

        pinglunLayout = findViewById(R.id.pinglunLayout);
        liveLayout = findViewById(R.id.liveLayout);
        titleLayout = findViewById(R.id.layout);
        controlLayout = findViewById(R.id.controlLayout);

        standard = (JCVideoPlayerStandard) findViewById(R.id.standard);
        standard.setAllControlsVisible(View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        standard.backButton.setVisibility(View.GONE);

        player = (VideoView) findViewById(R.id.player);
        thumbImageView = (ImageView) findViewById(R.id.thumbImageView);
        videoBack = getViewById(R.id.videoBack);

        controller = new MediaController(player.getContext(), controlLayout);
        player.setMediaController(controller);

        editMessage = (TextView) findViewById(R.id.edit_message);
        sortCTV = (CheckBox) findViewById(R.id.sort);
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mediacontroller_scale = getViewById(R.id.mediacontroller_scale);

        popleNum = getViewById(R.id.popleNum);

        title = (TextView) findViewById(R.id.title);
        msg = (TextView) findViewById(R.id.msg);
        other = (ImageView) findViewById(R.id.other);
        other.setVisibility(View.VISIBLE);

        showOrHideMsg = (CheckBox) findViewById(R.id.showOrHideMsg);

        mScrollView = (MScrollView) findViewById(R.id.mScrollView);
        mScrollView.setFocusable(true);
        mScrollView.setFocusableInTouchMode(true);

        mScrollView.requestFocus();

        liveLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, screenWidth * 9 / 16));
        standard.setLayoutParams(new LinearLayout.LayoutParams(-1, screenWidth * 9 / 16));

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = ImageLiveHallFragment.newInstance(refreshLayout);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.contentLayout, fragment, String.valueOf(R.id.hallRB)).commit();
        }
    }

    boolean isPlaying;

    public void onScaleChange(boolean isFullscreen) {
        isPlaying = player.isPlaying();
        currentPosition = player.getCurrentPosition();
        if (isFullscreen) {
            videoBack.setVisibility(View.VISIBLE);
            titleLayout.setVisibility(View.GONE);
            stateView.setVisibility(View.GONE);
            pinglunLayout.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = liveLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = screenWidth;
            liveLayout.setLayoutParams(layoutParams);
            player.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            thumbImageView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            controlLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ViewGroup.LayoutParams layoutParams = liveLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = screenWidth * 9 / 16;
            liveLayout.setLayoutParams(layoutParams);
            videoBack.setVisibility(View.GONE);
            titleLayout.setVisibility(View.VISIBLE);
            stateView.setVisibility(View.VISIBLE);
            pinglunLayout.setVisibility(View.VISIBLE);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (isPlaying && currentPosition > 0)
            handler.sendEmptyMessageDelayed(0, 1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            thumbImageView.setVisibility(View.GONE);
            player.seekTo(currentPosition);
            player.start();
        }
    };

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
        findViewById(R.id.comment_et).setOnClickListener(this);
        videoBack.setOnClickListener(this);
        mediacontroller_scale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onScaleChange(isChecked);
            }
        });
        editMessage.setOnClickListener(this);
        sortCTV.setOnClickListener(this);
        other.setOnClickListener(this);
        showOrHideMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    msg.setVisibility(View.GONE);
                } else {
                    msg.setVisibility(View.VISIBLE);
                }
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
                    startActivity(LoginActivity.newIntent(this, LoginActivity.class));
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
                    startActivity(LoginActivity.newIntent(this, LoginActivity.class));
                    return;
                } else {
                    startActivity(PublishActivity.newIntent(this, live_id));
                }
                break;
            case R.id.sort:
                sort();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.other:
                share();
                break;
            case R.id.videoBack:
                onBackPressed();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private class dataListener implements Response.Listener<BaseBeanRsp<GetLiveContentRsp>> {
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
        msg.setText(rsp.brief);
        title.setText(rsp.title);

        //处理视频
        if (rsp.mylive != null && rsp.mylive.size() > 0) {
            final GetLiveContentRsp.MyliveBean liveBean = rsp.mylive.get(0);
            final String liveUrl = liveBean.filepath;
            if (rsp.type.equals("over")) {
                standard.setVisibility(View.VISIBLE);
                liveLayout.setVisibility(View.GONE);
                standard.setUp(liveUrl, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                GlideTools.Glide(this, rsp.thumb, standard.thumbImageView, R.drawable.bg_morentu_datumoshi);
            } else {
                GlideTools.Glide(this, rsp.thumb, thumbImageView, R.drawable.bg_morentu_datumoshi);
                liveLayout.setVisibility(View.VISIBLE);
                standard.setVisibility(View.GONE);
                Uri uri = Uri.parse(liveUrl);
                player.setVideoURI(uri);//设置视频播放地址
                player.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
                player.requestFocus();
                player.setOnInfoListener(this);
                player.setOnBufferingUpdateListener(this);
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.setPlaybackSpeed(1.0f);
                        player.addTimedTextSource("");
                        player.setTimedTextShown(true);
                    }
                });
            }
        } else if (rsp.myvideo != null && rsp.myvideo.size() > 0) {
            standard.setVisibility(View.VISIBLE);
            standard.setUp(rsp.myvideo.get(0).filepath, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            GlideTools.Glide(this, rsp.thumb, standard.thumbImageView, R.drawable.bg_morentu_datumoshi);
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress())
            return;
        if (mediacontroller_scale.isChecked()) {
            mediacontroller_scale.setChecked(false);
            return;
        }
        super.onBackPressed();
    }

    private long currentPosition;

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            player.pause();
            currentPosition = player.getCurrentPosition();
        }
        try {
            if (JCMediaManager.instance().mediaPlayer.isPlaying())
                JCMediaManager.instance().mediaPlayer.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null && player.isPlaying() && currentPosition > 0) {
            player.seekTo(currentPosition);
            player.resume();
        }
        try {
            if (JCMediaManager.instance().mediaPlayer.isPlaying())
                JCMediaManager.instance().mediaPlayer.pause();
        } catch (IllegalStateException e) {
        }
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
