package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetMyTaskListRsp;
import com.hnzx.hnrb.ui.me.UserTaskActivity;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/5/12 0012.
 */

public class UserTaskAdapter extends BaseAdapter<GetMyTaskListRsp.ListsBean> {
    public UserTaskAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_user_task_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetMyTaskListRsp.ListsBean data = getItem(position);
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.name.setText(data.name);
        mHolder.progress.setText(data.user + "/" + data.total);
        //标记当前的任务状态,有-1,0,1三个取值，-1表示无法领取积分，0表示可以领取积分，1表示已经领取过积分
        if (data.status == 0) {
            mHolder.getPoint.setChecked(true);
            mHolder.getPoint.setEnabled(true);
            mHolder.getPoint.setClickable(true);
            mHolder.getPoint.setText("领取积分");
        } else if (data.status == 1) {
            mHolder.getPoint.setChecked(false);
            mHolder.getPoint.setEnabled(true);
            mHolder.getPoint.setClickable(false);
            mHolder.getPoint.setText("已领取");
        } else {
            mHolder.getPoint.setChecked(false);
            mHolder.getPoint.setEnabled(false);
            mHolder.getPoint.setClickable(true);
            mHolder.getPoint.setText("领取积分");
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, progress;
        private CheckedTextView getPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            progress = (TextView) itemView.findViewById(R.id.progress);
            getPoint = (CheckedTextView) itemView.findViewById(R.id.getPoint);
        }
    }
}
