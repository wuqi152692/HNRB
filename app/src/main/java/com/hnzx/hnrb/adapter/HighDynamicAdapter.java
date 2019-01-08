package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetHighDynamicListRsp;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.ui.news.HighDynamicActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class HighDynamicAdapter extends BaseAdapter<GetHighDynamicListRsp> {
    public HighDynamicAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_high_dynamic_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final GetHighDynamicListRsp rsp = getItem(position);
        try {
            if (position == 0) {
                if (rsp.created.contains("-")) {
                    mHolder.year.setText(rsp.created.split("-")[0] + "年");
                    mHolder.year.setVisibility(View.VISIBLE);
                }
            } else {
                GetHighDynamicListRsp rspNext = getItem(position - 1);
                String year = rsp.created.split("-")[0];
                String yearNext = rspNext.created.split("-")[0];
                if (year.equals(yearNext))
                    mHolder.year.setVisibility(View.GONE);
                else {
                    mHolder.year.setText(year + "年");
                    mHolder.year.setVisibility(View.VISIBLE);
                }
            }
            mHolder.date.setText(rsp.created.trim().substring(5, 10).replace("-", "月") + "日");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHolder.title.setText(rsp.title);
        mHolder.support.setText(String.valueOf(rsp.support));
        mHolder.look.setText(formatViews(rsp.views));
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSelect.goWhere(mContext, rsp.content_id, rsp.is_link, rsp.link_url, rsp.internal_type, rsp.internal_id, rsp.content_type, rsp.thumb);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView year, date, title, support, look;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            year = (TextView) itemView.findViewById(R.id.year);
            date = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById(R.id.title);
            support = (TextView) itemView.findViewById(R.id.support);
            look = (TextView) itemView.findViewById(R.id.look);
        }
    }
}
