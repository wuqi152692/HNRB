package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.MyActivityListAdapter;
import com.hnzx.hnrb.adapter.MyCollectAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetMineCollectionReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMineCollectionRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class MyCollectActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private MyCollectAdapter adapter;
    private final int number = 10;
    private int offset = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("我的收藏");
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, this);

        adapter = new MyCollectAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        GetMineCollectionReq req = new GetMineCollectionReq();
        req.offset = offset;
        req.number = number;

        App.getInstance().requestJsonArrayDataGet(req, new dataLIstener(), new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                initData();
            }

            @Override
            public void onLoadMore() {
                offset += number;
                initData();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private class dataLIstener implements Response.Listener<BaseBeanArrayRsp<GetMineCollectionRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetMineCollectionRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    if (response.Info != null && response.Info.size() < number)
                        recyclerView.setNoMore(true);
                } else {
                    adapter.addAll(response.Info);
                    recyclerView.loadMoreComplete();
                    if (response.Info == null || response.Info.size() < number)
                        recyclerView.setNoMore(true);
                }
            }
        }
    }
}
