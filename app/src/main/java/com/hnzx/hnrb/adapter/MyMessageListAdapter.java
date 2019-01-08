package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.requestbean.GetNewsCommentsSupportReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetUserMessageRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class MyMessageListAdapter extends BaseAdapter<GetUserMessageRsp> {
    public MyMessageListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_my_message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetUserMessageRsp rsp1 = getItem(position);
        GlideTools.GlideRound(mContext, rsp1.avatar, mHolder.header, R.drawable.icon_default_round_head);
        mHolder.name.setText(rsp1.username);
        mHolder.time.setText(formatTime(rsp1.created));
        mHolder.comment_msg.setText(Html.fromHtml("<font color='#4990e2'>回复你：</font>" + rsp1.content));
        mHolder.support.setText(String.valueOf(rsp1.support));

        mHolder.support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHolder.support.isChecked() && !rsp1.isSupport) {
                    rsp1.isSupport = true;
                    mHolder.support.setChecked(true);
                    GetNewsCommentsSupportReq req = new GetNewsCommentsSupportReq();
                    req.content_id = rsp1.content_id;
                    req.id = rsp1.comment_id;
                    App.getInstance().requestJsonDataGet(req, new supportListener(Integer.valueOf(rsp1.support), mHolder.support), null);
                }
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, time, comment_msg;
        private CheckedTextView support;
        private ImageView header;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            comment_msg = (TextView) itemView.findViewById(R.id.comment_msg);
            support = (CheckedTextView) itemView.findViewById(R.id.supportBtn);
            header = (ImageView) itemView.findViewById(R.id.header);
        }
    }

    private class supportListener implements Response.Listener<BaseBeanRsp<String>> {

        private int count;

        private CheckedTextView mTextView;

        public supportListener(int count, CheckedTextView mTextView) {
            this.count = count;
            this.mTextView = mTextView;
        }
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            try {
                if (response != null && response.isSuccess()) {
                    mTextView.setText(String.valueOf(count + 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
