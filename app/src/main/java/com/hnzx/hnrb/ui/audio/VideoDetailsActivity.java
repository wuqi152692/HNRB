package com.hnzx.hnrb.ui.audio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.VedioAboutAdapter;
import com.hnzx.hnrb.adapter.VedioAdapter;
import com.hnzx.hnrb.adapter.VedioAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetHotVedioListReq;
import com.hnzx.hnrb.requestbean.GetNewsDetailsReq;
import com.hnzx.hnrb.requestbean.GetVideoRelationsReq;
import com.hnzx.hnrb.requestbean.SetAddFavouriteReq;
import com.hnzx.hnrb.requestbean.SetCancelFavouriteReq;
import com.hnzx.hnrb.requestbean.SetNewsSupportReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetMediaVedioRsp;
import com.hnzx.hnrb.responsebean.GetNewsDetalisRsp;
import com.hnzx.hnrb.responsebean.GetVideoRelationsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.MImageGetter;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.ui.news.CommentActivity;
import com.hnzx.hnrb.ui.news.NewsDetailsActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.PLOnSeekCompleteListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.umeng.socialize.UMShareAPI;

import java.util.Timer;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.hnzx.hnrb.ui.audio.PLVideoViewStandard.DISMISS_CONTROL_VIEW_TIMER;
import static com.tencent.open.utils.Global.getContext;
import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.WIFI_TIP_DIALOG_SHOWED;

public class VideoDetailsActivity extends BaseActivity implements View.OnClickListener,MediaPlayer.OnInfoListener {
    private TextView mTextViewTitle, mTextViewAbout, mTextViewHot;
    private AppCompatTextView mTextViewMsg;
    private ImageView share,start;
    private CheckBox support, comment, collect;
    private TextView supportNum, commentNum;
    private RecyclerView mRecyclerViewAbout;
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private VedioAdapter adapter;
    private VedioAboutAdapter aboutAdapter;

    private JCVideoPlayerStandard player;

    private String vedio_id;
    private String shareUrl;
    private NewsShareDialog dialog;
    private GetNewsDetalisRsp vedioInfo;


    private static final int SCREEN_LAYOUT_NORMAL = 0;
    private static final int SCREEN_LAYOUT_LIST = 1;
    private static final int SCREEN_WINDOW_FULLSCREEN = 2;
    private static final int SCREEN_WINDOW_TINY = 3;

    private static final int CURRENT_STATE_NORMAL = 0;
    private static final int CURRENT_STATE_PREPARING = 1;
    private static final int CURRENT_STATE_PLAYING = 2;
    private static final int CURRENT_STATE_PLAYING_BUFFERING_START = 3;
    private static final int CURRENT_STATE_PAUSE = 5;
    private static final int CURRENT_STATE_AUTO_COMPLETE = 6;
    private static final int CURRENT_STATE_ERROR = 7;

    public static int BACKUP_PLAYING_BUFFERING_STATE = -1;

    private String url = "";
    private int CurrentPosition = 0;

    private Handler  DISMISS_CONTROL_VIEW_HANDLER;
    private Runnable DismissControlViewRunnable;

    public static final int UPDATE_CSEEK = 1;
    private TextView cCurrent,ctotal;
    private SeekBar cSeekBar;
    private ProgressBar loadingView,cbottomProgress;
    private PLVideoView mVideoView;
    private ImageView cthumb,cbackCtiny,cback,cfullscreenButton;
    private RelativeLayout cVideoController,cVideoLayout;

    private boolean isSupport = false;

    private boolean isFullScreen = false;

    private LinearLayout cbottomLinearLayout, ctopLinearLayout;

    private boolean isFirst = true;
    private int currentScreen= 0;
    private int currentState = 0;



