package com.hnzx.hnrb.ui.audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * @author: mingancai
 * @Time: 2017/5/29 0029.
 */

public class MusicService extends Service {
    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        mPlayer = new MediaPlayer();
        //设置可以重复播放
        mPlayer.setLooping(true);
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                // 释放资源
                try {
                    mp.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {//服务停止时停止播放音乐并释放资源
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        super.onDestroy();
    }
}
