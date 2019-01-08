package com.hnzx.hnrb.ui.live;

import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.responsebean.GetLiveContentRsp;
import com.hnzx.hnrb.tools.GlideTools;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

public class VideoFullScreenActivity extends BaseActivity implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener {
    private ImageView thumbImageView, videoBack;
    private VideoView player;
    private TextView mTextViewPause;
    private TextView mTextViewTime;
    private CheckBox mediacontroller_scale;

    private GetLiveContentRsp info;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_full_screen;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        player = (VideoView) findViewById(R.id.player);
        thumbImageView = (ImageView) findViewById(R.id.thumbImageView);
        videoBack = getViewById(R.id.videoBack);
        mTextViewPause = getViewById(R.id.video_pause);
        mTextViewTime = getViewById(R.id.mediacontroller_time_current);
        mediacontroller_scale = getViewById(R.id.mediacontroller_scale);
    }

    @Override
    protected void initData() {
        if(info!=null){
            GlideTools.Glide(this, info.thumb, thumbImageView, R.drawable.bg_morentu_datumoshi);

            //处理视频
            if (info.mylive != null && info.mylive.size() > 0) {
                final GetLiveContentRsp.MyliveBean liveBean = info.mylive.get(0);
                final String liveUrl = liveBean.filepath;
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
                    }
                });
            }
        }
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
