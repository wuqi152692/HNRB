package com.hnzx.hnrb.ui.leftsidebar;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.LeftMenuAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.db.Dao.GetAllCategoryRspDao;
import com.hnzx.hnrb.requestbean.GetAllCategoryReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetAllCategoryRsp;
import com.hnzx.hnrb.tools.IntentUtil;

import java.util.List;

/**
 * 左边栏
 */
public class LeftMenuFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private LeftMenuAdapter adapter;
    private static GetAllCategoryRspDao dao;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_left_menu, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        dao = new GetAllCategoryRspDao(mActivity);
        contentView.findViewById(R.id.news_24hours).setOnClickListener(this);
        contentView.findViewById(R.id.hot_push).setOnClickListener(this);
        contentView.findViewById(R.id.top_ten).setOnClickListener(this);

        recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        adapter = new LeftMenuAdapter(getActivity(), dao);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onResume() {
        super.onResume();
        loadMenuData();
    }

    /**
     * 加载栏目订阅数据加载  目前为权宜之计 fixme
     */
    private void loadMenuData() {

        GetAllCategoryReq req = new GetAllCategoryReq();

        App.getInstance().requestJsonArrayDataGet(req, new CategoryListener(), null);
    }


    @Override
    protected void initDatas() {
        try {
            adapter.setList(dao.queryAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news_24hours:
                IntentUtil.startActivity(getActivity(), News24HoursActivity.class);
                break;
            case R.id.hot_push:
                IntentUtil.startActivity(getActivity(), PushHotNewsActivity.class);
                break;
            case R.id.top_ten:
                IntentUtil.startActivity(getActivity(), Top10NewsActivity.class);
                break;
        }
    }

    private class CategoryListener implements Response.Listener<BaseBeanArrayRsp<GetAllCategoryRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetAllCategoryRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                adapter.setList(response.Info);
                if (dao != null) {
                    for (GetAllCategoryRsp rsp : response.Info) {
                        List<GetAllCategoryRsp> dbData = dao.queryByCatid("catid", rsp.catid);
                        if (dbData == null || dbData.size() == 0)
                            dao.add(rsp);
                        else {
                            GetAllCategoryRsp rtmp = dbData.get(0);
                            rtmp.is_ordered = rsp.is_ordered;
                            new updateCategoryDataAsync(rtmp).execute();
                        }
                    }
                }
            }
        }
    }

    private class updateCategoryDataAsync extends AsyncTask<String, Integer, Boolean> {
        GetAllCategoryRsp rsp;

        public updateCategoryDataAsync(GetAllCategoryRsp rsp) {
            this.rsp = rsp;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (dao == null)
                dao = new GetAllCategoryRspDao(mActivity);

            dao.update(rsp);

            return false;
        }
    }
}
