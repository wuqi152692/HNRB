package com.hnzx.hnrb.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.ui.audio.ImageActivity;
import com.hnzx.hnrb.ui.audio.VideoDetailsActivity;
import com.hnzx.hnrb.ui.government.square.SquareFragment;
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
 * @Time: 2017/4/7 0007.
 */

public class NewsSelect {
    /**
     * 新闻跳转
     *
     * @param con
     * @param content_id   新闻id
     * @param content_type 新闻类型
     */
    public static void goWhere(Context con, String content_id, int is_link, String link_url, String internal_type,
                               String internal_id, String content_type, String shareImgUrl) {
        if (is_link == 1) {
            // 链接地址错误，则不进行跳转
            if (TextUtils.isEmpty(link_url)) return;
            con.startActivity(WebActivity.newIntent(con, link_url, false));
            return;
        }
        switch (internal_type) {
            case "content_content":
                Intent intent = new Intent(con, NewsDetailsActivity.class);
                intent.putExtra(Constant.BEAN, internal_id);
                intent.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(intent);
                return;
            case "live_video":
                Intent live_video = new Intent(con, VideoLiveActivity.class);
                live_video.putExtra(Constant.BEAN, internal_id);
                live_video.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(live_video);
                return;
            case "live":
            case "live_picture":
                Intent live_picture = new Intent(con, ImageLiveActivity.class);
                live_picture.putExtra(Constant.BEAN, internal_id);
                live_picture.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(live_picture);
                return;
            case "activity":
                Intent activity = new Intent(con, ActiveActivity.class);
                activity.putExtra(ActiveActivity.DATA_KEY, internal_id);
                con.startActivity(activity);
                return;
            case "topic":
                Intent topic = new Intent(con, TopicDetailActivity.class);
                topic.putExtra(TopicDetailActivity.DATA_KEY, internal_id);
                con.startActivity(topic);
                return;
            case "vote":
                Intent vote = new Intent(con, VoteActivity.class);
                vote.putExtra(VoteActivity.DATA_KEY, internal_id);
                con.startActivity(vote);
                return;
            case "especial":
                Intent especial = new Intent(con, SubjectActivity.class);
                especial.putExtra(Constant.BEAN, internal_id);
                con.startActivity(especial);
                return;
            case "content_video":
                Intent vedio = new Intent(con, VideoDetailsActivity.class);
                vedio.putExtra(Constant.BEAN, internal_id);
                vedio.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(vedio);
                return;
            case "content_zutu":
                Intent zutu = new Intent(con, ImageActivity.class);
                zutu.putExtra(ImageActivity.DATA_KEY, internal_id);
                con.startActivity(zutu);
                return;
            case "content_hybrid":
                Intent hybrid = new Intent(con, NewsDetailsActivity.class);
                hybrid.putExtra(Constant.BEAN, internal_id);
                hybrid.putExtra(NewsDetailsActivity.SHARE_IMAGE, "");
                hybrid.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                con.startActivity(hybrid);
                return;
        }
        switch (content_type) {
            case "especial":
                Intent especial = new Intent(con, SubjectActivity.class);
                especial.putExtra(Constant.BEAN, content_id);
                con.startActivity(especial);
                break;
            case "content_video":
                Intent vedio = new Intent(con, VideoDetailsActivity.class);
                vedio.putExtra(Constant.BEAN, content_id);
                vedio.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(vedio);
                break;
            case "content_zutu":
                Intent zutu = new Intent(con, ImageActivity.class);
                zutu.putExtra(ImageActivity.DATA_KEY, content_id);
                con.startActivity(zutu);
                break;
            case "live_video":
                Intent live_video = new Intent(con, VideoLiveActivity.class);
                live_video.putExtra(Constant.BEAN, content_id);
                live_video.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(live_video);
                return;
            case "live":
            case "live_picture":
                Intent live_picture = new Intent(con, ImageLiveActivity.class);
                live_picture.putExtra(Constant.BEAN, content_id);
                live_picture.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(live_picture);
                return;
            case "activity":
                Intent activity = new Intent(con, ActiveActivity.class);
                activity.putExtra(ActiveActivity.DATA_KEY, content_id);
                con.startActivity(activity);
                return;
            case "topic":
                Intent topic = new Intent(con, TopicDetailActivity.class);
                topic.putExtra(TopicDetailActivity.DATA_KEY, content_id);
                con.startActivity(topic);
                return;
            case "vote":
                Intent vote = new Intent(con, VoteActivity.class);
                vote.putExtra(VoteActivity.DATA_KEY, content_id);
                con.startActivity(vote);
                return;
            default:
                Intent intent = new Intent(con, NewsDetailsActivity.class);
                intent.putExtra(Constant.BEAN, content_id);
                intent.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(intent);
                break;
        }
    }

    /**
     * 新闻跳转
     *
     * @param con
     * @param content_id   新闻id
     * @param content_type 新闻类型
     */
    public static void goWhere(Context con, String content_id, int is_link, String link_url, String internal_type,
                               String internal_id, String content_type, String shareImgUrl, boolean isSquare) {
        if (is_link == 1) {
            // 链接地址错误，则不进行跳转
            if (TextUtils.isEmpty(link_url)) return;
            con.startActivity(WebActivity.newIntent(con, link_url,false));
            return;
        }
        switch (internal_type) {
            case "content_content":
                Intent intent = new Intent(con, NewsDetailsActivity.class);
                intent.putExtra(Constant.BEAN, internal_id);
                intent.putExtra(SquareFragment.FROM_SQUARE, isSquare);
                intent.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(intent);
                return;
            case "live":

                return;
            case "activity":
                Intent activity = new Intent(con, ActiveActivity.class);
                activity.putExtra(ActiveActivity.DATA_KEY, internal_id);
                con.startActivity(activity);
                return;
            case "especial":
                Intent especial = new Intent(con, SubjectActivity.class);
                especial.putExtra(Constant.BEAN, internal_id);
                con.startActivity(especial);
                return;
            case "content_video":
                Intent vedio = new Intent(con, VideoDetailsActivity.class);
                vedio.putExtra(Constant.BEAN, internal_id);
                vedio.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(vedio);
                return;
            case "content_zutu":
                Intent zutu = new Intent(con, ImageActivity.class);
                zutu.putExtra(ImageActivity.DATA_KEY, internal_id);
                con.startActivity(zutu);
                return;
        }
        switch (content_type) {
            case "especial":
                Intent especial = new Intent(con, SubjectActivity.class);
                especial.putExtra(Constant.BEAN, content_id);
                con.startActivity(especial);
                break;
            case "content_video":
                Intent vedio = new Intent(con, VideoDetailsActivity.class);
                vedio.putExtra(Constant.BEAN, content_id);
                vedio.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(vedio);
                break;
            case "content_zutu":
                Intent zutu = new Intent(con, ImageActivity.class);
                zutu.putExtra(ImageActivity.DATA_KEY, content_id);
                con.startActivity(zutu);
                break;
            default:
                Intent intent = new Intent(con, NewsDetailsActivity.class);
                intent.putExtra(Constant.BEAN, content_id);
                intent.putExtra(SquareFragment.FROM_SQUARE, isSquare);
                intent.putExtra(NewsDetailsActivity.SHARE_IMAGE, shareImgUrl);
                con.startActivity(intent);
                break;
        }
    }
}
