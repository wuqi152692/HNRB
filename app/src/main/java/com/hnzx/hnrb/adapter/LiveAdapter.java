package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetLiveListRsp;
import com.hnzx.hnrb.tools.DateUtils;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.live.ImageLiveActivity;
import com.hnzx.hnrb.ui.live.VideoLiveActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: mingancai
 * @Time: 2017/4/1 0001.
 */

public class LiveAdapter extends BaseAdapter<GetLiveListRsp> {
    private List<MyCountDownTime> countTimes = new ArrayList<>();

    public LiveAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_vedio_live_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetLiveListRsp data = getItem(position);
        GlideTools.GlideRounded(mContext, data.thumb, mHolder.image, R.drawable.bg_morentu_datumoshi, 12);
        mHolder.title.setText(data.title);
        mHolder.title.setChecked(!data.type.equals("video"));
        mHolder.time.setText(data.created);
        if (data.current_time >= data.start_time) {
            mHolder.stateLive.setChecked(data.enabled == 1);
            mHolder.stateLive.setText(data.enabled == 1 ? "直播中" : "回顾");
        } else {
            mHolder.stateLive.setChecked(true);
            setTime(mHolder.stateLive, data.current_time, data.start_time);
        }
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.type.equals("video")) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constant.BEAN, data.live_id);
                    IntentUtil.startActivity(mContext, VideoLiveActivity.class, params);
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Constant.BEAN, data.live_id);
                    IntentUtil.startActivity(mContext, ImageLiveActivity.class, params);
                }
            }
        });
    }

    private void setTime(CheckedTextView stateLive, int current, int start) {
        int interval = start - current;
        if (interval < 60) {
            MyCountDownTime count = new MyCountDownTime(stateLive, interval);
            count.start();
            countTimes.add(count);
        } else if (interval < 3600) {
            MyCountDownTime count = new MyCountDownTime(stateLive, interval);
            count.start();
            countTimes.add(count);
        } else if (interval < 86400) {
            long hours = interval / (60 * 60);
            stateLive.setText("倒计时" + hours + "小时");
        } else if (interval < 604800) {
            long days = interval / (60 * 60 * 24);
            stateLive.setText("倒计时" + days + "天");
        } else {
            stateLive.setText(DateUtils.dateToString(new Date(start), "MM月dd HH:mm"));
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView time;
        private CheckedTextView title, stateLive;

        public ViewHolder(View item) {
            super(item);
            image = (ImageView) item.findViewById(R.id.image);
            time = (TextView) item.findViewById(R.id.time);
            title = (CheckedTextView) item.findViewById(R.id.title);
            stateLive = (CheckedTextView) item.findViewById(R.id.stateLive);
        }
    }

    private class MyCountDownTime extends CountDownTimer {
        private CheckedTextView tv;

        public MyCountDownTime(CheckedTextView tv, int interval) {
            this(interval * 1000, 1000);
            this.tv = tv;
        }

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */

        public MyCountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long minutes = (millisUntilFinished % (1000 * 60 * 60))
                    / (1000 * 60);
            if (minutes > 0 && !tv.getText().equals("倒计时" + minutes + "分")) {
                tv.setText("倒计时" + minutes + "分");
            } else if (minutes == 0) {
                long seconds = (millisUntilFinished % (1000 * 60)) / 1000;
                tv.setText("倒计时" + seconds + "秒");
            }
        }

        @Override
        public void onFinish() {
            tv.setText("直播中");
        }
    }
}
