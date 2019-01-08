package com.hnzx.hnrb.adapter;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetUserSignHistoryRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mingancai on 2016-06-12 at 5:37 PM.
 */
public class SignlogAdapter extends BaseAdapter {

    List<GetUserSignHistoryRsp.ListBean> mList = new ArrayList<>();

    @Override
    public int getCount() {
        if (null == mList)
            return 0;
        else
            return mList.size();
    }

    @Override
    public GetUserSignHistoryRsp.ListBean getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    /**
     * 增加lists
     */
    public void addAll(List<GetUserSignHistoryRsp.ListBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView,
                        final ViewGroup parent) {

        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_signlog_item, null);
            viewholder = new ViewHolder();
            /* 得到各个控件的对象 */
            viewholder.day = (TextView) convertView.findViewById(R.id.day);
            viewholder.itemBg = convertView.findViewById(R.id.itemBg);
            viewholder.signArrow = convertView.findViewById(R.id.signArrow);
            viewholder.signGlod = (CheckBox) convertView.findViewById(R.id.signGlod);
            convertView.setTag(viewholder);// 绑定ViewHolder对象
        } else
            viewholder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象

        final GetUserSignHistoryRsp.ListBean bean = getItem(position);
        if (position / 7 != getCount() / 7) {
            if ((position + 1) % 7 == 0)
                convertView.setPadding(2, position > 7 ? 1 : 2, 2, 1);
            else
                convertView.setPadding(2, position > 7 ? 1 : 2, 0, 1);
        } else {// 最后一行
            if ((position + 1) % 7 != 0)
                convertView.setPadding(2, 1, 0, 2);
            else
                convertView.setPadding(2, 1, 2, 2);
        }
        if (bean.day_no == 0) {
            viewholder.day.setVisibility(View.INVISIBLE);
            viewholder.signArrow.setVisibility(View.INVISIBLE);
            viewholder.signGlod.setVisibility(View.INVISIBLE);
        } else {
            viewholder.day.setVisibility(View.VISIBLE);
            viewholder.day.setText(String.valueOf(bean.day_no));
            viewholder.signArrow.setVisibility(bean.is_signed != 0 ? View.VISIBLE : View.INVISIBLE);
            viewholder.signGlod.setVisibility(bean.is_signed == 0 ? View.VISIBLE : View.INVISIBLE);
            viewholder.signGlod.setChecked(bean.is_today == 1);
            viewholder.itemBg.setBackgroundColor(bean.is_today == 1 ? Color.TRANSPARENT : Color.WHITE);
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView day;
        private View itemBg, signArrow;
        private CheckBox signGlod;
    }

}
