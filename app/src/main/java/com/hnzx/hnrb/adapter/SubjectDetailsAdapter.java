package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetNewsSpecialRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.ui.news.SubjectItemListActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class SubjectDetailsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;

    public SubjectDetailsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).lists != null ? (data.get(groupPosition).lists.size() >= 3 ? 4 : data.get(groupPosition).lists.size()) : 0;
    }

    @Override
    public GetNewsSpecialRsp.ListsBeanX getGroup(int groupPosition) {
        try {
            return data.get(groupPosition);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public GetNewsSpecialRsp.ListsBeanX.ListsBean getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).lists.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewGroupHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_subject_list_group_item, parent, false);
            holder = new ViewGroupHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewGroupHolder) convertView.getTag();
        GetNewsSpecialRsp.ListsBeanX bean = getGroup(groupPosition);

        holder.title.setText(bean.name);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        MoreViewChildHolder moreHolder = null;
        if (convertView != null) {
            if (childPosition == 3 && convertView.getTag() instanceof MoreViewChildHolder)
                moreHolder = (MoreViewChildHolder) convertView.getTag();
            else if (childPosition != 3 && convertView.getTag() instanceof ViewChildHolder)
                holder = (ViewChildHolder) convertView.getTag();
            else convertView = null;
        }
        if (convertView == null) {
            if (childPosition == 3) {
                convertView = inflater.inflate(R.layout.layout_subject_more, parent, false);
                moreHolder = new MoreViewChildHolder(convertView);
                convertView.setTag(moreHolder);
            } else {
                convertView = inflater.inflate(R.layout.layout_news_subject_small_image_item, parent, false);
                holder = new ViewChildHolder(convertView);
                convertView.setTag(holder);
            }
        }
        if (childPosition == 3) {
            moreHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(SubjectItemListActivity.newIntent(context, getGroup(groupPosition).cat_id, getGroup(groupPosition).name));
                }
            });
        } else {
            final GetNewsSpecialRsp.ListsBeanX.ListsBean bean = getChild(groupPosition, childPosition);
            GlideTools.GlideRounded(context, bean.thumb, holder.image, R.drawable.bg_morentu_xiaotumoshi, 6);
            holder.title.setText(bean.title);
            holder.look.setText(formatViews(bean.views));
            holder.type.setText(Html.fromHtml("<font color='#4990e2'>" + bean.type_name + "</font>"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsSelect.goWhere(context, bean.content_id, bean.is_link, bean.link_url, bean.internal_type, bean.internal_id, bean.content_type, bean.thumb);
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    protected class ViewChildHolder {
        private ImageView image;
        private TextView title, type, time, look;
        private View itemView;

        public ViewChildHolder(View itemView) {
            AutoUtils.auto(itemView);
            this.itemView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            time = (TextView) itemView.findViewById(R.id.time);
            look = (TextView) itemView.findViewById(R.id.look);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    protected class MoreViewChildHolder {
        private View itemView;

        public MoreViewChildHolder(View itemView) {
            AutoUtils.auto(itemView);
            this.itemView = itemView;
        }
    }

    protected class ViewGroupHolder {
        private TextView title;

        public ViewGroupHolder(View itemView) {
            AutoUtils.auto(itemView);
            title = (TextView) itemView;
        }
    }

    private List<GetNewsSpecialRsp.ListsBeanX> data;

    public void addDataList(List<GetNewsSpecialRsp.ListsBeanX> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    private String formatViews(int views) {
        return views > 10000 ? views / 10000 + "万+" : String.valueOf(views);
    }
}
