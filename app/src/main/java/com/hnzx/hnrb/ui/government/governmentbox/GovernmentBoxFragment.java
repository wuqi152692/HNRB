package com.hnzx.hnrb.ui.government.governmentbox;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.GovernmentBoxAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetResumeListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetResumeListRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * 政库
 */
public class GovernmentBoxFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private GovernmentBoxAdapter adapter;
    private int offset = 0;
    private final int number = 10;
    private static final String LEADORLIST_DATA = "leador_data";

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_government_box, container, false);
    }

    @Override
    protected void initViews(View view) {
        stateView = (MultiStateView) view.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, getActivity());

        adapter = new GovernmentBoxAdapter(getActivity());
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

        if (!App.getInstance().isNetworkConnected(mActivity))
            new sqliteAsync().execute();
    }

    @Override
    protected void initDatas() {
        GetResumeListReq req = new GetResumeListReq();
        req.number = number;
        req.offset = offset;

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), (offset == 0 && adapter.getItemCount() == 0) || !isFirstRun ? new MyErrorListener(stateView) : null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                isFirstRun = false;
                initDatas();
                break;
        }
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<GetResumeListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetResumeListRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    stateView.setViewState(response.Info != null && response.Info.size() > 0 ? MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    new saveDataAsync(response.Info, LEADORLIST_DATA).execute();
                } else {
                    adapter.addAll(response.Info);
                    recyclerView.loadMoreComplete();
                    if (response.Info == null || response.Info.size() < number)
                        recyclerView.setNoMore(true);
                }
            } else stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        List<GetResumeListRsp> list;
        String key;

        saveDataAsync(List<GetResumeListRsp> list, String key) {
            this.list = list;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... params) {
            CDUtil.saveObject(list, key);
            return null;
        }
    }

    private class sqliteAsync extends AsyncTask<String, Integer, String> {

        List<GetResumeListRsp> listRsps = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            listRsps = (List<GetResumeListRsp>) CDUtil.readObject(LEADORLIST_DATA);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (listRsps != null && listRsps.size() != 0) {
                adapter.setList(listRsps);
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}
