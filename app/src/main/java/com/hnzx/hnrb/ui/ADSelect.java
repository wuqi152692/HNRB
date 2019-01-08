package com.hnzx.hnrb.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.jpush.JPushReceiver;
import com.hnzx.hnrb.responsebean.GetAdsRsp;
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

/**
 * @author: mingancai
 * @Time: 2017/5/17 0017.
 */

public class ADSelect {

    public static void goWhere(Context con, GetAdsRsp adsRsp, boolean isFromWelcome) {
        if (adsRsp.is_link == 1) {
            // 链接地址错误，则不进行跳转
            if (TextUtils.isEmpty(adsRsp.link_url)) return;
            Intent webIntent = WebActivity.newIntent(con, adsRsp.link_url, false);
            if (isFromWelcome) {
                webIntent.putExtra(JPushReceiver.TAG, true);
                ((Activity) con).finish();
            }
            webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            con.startActivity(webIntent);
            return;
        }
        switch (adsRsp.internal_type) {
            case "content_content":
                Intent intent = new Intent(con, NewsDetailsActivity.class);
                intent.putExtra(Constant.BEAN, adsRsp.internal_id);
                if (isFromWelcome) {
                    intent.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                intent.putExtra(NewsDetailsActivity.SHARE_IMAGE, adsRsp.thumb);
                con.startActivity(intent);
                return;
            case "live_video":
                Intent live_video = new Intent(con, VideoLiveActivity.class);
                live_video.putExtra(Constant.BEAN, adsRsp.internal_id);
                if (isFromWelcome) {
                    live_video.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                live_video.putExtra(NewsDetailsActivity.SHARE_IMAGE, adsRsp.thumb);
                con.startActivity(live_video);
                return;
            case "live":
            case "live_picture":
                Intent live_picture = new Intent(con, ImageLiveActivity.class);
                live_picture.putExtra(Constant.BEAN, adsRsp.internal_id);
                if (isFromWelcome) {
                    live_picture.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                live_picture.putExtra(NewsDetailsActivity.SHARE_IMAGE, adsRsp.thumb);
                con.startActivity(live_picture);
                return;
            case "activity":
                Intent activity = new Intent(con, ActiveActivity.class);
                activity.putExtra(ActiveActivity.DATA_KEY, adsRsp.internal_id);
                if (isFromWelcome) {
                    activity.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                con.startActivity(activity);
                return;
            case "topic":
                Intent topic = new Intent(con, TopicDetailActivity.class);
                topic.putExtra(TopicDetailActivity.DATA_KEY, adsRsp.internal_id);
                if (isFromWelcome) {
                    topic.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                con.startActivity(topic);
                return;
            case "vote":
                Intent vote = new Intent(con, VoteActivity.class);
                vote.putExtra(VoteActivity.DATA_KEY, adsRsp.internal_id);
                if (isFromWelcome) {
                    vote.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                con.startActivity(vote);
                return;
            case "especial":
                Intent especial = new Intent(con, SubjectActivity.class);
                especial.putExtra(Constant.BEAN, adsRsp.internal_id);
                if (isFromWelcome) {
                    especial.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                con.startActivity(especial);
                return;
            case "content_video":
                Intent vedio = new Intent(con, VideoDetailsActivity.class);
                vedio.putExtra(Constant.BEAN, adsRsp.internal_id);
                vedio.putExtra(NewsDetailsActivity.SHARE_IMAGE, adsRsp.thumb);
                if (isFromWelcome) {
                    vedio.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                con.startActivity(vedio);
                return;
            case "content_zutu":
                Intent zutu = new Intent(con, ImageActivity.class);
                zutu.putExtra(ImageActivity.DATA_KEY, adsRsp.internal_id);
                if (isFromWelcome) {
                    zutu.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                con.startActivity(zutu);
                return;
            case "content_hybrid":
                Intent hybrid = new Intent(con, NewsDetailsActivity.class);
                hybrid.putExtra(Constant.BEAN, adsRsp.internal_id);
                if (isFromWelcome) {
                    hybrid.putExtra(JPushReceiver.TAG, true);
                    ((Activity) con).finish();
                }
                hybrid.putExtra(NewsDetailsActivity.SHARE_IMAGE, "");
                hybrid.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                con.startActivity(hybrid);
                return;
        }
    }
}
