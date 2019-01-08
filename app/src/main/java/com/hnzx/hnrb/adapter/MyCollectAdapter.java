package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetMineCollectionRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class MyCollectAdapter extends BaseAdapter<GetMineCollectionRsp> {
    private int IMAGE_TYPE = 1;
    private int GENERAL_TYPE = 2;

    public MyCollectAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).content_type.equals("content_zutu") ? IMAGE_TYPE : GENERAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IMAGE_TYPE)
            return new ViewHolder(mInflater.inflate(R.layout.layout_collet_big_image_item, parent, false));
        else
            return new ViewHolder(mInflater.inflate(R.layout.layout_collet_small_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetMineCollectionRsp relation = getItem(position);
        GlideTools.GlideRounded(mContext, relation.thumb, mHolder.image, relation.content_type.equals("content_zutu") ? R.drawable.bg_morentu_datumoshi : R.drawable.bg_morentu_xiaotumoshi, 8);
        mHolder.title.setText(relation.title);
        if (relation.topname != null && relation.topname.length() > 0) {
            if (relation.catname != null && relation.catname.length() > 0)
                mHolder.type.setText(Html.fromHtml(relation.topname + " | <font color='#4990e2'>" + relation.catname + "</font>"));
            else
                mHolder.type.setText(relation.topname);
        } else {
            if (relation.catname != null && relation.catname.length() > 0)
                mHolder.type.setText(Html.fromHtml("<font color='#4990e2'>" + relation.catname + "</font>"));
            else
                mHolder.type.setText("");
        }
        mHolder.time.setText(relation.addtime);
        mHolder.look.setText(formatViews(relation.views));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSelect.goWhere(mContext, relation.content_id, 0, "", "", "", relation.content_type, relation.thumb);
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
