package com.hnzx.hnrb.ui.audio;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.Timer;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;



public class PLVideoViewStandard extends PLVideoView {





    public PLVideoViewStandard(Context context) {
        super(context);
    }

    public PLVideoViewStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PLVideoViewStandard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int getLayoutId() {
        return fm.jiecao.jcvideoplayer_lib.R.layout.jc_layout_standard;
    }



    protected static Timer DISMISS_CONTROL_VIEW_TIMER;

    public ImageView backButton,fullscreenButton,startButton;
    public ProgressBar bottomProgressBar, loadingProgressBar;
    public TextView titleTextView;
    public ImageView thumbImageView;
    public ImageView tinyBackImageView;
    private ViewGroup bottomContainer,topContainer;
    private LinearLayout bottomLayout;


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        return super.onCreateDrawableState(extraSpace);
    }

    public void controllerProgress(int screenStart){
        if (screenStart==0){

        }
    }


}
