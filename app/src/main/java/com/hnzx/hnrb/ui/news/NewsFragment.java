package com.hnzx.hnrb.ui.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsFragmentViewPagerAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.db.Dao.GetTopCategoryRspDao;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetTopCategoryReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.view.MultiStateView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements View.OnClickListener {
    private MultiStateView stateView;
    private ViewPager viewPager;
    private SlidingTabLayout tabLayout;
    private NewsFragmentViewPagerAdapter adapter;
    private ArrayList<GetTopCategoryRsp> datas;
    public static final String COLUMNDATA = "COLUMNDATA";
    private GetTopCategoryRspDao dao;
    private View topLayout;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    protected void initViews(View view) {
        AutoUtils.auto(view);
        topLayout = view.findViewById(R.id.topLayout);
        stateView = (MultiStateView) view.findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);

        tabLayout = (SlidingTabLayout) view.findViewById(R.id.tabLayout);

        view.findViewById(R.id.add).setOnClickListener(this);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initDatas() {
        adapter = new NewsFragmentViewPagerAdapter(getFragmentManager(), topLayout);
        viewPager.setAdapter(adapter);
        new readDataAsync().execute();
    }

    private void getData() {
        GetTopCategoryReq req = new GetTopCategoryReq();
        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), new MyErrorListener(stateView));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                mActivity.startActivityForResult(ChannelActivity.newIntent(mActivity, datas), 0xff);
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initDatas();
                break;
        }
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetTopCategoryRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetTopCategoryRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                datas = response.Info;
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                GetTopCategoryRsp rsp = new GetTopCategoryRsp();
                rsp.catname = "头条";
                rsp.cat_id = "头条";

                response.Info.add(0, rsp);
                if (isSave)
                    new saveDataAsync(response.Info).execute();
                else
                    new updataDataAsync(response.Info).execute();
                adapter.notifyData(response.Info);
                tabLayout.setViewPager(viewPager);
                AutoUtils.auto(tabLayout);
            } else
                stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //处理栏目编辑
        if (requestCode == 0xff && resultCode == 10 && data != null) {
            GetTopCategoryRsp rsp = datas.get(datas.size() - 1);
            datas = data.getParcelableArrayListExtra(COLUMNDATA);
            datas.add(rsp);
            adapter.notifyData(datas);
            tabLayout.notifyDataSetChanged();
        }
    }

    public void showSubjectFragment() {
        tabLayout.setCurrentTab(datas.size() - 1);
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        List<GetTopCategoryRsp> list;

        saveDataAsync(List<GetTopCategoryRsp> list) {
            this.list = list;
        }

        @Override
        protected String doInBackground(String... params) {

            for (GetTopCategoryRsp rsp : list) {
                dao.add(rsp);
            }
            return null;
        }

    }

    private boolean isSave;

    private class readDataAsync extends AsyncTask<String, Integer, String> {

        List<GetTopCategoryRsp> rsp = null;

        @Override
        protected String doInBackground(String... params) {

            dao = new GetTopCategoryRspDao(mActivity);
            rsp = dao.queryAll();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (rsp != null && rsp.size() != 0) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.notifyData(rsp);
                tabLayout.setViewPager(viewPager);
            }
            if (App.getInstance().isNetworkConnected(getActivity())) {
                isSave = !(rsp != null && rsp.size() != 0);
                getData();
            }
        }
    }

    private class updataDataAsync extends AsyncTask<String, Integer, String> {

        List<GetTopCategoryRsp> list;

        updataDataAsync(List<GetTopCategoryRsp> list) {
            this.list = list;
        }

        @Override
        protected String doInBackground(String... params) {
            for (GetTopCategoryRsp rsp : list) {
                dao.update(rsp);
            }
            return null;
        }
    }
}