    @Override
    protected int getLayoutId() {
        vedio_id = getIntent().getStringExtra(Constant.BEAN);
        shareUrl = getIntent().getStringExtra(NewsDetailsActivity.SHARE_IMAGE);
        return R.layout.activity_video_details;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        try {
            if (JCMediaManager.instance().mediaPlayer.isPlaying())
                JCMediaManager.instance().mediaPlayer.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        Log.d("activitystate", "initViews:--- ");

        ((TextView) findViewById(R.id.title)).setText("视频");
        share = (ImageView) findViewById(R.id.other);
        share.setVisibility(View.VISIBLE);

        support = (CheckBox) findViewById(R.id.support);
        comment = (CheckBox) findViewById(R.id.comment);
        collect = (CheckBox) findViewById(R.id.collect);

        supportNum = (TextView) findViewById(R.id.supportNum);
        commentNum = (TextView) findViewById(R.id.commentNum);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        player = (JCVideoPlayerStandard) findViewById(R.id.player);



        /**
         * 七牛播放器
         */
        cVideoLayout = findViewById(R.id.cVideoLayout);
        cVideoController = findViewById(R.id.cVideoController);
        cbottomLinearLayout = findViewById(R.id.layout_cbottom);
        ctopLinearLayout = findViewById(R.id.layout_ctop);
        cthumb = findViewById(R.id.cthumb);
        cfullscreenButton = findViewById(R.id.cfullscreen);
        cSeekBar = findViewById(R.id.cbottom_seek_progress);
        cCurrent = findViewById(R.id.cCurrent);
        ctotal = findViewById(R.id.cTotal);
        cbackCtiny = findViewById(R.id.back_ctiny);
        loadingView = findViewById(R.id.jc_cloading);
        cbottomProgress = findViewById(R.id.bottom_cprogress);
        cback = findViewById(R.id.cback);
        start = findViewById(R.id.cstart);



        mVideoView = findViewById(R.id.PLVideoView);


        //设置视频预览模式
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);  //全屏铺满

        View loadingView = findViewById(R.id.jc_cloading);
        mVideoView.setBufferingIndicator(loadingView);


        isOnlyShow();
        setUiWithStateAndScreen(currentState);
        setPlayerEvent();


        // 缓存好可以播放了
        mVideoView.setOnPreparedListener(new PLOnPreparedListener() {
            @Override
            public void onPrepared(int i) {
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.GONE);

                SeekHandler.sendEmptyMessage(UPDATE_CSEEK);

            }
        });

