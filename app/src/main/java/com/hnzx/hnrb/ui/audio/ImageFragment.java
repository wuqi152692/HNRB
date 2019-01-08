package com.hnzx.hnrb.ui.audio;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.ImageAudioAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetMediaImageReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLiveListRsp;
import com.hnzx.hnrb.responsebean.GetMediaImageRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

/**
 * Image Fragment For Audio
 */
public class ImageFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private ImageAudioAdapter adapter;
    private int offset = 0;
    private final int number = 10;
    private static final String IMAGE_DATA = "image_data";

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    protected void initViews(View view) {
        stateView = (MultiStateView) view.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
        recyclerView = (XRecyclerView) view.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, getActivity(), false);

        adapter = new ImageAudioAdapter(getActivity());
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
        GetMediaImageReq req = new GetMediaImageReq();
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

    private class dataLIstener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetMediaImageRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetMediaImageRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    stateView.setViewState(response.Info != null && response.Info.size() > 0 ? MultiStateView.VIEW_STATE_CONTENT : MultiStateView.VIEW_STATE_EMPTY);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    new saveDataAsync(response.Info, IMAGE_DATA).execute();
                } else {
                    adapter.addAll(response.Info);
                    recyclerView.loadMoreComplete();
                }
            } else stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        List<GetMediaImageRsp> list;
        String key;

        saveDataAsync(List<GetMediaImageRsp> list, String key) {
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

        List<GetMediaImageRsp> listRsps = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            listRsps = (List<GetMediaImageRsp>) CDUtil.readObject(IMAGE_DATA);
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
