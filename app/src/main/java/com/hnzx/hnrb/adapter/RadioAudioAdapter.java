package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetMediaRadioRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.PermissionCheckUtil;
import com.hnzx.hnrb.ui.me.PersonInfoActivity;
import com.hnzx.hnrb.view.PlayView;
import com.hnzx.hnrb.view.SoundTrackView;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.IOException;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class RadioAudioAdapter extends BaseAdapter<GetMediaRadioRsp> {
    public MediaPlayer player;
    private RecyclerView recyclerView;
    private CheckedTextView stateView;
    //    private SoundTrackView soundTrackView;
    private PlayView mPlayView;
    private boolean isPause;
    private int playPosition;

    public RadioAudioAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_radio_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetMediaRadioRsp imageRsp = getItem(position);
        GlideTools.GlideRounded(mContext, imageRsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 8);
        mHolder.duration.setText(imageRsp.duration);
        mHolder.title.setText(imageRsp.title);
        mHolder.time.setText(imageRsp.created);
        mHolder.scan.setText(String.valueOf(imageRsp.hits));
        mHolder.stateView.setChecked(imageRsp.isPlaying);
        mHolder.mPlayView.setVisibility(imageRsp.isPlaying ? View.VISIBLE : View.GONE);
        mHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHolder.stateView.isChecked()) {//start
                    try {
                        if (stateView != null)
                            stateView.setChecked(false);
                        if (mPlayView != null)
                            mPlayView.setVisibility(View.GONE);
                        stateView = mHolder.stateView;
                        mPlayView = mHolder.mPlayView;
                        mHolder.stateView.setChecked(true);
                        mHolder.mPlayView.setVisibility(View.VISIBLE);
                        if (isPause && playPosition == position) {
                            isPause = false;
                            player.start();
                            mPlayView.startViewAnim();
                            return;
                        }
                        isPause = false;
                        if (player != null && player.isPlaying()) {
                            player.stop();
                            player.reset();
                            player.release();
                        }
                        player = new MediaPlayer();
                        player.setDataSource(imageRsp.url);
                        player.prepare();
                        player.seekTo(0);

//                                Visualizer visualizer = new Visualizer(player.getAudioSessionId());
//                                visualizer.setCaptureSize(4);
//                                visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
//                                    @Override
//                                    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
////                                mHolder.soundTrackView.updateState(waveform);
//                                    }
//
//                                    @Override
//                                    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
//                                        mHolder.soundTrackView.updateState(fft);
//                                    }
//                                }, 4096, false, true);
//                                visualizer.setEnabled(true);
//
//                                Equalizer mEqualizer = new Equalizer(0, player.getAudioSessionId());
//                                mEqualizer.setEnabled(true);

                        player.start();
                        mPlayView.startViewAnim();
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                stateView.setChecked(false);
                                mPlayView.setVisibility(View.GONE);
                                mPlayView.stopAnim();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {//stop
                    isPause = true;
                    playPosition = position;
                    stateView = null;
                    mPlayView = null;
                    mHolder.stateView.setChecked(false);
                    mHolder.mPlayView.setVisibility(View.GONE);
                    mHolder.mPlayView.stopAnim();
                    if (player != null)
                        player.pause();
                }
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView duration, title, time, scan;
        private CheckedTextView stateView;
        //        private SoundTrackView soundTrackView;
        private PlayView mPlayView;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            duration = (TextView) itemView.findViewById(R.id.duration);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            scan = (TextView) itemView.findViewById(R.id.scan);
            stateView = (CheckedTextView) itemView.findViewById(R.id.stateView);
//            soundTrackView = (SoundTrackView) itemView.findViewById(R.id.soundTrackView);
            mPlayView = (PlayView) itemView.findViewById(R.id.mPlayView);
        }
    }
}
