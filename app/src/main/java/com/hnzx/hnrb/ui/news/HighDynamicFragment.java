package com.hnzx.hnrb.ui.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.HighDynamicAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetHighDynamicListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetHighDynamicListRsp;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class HighDynamicFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;

    private HighDynamicAdapter adapter;

    private int offset = 0;
    private final int number = 10;

    private int index = 1;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        Bundle bundle = getArguments();
        if (bundle != null) index = bundle.getInt(Constant.BEAN);

        return inflater.inflate(R.layout.fragment_high_dynamic, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false));

        adapter = new HighDynamicAdapter(mActivity);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                initDatas();
            }

            @Override
            public void onLoadMore() {
                offset += number;
                initDatas();
            }
        });
    }

    @Override
    protected void initDatas() {
        GetHighDynamicListReq req = new GetHighDynamicListReq();
        req.index = index;
        req.number = number;
        req.offset = offset;

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), new MyErrorListener(stateView));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initDatas();
                break;
        }
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetHighDynamicListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetHighDynamicListRsp> response) {
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
            } else stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}
