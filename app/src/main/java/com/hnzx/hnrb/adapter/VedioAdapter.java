


package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetMediaVedioRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.audio.VideoDetailsActivity;
import com.hnzx.hnrb.ui.news.NewsDetailsActivity;
import com.pili.pldroid.player.widget.PLVideoView;
import com.zhy.autolayout.utils.AutoUtils;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class VedioAdapter extends BaseAdapter<GetMediaVedioRsp> implements JCVideoPlayer.JCOnPlayListener {
    public int playPosition = 0;
    public JCVideoPlayerStandard playerStandard;



    public VedioAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_vedio_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetMediaVedioRsp imageRsp = getItem(position);
        Log.d("videourl", "onBindViewHolder: "+imageRsp.url);
        Log.d("videourl", "onBindViewHolder: "+imageRsp.myvideo);
        Log.d("videourl", "onBindViewHolder: "+imageRsp.thumb);
        Log.d("videourl", "onBindViewHolder: "+imageRsp.views);
        JCVideoPlayerStandard standard;
        if (playPosition == position && playerStandard != null) {
            standard = playerStandard;
        } else {
            standard = new JCVideoPlayerStandard(mContext);
            //imageRsp.thumb
            GlideTools.GlideRounded(mContext, imageRsp.thumb, standard.thumbImageView, R.drawable.bg_morentu_datumoshi, 8);
            // imageRsp.myvideo
            // http://yun.henandaily.cn/video_20181212111403_5256   //----- H264
            standard.setUp(imageRsp.myvideo, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            standard.setAllControlsVisible(View.VISIBLE, View.GONE, View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
            standard.backButton.setVisibility(View.GONE);
            standard.tinyBackImageView.setVisibility(View.GONE);


//            mHolder.mVideoView.setVideoURI(Uri.parse("http://img.henandaily.cn/uploadfile/myvideo/20181026/1540551135630.mp4"));
//            mHolder.mVideoView.start();
        }



        mHolder.duration.setText(imageRsp.duration);
        mHolder.title.setText(imageRsp.title);
        if (imageRsp.created == null || imageRsp.created.equals("0:0") || imageRsp.created.equals("0:00"))
            mHolder.time.setVisibility(View.INVISIBLE);
        else {
            mHolder.time.setVisibility(View.VISIBLE);
            mHolder.time.setText(imageRsp.created);
        }
        mHolder.scan.setText(String.valueOf(imageRsp.views));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((VideoDetailsActivity) mContext).finish();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(mContext, VideoDetailsActivity.class);
                intent.putExtra(Constant.BEAN, imageRsp.video_id);
                intent.putExtra(NewsDetailsActivity.SHARE_IMAGE, imageRsp.thumb);
                mContext.startActivity(intent);
            }
        });
        standard.setOnJCPlayListener(this, position);

        try {
            mHolder.playerLayout.removeAllViews();
            mHolder.playerLayout.addView(standard);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(JCVideoPlayerStandard jps, int position) {
        playerStandard = jps;
        this.playPosition = position;
    }

    @Override
    public void onPause(JCVideoPlayerStandard jps, int position) {
        playerStandard = null;
        this.playPosition = 0;
    }

    @Override
    public void onComplete(JCVideoPlayerStandard jps, int position) {
        jps.backPress();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout playerLayout;
        private TextView title, time, duration, scan;
//        private JCVideoPlayerStandard playerStandard;
        private PLVideoView mVideoView ;


        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
//            playerStandard = (JCVideoPlayerStandard) itemView.findViewById(R.id.player);
            playerLayout = (FrameLayout) itemView.findViewById(R.id.playerLayout);
            title = (TextView) itemView.findViewById(R.id.video_title);
            time = (TextView) itemView.findViewById(R.id.time);
            duration = (TextView) itemView.findViewById(R.id.duration);
            scan = (TextView) itemView.findViewById(R.id.scan);

//            mVideoView= (PLVideoView) itemView.findViewById(R.id.PLVideoView);
        }
    }
}
