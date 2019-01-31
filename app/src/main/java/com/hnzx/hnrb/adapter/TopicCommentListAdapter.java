package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.SetTopicCommentsSupportReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.CommentsBean;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.interact.TopicCommentActivity;
import com.hnzx.hnrb.ui.interact.TopicCommentReplyListActivity;
import com.hnzx.hnrb.ui.news.NewsScanBigImageActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author: mingancai
 * @Time: 2017/4/26 0026.
 */

public class TopicCommentListAdapter extends BaseAdapter<String> {
    private Map<String, CommentsBean> datas;
    private String content_id;

    public TopicCommentListAdapter(Context context, String content_id) {
        super(context);
        this.content_id = content_id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == 0 ? new ViewHolder(mInflater.inflate(R.layout.layout_comment_item, parent, false))
                : new VideoViewHolder(mInflater.inflate(R.layout.layout_comment_video_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        String ids = getItem(position);
        boolean containsReplace = ids.contains(",");
        final CommentsBean rsp0 = datas.get(containsReplace ? ids.split(",")[0] : ids);
        return rsp0.video.length() > 1 ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            final VideoViewHolder mHolder = (VideoViewHolder) holder;
            String ids = getItem(position);
            boolean containsReplace = ids.contains(",");
            final CommentsBean rsp0 = datas.get(containsReplace ? ids.split(",")[0] : ids);
            GlideTools.GlideRound(mContext, rsp0.avatar, mHolder.header, R.drawable.icon_default_round_head);
            mHolder.name.setText(rsp0.username);
            mHolder.time.setText(formatTime(rsp0.created));
            mHolder.comment_msg.setText(rsp0.content);

            JCVideoPlayerStandard standard = new JCVideoPlayerStandard(mContext);
            GlideTools.GlideFit(mContext, rsp0.video, standard.thumbImageView, R.drawable.bg_morentu_datumoshi);
            standard.setUp(rsp0.video, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            standard.setAllControlsVisible(View.VISIBLE, View.GONE, View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
            standard.backButton.setVisibility(View.GONE);
            standard.tinyBackImageView.setVisibility(View.GONE);

            mHolder.mVideoLayout.removeAllViews();
            mHolder.mVideoLayout.addView(standard);

            if (containsReplace) {
                StringBuilder replaceMsg = new StringBuilder();
                String[] replaceIds = ids.split(",");
                int totalRepalce = replaceIds.length - 1;
                for (int i = 1; i <= (2 < totalRepalce ? 2 : totalRepalce); i++) {
                    try {
                        final CommentsBean rsp = datas.get(replaceIds[i]);
                        if (i == 1 && totalRepalce > 1)
                            replaceMsg.append("<p>" + rsp.username + (rsp.replied.length() > 0 ? ("：回复 " + rsp.replied) :
                                    "") + "：<font color='black'>" + rsp.content + "</font></p>");
                        else
                            replaceMsg.append(rsp.username + (rsp.replied.length() > 0 ? ("：回复 " + rsp.replied) :
                                    "") + "：<font color='black'>" + rsp.content + "</font>");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                mHolder.replaceMsg.setText(Html.fromHtml(replaceMsg.toString()));
                mHolder.replaceLayout.setVisibility(View.VISIBLE);
                if (totalRepalce > 0) {
                    mHolder.replaceNum.setText(totalRepalce + "回复<<");
                    mHolder.replaceNum.setVisibility(View.VISIBLE);
                    mHolder.replaceLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(TopicCommentReplyListActivity.newIntent(mContext, content_id, rsp0.comment_id));
                        }
                    });
                }
            } else mHolder.replaceLayout.setVisibility(View.GONE);

            mHolder.support.setText(String.valueOf(rsp0.support));

            mHolder.support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (App.getInstance().isLogin())
                        if (!mHolder.support.isChecked() && !rsp0.isSupport) {
                            rsp0.isSupport = true;
                            mHolder.support.setChecked(true);
                            SetTopicCommentsSupportReq req = new SetTopicCommentsSupportReq();
                            req.topic_id = content_id;
                            req.comment_id = rsp0.comment_id;
                            App.getInstance().requestJsonDataGet(req, new supportListener(mHolder.support, rsp0.support), null);
                        } else ((BaseActivity) mContext).showTopToast("您已点过赞", false);
                    else mContext.startActivity(LoginActivity.newIntent(mContext, false));
                }
            });

            mHolder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!App.getInstance().isLogin()) {
                        mContext.startActivity(LoginActivity.newIntent(mContext, LoginActivity.class));
                        return;
                    }
                    mContext.startActivity(TopicCommentActivity.newIntent(mContext, content_id, String.valueOf(rsp0.comment_id)));
                }
            });
        } else {
            final ViewHolder mHolder = (ViewHolder) holder;
            String ids = getItem(position);
            boolean containsReplace = ids.contains(",");
            final CommentsBean rsp0 = datas.get(containsReplace ? ids.split(",")[0] : ids);
            GlideTools.GlideRound(mContext, rsp0.avatar, mHolder.header, R.drawable.icon_default_round_head);
            mHolder.name.setText(rsp0.username);
            mHolder.time.setText(formatTime(rsp0.created));
            mHolder.comment_msg.setText(rsp0.content);

            if (rsp0.imgs != null && rsp0.imgs.size() > 0) {
                mHolder.imageLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < (rsp0.imgs.size() > 3 ? 3 : rsp0.imgs.size()); i++) {
                    if (i == 0) {
                        GlideTools.Glide(mContext, rsp0.imgs.get(i), mHolder.image0, R.drawable.bg_morentu_xiaotumoshi);
                        mHolder.image0.setOnClickListener(new ImageClick(rsp0.imgs, i));
                    } else if (i == 1) {
                        GlideTools.Glide(mContext, rsp0.imgs.get(i), mHolder.image1, R.drawable.bg_morentu_xiaotumoshi);
                        mHolder.image1.setOnClickListener(new ImageClick(rsp0.imgs, i));
                    } else if (i == 2) {
                        GlideTools.Glide(mContext, rsp0.imgs.get(i), mHolder.image2, R.drawable.bg_morentu_xiaotumoshi);
                        mHolder.image2.setOnClickListener(new ImageClick(rsp0.imgs, i));
                    }
                }
            } else {
                mHolder.imageLayout.setVisibility(View.GONE);
            }

            if (containsReplace) {
                StringBuilder replaceMsg = new StringBuilder();
                String[] replaceIds = ids.split(",");
                int totalRepalce = replaceIds.length - 1;
                for (int i = 1; i <= (2 < totalRepalce ? 2 : totalRepalce); i++) {
                    try {
                        final CommentsBean rsp = datas.get(replaceIds[i]);
                        if (i == 1 && totalRepalce > 1)
                            replaceMsg.append("<p>" + rsp.username + (rsp.replied.length() > 0 ? ("：回复 " + rsp.replied) :
                                    "") + "：<font color='black'>" + rsp.content + "</font></p>");
                        else
                            replaceMsg.append(rsp.username + (rsp.replied.length() > 0 ? ("：回复 " + rsp.replied) :
                                    "") + "：<font color='black'>" + rsp.content + "</font>");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                mHolder.replaceMsg.setText(Html.fromHtml(replaceMsg.toString()));
                mHolder.replaceLayout.setVisibility(View.VISIBLE);
                if (totalRepalce > 0) {
                    mHolder.replaceNum.setText(totalRepalce + "回复<<");
                    mHolder.replaceNum.setVisibility(View.VISIBLE);
                    mHolder.replaceLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(TopicCommentReplyListActivity.newIntent(mContext, content_id, rsp0.comment_id));
                        }
                    });
                }
            } else mHolder.replaceLayout.setVisibility(View.GONE);

            mHolder.support.setText(String.valueOf(rsp0.support));

            mHolder.support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (App.getInstance().isLogin())
                        if (!mHolder.support.isChecked() && !rsp0.isSupport) {
                            rsp0.isSupport = true;
                            mHolder.support.setChecked(true);
                            SetTopicCommentsSupportReq req = new SetTopicCommentsSupportReq();
                            req.topic_id = content_id;
                            req.comment_id = rsp0.comment_id;
                            App.getInstance().requestJsonDataGet(req, new supportListener(mHolder.support, rsp0.support), null);
                        } else ((BaseActivity) mContext).showTopToast("您已点过赞", false);
                    else mContext.startActivity(LoginActivity.newIntent(mContext, false));
                }
            });

            mHolder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!App.getInstance().isLogin()) {
                        mContext.startActivity(LoginActivity.newIntent(mContext, LoginActivity.class));
                        return;
                    }
                    mContext.startActivity(TopicCommentActivity.newIntent(mContext, content_id, String.valueOf(rsp0.comment_id)));
                }
            });
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, comment_msg, replaceMsg, reply, replaceNum;
        private CheckedTextView support;
        private ImageView header, image0, image1, image2;
        private View imageLayout, replaceLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            reply = (TextView) itemView.findViewById(R.id.reply);
            comment_msg = (TextView) itemView.findViewById(R.id.comment_msg);
            replaceMsg = (TextView) itemView.findViewById(R.id.replaceMsg);
            replaceNum = (TextView) itemView.findViewById(R.id.replaceNum);
            support = (CheckedTextView) itemView.findViewById(R.id.supportBtn);
            header = (ImageView) itemView.findViewById(R.id.header);
            image0 = (ImageView) itemView.findViewById(R.id.image0);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            imageLayout = itemView.findViewById(R.id.imageLayout);
            replaceLayout = itemView.findViewById(R.id.replaceLayout);
        }
    }

    protected class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, comment_msg, replaceMsg, reply, replaceNum;
        private CheckedTextView support;
        private ImageView header;
        private View imageLayout, replaceLayout;
        private LinearLayout mVideoLayout;

        public VideoViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            reply = (TextView) itemView.findViewById(R.id.reply);
            comment_msg = (TextView) itemView.findViewById(R.id.comment_msg);
            replaceMsg = (TextView) itemView.findViewById(R.id.replaceMsg);
            replaceNum = (TextView) itemView.findViewById(R.id.replaceNum);
            support = (CheckedTextView) itemView.findViewById(R.id.supportBtn);
            header = (ImageView) itemView.findViewById(R.id.header);
            imageLayout = itemView.findViewById(R.id.imageLayout);
            replaceLayout = itemView.findViewById(R.id.replaceLayout);
            mVideoLayout = itemView.findViewById(R.id.videoLayout);
        }
    }

    public void addMap(Map<String, CommentsBean> datas, int offset) {
        if (this.datas == null)
            this.datas = datas;
        else {
            if (offset != 0)
                this.datas.putAll(datas);
            else {
                this.datas.clear();
                this.datas = datas;
            }
        }
    }

    private class supportListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<String>> {
        private TextView mTextViewCount;

        private int count;

        public supportListener(TextView mTextViewCount, int count) {
            this.mTextViewCount = mTextViewCount;
            this.count = count;
        }

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                try {
                    mTextViewCount.setText(String.valueOf(count + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "点赞失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ImageClick implements View.OnClickListener {
        private ArrayList<String> urls;
        private int position;

        public ImageClick(ArrayList<String> urls, int position) {
            this.urls = urls;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            scanBigImage(urls, position);
        }
    }

    private void scanBigImage(ArrayList<String> imgSrc, int position) {
        Intent intent = new Intent(mContext, NewsScanBigImageActivity.class);
        intent.putStringArrayListExtra(Constant.BEAN, imgSrc);
        intent.putExtra(NewsScanBigImageActivity.SHOW_POSITION, position);
        mContext.startActivity(intent);
    }
}
