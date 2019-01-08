package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.SetCancelOrderCategoryReq;
import com.hnzx.hnrb.requestbean.SetMakeOrderCategoryReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsListRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.government.square.GovernmentListActivity;
import com.hnzx.hnrb.ui.government.square.UnitDetailsActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class GovernmentListAdapter extends BaseAdapter<GetPoliticsListRsp.TuijianBean> {
    public GovernmentListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_square_government_vertical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetPoliticsListRsp.TuijianBean rsp = getItem(position);
        GlideTools.GlideRounded(mContext, rsp.thumb, mHolder.image, R.drawable.bg_morentu_xiaotumoshi, 6);
        mHolder.title.setText(rsp.catname);
        mHolder.num.setText("订阅量:" + rsp.ordered);
        mHolder.state.setChecked(rsp.is_ordered == 1);
        mHolder.state.setText(rsp.is_ordered == 1 ? "已订阅" : "订阅");
        mHolder.state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().isLogin()) {
                    mHolder.state.setChecked(!mHolder.state.isChecked());
                    mHolder.state.setText(mHolder.state.isChecked() ? "已订阅" : "订阅");
                    //添加机构订阅接口
                    if (!mHolder.state.isChecked()) {
                        SetMakeOrderCategoryReq req = new SetMakeOrderCategoryReq();
                        req.cat_id = rsp.cat_id;
                        App.getInstance().requestJsonDataGet(req, new makeOrderAuthorListenner(), null);
                    } else {
                        SetCancelOrderCategoryReq req = new SetCancelOrderCategoryReq();
                        req.cat_id = rsp.cat_id;
                        App.getInstance().requestJsonDataGet(req, new cancelOrderAuthorListenner(), null);
                    }
                } else {
                    mContext.startActivity(LoginActivity.newIntent(mContext, false));
                }
            }
        });
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.BEAN, rsp.cat_id);
                IntentUtil.startActivityForResult((Activity) mContext, UnitDetailsActivity.class, params, UnitDetailsActivity.RESULT_CODE);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, num;
        private CheckedTextView state;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            num = (TextView) itemView.findViewById(R.id.num);
            state = (CheckedTextView) itemView.findViewById(R.id.state);
        }
    }

    private class makeOrderAuthorListenner implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {

        }
    }

    private class cancelOrderAuthorListenner implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {

        }
    }
}