        //播放信息监听
        mVideoView.setOnInfoListener(new PLOnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                // 第一帧视频播放成功时
                switch (what) {
                    case MEDIA_INFO_VIDEO_RENDERING_START:

                        currentState = CURRENT_STATE_PLAYING;
                        changeUiToPlayingClear();
                        Log.d("activitystate", "currentState: "+currentState);

                        break;

                    case MEDIA_INFO_BUFFERING_START:
                        Log.d("loadingshow", "start");
                        break;

                    case MEDIA_INFO_BUFFERING_END:
                        Log.d("loadingshow", "end ");
                        break;

                    case MEDIA_INFO_CACHE_DOWN:
                        Log.d("loadingshow", "down: ");
                        break;

                    case MEDIA_INFO_LOOP_DONE:
                        Log.d("loadingshow", "loop");
                        break;

                    case MEDIA_INFO_CACHED_COMPLETE:
                        Log.d("loadingshow", "complete");
                        break;

                    case MEDIA_INFO_IS_SEEKING:
                        Log.d("loadingshow", "seeking");
                        break;

                    default:
//                            Log.d("loadingshow", "  ---  "+extra);
                        break;
                }
            }
        });

        //播放完成监听
        mVideoView.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                currentState = CURRENT_STATE_AUTO_COMPLETE;
                setUiWithStateAndScreen(currentState);
            }
        });


        //错误信息监听
        mVideoView.setOnErrorListener(new PLOnErrorListener() {
            @Override
            public boolean onError(int i) {

                currentState = CURRENT_STATE_ERROR;
                setUiWithStateAndScreen(currentState);
                return true;
            }
        });

        //seek完成的消息
        mVideoView.setOnSeekCompleteListener(new PLOnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {

                Log.d("loadingshow", "  seek完成了  ");
            }
        });

        // 缓存百分比
        mVideoView.setOnBufferingUpdateListener(new PLOnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(int i) {
//                Log.d("loadingshow", "  缓存百分比  "+i);
            }
        });


        // 播放状态监听



        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, this);
        recyclerView.addHeaderView(getHeaderView());
        recyclerView.setPullRefreshEnabled(false);

        adapter = new VedioAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private View getHeaderView() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_vedio_details_header, recyclerView, false);
        mTextViewTitle = (TextView) header.findViewById(R.id.mTextViewTitle);
        mTextViewMsg = (AppCompatTextView) header.findViewById(R.id.mTextViewMsg);
        mTextViewAbout = (TextView) header.findViewById(R.id.mTextViewAbout);
        mTextViewHot = (TextView) header.findViewById(R.id.mTextViewHot);
        mRecyclerViewAbout = (RecyclerView) header.findViewById(R.id.mRecyclerViewAbout);
        mRecyclerViewAbout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        aboutAdapter = new VedioAboutAdapter(this);
        mRecyclerViewAbout.setAdapter(aboutAdapter);
        return header;
    }

    @Override
    protected void initData() {
        GetNewsDetailsReq detailsReq = new GetNewsDetailsReq();
        detailsReq.content_id = vedio_id;

        App.getInstance().requestJsonDataGet(detailsReq, new detailsListener(), new MyErrorListener(stateView));

        GetVideoRelationsReq relationsReq = new GetVideoRelationsReq();
        relationsReq.content_id = vedio_id;

        App.getInstance().requestJsonDataGet(relationsReq, new relationListener(), null);

        GetHotVedioListReq req = new GetHotVedioListReq();

        App.getInstance().requestJsonArrayDataGet(req, new dataLIstener(), null);
    }

    @Override
    protected void initListeners() {
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vedioInfo == null) {
                    showToast("分享失败");
                    return;
                }
                if (dialog == null || dialog.isAdded()) {
                    dialog = NewsShareDialog.newInstance(vedioInfo.title, vedioInfo.title, shareUrl, vedioInfo.url);
                }
                dialog.show(getFragmentManager(), getLocalClassName());
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSupport)
                    showToast("您已点过赞");
                else {
                    SetNewsSupportReq req = new SetNewsSupportReq();
                    req.content_id = vedio_id;

                    App.getInstance().requestJsonDataGet(req, new supportListener(), null);
                }
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(VideoDetailsActivity.this, CommentActivity.class);
                it.putExtra(Constant.BEAN, vedio_id);
                startActivity(it);
            }
        });
        commentNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(VideoDetailsActivity.this, CommentActivity.class);
                it.putExtra(Constant.BEAN, vedio_id);
                startActivity(it);
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collect.setChecked(!collect.isChecked());
                if (collect.isChecked()) {
                    SetAddFavouriteReq req = new SetAddFavouriteReq();
                    req.content_id = vedio_id;

                    App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
                } else {
                    SetCancelFavouriteReq req = new SetCancelFavouriteReq();
                    req.content_id = vedio_id;

                    App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.d("setUiWitStateAndScreen", "onInfo ----- : "+what+" ; extra: "+extra);
        return true;
    }


    private class dataLIstener implements Response.Listener<BaseBeanArrayRsp<GetMediaVedioRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetMediaVedioRsp> response) {
            if (response != null && response.Status == 1) {
                adapter.setList(response.Info);
                recyclerView.refreshComplete();
            }
        }
    }

    private class detailsListener implements Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsDetalisRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsDetalisRsp> response) {
            if (response != null && response.Status == 1) {
                vedioInfo = response.Info;
                stateView.setViewState(vedioInfo != null ? MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
                mTextViewTitle.setText(vedioInfo.title);
                mTextViewMsg.setText(Html.fromHtml(vedioInfo.content, new MImageGetter(mTextViewMsg, VideoDetailsActivity.this), null));
                supportNum.setText(vedioInfo.support);
                commentNum.setText(String.valueOf(vedioInfo.comment));
                collect.setChecked(response.Info.is_favor == 1);
                //处理视频
                final GetNewsDetalisRsp.MyvideoBean myvideoBean = vedioInfo.myvideo.get(0);

                player.setUp(myvideoBean.filepath, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                GlideTools.GlideRounded(VideoDetailsActivity.this, myvideoBean.filethumb, player.thumbImageView, R.drawable.bg_morentu_datumoshi, 0);


                url = myvideoBean.filepath;
                mVideoView.setVideoPath(myvideoBean.filepath);
                GlideTools.GlideRounded(VideoDetailsActivity.this, myvideoBean.filethumb, cthumb, R.drawable.bg_morentu_datumoshi, 0);
                Log.e("initViews", "onResponse: "+mVideoView.getDuration());
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mVideoView.isPlaying()){
                            currentState = CURRENT_STATE_PAUSE;
                            updateStartImage();
                            //暂停播放
                            mVideoView.pause();
                            SeekHandler.removeMessages(UPDATE_CSEEK);

                        } else {



                            if (mVideoView.getCurrentPosition()==0 && CurrentPosition==0){
                                currentState = CURRENT_STATE_PREPARING;
                            } else {
                                currentState = CURRENT_STATE_PLAYING;
                            }

                            updateStartImage();



                            //继续播放
                            mVideoView.start();
                            SeekHandler.sendEmptyMessage(UPDATE_CSEEK);

                            if (CurrentPosition!=0){

                                mVideoView.seekTo(CurrentPosition);
                            }

                            Log.d("activitystate", "getCurrentPosition: "+mVideoView.getCurrentPosition());
                        }
                        Log.d("activitystate", "currentState: "+currentState);
                        setUiWithStateAndScreen(currentState);
                        
                    }
                });

            }
        }
    }






    private class relationListener implements Response.Listener<BaseBeanRsp<GetVideoRelationsRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetVideoRelationsRsp> response) {
            if (response != null && response.Status == 1) {
                if (response.Info.relations != null) {
                    mTextViewAbout.setText("相关：" + response.Info.keyword);
                    aboutAdapter.setList(response.Info.relations);
                } else mTextViewAbout.setVisibility(View.GONE);
            }
        }
    }

    private class supportListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                isSupport = true;
                support.setChecked(true);
                showToast("点赞成功");
                supportNum.setText(Integer.parseInt(vedioInfo.support) + 1 + "");
            }
        }
    }

    private class addCollectListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                showToast(!collect.isChecked() ? "添加收藏成功" : "取消收藏成功");
                collect.setChecked(!collect.isChecked());
            } else showToast(!collect.isChecked() ? "添加收藏失败" : "取消收藏失败");
        }
    }

    /**
     * 七牛播放状态监听
     * @param mp
     */





    /**
     *
     * 七牛控制器
     *
     */

    // 控制器的显示
    private void isOnlyShow(){
//        if (!screenState){
//            ctopLinearLayout.setVisibility(View.GONE);
//            cbottomLinearLayout.setVisibility(View.GONE);
//            cbackCtiny.setVisibility(View.GONE);
//        }

        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            cfullscreenButton.setImageResource(R.drawable.jc_shrink);
            cback.setVisibility(View.VISIBLE);
            cbackCtiny.setVisibility(View.INVISIBLE);
            changeStartButtonSize((int) getResources().getDimension(R.dimen.jc_start_button_w_h_fullscreen));
        } else if (currentScreen == SCREEN_LAYOUT_NORMAL
                || currentScreen == SCREEN_LAYOUT_LIST) {
            cfullscreenButton.setImageResource(R.drawable.jc_enlarge);
            cback.setVisibility(View.GONE);
            cbackCtiny.setVisibility(View.INVISIBLE);
            changeStartButtonSize((int) getResources().getDimension(R.dimen.jc_start_button_w_h_normal));
        } else if (currentScreen == SCREEN_WINDOW_TINY) {
            cbackCtiny.setVisibility(View.VISIBLE);
            setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                    View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
        }
    }

    public void changeStartButtonSize(int size) {
        ViewGroup.LayoutParams lp = start.getLayoutParams();
        lp.height = size;
        lp.width = size;
        lp = loadingView.getLayoutParams();
        lp.height = size;
        lp.width = size;
    }


    public void setUiWithStateAndScreen(int state){
        currentState = state;
        switch (currentState) {
            case CURRENT_STATE_NORMAL:
                changeUiToNormal();
                break;
            case CURRENT_STATE_PREPARING:
                changeUiToPreparingShow();
                break;
            case CURRENT_STATE_PLAYING:
                changeUiToPlayingShow();
                startDismissControlViewHandler();
                break;
            case CURRENT_STATE_PAUSE:
                cancelDismissControlViewHandler();
                changeUiToPauseShow();

                break;
            case CURRENT_STATE_ERROR:
                changeUiToError();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                changeUiToCompleteShow();
                cancelDismissControlViewHandler();
                cSeekBar.setProgress(100);
                cbottomProgress.setProgress(100);
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                cancelDismissControlViewHandler();
                break;
        }
    }



    public void changeUiToPlayingShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

        if (isFullScreen){

        }

    }

    public void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro,
                                      int thumbImg, int coverImg, int bottomPro) {
        ctopLinearLayout.setVisibility(topCon);
        cbottomLinearLayout.setVisibility(bottomCon);
        start.setVisibility(startBtn);
        loadingView.setVisibility(loadingPro);
        cback.setVisibility(coverImg);
        cbottomProgress.setVisibility(bottomPro);
        cthumb.setVisibility(thumbImg);
    }


    /**
     * 根据屏幕状态，改变UI
     */
    public void changeUiToNormal() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPreparingShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPreparingClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPauseShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    public void changeUiToError() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    public void startDismissControlViewHandler() {
//        cancelDismissControlViewHandler();
        Log.d("startDismissControl", ""+DISMISS_CONTROL_VIEW_HANDLER + " ==  "+DismissControlViewRunnable);
        if (DISMISS_CONTROL_VIEW_HANDLER!=null){
            DISMISS_CONTROL_VIEW_HANDLER.removeCallbacks(DismissControlViewRunnable);
        }
        DISMISS_CONTROL_VIEW_HANDLER = new Handler();
        DISMISS_CONTROL_VIEW_HANDLER.postDelayed(DismissControlViewRunnable,2500);
    }

    public void cancelDismissControlViewHandler(){
//        DISMISS_CONTROL_VIEW_HANDLER = new Handler();
        DISMISS_CONTROL_VIEW_HANDLER.removeCallbacks(DismissControlViewRunnable);
    }



    public void changeUiToCompleteShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }


    public void onClickUiToggle() {
        if (currentState == CURRENT_STATE_PREPARING) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPreparingClear();
            } else {
                changeUiToPreparingShow();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToCompleteClear();
            } else {
                changeUiToCompleteShow();
            }
        } else if (currentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPlayingBufferingClear();
            } else {
                changeUiToPlayingBufferingShow();
            }
        }
    }

    public void changeUiToCompleteClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingBufferingShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }
    public void changeUiToPlayingBufferingClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPauseClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_NORMAL:
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            start.setImageResource(R.drawable.jc_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            start.setImageResource(R.drawable.jc_click_error_selector);
        } else {
            start.setImageResource(R.drawable.jc_click_play_selector);
        }
    }

    /**
     * 手势滑动，控制播放进度
     * @param deltaX
     * @param seekTime
     * @param seekTimePosition
     * @param totalTime
     * @param totalTimeDuration
     */
    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;
    public void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {

        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_dialog_progress, null);
            mDialogProgressBar = ((ProgressBar) localView.findViewById(R.id.duration_progressbar));
            mDialogSeekTime = ((TextView) localView.findViewById(R.id.tv_current));
            mDialogTotalTime = ((TextView) localView.findViewById(R.id.tv_duration));
            mDialogIcon = ((ImageView) localView.findViewById(R.id.duration_image_tip));
            mProgressDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.CENTER;
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(" / " + totalTime);
        mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (seekTimePosition * 100 / totalTimeDuration));
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(R.drawable.jc_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(R.drawable.jc_backward_icon);
        }
        onCLickUiToggleToClear();
    }

    public void onCLickUiToggleToClear() {
        if (currentState == CURRENT_STATE_PREPARING) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPreparingClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToCompleteClear();
            } else {
            }
        } else if (currentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
            if (cbottomLinearLayout.getVisibility() == View.VISIBLE) {
                changeUiToPlayingBufferingClear();
            } else {
            }
        }
    }



