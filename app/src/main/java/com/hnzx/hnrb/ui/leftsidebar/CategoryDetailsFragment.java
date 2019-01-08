package com.hnzx.hnrb.ui.leftsidebar;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.serializer.FieldSerializer;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.CategoryAdapter;
import com.hnzx.hnrb.adapter.LeftMenuAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.db.Dao.CategoryBeanDao;
import com.hnzx.hnrb.db.Dao.GetAllCategoryRspDao;
import com.hnzx.hnrb.db.bean.CategoryBean;
import com.hnzx.hnrb.requestbean.GetCategoryListReq;
import com.hnzx.hnrb.requestbean.SetUserCancelDingyueColumnReq;
import com.hnzx.hnrb.requestbean.SetUserDingyueColumnReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAllCategoryRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.tools.DateUtils;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.LoginActivity;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

public class CategoryDetailsFragment extends BaseFragment {
    private XRecyclerView recyclerView;
    private CategoryAdapter adapter;
    private int offset = 0;
    private final int number = 10;
    private GetAllCategoryRsp data;
    private TextView date;
    private ImageView headerImage;
    private CheckedTextView subscribe;
    private int headHeight;
    private int titleHeight;
    private CategoryBeanDao dao;

    private View getHeaderView() {
        final View header = LayoutInflater.from(mActivity).inflate(R.layout.layout_category_header, recyclerView, false);
        AutoUtils.auto(header);
        if (data != null) {
            headerImage = (ImageView) header.findViewById(R.id.image);
            GlideTools.Glide(mActivity, data.image, headerImage, R.drawable.icon_default_blue);
            ((TextView) header.findViewById(R.id.catName)).setText(data.name);
            date = (TextView) header.findViewById(R.id.date);
            subscribe = (CheckedTextView) header.findViewById(R.id.subscribe);
            subscribe.setChecked(App.getInstance().isLogin() && data.is_ordered == 1);
        }

        ViewTreeObserver vto2 = header.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                header.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                headHeight = header.getHeight();
            }
        });
        return header;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        data = (GetAllCategoryRsp) getArguments().getSerializable(Constant.BEAN);
        GetAllCategoryRsp rtmp = ((ColumnActivity) mActivity).queryByCatid(data);
        if (rtmp != null)
            data = rtmp;
        titleHeight = headHeight * 8 / 23;
        return inflater.inflate(R.layout.fragment_category_details, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        recyclerView = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.addHeaderView(getHeaderView());

        adapter = new CategoryAdapter(mActivity);
        recyclerView.setAdapter(adapter);
    }

    private boolean titleShow = false;
    private long totalScrollY = 0;

    @Override
    protected void initListeners() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisiableChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisiableChildView.getHeight();
                totalScrollY = position * itemHeight - firstVisiableChildView.getTop() - headHeight;
                if (!titleShow && dy > 0 && totalScrollY >= headHeight - titleHeight) {
                    ((ColumnActivity) mActivity).ShowOrHide(true);
                    titleShow = true;
                } else if (titleShow && totalScrollY < headHeight - titleHeight) {
                    ((ColumnActivity) mActivity).ShowOrHide(false);
                    titleShow = false;
                }
            }
        });

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

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getInstance().isLogin()) {
                    if (data.is_ordered == 1) {
                        SetUserCancelDingyueColumnReq req = new SetUserCancelDingyueColumnReq();
                        req.cat_id = data.catid;
                        App.getInstance().requestJsonDataGet(req, new orderCancelListener(), null);
                    } else {
                        SetUserDingyueColumnReq req = new SetUserDingyueColumnReq();
                        req.cat_id = data.catid;
                        App.getInstance().requestJsonDataGet(req, new orderListener(), null);
                    }
                } else {
                    startActivity(LoginActivity.newIntent(mActivity, false));
                }
            }
        });
    }

    @Override
    protected void initDatas() {
        dao = new CategoryBeanDao(mActivity);

        GetCategoryListReq req = new GetCategoryListReq();
        req.offset = offset;
        req.number = number;
        req.cat_id = data != null ? data.catid : 0;

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), null);
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    adapter.setList(response.Info);
                    if (response.Info != null && response.Info.size() > 0) {
                        List<CategoryBean> dbData = dao.queryByCatid("catid", response.Info.get(0).cat_id);
                        if (dbData != null && dbData.size() == 1) {
                            if (!dbData.get(0).update.equals(response.Info.get(0).updated)) {
                                CategoryBean dbRsp = dbData.get(0);
                                dbRsp.update = response.Info.get(0).updated;
                                new updateCategoryDataAsync(dbRsp, true).execute();
                            }
                        } else {
                            CategoryBean bean = new CategoryBean();
                            bean.catid = data.catid;
                            bean.update = response.Info.get(0).updated;

                            new updateCategoryDataAsync(bean, true).execute();
                        }
                    }

                    if (response.Info.size() > 0) {
                        date.setText(DateUtils.dateToString(DateUtils.stringToDate(response.Info.get(0).created, DateUtils.patternLong), "yyyy"));
                    }
                    recyclerView.refreshComplete();
                    if (response.Info != null && response.Info.size() < number)
                        recyclerView.setNoMore(true);
                } else {
                    adapter.addAll(response.Info);
                    recyclerView.loadMoreComplete();
                    if (response.Info == null || response.Info.size() < number)
                        recyclerView.setNoMore(true);
                }
            }
        }
    }

    private class orderCancelListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<String>> {

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                subscribe.setChecked(false);
                showTopToast("取消订阅", false);
                data.is_ordered = 0;
                ((ColumnActivity) mActivity).updateData(data);
            }
        }
    }

    private class orderListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                subscribe.setChecked(true);
                showTopToast("订阅成功", false);
                data.is_ordered = 1;
                ((ColumnActivity) mActivity).updateData(data);
            }
        }
    }

    private class updateCategoryDataAsync extends AsyncTask<String, Integer, Boolean> {
        CategoryBean rsp;
        boolean updateTimeStamp;

        public updateCategoryDataAsync(CategoryBean rsp) {
            this.rsp = rsp;
        }

        public updateCategoryDataAsync(CategoryBean rsp, boolean updateTimeStamp) {
            this.rsp = rsp;
            this.updateTimeStamp = updateTimeStamp;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (dao == null)
                dao = new CategoryBeanDao(mActivity);

            List<CategoryBean> dbData = dao.queryByCatid("catid", rsp.catid);
            if (dbData != null && dbData.size() > 0) {
                CategoryBean rtmp = dbData.get(0);
                rtmp.update = rsp.update;
                dao.update(rtmp);
            } else {
                dao.add(rsp);
            }
            return false;
        }
    }
}
