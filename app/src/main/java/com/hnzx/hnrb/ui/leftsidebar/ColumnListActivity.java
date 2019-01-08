package com.hnzx.hnrb.ui.leftsidebar;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.ColumnListAdapter;
import com.hnzx.hnrb.adapter.News24HoursAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetCategoryReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetCategoryRsp;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class ColumnListActivity extends BaseActivity implements View.OnClickListener{
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private ColumnListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_column_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreEnabled(false);

        adapter = new ColumnListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        GetCategoryReq req = new GetCategoryReq();

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), new MyErrorListener(stateView));

    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetCategoryRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetCategoryRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(response.Info != null && response.Info.size() > 0 ? MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
                adapter.setList(response.Info);
            }
            recyclerView.refreshComplete();
        }
    }
}
