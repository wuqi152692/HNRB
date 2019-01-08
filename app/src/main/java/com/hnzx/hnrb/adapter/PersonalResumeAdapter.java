package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetResumeContentRsp;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class PersonalResumeAdapter extends BaseAdapter<GetResumeContentRsp> {
    public PersonalResumeAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_personal_resume_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        GetResumeContentRsp content = getItem(position);
        mHolder.content.setText(content.position);
        mHolder.startYear.setText(content.begin_year);
        mHolder.startMonth.setText(content.begin_month);
        if (content.end_month.equals("now")) {
            mHolder.endYear.setText("至今");
            mHolder.endMonth.setText("");
        } else {
            mHolder.endYear.setText(content.end_year);
            mHolder.endMonth.setText(content.end_month);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView startYear, endYear, startMonth, endMonth, content;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            startYear = (TextView) itemView.findViewById(R.id.start_year);
            startMonth = (TextView) itemView.findViewById(R.id.start_month);
            endYear = (TextView) itemView.findViewById(R.id.end_year);
            endMonth = (TextView) itemView.findViewById(R.id.end_month);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
