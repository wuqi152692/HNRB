package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetMediaImageRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.audio.ImageActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class ImageAudioAdapter extends BaseAdapter<GetMediaImageRsp> {
    public ImageAudioAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_image_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetMediaImageRsp imageRsp = getItem(position);
        GlideTools.GlideRounded(mContext, imageRsp.thumb, mHolder.image, R.drawable.bg_morentu_datumoshi, 8);
        mHolder.imagePages.setText(imageRsp.total + "张");
        mHolder.title.setText(imageRsp.title);
        mHolder.time.setText(imageRsp.created);
        mHolder.scan.setText(formatViews(imageRsp.views));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageActivity.class);
                intent.putExtra(ImageActivity.DATA_KEY, imageRsp.content_id);
                mContext.startActivity(intent);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView imagePages, title, time, scan;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            imagePages = (TextView) itemView.findViewById(R.id.imagePages);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            scan = (TextView) itemView.findViewById(R.id.scan);
        }
    }
}
