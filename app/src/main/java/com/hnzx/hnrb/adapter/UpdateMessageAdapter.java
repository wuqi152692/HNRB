package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/5/17 0017.
 */

public class UpdateMessageAdapter extends BaseAdapter<String> {
    public UpdateMessageAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.dialog_update_app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.message.setText(getItem(position));
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView message;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }
}