//    public void showWifiDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
//        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                startVideo();
//                WIFI_TIP_DIALOG_SHOWED = true;
//            }
//        });
//        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//                    dialog.dismiss();
//                    clearFullscreenLayout();
//                }
//            }
//        });
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//                if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
//                    dialog.dismiss();
//                    clearFullscreenLayout();
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//
//    private void startVideo(){
//        mVideoView.start();
//    }


    /**
     *
     * 进度条
     */
    private void updateTextViewWithTimeFormat(TextView textView,int millisecond){
        int second = millisecond/1000;
        int hh = second/3600;
        int mm = second%3600/60;
        int ss = second%60;
        String str = null;
        if (hh!=0){
            str = String.format("%02d:%02d:%02d",hh,mm,ss);
        }
        else
        {
            str = String.format("%02d:%02d",mm,ss);
        }
        textView.setText(str);

    }

    private Handler SeekHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==UPDATE_CSEEK) {
                // 获取是视频当前的播放时间
                int currentPosition = (int) mVideoView.getCurrentPosition();
                // 获取视频播放的总时间
                int totalduration = (int) mVideoView.getDuration();

                // 格式化视频播放时间
                updateTextViewWithTimeFormat(cCurrent, currentPosition);
                updateTextViewWithTimeFormat(ctotal, totalduration);

                // 控制栏进度条
                cSeekBar.setMax(totalduration);
                cSeekBar.setProgress(currentPosition);

                //底部进度条
                cbottomProgress.setMax(totalduration);
                cbottomProgress.setProgress(currentPosition);
                sendEmptyMessageDelayed(UPDATE_CSEEK, 500);
            }
        }
    };



    /**
     *
     * 初始化事件
     *
     */
    private void setPlayerEvent () {

        DismissControlViewRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentState != CURRENT_STATE_NORMAL
                        && currentState != CURRENT_STATE_ERROR
                        && currentState != CURRENT_STATE_AUTO_COMPLETE) {
                    cbottomLinearLayout.setVisibility(View.INVISIBLE);
                    ctopLinearLayout.setVisibility(View.INVISIBLE);
                    start.setVisibility(View.INVISIBLE);
                    Log.d("togle", "run: ");

                    if (currentScreen != SCREEN_WINDOW_TINY) {
                        cbottomProgress.setVisibility(View.VISIBLE);
                    }
                }
            }
        };


        /**
         *
         * 拖动进度条
         *
         */
        cSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                updateTextViewWithTimeFormat(cCurrent,progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                cancelDismissControlViewHandler();
                SeekHandler.removeMessages(UPDATE_CSEEK);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mVideoView.isPlaying()){
                    startDismissControlViewHandler();
                }

                int progress = seekBar.getProgress();
                // 令视频播放进度遵循seekBar停止拖动的这一刻的进度
                mVideoView.seekTo(progress);
                SeekHandler.sendEmptyMessage(UPDATE_CSEEK);
            }
        });





