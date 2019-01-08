package com.hnzx.hnrb.ui.audio;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.VedioAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetMediaVedioReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMediaVedioRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * 视频Fragment
 */
public class VedioFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private VedioAdapter adapter;
    private int offset = 0;
    private final int number = 10;
    private static final String VIDEOLIST_DATA = "video_data";

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_vedio, container, false);
    }

    @Override
    protected void initViews(View view) {
        stateView = (MultiStateView) view.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, getActivity(), false);

        adapter = new VedioAdapter(getActivity());
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                handler.sendEmptyMessage(0);
            }
        });

        if (!App.getInstance().isNetworkConnected(mActivity))
            new sqliteAsync().execute();
    }

    private LinearLayoutManager manager;
    private int firstTemp, lastTemp;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (manager == null)
                    manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int first = manager.findFirstVisibleItemPosition() - 1;
                int last = manager.findLastVisibleItemPosition() - 1;

                if (first == firstTemp && last == lastTemp)
                    return;
                else {
                    firstTemp = first;
                    lastTemp = last;
                }
//                if ((firstTemp > adapter.playPosition || lastTemp < adapter.playPosition) && adapter.playerStandard != null && !isShowWindowTiny) {
//                    adapter.playerStandard.startWindowTiny();
//                    isShowWindowTiny = true;
//                } else if ((firstTemp <= adapter.playPosition && lastTemp >= adapter.playPosition) && adapter.playerStandard != null && isShowWindowTiny) {
//                    isShowWindowTiny = false;
//                    adapter.playerStandard.backPress();
//                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    };

    boolean isShowWindowTiny = false;

    @Override
    protected void initDatas() {
        GetMediaVedioReq req = new GetMediaVedioReq();
        req.offset = offset;
        req.number = number;
        App.getInstance().requestJsonArrayDataGet(req, new dataLIstener(), (offset == 0 && adapter.getItemCount() == 0) || !isFirstRun ? new MyErrorListener(stateView) : null);
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

    private class dataLIstener implements Response.Listener<BaseBeanArrayRsp<GetMediaVedioRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetMediaVedioRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    stateView.setViewState(response.Info != null && response.Info.size() > 0 ? MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    if (response.Info != null && response.Info.size() < number)
                        recyclerView.setNoMore(true);
                    new saveDataAsync(response.Info, VIDEOLIST_DATA).execute();
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

        List<GetMediaVedioRsp> list;
        String key;

        saveDataAsync(List<GetMediaVedioRsp> list, String key) {
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

        List<GetMediaVedioRsp> listRsps = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            listRsps = (List<GetMediaVedioRsp>) CDUtil.readObject(VIDEOLIST_DATA);
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
