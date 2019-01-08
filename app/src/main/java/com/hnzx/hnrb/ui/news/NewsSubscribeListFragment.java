package com.hnzx.hnrb.ui.news;

import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsListAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.GetCategoryListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

public class NewsSubscribeListFragment extends BaseFragment implements View.OnClickListener {
    private int offset = 0;
    private final int number = 10;

    private MultiStateView stateView;
    private XRecyclerView recyclerview;
    private TextView guide;
    private NewsListAdapter adapter;

    private GetTopCategoryRsp topCategory;
    private RecyclerViewScrollListener listener;

    private String NEWSLIST_DATA;

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerview != null && listener != null)
            listener.downScroll(-10);

        initData();
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        topCategory = getArguments().getParcelable(Constant.BEAN);
        NEWSLIST_DATA = topCategory.cat_id;
        return inflater.inflate(R.layout.fragment_news_subscribe_list, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        View emptyView = stateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        emptyView.findViewById(R.id.add_subscribe).setOnClickListener(this);
        guide = (TextView) emptyView.findViewById(R.id.guide);
        recyclerview = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerview, mActivity, false);
        recyclerview.addItemDecoration(new XDecoration(mActivity, XDecoration.VERTICAL_LIST));
        View header = LayoutInflater.from(mActivity).inflate(R.layout.layout_news_list_header, recyclerview, false);
        AutoUtils.auto(header);
        recyclerview.addHeaderView(header);
        adapter = new NewsListAdapter(mActivity);
        adapter.notifyItemStyle(0);
        recyclerview.setAdapter(adapter);

        if (App.getInstance().isLogin())
            new sqliteAsync().execute();
        else {
            stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            guide.setVisibility(View.VISIBLE);
            guide.setText(Html.fromHtml("看不到已订阅的？<font color='#ea4335'>请登录"));
            emptyView.findViewById(R.id.type).setVisibility(View.INVISIBLE);
        }
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
        if (guide != null)
            guide.setOnClickListener(this);
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
                    stateView.setViewState(response.Info == null || response.Info.size() < 1 ?
                            MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);
                    adapter.setList(response.Info);
                    recyclerview.refreshComplete();
                    if (response.Info != null && response.Info.size() < number)
                        recyclerview.setNoMore(true);
                    new saveDataAsync(response.Info).execute();
                } else {
                    recyclerview.loadMoreComplete();
                    adapter.addAll(response.Info);
                    recyclerview.loadMoreComplete();
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

    }

    void initData() {
        if (App.getInstance().isLogin()) {
            GetCategoryListReq req = new GetCategoryListReq();
            req.offset = offset;
            req.number = number;
            req.cat_id = topCategory.cat_id;
            App.getInstance().requestJsonArrayDataGet(req, new dataListener(), new errorListener());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_subscribe:
                if (App.getInstance().isLogin()) {
                    ((MainActivity) mActivity).drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    mActivity.startActivity(LoginActivity.newIntent(mActivity, LoginActivity.class));
                }
                break;
            case R.id.guide:
                mActivity.startActivity(LoginActivity.newIntent(mActivity, LoginActivity.class));
                break;
        }
    }


    private class errorListener implements com.android.volley.Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (adapter.getItemCount() < 1)
                stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
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
                adapter.setList(listRsps);
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        }
    }
}
