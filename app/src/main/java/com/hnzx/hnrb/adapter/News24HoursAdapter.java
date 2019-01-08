package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.requestbean.GetLatestNewsReq;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.tools.DateUtils;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Date;

/**
 * @author: mingancai
 * @Time: 2017/4/11 0011.
 */

public class News24HoursAdapter extends BaseAdapter<GetLatestNewsRsp> {

    public News24HoursAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position != 0 ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new ViewHolder(mInflater.inflate(R.layout.layout_news_24hours_item, parent, false));
        else
            return new FristViewHolder(mInflater.inflate(R.layout.layout_news_24hours_frist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GetLatestNewsRsp rsp = getItem(position);
        Date now = DateUtils.stringToDate(rsp.created, DateUtils.patternLong);
        if (getItemViewType(position) == 1) {
            ViewHolder mHolder = (ViewHolder) holder;
            GlideTools.GlideRounded(mContext, rsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 6);
            Date before = DateUtils.stringToDate(getItem(position - 1).created, DateUtils.patternLong);
            if (now.getDay() != before.getDay()) {
                mHolder.date.setText(DateUtils.dateToString(now, DateUtils.patternMMdd));
                mHolder.date.setVisibility(View.VISIBLE);
            } else {
                mHolder.date.setVisibility(View.GONE);
            }
            mHolder.time.setText(DateUtils.dateToString(now, DateUtils.patternHHmm));
            mHolder.title.setText(rsp.title);
            if (rsp.topname != null && rsp.topname.length() > 0) {
                if (rsp.catname != null && rsp.catname.length() > 0)
                    mHolder.type.setText(Html.fromHtml(rsp.topname + " | <font color='#4990e2'>" + rsp.catname + "</font>"));
                else
                    mHolder.type.setText(rsp.topname);
            } else {
                if (rsp.catname != null && rsp.catname.length() > 0)
                    mHolder.type.setText(Html.fromHtml("<font color='#4990e2'>" + rsp.catname + "</font>"));
                else
                    mHolder.type.setText("");
            }
            mHolder.look.setText(formatViews(rsp.views));
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsSelect.goWhere(mContext, rsp.content_id, rsp.is_link, rsp.link_url, rsp.internal_type,
                            rsp.internal_id, rsp.content_type, rsp.thumb);
                }
            });
        } else {
            FristViewHolder mHolder = (FristViewHolder) holder;
            GlideTools.GlideRounded(mContext, rsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 6);
            mHolder.date.setText(DateUtils.dateToString(now, DateUtils.patternMMdd));
            mHolder.time.setText(DateUtils.dateToString(now, DateUtils.patternHHmm));
            mHolder.date.setVisibility(View.VISIBLE);
            mHolder.title.setText(rsp.title);
            mHolder.type.setText(Html.fromHtml("新闻24小时" + " | <font color='#4990e2'>" + rsp.catname + "</font>"));
            mHolder.look.setText(formatViews(rsp.views));
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsSelect.goWhere(mContext, rsp.content_id, rsp.is_link, rsp.link_url, rsp.internal_type,
                            rsp.internal_id, rsp.content_type, rsp.thumb);
                }
            });
        }
    }

    private class FristViewHolder extends RecyclerView.ViewHolder {
        private TextView date, time, title, type, look;
        private ImageView image;

        public FristViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            look = (TextView) itemView.findViewById(R.id.look);

            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date, time, title, type, look;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            look = (TextView) itemView.findViewById(R.id.look);

            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
