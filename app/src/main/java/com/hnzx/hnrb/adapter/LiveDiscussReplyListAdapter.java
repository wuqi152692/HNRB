package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.SetLiveDiscussSupportReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetTopicCommentMoreRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsCommentDialog;
import com.hnzx.hnrb.ui.news.NewsScanBigImageActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * @author: mingancai
 * @Time: 2017/7/7 0007.
 */

public class LiveDiscussReplyListAdapter extends BaseAdapter<GetTopicCommentMoreRsp> {
    private String content_id;
    private String quoted;

    public LiveDiscussReplyListAdapter(Context context, String content_id, String quoted) {
        super(context);
        this.content_id = content_id;
        this.quoted = quoted;
    }

    @Override
    public int getItemViewType(int position) {
        return position != 0 ? R.layout.layout_comment_reply_item : R.layout.layout_comment_reply_first_item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetTopicCommentMoreRsp rsp0 = getItem(position);
        GlideTools.GlideRound(mContext, rsp0.avatar, mHolder.header, R.drawable.icon_default_round_head);
        mHolder.name.setText(rsp0.username);
        mHolder.time.setText(formatTime(rsp0.created));
        mHolder.comment_msg.setText(Html.fromHtml((rsp0.replied == null || rsp0.replied.trim().length() == 0 ? "" : ("<font color='#898989'>回复 " + rsp0.replied + "：</font>")) + rsp0.content));

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

        mHolder.support.setText(String.valueOf(rsp0.support));

        mHolder.support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().isLogin())
                    if (!mHolder.support.isChecked() && !rsp0.isSupport) {
                        rsp0.isSupport = true;
                        mHolder.support.setChecked(true);
                        SetLiveDiscussSupportReq req = new SetLiveDiscussSupportReq();
                        req.live_id = content_id;
                        req.id = rsp0.comment_id;
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
                if (position != 0) {
                    NewsCommentDialog dialog = NewsCommentDialog.newInstance(content_id, quoted, String.valueOf(rsp0.comment_id),
                            NewsCommentDialog.LIVE_COMMENT, rsp0.username);
                    dialog.show(((Activity) mContext).getFragmentManager(), getClass().getName());
                } else {
                    NewsCommentDialog dialog = NewsCommentDialog.newInstance(content_id, quoted,
                            NewsCommentDialog.LIVE_COMMENT, rsp0.username);
                    dialog.show(((Activity) mContext).getFragmentManager(), getClass().getName());
                }
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, comment_msg, reply;
        private CheckedTextView support;
        private ImageView header, image0, image1, image2;
        private View imageLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            reply = (TextView) itemView.findViewById(R.id.reply);
            comment_msg = (TextView) itemView.findViewById(R.id.comment_msg);
            support = (CheckedTextView) itemView.findViewById(R.id.supportBtn);
            header = (ImageView) itemView.findViewById(R.id.header);
            image0 = (ImageView) itemView.findViewById(R.id.image0);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            imageLayout = itemView.findViewById(R.id.imageLayout);
        }
    }

    private class supportListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
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
                Toast.makeText(mContext, response.Message, Toast.LENGTH_SHORT).show();
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
