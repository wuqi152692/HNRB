package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetCategoryRsp;
import com.hnzx.hnrb.ui.leftsidebar.ColumnListActivity;
import com.hnzx.hnrb.view.FlowLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/27 0027.
 */

public class ColumnListAdapter extends BaseAdapter<GetCategoryRsp> {
    public ColumnListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_column_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetCategoryRsp rsp = getItem(position);
        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.name.setText(rsp.name);
        for (final GetCategoryRsp.ChildrenBean child : rsp.children) {
            TextView cat = new TextView(mContext);
            cat.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            cat.setPadding(20, 20, 20, 20);
            cat.setBackgroundColor(Color.parseColor("#f2f2f2"));
            cat.setTextColor(Color.parseColor("#131313"));
            cat.setTextSize(14);
            cat.setText(child.catname);
            cat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();
                    data.putExtra(Constant.BEAN, child.cat_id);
                    ((Activity) mContext).setResult(0x01, data);
                    ((Activity) mContext).finish();
                }
            });
            mHolder.catLayout.addView(cat);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private FlowLayout catLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            catLayout = (FlowLayout) itemView.findViewById(R.id.catLayout);
        }
    }
}
