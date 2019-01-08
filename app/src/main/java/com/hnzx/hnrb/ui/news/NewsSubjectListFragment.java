package com.hnzx.hnrb.ui.news;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsSubjectListAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.GetCategoryListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 新闻专题列表
 */
public class NewsSubjectListFragment extends BaseFragment implements View.OnClickListener {
    private int offset = 0;
    private final int number = 10;
    private MultiStateView stateView;
    private XRecyclerView recyclerview;
    private NewsSubjectListAdapter adapter;

    private GetTopCategoryRsp topCategory;
    private RecyclerViewScrollListener listener;

    private String NEWSLIST_DATA;
    private boolean isRunCacheOrNetError;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            if (!App.getInstance().isNetworkConnected(mActivity))
//                new sqliteAsync().execute();
//            GetCategoryListReq req = new GetCategoryListReq();
//            req.offset = offset;
//            req.number = number;
//            req.cat_id = topCategory.cat_id;
//
//            App.getInstance().requestJsonArrayDataGet(req, new dataListener(), new errorListener());
//        } else isRunCacheOrNetError = false;
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerview != null && listener != null)
            listener.downScroll(-10);
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        topCategory = getArguments().getParcelable(Constant.BEAN);
        NEWSLIST_DATA = topCategory.cat_id;
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        recyclerview = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerview, mActivity, false);
        recyclerview.addItemDecoration(new XDecoration(mActivity, XDecoration.VERTICAL_LIST));
        View header = LayoutInflater.from(mActivity).inflate(R.layout.layout_news_list_header, recyclerview, false);
        AutoUtils.auto(header);
        recyclerview.addHeaderView(header);
        adapter = new NewsSubjectListAdapter(mActivity);
        recyclerview.setAdapter(adapter);

        if (!App.getInstance().isNetworkConnected(mActivity))
            new sqliteAsync().execute();
    }

    @Override
    protected void initListeners() {
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 50) {
                    listener.upScroll(dy);
                } else if (dy < -50) {
                    listener.downScroll(dy);
                }
            }
        });
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
    }

    public void setRecyclerViewScrollListener(RecyclerViewScrollListener listener) {
        this.listener = listener;
    }

    private class dataListener implements com.android.volley.Response.Listener<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    recyclerview.refreshComplete();
                    adapter.setList(response.Info);
                    if (response.Info != null && response.Info.size() < number)
                        recyclerview.setNoMore(true);
                    new saveDataAsync(response.Info).execute();
                    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                } else {
                    recyclerview.loadMoreComplete();
                    adapter.addAll(response.Info);
                    if (response.Info == null || response.Info.size() < number)
                        recyclerview.setNoMore(true);
                }
            } else {
                if (offset == 0) {
                    recyclerview.refreshComplete();
                } else {
                    recyclerview.loadMoreComplete();
                }
            }
        }
    }

    @Override
    protected void initDatas() {
        GetCategoryListReq req = new GetCategoryListReq();
        req.offset = offset;
        req.number = number;
        req.cat_id = topCategory.cat_id;

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), new errorListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_reload_data:
                initDatas();
                break;
        }
    }

    private class errorListener implements com.android.volley.Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (offset == 0 && isRunCacheOrNetError)
                try {
                    stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else isRunCacheOrNetError = true;
        }
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        List<GetFeaturedNewsListRsp> list;

        saveDataAsync(List<GetFeaturedNewsListRsp> list) {
            this.list = list;
        }

        @Override
        protected String doInBackground(String... params) {
            CDUtil.saveObject(list, NEWSLIST_DATA);
            return null;
        }
    }

    private class sqliteAsync extends AsyncTask<String, Integer, List<GetFeaturedNewsListRsp>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<GetFeaturedNewsListRsp> doInBackground(String... params) {
            return (List<GetFeaturedNewsListRsp>) CDUtil.readObject(NEWSLIST_DATA);
        }

        @Override
        protected void onPostExecute(List<GetFeaturedNewsListRsp> listRsps) {
            if (listRsps != null && listRsps.size() != 0) {
                recyclerview.refreshComplete();
                adapter.setList(listRsps);
            } else {
                if (isRunCacheOrNetError)
                    try {
                        stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else isRunCacheOrNetError = true;
            }
        }
    }
}