//        cthumb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(url)) {
//                    Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (currentState == CURRENT_STATE_NORMAL) {
////                    if (!url.startsWith("file") && !url.startsWith("/") && !isWifi(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
//                    if (!url.startsWith("file") && !url.startsWith("/")){
////                    showWifiDialog();
//                        return;
//                    }
////                startVideo();
//                } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
//                    onClickUiToggle();
//                }
//            }
//        });

        // 推出全屏
        cback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


//        cSeekBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        cancelDismissControlViewHandler();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Log.d("click-", "onTouch:--- ");
//                        startDismissControlViewHandler();
//                        break;
//                }
//                return true;
//            }
//        });



        cbottomProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("click_", "onTouch: ");
                        cancelDismissControlViewHandler();
                        break;
                    case MotionEvent.ACTION_UP:
                        startDismissControlViewHandler();
                        break;
                }
                return true;
            }
        });





        cVideoController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click-", "onClick: ");
                onClickUiToggle();
                startDismissControlViewHandler();

            }
        });

    }

    public boolean isWifi(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        mVideoView.seekTo(CurrentPosition);

    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();


        // 记录播放进度
        CurrentPosition = (int) mVideoView.getCurrentPosition();
        // 当被activity被遮挡时，停止播放
        mVideoView.stopPlayback();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setUiWithStateAndScreen(CURRENT_STATE_NORMAL);
//        mVideoView.seekTo(CurrentPosition);
        mVideoView.setVideoPath(url);
        Log.d("activitystate", "Restart: "+ (CurrentPosition!=0)+ "   "+CurrentPosition);
    }


}
