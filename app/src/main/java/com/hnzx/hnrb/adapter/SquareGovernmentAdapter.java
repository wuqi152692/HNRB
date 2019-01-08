package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.hnzx.hnrb.responsebean.GetInteractListRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsRecommendRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.government.square.UnitDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class SquareGovernmentAdapter extends BaseAdapter<GetPoliticsRecommendRsp> {
    private Fragment fragment;

    public SquareGovernmentAdapter(Context context, Fragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_square_government_horizontal_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mList != null ? (mList.size() > 8 ? 8 : mList.size()) : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetPoliticsRecommendRsp rsp = getItem(position);
        GlideTools.GlideRounded(mContext, rsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 16);
        mHolder.title.setText(rsp.catname);
        mHolder.state.setChecked(rsp.is_ordered == 1);
        mHolder.state.setText(rsp.is_ordered == 1 ? "已关注" : "关注");
        /*if (position == mList.size() - 1) {
            mHolder.itemView.setPadding(12, 0, 20, 0);
        } else if (position == 0){
            mHolder.itemView.setPadding(20, 0, 12, 0);
        }*/
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
                if (App.getInstance().isLogin()) {
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
                } else {
                    mContext.startActivity(LoginActivity.newIntent(mContext, false));
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
        private TextView title;
        private CheckedTextView state;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            state = (CheckedTextView) itemView.findViewById(R.id.state);
        }
    }
}
