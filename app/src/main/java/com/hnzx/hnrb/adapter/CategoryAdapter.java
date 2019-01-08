package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetCategoryListRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/12 0012.
 */

public class CategoryAdapter extends BaseAdapter<GetFeaturedNewsListRsp> {
    public CategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_news_small_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetFeaturedNewsListRsp rsp = getItem(position);
        mHolder.title.setText(rsp.title);
        mHolder.look.setText(formatViews(rsp.views));
        GlideTools.GlideRounded(mContext, rsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 6);
        if (rsp.content_type.equals("content_zutu")) {
            mHolder.news_image_type.setVisibility(View.VISIBLE);
            mHolder.image_num.setVisibility(View.VISIBLE);
            mHolder.image_num.setText(rsp.zutu_total);
        } else if (rsp.content_type.equals("content_video")) {
            mHolder.news_video_time.setVisibility(View.VISIBLE);
            mHolder.news_video_type.setVisibility(View.VISIBLE);
            mHolder.news_video_time.setText(rsp.video_duration);
        }
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSelect.goWhere(mContext, rsp.content_id, rsp.is_link, rsp.link_url, rsp.internal_type, rsp.internal_id, rsp.content_type, rsp.thumb);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image, news_image_type, news_video_type;
        private TextView title, type, time, look, image_num, news_video_time;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            type.setVisibility(View.INVISIBLE);
            time = (TextView) itemView.findViewById(R.id.time);
            time.setVisibility(View.INVISIBLE);
            look = (TextView) itemView.findViewById(R.id.look);
            image = (ImageView) itemView.findViewById(R.id.image);
            news_image_type = (ImageView) itemView.findViewById(R.id.news_image_type);
            news_video_type = (ImageView) itemView.findViewById(R.id.news_video_type);
            image_num = (TextView) itemView.findViewById(R.id.image_num);
            news_video_time = (TextView) itemView.findViewById(R.id.news_video_time);
        }
    }
}
