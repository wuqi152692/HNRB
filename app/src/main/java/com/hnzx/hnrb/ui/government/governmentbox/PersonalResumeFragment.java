package com.hnzx.hnrb.ui.government.governmentbox;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.PersonalResumeAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.requestbean.GetResumeContentReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetResumeContentRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;


public class PersonalResumeFragment extends BaseFragment {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private PersonalResumeAdapter adapter;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_personal_resume, container, false);
    }

    @Override
    protected void initViews(View view) {
        stateView = (MultiStateView) view.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);

        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);

        adapter = new PersonalResumeAdapter(getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        String resume_id = getArguments().getString(LeaderAboutActivity.DATA_KEY);
        GetResumeContentReq req = new GetResumeContentReq();
        req.resume_id = resume_id;

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), null);
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<GetResumeContentRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetResumeContentRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.addAll(response.Info);
            }
        }
    }
}
