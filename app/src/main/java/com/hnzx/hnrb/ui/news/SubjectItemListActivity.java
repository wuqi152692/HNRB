package com.hnzx.hnrb.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetCategoryListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.view.MultiStateView;
import com.hnzx.hnrb.view.TopHeadView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class SubjectItemListActivity extends BaseActivity {
    private TopHeadView headView;
    private MultiStateView stateView;
    private XRecyclerView recyclerview;
    private NewsListAdapter adapter;
    private static final String CATIDKEY = "CATIDKEY";
    private static final String CATNAMEKEY = "CATNAMEKEY";
    private String cat_id, cat_name;
    private final int number = 10;
    private int offset = 0;

    public static Intent newIntent(Context context, String cat_id, String cat_name) {
        Intent intent = new Intent(context, SubjectItemListActivity.class);
        intent.putExtra(CATIDKEY, cat_id);
        intent.putExtra(CATNAMEKEY, cat_name);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        cat_id = getIntent().getStringExtra(CATIDKEY);
        cat_name = getIntent().getStringExtra(CATNAMEKEY);
        return R.layout.activity_subject_item_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        headView = (TopHeadView) findViewById(R.id.head_view);
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        recyclerview = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new NewsListAdapter(this);
        recyclerview.setAdapter(adapter);

        headView.setHeadTitle(cat_name);
    }

    @Override
    protected void initData() {
        GetCategoryListReq req = new GetCategoryListReq();
        req.cat_id = cat_id;
        req.number = number;
        req.offset = offset;

        App.getInstance().requestJsonArrayDataGet(req, new Response.Listener<BaseBeanArrayRsp<GetFeaturedNewsListRsp>>() {
            @Override
            public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
                if (response != null && response.Info != null && response.Info.size() > 0) {
                    if (offset == 0) {
                        stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                        adapter.setList(response.Info);
                        if (response.Info.size() < number)
                            recyclerview.setNoMore(true);
                    } else
                        adapter.addAll(response.Info);
                } else {
                    if (offset == 0)
                        stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    else recyclerview.setNoMore(true);
                }
                if (offset == 0) recyclerview.refreshComplete();
                else recyclerview.loadMoreComplete();
            }
        }, new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
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
}
