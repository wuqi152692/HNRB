package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.db.Dao.GetAllCategoryRspDao;
import com.hnzx.hnrb.requestbean.SetUserCancelDingyueColumnReq;
import com.hnzx.hnrb.requestbean.SetUserDingyueColumnReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAllCategoryRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.leftsidebar.ColumnActivity;
import com.hnzx.hnrb.ui.leftsidebar.LeftMenuFragment;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/12 0012.
 */

public class LeftMenuAdapter extends BaseAdapter<GetAllCategoryRsp> {
    private GetAllCategoryRspDao dao;

    public LeftMenuAdapter(Context context, GetAllCategoryRspDao dao) {
        super(context);
        this.dao = dao;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_left_menu_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        final GetAllCategoryRsp rsp = getItem(position);
        mHolder.loading.setVisibility(View.VISIBLE);
        List<GetAllCategoryRsp> dbData = dao.queryByCatid("catid", rsp.catid);
        if (dbData != null && dbData.size() == 1) {
            if (!dbData.get(0).timestamp.equals(rsp.timestamp)) {
                mHolder.loading.setVisibility(View.VISIBLE);
            } else mHolder.loading.setVisibility(View.INVISIBLE);
        }

        mHolder.name.setText(rsp.name);

        mHolder.subscribe.setChecked(rsp.is_ordered != 0);

        mHolder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().isLogin()) {
                    if (rsp.is_ordered == 1) {
                        SetUserCancelDingyueColumnReq req = new SetUserCancelDingyueColumnReq();
                        req.cat_id = rsp.catid;
                        App.getInstance().requestJsonDataGet(req, new orderCancelListener(mHolder.subscribe, rsp), null);
                    } else {
                        SetUserDingyueColumnReq req = new SetUserDingyueColumnReq();
                        req.cat_id = rsp.catid;
                        App.getInstance().requestJsonDataGet(req, new orderListener(mHolder.subscribe, rsp), null);
                    }
                } else {
                    mContext.startActivity(LoginActivity.newIntent(mContext, false));
                }
            }
        });

        mHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Constant.BEAN, rsp.catid);
                IntentUtil.startActivity(mContext, ColumnActivity.class, params);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private CheckedTextView subscribe;
        private ImageView loading;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            subscribe = (CheckedTextView) itemView.findViewById(R.id.subscribe);
            loading = (ImageView) itemView.findViewById(R.id.loading);
        }
    }

    private class orderCancelListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<String>> {
        private CheckedTextView subscribe;
        private GetAllCategoryRsp rsp;

        public orderCancelListener(CheckedTextView subscribe, GetAllCategoryRsp rsp) {
            this.subscribe = subscribe;
            this.rsp = rsp;
        }

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                subscribe.setChecked(false);
                Toast.makeText(mContext, "取消订阅成功", Toast.LENGTH_SHORT).show();
                rsp.is_ordered = 0;
                new updateCategoryDataAsync(rsp).execute();
            }
        }
    }

    private class orderListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        private CheckedTextView subscribe;
        private GetAllCategoryRsp rsp;

        public orderListener(CheckedTextView subscribe, GetAllCategoryRsp rsp) {
            this.subscribe = subscribe;
            this.rsp = rsp;
        }

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                subscribe.setChecked(true);
                Toast.makeText(mContext, "订阅成功", Toast.LENGTH_SHORT).show();
                rsp.is_ordered = 1;
                new updateCategoryDataAsync(rsp).execute();
            }
        }
    }

    private class updateCategoryDataAsync extends AsyncTask<String, Integer, Boolean> {
        GetAllCategoryRsp rsp;
        boolean updateTimeStamp;

        public updateCategoryDataAsync(GetAllCategoryRsp rsp) {
            this.rsp = rsp;
        }

        public updateCategoryDataAsync(GetAllCategoryRsp rsp, boolean updateTimeStamp) {
            this.rsp = rsp;
            this.updateTimeStamp = updateTimeStamp;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (dao == null)
                dao = new GetAllCategoryRspDao(mContext);

            List<GetAllCategoryRsp> dbData = dao.queryByCatid("catid", rsp.catid);
            if (dbData != null && dbData.size() > 0) {
                GetAllCategoryRsp rtmp = dbData.get(0);
                if (updateTimeStamp)
                    rtmp.timestamp = rsp.timestamp;
                else
                    rtmp.is_ordered = rsp.is_ordered;
                dao.update(rtmp);
            }
            return false;
        }
    }
}
