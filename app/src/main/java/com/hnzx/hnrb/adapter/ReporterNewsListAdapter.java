package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.responsebean.GetNewsSpecialRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class ReporterNewsListAdapter extends BaseAdapter<GetLatestNewsRsp> {
    public ReporterNewsListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_news_small_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetLatestNewsRsp relation = getItem(position);
        GlideTools.GlideRounded(mContext, relation.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 8);
        mHolder.title.setText(relation.title);
        mHolder.type.setText(Html.fromHtml("<font color='#ff0000'>" + relation.type_name + "</font>") + relation.internal_type);
        mHolder.time.setText(relation.created);
        mHolder.look.setText(formatViews(relation.views));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSelect.goWhere(mContext, relation.content_id, relation.is_link, relation.link_url, relation.internal_type, relation.internal_id, relation.content_type, relation.thumb);
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