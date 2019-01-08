package com.hnzx.hnrb.ui.government.square;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.SquareGovernmentAdapter;
import com.hnzx.hnrb.adapter.SquareLatestReleaseAdapter;
import com.hnzx.hnrb.adapter.SquareSelectionAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetPoliticsFeaturedReq;
import com.hnzx.hnrb.requestbean.GetPoliticsNewsReq;
import com.hnzx.hnrb.requestbean.GetPoliticsRecommendReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.responsebean.GetMediaVedioRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsFeaturedRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsRecommendRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.ui.web.WebActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.hnzx.hnrb.view.NSRecyclerView;
import com.hnzx.hnrb.view.banner.Banner;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class SquareFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private SquareLatestReleaseAdapter adapter;

    private Banner banner;

    private RecyclerView topRecyclerView;
    private SquareSelectionAdapter pagerAdapter;

    private NSRecyclerView governmentRecyclerView;
    private SquareGovernmentAdapter governmentAdapter;

    private int offset = 0;
    private final int number = 10;

    public static final String FROM_SQUARE = "FROMSQUARE";

    private static final String LATEST_NEWS_DATA = "latestnewsdata";
    private static final String POLITICS_FEATURED_BANNER_DATA = "featured_banner_data";
    private static final String POLITICS_FEATURED_SCROLL_DATA = "featured_scroll_data";
    private static final String POLITICS_RECOMMEND_DATA = "recommend_data";

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_government_box, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
        recyclerView = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.addHeaderView(getHeaderView());

        adapter = new SquareLatestReleaseAdapter(mActivity, this);
        recyclerView.setAdapter(adapter);
    }

    private View getHeaderView() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_square_header, recyclerView, false);
        AutoUtils.auto(header);
        banner = (Banner) header.findViewById(R.id.banner);
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);

        topRecyclerView = (RecyclerView) header.findViewById(R.id.recyclerView);
        topRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

        governmentRecyclerView = (NSRecyclerView) header.findViewById(R.id.governmentRecyclerView);
        governmentRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));

        header.findViewById(R.id.goGovernmentCenter).setOnClickListener(this);

        return header;
    }

    @Override
    protected void initListeners() {
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                GetPoliticsFeatured();
                getPoliticsNews();
                getPoliticsRecommend();
            }

            @Override
            public void onLoadMore() {
                offset += number;
                getPoliticsNews();
            }
        });
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                if (bannerDatas != null) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(WebActivity.WEB_URL_KEY, bannerDatas.get(position).url);
                    IntentUtil.startActivity(mActivity, WebActivity.class, params);
                }
            }
        });

        if (!App.getInstance().isNetworkConnected(mActivity))
            new sqliteAsync().execute();
    }

    @Override
    protected void initDatas() {
        pagerAdapter = new SquareSelectionAdapter(mActivity);
        topRecyclerView.setAdapter(pagerAdapter);

        governmentAdapter = new SquareGovernmentAdapter(mActivity, this);
        governmentRecyclerView.setAdapter(governmentAdapter);

        GetPoliticsFeatured();

        getPoliticsNews();

        getPoliticsRecommend();
    }

    private void GetPoliticsFeatured() {
        GetPoliticsFeaturedReq req = new GetPoliticsFeaturedReq();

        App.getInstance().requestJsonDataGet(req, new featuredListener(), null);
    }

    private void getPoliticsRecommend() {
        GetPoliticsRecommendReq req = new GetPoliticsRecommendReq();

        App.getInstance().requestJsonArrayDataGet(req, new politicsRecommendListener(), null);
    }

    /**
     * 广场最新发布
     */
    private void getPoliticsNews() {
        GetPoliticsNewsReq req = new GetPoliticsNewsReq();
        req.offset = offset;
        req.number = number;

        App.getInstance().requestJsonArrayDataGet(req, new newsListListener(), offset == 0 && adapter.getItemCount() == 0 ? new MyErrorListener(stateView) : null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goGovernmentCenter:
                IntentUtil.startActivityForResult(this, GovernmentListActivity.class, UnitDetailsActivity.RESULT_CODE);
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initDatas();
                break;
        }
    }

    private class newsListListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetLatestNewsRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetLatestNewsRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                if (offset == 0) {
                    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    if (response.Info != null && response.Info.size() < number)
                        recyclerView.setNoMore(true);
                    new saveDataAsync(response.Info, LATEST_NEWS_DATA).execute();
                } else {
                    adapter.addAll(response.Info);
                    recyclerView.loadMoreComplete();
                    if (response.Info == null || response.Info.size() < number)
                        recyclerView.setNoMore(true);
                }
            }
        }
    }

    private class politicsRecommendListener implements com.android.volley.Response.Listener<BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetPoliticsRecommendRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetPoliticsRecommendRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                governmentAdapter.setList(response.Info);
                new saveDataAsync(response.Info, POLITICS_RECOMMEND_DATA).execute();
            }
        }
    }

    private List<GetPoliticsFeaturedRsp.ShujixinxiangBean> bannerDatas;

    private class featuredListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetPoliticsFeaturedRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetPoliticsFeaturedRsp> response) {
            if (response != null && response.Status == 1) {
                addBannerData(response.Info);
                new saveDataAsync(response.Info.jingpintuijian, POLITICS_FEATURED_SCROLL_DATA).execute();
                new saveDataAsync(response.Info.shujixinxiang, POLITICS_FEATURED_BANNER_DATA).execute();
            }
        }
    }

    void addBannerData(GetPoliticsFeaturedRsp rsp) {
        bannerDatas = rsp.shujixinxiang;
        if (bannerDatas != null && bannerDatas.size() > 0) {
            banner.setVisibility(View.VISIBLE);
            List<String> paths = new ArrayList<>();
            for (GetPoliticsFeaturedRsp.ShujixinxiangBean bannerData : bannerDatas) {
                paths.add(bannerData.thumb);
            }
            banner.setImages(paths);
        }
        if (rsp.jingpintuijian != null && rsp.jingpintuijian.size() > 0) {
            pagerAdapter.setList(rsp.jingpintuijian);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UnitDetailsActivity.RESULT_CODE) {
            getPoliticsRecommend();
            getPoliticsNews();
        }
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        List<?> list;
        String key;

        saveDataAsync(List<?> list, String key) {
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

        List<GetLatestNewsRsp> listRsps = null;
        List<GetPoliticsRecommendRsp> politicsRecommendRsps = null;
        GetPoliticsFeaturedRsp politicsFeaturedRsp = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            listRsps = (List<GetLatestNewsRsp>) CDUtil.readObject(LATEST_NEWS_DATA);
            politicsRecommendRsps = (List<GetPoliticsRecommendRsp>) CDUtil.readObject(POLITICS_RECOMMEND_DATA);
            politicsFeaturedRsp = new GetPoliticsFeaturedRsp();
            politicsFeaturedRsp.shujixinxiang = (List<GetPoliticsFeaturedRsp.ShujixinxiangBean>) CDUtil.readObject(POLITICS_FEATURED_BANNER_DATA);
            politicsFeaturedRsp.jingpintuijian = (List<GetPoliticsFeaturedRsp.JingpintuijianBean>) CDUtil.readObject(POLITICS_FEATURED_SCROLL_DATA);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (listRsps != null && listRsps.size() != 0) {
                adapter.setList(listRsps);
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
            if (politicsRecommendRsps != null && politicsRecommendRsps.size() != 0) {
                governmentAdapter.setList(politicsRecommendRsps);
            }
            if (politicsFeaturedRsp != null) {
                addBannerData(politicsFeaturedRsp);
            }
        }
    }
}
