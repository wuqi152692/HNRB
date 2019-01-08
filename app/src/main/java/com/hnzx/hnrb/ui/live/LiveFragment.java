package com.hnzx.hnrb.ui.live;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.LiveAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetLiveListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLiveListRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.ui.news.NewsListFragment;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

public class LiveFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private LiveAdapter adapter;
    private int offset = 0;
    private final int number = 10;

    private static final String LIVELIST_DATA = "live_data";

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_live, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView);
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
        recyclerView = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
    }

    @Override
    protected void initListeners() {
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).setOnClickListener(this);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                getNetData();
            }

            @Override
            public void onLoadMore() {
                offset += number;
                getNetData();
            }
        });

        if (!App.getInstance().isNetworkConnected(mActivity))
            new sqliteAsync().execute();
    }

    @Override
    protected void initDatas() {
        adapter = new LiveAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        getNetData();
    }

    private void getNetData() {
        GetLiveListReq req = new GetLiveListReq();
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
            default:
                break;
        }
    }

    private class dataListener implements Response.Listener<BaseBeanArrayRsp<GetLiveListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetLiveListRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    stateView.setViewState(response.Info != null && response.Info.size() > 0 ?
                            MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    if (response.Info != null && response.Info.size() < number)
                        recyclerView.setNoMore(true);
                    new saveDataAsync(response.Info, LIVELIST_DATA).execute();
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

        List<GetLiveListRsp> list;
        String key;

        saveDataAsync(List<GetLiveListRsp> list, String key) {
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

        List<GetLiveListRsp> listRsps = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            listRsps = (List<GetLiveListRsp>) CDUtil.readObject(LIVELIST_DATA);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (listRsps != null && listRsps.size() != 0) {
                adapter.setList(listRsps);
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        }
    }
}
