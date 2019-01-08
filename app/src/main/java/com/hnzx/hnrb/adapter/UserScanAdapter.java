package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.hnzx.hnrb.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

public class UserScanAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> data = new ArrayList<>();
    int selectPositon;

    public UserScanAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Integer getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_user_scan_item, parent, false);
            AutoUtils.auto(convertView);
            holder.month = (CheckedTextView) convertView;

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (selectPositon == position) {
            holder.month.setChecked(true);
        } else {
            holder.month.setChecked(false);
        }
        holder.month.setText(getItem(position) + "月");
        return convertView;
    }

    private class ViewHolder {
        private CheckedTextView month;
    }

    public void addData(ArrayList<Integer> data, int selectPositon) {
        this.data = data;
        this.selectPositon = selectPositon;
        notifyDataSetChanged();
    }

}
