package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/11 0011.
 */

public class Top10NewsAdapter extends BaseAdapter<GetLatestNewsRsp> {

    public Top10NewsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_top10_news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.inOrder.setVisibility(position > 1 ? View.INVISIBLE : View.VISIBLE);
        mHolder.inOrder1.setVisibility(position > 1 ? View.VISIBLE : View.INVISIBLE);
        if (position < 2) {
            mHolder.inOrder.setChecked(position == 0 ? true : false);
            mHolder.inOrder.setText("NO." + (position + 1));
        } else {
            mHolder.inOrder1.setChecked(position == 2 ? true : false);
            mHolder.inOrder1.setText("NO." + (position + 1));
        }
        final GetLatestNewsRsp rsp = getItem(position);
        GlideTools.GlideRounded(mContext, rsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 6);
        mHolder.title.setText(rsp.title);
        if (rsp.topname != null && rsp.topname.length() > 0) {
            if (rsp.catname != null && rsp.catname.length() > 0)
                mHolder.type.setText(Html.fromHtml(rsp.topname + " | <font color='#4990e2'>" + rsp.catname + "</font>"));
            else
                mHolder.type.setText(rsp.topname);
        } else {
            if (rsp.catname != null && rsp.catname.length() > 0)
                mHolder.type.setText(Html.fromHtml("<font color='#4990e2'>" + rsp.catname + "</font>"));
            else
                mHolder.type.setText("");
        }
        mHolder.look.setText(formatViews(rsp.views));

        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSelect.goWhere(mContext, rsp.content_id, rsp.is_link, rsp.link_url, rsp.internal_type, rsp.internal_id,
                        rsp.content_type, rsp.thumb);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private CheckedTextView inOrder, inOrder1;
        private TextView title, type, look;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            inOrder = (CheckedTextView) itemView.findViewById(R.id.inOrder);
            inOrder1 = (CheckedTextView) itemView.findViewById(R.id.inOrder1);

            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            look = (TextView) itemView.findViewById(R.id.look);

            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
