package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetVideoRelationsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.ui.audio.VideoDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class VedioAboutAdapter extends BaseAdapter<GetVideoRelationsRsp.RelationsBean> {
    public VedioAboutAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_vedio_about_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetVideoRelationsRsp.RelationsBean imageRsp = getItem(position);
        GlideTools.GlideRounded(mContext, imageRsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 8);
        mHolder.title.setText(imageRsp.title);
        if (position == getItemCount() - 1) {
            mHolder.itemView.setPadding(0, 0, 22, 0);
        }
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VideoDetailsActivity) mContext).finish();
                NewsSelect.goWhere(mContext, imageRsp.content_id, 0, "", "", "", "", imageRsp.thumb);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
