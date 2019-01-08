package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetPoliticsserviceRsp;
import com.hnzx.hnrb.view.NSGridView;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class ServiceCenterAdapter extends BaseAdapter<GetPoliticsserviceRsp> {
    public ServiceCenterAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_service_center_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        GetPoliticsserviceRsp rsp = getItem(position);
        mHolder.title.setText(rsp.name);
        mHolder.nsGridView.setAdapter(new ServiceCenterGridAdapter(rsp.children));
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private NSGridView nsGridView;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            nsGridView = (NSGridView) itemView.findViewById(R.id.nsGridView);
        }
    }
}
