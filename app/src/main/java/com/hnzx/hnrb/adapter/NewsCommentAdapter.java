package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.GetNewsCommentsSupportReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.CommentsBean;
import com.hnzx.hnrb.responsebean.GetLiveListRsp;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsCommentDialog;
import com.hnzx.hnrb.ui.interact.TopicCommentReplyListActivity;
import com.hnzx.hnrb.ui.news.CommentReplyListActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/1 0001.
 */

public class NewsCommentAdapter extends BaseAdapter<String> {
    private String content_id;
    private Map<String, CommentsBean> datas;

    public NewsCommentAdapter(Context context, String content_id) {
        super(context);
        this.content_id = content_id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        String ids = getItem(position);
        boolean containsReplace = ids.contains(",");
        final CommentsBean rsp0 = datas.get(containsReplace ? ids.split(",")[0] : ids);
        GlideTools.GlideRound(mContext, rsp0.avatar, mHolder.header, R.drawable.icon_default_round_head);
        mHolder.name.setText(rsp0.username);
        mHolder.time.setText(formatTime(rsp0.created));
        mHolder.comment_msg.setText(rsp0.content);

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
                        mContext.startActivity(CommentReplyListActivity.newIntent(mContext, content_id, rsp0.comment_id));
                    }
                });
            }
        } else mHolder.replaceLayout.setVisibility(View.GONE);

        mHolder.support.setText(String.valueOf(rsp0.support));

        mHolder.support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHolder.support.isChecked() && !rsp0.isSupport) {
                    rsp0.isSupport = true;
                    mHolder.support.setChecked(true);
                    GetNewsCommentsSupportReq req = new GetNewsCommentsSupportReq();
                    req.content_id = content_id;
                    req.id = rsp0.comment_id;
                    App.getInstance().requestJsonDataGet(req, new supportListener(mHolder.support, rsp0.support), null);
                } else ((BaseActivity) mContext).showTopToast("您已点过赞", false);
            }
        });

        mHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.getInstance().isLogin()) {
                    mContext.startActivity(LoginActivity.newIntent(mContext, false));
                }
                NewsCommentDialog dialog = NewsCommentDialog.newInstance(content_id, String.valueOf(rsp0.comment_id),
                        NewsCommentDialog.NEWS_COMMENT, rsp0.username);
                dialog.show(((Activity) mContext).getFragmentManager(), getClass().getName());
            }
        });
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, comment_msg, replaceMsg, reply, replaceNum;
        private CheckedTextView support;
        private ImageView header;
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
            imageLayout = itemView.findViewById(R.id.imageLayout);
            replaceLayout = itemView.findViewById(R.id.replaceLayout);
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

    // 增加评论点赞接口
    private class supportListener implements Response.Listener<BaseBeanRsp<String>> {

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
}
