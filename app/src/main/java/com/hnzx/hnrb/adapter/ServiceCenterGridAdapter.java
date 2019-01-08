package com.hnzx.hnrb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetPoliticsserviceRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.web.WebActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class ServiceCenterGridAdapter extends android.widget.BaseAdapter {
    private List<GetPoliticsserviceRsp.ChildrenBean> children;

    public ServiceCenterGridAdapter(List<GetPoliticsserviceRsp.ChildrenBean> children) {
        this.children = children;
    }

    @Override
    public int getCount() {
        return children != null ? children.size() : 0;
    }

    @Override
    public GetPoliticsserviceRsp.ChildrenBean getItem(int position) {
        return children.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_service_center_grid_item, parent, false);
            AutoUtils.auto(convertView);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        final GetPoliticsserviceRsp.ChildrenBean bean = getItem(position);
        GlideTools.Glide(parent.getContext(), bean.logo, holder.image, R.drawable.bg_morentu_xiaotumoshi);
        holder.title.setText(bean.name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(WebActivity.WEB_URL_KEY, bean.url);
                IntentUtil.startActivity(parent.getContext(), WebActivity.class, params);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView title;
        private ImageView image;

        public ViewHolder(View item) {
            title = (TextView) item.findViewById(R.id.title);
            image = (ImageView) item.findViewById(R.id.image);
        }
    }
}
