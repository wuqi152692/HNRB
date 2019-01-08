package com.hnzx.hnrb.ui.government.governmentbox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.AboutNewsAdapter;
import com.hnzx.hnrb.adapter.PersonalResumeAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.requestbean.GetResumeContentReq;
import com.hnzx.hnrb.requestbean.GetResumeRelationReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetResumeContentRsp;
import com.hnzx.hnrb.responsebean.GetResumeRelationRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * 个人履历 相关新闻
 */
public class AboutNewsFragment extends BaseFragment {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private AboutNewsAdapter adapter;
    private final int number = 10;
    private int offset = 0;

    private String resume_id;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        resume_id = getArguments().getString(LeaderAboutActivity.DATA_KEY);
        return inflater.inflate(R.layout.fragment_about_news, container, false);
    }

    @Override
    protected void initViews(View view) {
        stateView = (MultiStateView) view.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, getActivity());

        adapter = new AboutNewsAdapter(getActivity());
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
        GetResumeRelationReq req = new GetResumeRelationReq();
        req.resume_id = resume_id;
        req.number = number;
        req.offset = offset;

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), null);
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<GetResumeRelationRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetResumeRelationRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.addAll(response.Info);
                recyclerView.setNoMore(true);
            }
            recyclerView.refreshComplete();
            recyclerView.loadMoreComplete();
        }
    }
}
