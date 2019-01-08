package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetResumeRelationRsp;
import com.hnzx.hnrb.ui.NewsSelect;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class AboutNewsAdapter extends BaseAdapter<GetResumeRelationRsp> {
    public AboutNewsAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_news_big_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetResumeRelationRsp relation = getItem(position);
        mHolder.imageLayout.setVisibility(View.GONE);
        mHolder.title.setText(relation.title);
        mHolder.type.setText(Html.fromHtml("<font color='#ff0000'>" + relation.type_name + "</font>") +
                (relation.internal_type.equals("none") ? "" : relation.internal_type));
        mHolder.time.setText(relation.created);
        mHolder.look.setVisibility(View.GONE);
//        mHolder.look.setText(formatViews(relation.views));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mHolder.look.setText(formatViews(relation.views + 1));
                NewsSelect.goWhere(mContext, relation.content_id, relation.is_link, relation.link_url, relation.internal_type, relation.internal_id, relation.content_type, relation.thumb);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private View imageLayout;
        private TextView title, type, time, look;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            time = (TextView) itemView.findViewById(R.id.time);
            look = (TextView) itemView.findViewById(R.id.look);
            imageLayout = itemView.findViewById(R.id.imageLayout);
        }
    }
}
