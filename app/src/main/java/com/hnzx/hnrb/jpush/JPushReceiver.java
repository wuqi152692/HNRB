package com.hnzx.hnrb.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.ui.audio.ImageActivity;
import com.hnzx.hnrb.ui.audio.VideoDetailsActivity;
import com.hnzx.hnrb.ui.interact.ActiveActivity;
import com.hnzx.hnrb.ui.interact.TopicDetailActivity;
import com.hnzx.hnrb.ui.interact.VoteActivity;
import com.hnzx.hnrb.ui.live.ImageLiveActivity;
import com.hnzx.hnrb.ui.live.VideoLiveActivity;
import com.hnzx.hnrb.ui.news.NewsDetailsActivity;
import com.hnzx.hnrb.ui.news.SubjectActivity;
import com.hnzx.hnrb.ui.web.WebActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    public static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                }
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 用户点击打开了通知");

                //打开自定义的Activity
                HBPush(bundle.getString(JPushInterface.EXTRA_ALERT), context,
                        intent.getStringExtra(JPushInterface.EXTRA_EXTRA));

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
//                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }

    void HBPush(String msgs, Context con, String json) {

        if (msgs == null)
            return;
        if (json == null)
            return;

        PushMessage message = JSON.parseObject(json, new TypeReference<PushMessage>() {
        });
        if (message != null) {
            if (message.is_link == 1) {
                Intent webIntent = WebActivity.newIntent(con, message.link_url, false);
                webIntent.putExtra(TAG, true);
                webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                con.startActivity(webIntent);
                return;
            }
            switch (message.content_type) {
                case "content_content":
                    Intent intent = new Intent(con, NewsDetailsActivity.class);
                    intent.putExtra(Constant.BEAN, message.content_id);
                    intent.putExtra(NewsDetailsActivity.SHARE_IMAGE, "");
                    intent.putExtra(TAG, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(intent);
                    return;
                case "live_video":
                    Intent live_video = new Intent(con, VideoLiveActivity.class);
                    live_video.putExtra(Constant.BEAN, message.content_id);
                    live_video.putExtra(NewsDetailsActivity.SHARE_IMAGE, "");
                    live_video.putExtra(TAG, true);
                    live_video.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(live_video);
                    return;
                case "live_picture":
                    Intent live_picture = new Intent(con, ImageLiveActivity.class);
                    live_picture.putExtra(Constant.BEAN, message.content_id);
                    live_picture.putExtra(NewsDetailsActivity.SHARE_IMAGE, "");
                    live_picture.putExtra(TAG, true);
                    live_picture.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(live_picture);
                    return;
                case "activity":
                    Intent activity = new Intent(con, ActiveActivity.class);
                    activity.putExtra(ActiveActivity.DATA_KEY, message.content_id);
                    activity.putExtra(TAG, true);
                    activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(activity);
                    return;
                case "topic":
                    Intent topic = new Intent(con, TopicDetailActivity.class);
                    topic.putExtra(TopicDetailActivity.DATA_KEY, message.content_id);
                    topic.putExtra(TAG, true);
                    topic.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(topic);
                    return;
                case "vote":
                    Intent vote = new Intent(con, VoteActivity.class);
                    vote.putExtra(VoteActivity.DATA_KEY, message.content_id);
                    vote.putExtra(TAG, true);
                    vote.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(vote);
                    return;
                case "especial":
                    Intent especial = new Intent(con, SubjectActivity.class);
                    especial.putExtra(Constant.BEAN, message.content_id);
                    especial.putExtra(TAG, true);
                    especial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(especial);
                    return;
                case "content_video":
                    Intent vedio = new Intent(con, VideoDetailsActivity.class);
                    vedio.putExtra(ImageActivity.DATA_KEY, message.content_id);
                    vedio.putExtra(NewsDetailsActivity.SHARE_IMAGE, "");
                    vedio.putExtra(TAG, true);
                    vedio.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(vedio);
                    return;
                case "content_zutu":
                    Intent zutu = new Intent(con, ImageActivity.class);
                    zutu.putExtra(ImageActivity.DATA_KEY, message.content_id);
                    zutu.putExtra(TAG, true);
                    zutu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(zutu);
                    return;
                case "content_hybrid":
                    Intent hybrid = new Intent(con, NewsDetailsActivity.class);
                    hybrid.putExtra(Constant.BEAN, message.content_id);
                    hybrid.putExtra(NewsDetailsActivity.SHARE_IMAGE, "");
                    hybrid.putExtra(TAG, true);
                    hybrid.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    con.startActivity(hybrid);
                    return;
            }
        }
    }
}
