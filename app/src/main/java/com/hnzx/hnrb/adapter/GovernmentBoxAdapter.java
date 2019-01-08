package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetResumeListRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.government.governmentbox.LeaderAboutActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class GovernmentBoxAdapter extends BaseAdapter<GetResumeListRsp> {
    public GovernmentBoxAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_government_box_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GetResumeListRsp resume = getItem(position);
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, LeaderAboutActivity.class);
                it.putExtra(LeaderAboutActivity.DATA_KEY, resume);
                mContext.startActivity(it);
            }
        });
        mHolder.name.setText(resume.name);
        mHolder.msg.setText(resume.brief);
        GlideTools.GlideRounded(mContext, resume.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 8);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, msg;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            msg = (TextView) itemView.findViewById(R.id.msg);
        }
    }
}
