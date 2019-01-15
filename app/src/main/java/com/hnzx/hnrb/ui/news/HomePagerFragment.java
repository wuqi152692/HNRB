package com.hnzx.hnrb.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.HomePagerBannerAdapter;
import com.hnzx.hnrb.adapter.NewsListAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.GetFeaturedNewsListReq;
import com.hnzx.hnrb.requestbean.GetHomePagerDataReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetHomePagerDataRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.DateUtils;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.LogUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.view.AutoScrollViewPager;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePagerFragment extends BaseFragment implements View.OnClickListener {
    private int offset = 0;
    private final int number = 10;
    private MultiStateView stateView;
    private XRecyclerView recyclerview;
    private AutoScrollViewPager viewPager;
    private TextView bannerTitle, bannerIndex, bannerTotal, hdTitle0, hdTitle1, hdTitle2, hdDate0, hdDate1, hdDate2;
    private CheckBox typeList;
    private View hdLayout0, hdLayout1, hdLayout2;
    private HomePagerBannerAdapter bannerAdapter;
    private NewsListAdapter adapter;

    private List<GetHomePagerDataRsp.FocusBean> focus = new ArrayList<>();
    private ArrayList<String> tag = new ArrayList<>();
    autoPagerChangeListener autoPC = new autoPagerChangeListener();

    RecyclerViewScrollListener listener;

    private static final String LIST_TYPE = "LIST_TYPE";
    private String HOME_DATA_TAG = "homedata_tag";
    private String HOME_DATA_BANNER = "homedata_ban";
    private String HOME_DATA_DYNAMICS = "homedata_dyn";
    private String HOME_DATA_LIST = "homedata_list";
    private boolean isRunCacheOrNetError;

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        if (isVisibleToUser) {
//            new sqliteAsync().execute();
//            GetHomeData();
//        } else isRunCacheOrNetError = false;
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager == null)
            return;
        viewPager.removeOnPageChangeListener(autoPC);
        viewPager.setOnPageChangeListener(autoPC);
        if (viewPager != null && viewPager.getChildCount() > 0)
            viewPager.startAutoScroll(3 * 1000);
        if (recyclerview != null && listener != null)
            listener.downScroll(-10);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.removeOnPageChangeListener(autoPC);
        viewPager.stopAutoScroll();
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        stateView = (MultiStateView) contentView.findViewById(R.id.stateView);
        recyclerview = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerview, mActivity, false);
        recyclerview.addItemDecoration(new XDecoration(mActivity, XDecoration.VERTICAL_LIST));
        recyclerview.addHeaderView(getHeaderView());
        adapter = new NewsListAdapter(mActivity);
        recyclerview.setAdapter(adapter);
        typeList.setChecked(((int) SharePreferenceTool.get(mActivity, LIST_TYPE, 0)) == 1);
        new sqliteAsync().execute();
    }

    private View getHeaderView() {
        View head = LayoutInflater.from(mActivity).inflate(R.layout.layout_home_pager_header, recyclerview, false);
        AutoUtils.auto(head);
        hdLayout0 = head.findViewById(R.id.layout0);
        hdLayout1 = head.findViewById(R.id.layout1);
        hdLayout2 = head.findViewById(R.id.layout2);

        hdTitle0 = (TextView) hdLayout0.findViewById(R.id.title);
        hdTitle1 = (TextView) hdLayout1.findViewById(R.id.title);
        hdTitle2 = (TextView) hdLayout2.findViewById(R.id.title);

        hdDate0 = (TextView) hdLayout0.findViewById(R.id.date);
        hdDate1 = (TextView) hdLayout1.findViewById(R.id.date);
        hdDate2 = (TextView) hdLayout2.findViewById(R.id.date);

        bannerTitle = (TextView) head.findViewById(R.id.bannerTitle);
        bannerIndex = (TextView) head.findViewById(R.id.bannerIndex);
        bannerTotal = (TextView) head.findViewById(R.id.bannerTotal);

        viewPager = (AutoScrollViewPager) head.findViewById(R.id.viewPager);

        bannerAdapter = new HomePagerBannerAdapter();

        typeList = (CheckBox) head.findViewById(R.id.typeList);
        typeList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.notifyItemStyle(isChecked ? 1 : 0);
                SharePreferenceTool.put(mActivity, LIST_TYPE, isChecked ? 1 : 0);
            }
        });
        return head;
    }

    @Override
    protected void initListeners() {
        recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                GetHomeData();
            }

            @Override
            public void onLoadMore() {
                offset += number;
                GetNewsList();
            }
        });

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try {
                    if (dy > 50) {
                        listener.upScroll(dy);
                    } else if (dy < -50) {
                        listener.downScroll(dy);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
    }

    public void setRecyclerViewScrollListener(RecyclerViewScrollListener listener) {
        this.listener = listener;
    }

    @Override
    protected void initDatas() {
        GetHomeData();
    }

    private void GetNewsList() {
        GetFeaturedNewsListReq req = new GetFeaturedNewsListReq();
        req.offset = offset;
        req.number = number;

        App.getInstance().requestJsonArrayDataGet(req, new newsListListener(), new errorListener(false));
    }

    private void GetHomeData() {
        GetHomePagerDataReq req = new GetHomePagerDataReq();
        App.getInstance().requestJsonDataGet(req, new homeListener(), new errorListener(true));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout0:
                goToHighDynamic(1);
                break;
            case R.id.layout1:
                goToHighDynamic(2);
                break;
            case R.id.layout2:
                goToHighDynamic(3);
                break;
            case R.id.error_reload_data:
                initDatas();
                break;
        }

    }

    private void goToHighDynamic(int position) {
        Intent intent = new Intent(mActivity, HighDynamicActivity.class);
        intent.putStringArrayListExtra(Constant.BEAN, tag);
        intent.putExtra("Tag", position);
        mActivity.startActivity(intent);
    }


    private class errorListener implements com.android.volley.Response.ErrorListener {
        private boolean isHomeData;

        public errorListener(boolean isHomeData) {
            this.isHomeData = isHomeData;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (offset == 0)
                recyclerview.refreshComplete();
            else
                recyclerview.loadMoreComplete();
            if (isHomeData)
                if (offset == 0 && isRunCacheOrNetError)
                    try {
                        stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else isRunCacheOrNetError = true;
        }
    }

    private class newsListListener implements com.android.volley.Response.Listener<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                adapter.addAll(response.Info);
                recyclerview.loadMoreComplete();
                if (response.Info == null || response.Info.size() < number)
                    recyclerview.setNoMore(true);
            }
        }
    }


    private class homeListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetHomePagerDataRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetHomePagerDataRsp> response) {
            if (response != null && response.Status == 1) {
                focus.clear();
                focus = new ArrayList<>();
                focus = response.Info.focus;
                addBannerData();
                addHDData(response.Info.dynamics);
                adapter.setList(response.Info.featured);
                recyclerview.refreshComplete();
                tag.addAll(response.Info.tags);
                new saveDataAsync(response.Info).execute();
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        }
    }

    /**
     * 添加高层动态数据
     *
     * @param dynamics
     */
    private void addHDData(List<GetHomePagerDataRsp.DynamicsBean> dynamics) {
        hdTitle0.setText(dynamics.get(0).title);
        hdTitle1.setText(dynamics.get(1).title);
        hdTitle2.setText(dynamics.get(2).title);
        // 直接显示时间格式
        hdDate0.setText(dynamics.get(0).created);
        hdDate1.setText(dynamics.get(1).created);
        hdDate2.setText(dynamics.get(2).created);

        hdLayout0.setOnClickListener(this);
        hdLayout1.setOnClickListener(this);
        hdLayout2.setOnClickListener(this);
    }

    private void addBannerData() {
        viewPager.setAdapter(bannerAdapter);
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(0);
        bannerTitle.setText(focus.get(0).title);
        bannerIndex.setText(String.valueOf(1));
        bannerTotal.setText(String.valueOf(focus.size()));
        List<View> views = new ArrayList<>();
        for (GetHomePagerDataRsp.FocusBean focu : focus) {
            ImageView iv = new ImageView(mActivity);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())
                return;
            GlideTools.Glide(mActivity, focu.thumb, iv, R.drawable.bg_morentu_datumoshi);
            iv.setOnClickListener(new bannerImageListener(focu));
            views.add(iv);
        }
        bannerAdapter.notifyDataSetChanged(views);
        viewPager.setVisibility(View.VISIBLE);
        if (viewPager.isAutoScroll())
            return;
        if (viewPager.getChildCount() > 1) {
            viewPager.setInterval(4 * 1000);
            viewPager.startAutoScroll(3 * 1000);
        }
    }

    private class bannerImageListener implements View.OnClickListener {
        private GetHomePagerDataRsp.FocusBean focu;

        public bannerImageListener(GetHomePagerDataRsp.FocusBean focu) {
            this.focu = focu;
        }

        @Override
        public void onClick(View v) {
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

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        GetHomePagerDataRsp rsp;

        saveDataAsync(GetHomePagerDataRsp rsp) {
            this.rsp = rsp;
        }

        @Override
        protected String doInBackground(String... params) {
            CDUtil.saveObject(rsp.focus, HOME_DATA_BANNER);
            CDUtil.saveObject(rsp.dynamics, HOME_DATA_DYNAMICS);
            CDUtil.saveObject(rsp.tags, HOME_DATA_TAG);
            CDUtil.saveObject(rsp.featured, HOME_DATA_LIST);
            return null;
        }
    }

    private class sqliteAsync extends AsyncTask<String, Integer, String> {

        GetHomePagerDataRsp rsp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            rsp = new GetHomePagerDataRsp();
            rsp.focus = (List<GetHomePagerDataRsp.FocusBean>) CDUtil.readObject(HOME_DATA_BANNER);
            rsp.dynamics = (List<GetHomePagerDataRsp.DynamicsBean>) CDUtil.readObject(HOME_DATA_DYNAMICS);
            rsp.tags = (ArrayList<String>) CDUtil.readObject(HOME_DATA_TAG);
            rsp.featured = (List<GetFeaturedNewsListRsp>) CDUtil.readObject(HOME_DATA_LIST);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (rsp != null) {
                if (rsp.focus != null) {
                    focus = rsp.focus;
                    addBannerData();
                }
                if (rsp.dynamics != null) {
                    addHDData(rsp.dynamics);
                    tag.addAll(rsp.tags);
                }
                if (rsp.featured != null)
                    adapter.setList(rsp.featured);
                else {
                    if (isRunCacheOrNetError)
                        try {
                            stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    else isRunCacheOrNetError = true;
                }
                recyclerview.refreshComplete();
            }
        }
    }
}
