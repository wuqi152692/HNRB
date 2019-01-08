package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetPoliticsFeaturedRsp;
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

public class SquareSelectionAdapter extends BaseAdapter<GetPoliticsFeaturedRsp.JingpintuijianBean> {

    public SquareSelectionAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.from(mContext).inflate(R.layout.layout_square_selection_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GetPoliticsFeaturedRsp.JingpintuijianBean bean = getItem(position);
        ViewHolder mHolder = (ViewHolder) holder;
        GlideTools.GlideRounded(mContext, bean.thumb, mHolder.image, R.drawable.bg_morentu_datumoshi, 0);
        mHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(WebActivity.WEB_URL_KEY, bean.url);
                IntentUtil.startActivity(mContext, WebActivity.class, params);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
