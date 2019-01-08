package com.hnzx.hnrb.ui.news;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.HomePagerBannerAdapter;
import com.hnzx.hnrb.adapter.NewsListAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.requestbean.GetCategoryListReq;
import com.hnzx.hnrb.requestbean.GetCategoryPositonReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetHomePagerDataRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.LogUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.view.AutoScrollViewPager;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class NewsListFragment extends BaseFragment implements View.OnClickListener {
    private int offset = 0;
    private final int number = 10;
    private MultiStateView stateView;
    private XRecyclerView recyclerview;
    private NewsListAdapter adapter;
    private TextView bannerTitle, bannerIndex, bannerTotal;
    private AutoScrollViewPager viewPager;
    private View viewPagerTitleLayout;
    private HomePagerBannerAdapter bannerAdapter;

    private GetTopCategoryRsp topCategory;
    private List<GetFeaturedNewsListRsp> focus;
    private autoPagerChangeListener autoPC = new autoPagerChangeListener();
    private RecyclerViewScrollListener listener;
    private String NEWSLIST_DATA, BANNERLIST_DATA;
    private boolean isRunCacheOrNetError;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            if (!App.getInstance().isNetworkConnected(mActivity))
//                new sqliteAsync().execute();
//            if (offset == 0) {
//                GetCategoryPositonReq position = new GetCategoryPositonReq();
//                position.cat_id = topCategory.cat_id;
//                App.getInstance().requestJsonArrayDataGet(position, new bannerDataListener(), null);
//            }
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
        if (viewPager == null)
            return;
        viewPager.removeOnPageChangeListener(autoPC);
        viewPager.setOnPageChangeListener(autoPC);
        if (viewPager != null && viewPager.getChildCount() > 0) {
            viewPager.setInterval(4 * 1000);
            viewPager.startAutoScroll(3 * 1000);
        }
        if (recyclerview != null && listener != null)
            listener.downScroll(-10);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewPager == null)
            return;
        viewPager.removeOnPageChangeListener(autoPC);
        viewPager.stopAutoScroll();
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        topCategory = getArguments().getParcelable(Constant.BEAN);
        NEWSLIST_DATA = "GetCategoryList" + topCategory.cat_id;
        BANNERLIST_DATA = "GetCategoryPositon" + topCategory.cat_id;
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        recyclerview = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerview, mActivity, false);
        recyclerview.addItemDecoration(new XDecoration(mActivity, XDecoration.VERTICAL_LIST));
        recyclerview.addHeaderView(getHeaderView());
        adapter = new NewsListAdapter(mActivity, true);
        adapter.notifyItemStyle(0);
        recyclerview.setAdapter(adapter);
        if (topCategory.catname.equals("入驻"))
            adapter.setFromRuZhu(true);
        if (!App.getInstance().isNetworkConnected(mActivity))
            new sqliteAsync().execute();
    }

    private View getHeaderView() {
        View head = LayoutInflater.from(mActivity).inflate(R.layout.layout_news_list_header, recyclerview, false);
        AutoUtils.auto(head);
        viewPagerTitleLayout = head.findViewById(R.id.layout);
        bannerTitle = (TextView) head.findViewById(R.id.bannerTitle);
        bannerIndex = (TextView) head.findViewById(R.id.bannerIndex);
        bannerTotal = (TextView) head.findViewById(R.id.bannerTotal);

        viewPager = (AutoScrollViewPager) head.findViewById(R.id.viewPager);

        bannerAdapter = new HomePagerBannerAdapter();
        viewPager.setAdapter(bannerAdapter);
        return head;
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
                if (listener != null)
                    if (dy > 10) {
                        listener.upScroll(dy);
                    } else if (dy < -10) {
                        listener.downScroll(dy);
                    }
            }
        });
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
    }

    public void setRecyclerViewScrollListener(RecyclerViewScrollListener listener) {
        this.listener = listener;
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    recyclerview.refreshComplete();
                    adapter.setList(response.Info);
                    new saveDataAsync(response.Info, NEWSLIST_DATA).execute();
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
        if (offset == 0) {
            GetCategoryPositonReq position = new GetCategoryPositonReq();
            position.cat_id = topCategory.cat_id;
            App.getInstance().requestJsonArrayDataGet(position, new bannerDataListener(), null);
        }
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
            LogUtil.e("加载列表发生了错误");
            if (offset == 0 && isRunCacheOrNetError)
                try {
                    stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else isRunCacheOrNetError = true;
        }
    }

    /**
     * 设置Banner数据适配器
     */
    private class bannerDataListener implements com.android.volley.Response.Listener<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                focus = response.Info;
                if (focus.size() > 0) {
                    addBannerData();
                    new saveDataAsync(focus, BANNERLIST_DATA).execute();
                } else new deleteDataAsync().execute();
            }
        }
    }

    private void addBannerData() {
        bannerTitle.setText(focus.get(viewPager.getCurrentItem() % focus.size()).title);
        bannerIndex.setText(String.valueOf(viewPager.getCurrentItem() % focus.size() + 1));
        bannerTotal.setText(String.valueOf(focus.size()));
        List<View> views = new ArrayList<>();
        for (GetFeaturedNewsListRsp focu : focus) {
            ImageView iv = new ImageView(mActivity);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideTools.Glide(mActivity, focu.thumb, iv, R.drawable.bg_morentu_datumoshi);
            iv.setOnClickListener(new bannerImageListener(focu));
            views.add(iv);
        }
        bannerAdapter.notifyDataSetChanged(views);
        viewPager.setVisibility(View.VISIBLE);
        viewPagerTitleLayout.setVisibility(View.VISIBLE);
        if (viewPager.isAutoScroll())
            return;
        if (views.size() > 1) {
            viewPager.setInterval(4 * 1000);
            viewPager.startAutoScroll(3 * 1000);
        }
    }

    private class bannerImageListener implements View.OnClickListener {
        private GetFeaturedNewsListRsp focu;

        public bannerImageListener(GetFeaturedNewsListRsp focu) {
            this.focu = focu;
        }

        @Override
        public void onClick(View v) {
            if (topCategory.catname.equals("入驻"))
                NewsSelect.goWhere(mActivity, focu.content_id, focu.is_link, focu.link_url, focu.internal_type, focu.internal_id, focu.content_type, focu.thumb, true);
            else
                NewsSelect.goWhere(mActivity, focu.content_id, focu.is_link, focu.link_url, focu.internal_type, focu.internal_id, focu.content_type, focu.thumb);
        }
    }

    class autoPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            bannerTitle.setText(focus.get(viewPager.getCurrentItem() % focus.size()).title);
            bannerIndex.setText(String.valueOf(viewPager.getCurrentItem() % focus.size() + 1));
        }
    }

    private class deleteDataAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            CDUtil.deleteDataCache(BANNERLIST_DATA);
            return null;
        }
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        List<GetFeaturedNewsListRsp> list;
        String key;

        saveDataAsync(List<GetFeaturedNewsListRsp> list, String key) {
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

        List<GetFeaturedNewsListRsp> listRsps = null;
        List<GetFeaturedNewsListRsp> bannerListRsps = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            listRsps = (List<GetFeaturedNewsListRsp>) CDUtil.readObject(NEWSLIST_DATA);
            bannerListRsps = (List<GetFeaturedNewsListRsp>) CDUtil.readObject(BANNERLIST_DATA);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (listRsps != null && listRsps.size() != 0) {
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
            if (bannerListRsps != null && bannerListRsps.size() != 0) {
                focus = bannerListRsps;
                addBannerData();
            }
        }
    }
}
