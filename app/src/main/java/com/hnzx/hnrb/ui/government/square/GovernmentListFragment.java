package com.hnzx.hnrb.ui.government.square;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.GovernmentListAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetPoliticsListRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class GovernmentListFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private GovernmentListAdapter adapter;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_government_list, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, mActivity);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);

        adapter = new GovernmentListAdapter(mActivity);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        GetPoliticsListRsp rsp = (GetPoliticsListRsp) getArguments().getSerializable(Constant.BEAN);
        adapter.setList(rsp.tuijian);
        stateView.setViewState(rsp != null && rsp.tuijian != null && rsp.tuijian.size() > 0 ? MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                isFirstRun = false;
                mActivity.recreate();
                break;
        }
    }
}
