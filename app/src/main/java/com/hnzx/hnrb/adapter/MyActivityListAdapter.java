package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetMyActivityRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.interact.ActiveActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class MyActivityListAdapter extends BaseAdapter<GetMyActivityRsp> {

    public MyActivityListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActiveViewHolder(mInflater.inflate(R.layout.layout_interact_item_active, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setActiveHolder((ActiveViewHolder) holder, getItem(position));
    }

    /**
     * 设置活动
     *
     * @param holder
     * @param rsp
     */
    private void setActiveHolder(ActiveViewHolder holder, final GetMyActivityRsp rsp) {
        GlideTools.GlideRounded(mContext, rsp.thumb, holder.image, R.drawable.bg_morentu_datumoshi, 8);
        holder.title.setText(rsp.title);
        holder.time.setText(rsp.created);
        holder.lookNum.setText(formatViews(rsp.views));
//        holder.remain_num_value.setText(String.valueOf(rsp.remained));
        holder.state.setText(rsp.type_name);
//        holder.end_time.setText(rsp.start_hm);
//        holder.end_date.setText(rsp.start_md);
        if (rsp.type_name.equals("进行中")) {
            holder.stateView.setEnabled(true);
            holder.stateView.setClickable(true);
            holder.stateView.setChecked(true);
            holder.state.setChecked(true);
        } else if (rsp.type_name.equals("已结束")) {
            holder.stateView.setChecked(false);
            holder.stateView.setEnabled(true);
            holder.stateView.setClickable(false);
            holder.state.setChecked(false);
        } else {
            holder.stateView.setChecked(false);
            holder.stateView.setEnabled(false);
            holder.stateView.setClickable(true);
            holder.state.setChecked(true);
            holder.state.setTextColor(Color.parseColor("#46a9ec"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ActiveActivity.class);
                it.putExtra(ActiveActivity.DATA_KEY, rsp.activity_id);
                mContext.startActivity(it);
            }
        });
    }

    private class ActiveViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private CheckedTextView state, stateView;
        private TextView title, time, lookNum, end_time, end_date, remain_num_value;

        public ActiveViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            lookNum = (TextView) itemView.findViewById(R.id.lookNum);
            end_time = (TextView) itemView.findViewById(R.id.end_time);
            end_date = (TextView) itemView.findViewById(R.id.end_date);
            remain_num_value = (TextView) itemView.findViewById(R.id.remain_num_value);

            stateView = (CheckedTextView) itemView.findViewById(R.id.stateView);
            state = (CheckedTextView) itemView.findViewById(R.id.state);
        }
    }
}
