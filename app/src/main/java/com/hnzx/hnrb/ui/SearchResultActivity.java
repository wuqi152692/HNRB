package com.hnzx.hnrb.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsCommentAdapter;
import com.hnzx.hnrb.adapter.NewsListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetSearchDataReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private NewsListAdapter adapter;
    private String content;
    private int offset = 0;
    private final int number = 10;
    private GetSearchDataReq bean;

    @Override
    protected int getLayoutId() {
        content = getIntent().getStringExtra(Constant.BEAN);
        return R.layout.activity_search_result;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("搜索");

        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, this);
        adapter = new NewsListAdapter(this);
        adapter.notifyItemStyle(0);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        if (bean == null) {
            bean = new GetSearchDataReq();
            bean.content = content;
            bean.number = number;
        }
        bean.offset = offset;

        App.getInstance().requestJsonArrayDataGet(bean, new Response.Listener<BaseBeanArrayRsp<GetFeaturedNewsListRsp>>() {
            @Override
            public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
                if (response != null && response.Status == 1) {
                    if (offset == 0) {
                        stateView.setViewState(response.Info != null && response.Info.size() > 0 ? MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
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
                } else {
                    if (offset == 0)
                        stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        }, new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }
}
