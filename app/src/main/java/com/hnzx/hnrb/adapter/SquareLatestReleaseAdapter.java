package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.SetCancelOrderCategoryReq;
import com.hnzx.hnrb.requestbean.SetMakeOrderCategoryReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.ui.government.square.UnitDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class SquareLatestReleaseAdapter extends BaseAdapter<GetLatestNewsRsp> {
    private Fragment fragment;

    public SquareLatestReleaseAdapter(Context context, Fragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_square_latest_release_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetLatestNewsRsp rsp = getItem(position);
        Log.d("getlates_", "onBindViewHolder: "+rsp);
        GlideTools.Glide(mContext, rsp.image, mHolder.image, R.drawable.bg_morentu_xiaotumoshi);
        mHolder.title.setText(rsp.title);
        mHolder.name.setText(rsp.catname);
        mHolder.date.setText(rsp.created);
        mHolder.state.setChecked(rsp.is_ordered == 1);
        mHolder.state.setText(rsp.is_ordered == 1 ? "已关注" : "关注");
//        if (position == mList.size() - 1)
//            mHolder.itemView.setPadding(12, 0, 20, 0);
//        else if (position == 0) mHolder.itemView.setPadding(20, 0, 12, 0);
        // 此处点击跳转到机构详情页面
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsSelect.goWhere(mContext, rsp.content_id, rsp.is_link, rsp.link_url, rsp.internal_type,
                        rsp.internal_id, rsp.content_type, rsp.thumb, true);
            }
        });

        // 点击跳转到机构详情页面
        mHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.BEAN, rsp.cat_id);
                IntentUtil.startActivityForResult(fragment, UnitDetailsActivity.class, params, UnitDetailsActivity.RESULT_CODE);
            }
        });

        mHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.BEAN, rsp.cat_id);
                IntentUtil.startActivityForResult(fragment, UnitDetailsActivity.class, params, UnitDetailsActivity.RESULT_CODE);
            }
        });

        // 机构订阅开启以及关闭
        mHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!App.getInstance().isLogin()) {
                    mContext.startActivity(LoginActivity.newIntent(mContext, false));
                } else {
                    mHolder.state.setChecked(!mHolder.state.isChecked());
                    mHolder.state.setText(mHolder.state.isChecked() ? "已关注" : "关注");
                    //添加机构订阅接口
                    if (mHolder.state.isChecked()) {
                        SetMakeOrderCategoryReq req = new SetMakeOrderCategoryReq();
                        req.cat_id = rsp.cat_id;
                        App.getInstance().requestJsonDataGet(req, new makeOrderAuthorListener(), null);
                    } else {
                        SetCancelOrderCategoryReq req = new SetCancelOrderCategoryReq();
                        req.cat_id = rsp.cat_id;
                        App.getInstance().requestJsonDataGet(req, new cancelOrderAuthorListener(), null);
                    }
                }
            }
        });


    }

    private class makeOrderAuthorListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {

        }
    }

    private class cancelOrderAuthorListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {

        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, date, title;
        private CheckedTextView state;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById(R.id.title);
            state = (CheckedTextView) itemView.findViewById(R.id.state);
        }
    }
}
