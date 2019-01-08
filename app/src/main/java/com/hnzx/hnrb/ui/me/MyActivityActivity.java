package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.InteractListAdapter;
import com.hnzx.hnrb.adapter.MyActivityListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetInteractListReq;
import com.hnzx.hnrb.requestbean.GetMyActivityReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetInteractListRsp;
import com.hnzx.hnrb.responsebean.GetMyActivityRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.ui.interact.InteractFragment;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class MyActivityActivity extends BaseActivity {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private MyActivityListAdapter adapter;
    private final int number = 10;
    private int offset = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_activity;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("我的活动");
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, this);

        adapter = new MyActivityListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        GetMyActivityReq req = new GetMyActivityReq();
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

    private class dataLIstener implements Response.Listener<BaseBeanArrayRsp<GetMyActivityRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetMyActivityRsp> response) {
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
