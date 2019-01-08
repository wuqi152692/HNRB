package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetAboutNewsListRsp;
import com.hnzx.hnrb.responsebean.GetResumeRelationRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.ui.leftsidebar.ColumnActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class NewsDetailsAboutAdapter extends BaseAdapter<GetAboutNewsListRsp> {
    public NewsDetailsAboutAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_news_small_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetAboutNewsListRsp about = getItem(position);
        GlideTools.GlideRounded(mContext, about.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 8);
        mHolder.title.setText(about.title);
        if (about.topname != null && about.topname.length() > 0) {
            if (about.catname != null && about.catname.length() > 0)
                mHolder.type.setText(Html.fromHtml(about.topname + " | <font color='#4990e2'>" + about.catname + "</font>"));
            else
                mHolder.type.setText(about.topname);
        } else {
            if (about.catname != null && about.catname.length() > 0)
                mHolder.type.setText(Html.fromHtml("<font color='#4990e2'>" + about.catname + "</font>"));
            else
                mHolder.type.setText("");
        }
        mHolder.time.setText(about.created);
        mHolder.look.setText(formatViews(about.views));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSelect.goWhere(mContext, about.content_id, about.is_link, about.link_url, "", "", about.content_type, about.thumb);
            }
        });
        mHolder.type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.startActivity(ColumnActivity.newIntence(mContext, about.cat_id));
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, type, time, look;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            time = (TextView) itemView.findViewById(R.id.time);
            look = (TextView) itemView.findViewById(R.id.look);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
