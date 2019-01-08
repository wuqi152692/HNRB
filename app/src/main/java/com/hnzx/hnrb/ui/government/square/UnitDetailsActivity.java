package com.hnzx.hnrb.ui.government.square;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsListAdapter;
import com.hnzx.hnrb.adapter.ReporterNewsListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetAuthorCenterReq;
import com.hnzx.hnrb.requestbean.GetAuthorNewsListReq;
import com.hnzx.hnrb.requestbean.GetCategoryListReq;
import com.hnzx.hnrb.requestbean.GetUnitDetailsReq;
import com.hnzx.hnrb.requestbean.SetCancelOrderAuthorReq;
import com.hnzx.hnrb.requestbean.SetCancelOrderCategoryReq;
import com.hnzx.hnrb.requestbean.SetMakeOrderAuthorReq;
import com.hnzx.hnrb.requestbean.SetMakeOrderCategoryReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAuthorCenterRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.responsebean.GetUnitDetailsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * 入住单位
 *
 * @author: mingancai
 * @Time: 2017/4/20 0020.
 */

public class UnitDetailsActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private NewsListAdapter adapter;

    private ImageView image;
    private TextView title, name, newsNum, viewsNum, attenteNum, msg;
    private CheckedTextView attention;

    private int offset = 0;
    private final int number = 10;
    private String cat_id;

    private int is_ordered;

    public static final int RESULT_CODE = 0x01;

    @Override
    protected int getLayoutId() {
        cat_id = getIntent().getStringExtra(Constant.BEAN);
        return R.layout.activity_news24_hours;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        title = (TextView) findViewById(R.id.title);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.addHeaderView(getHeaderView());

        adapter = new NewsListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private View getHeaderView() {
        View head = LayoutInflater.from(this).inflate(R.layout.layout_unit_header, recyclerView, false);
        AutoUtils.auto(head);
        image = (ImageView) head.findViewById(R.id.image);
        name = (TextView) head.findViewById(R.id.name);
        attention = (CheckedTextView) head.findViewById(R.id.attention);
        newsNum = (TextView) head.findViewById(R.id.newsNum);
        viewsNum = (TextView) head.findViewById(R.id.viewsNum);
        attenteNum = (TextView) head.findViewById(R.id.attenteNum);
        msg = (TextView) head.findViewById(R.id.msg);

        return head;
    }

    @Override
    protected void initData() {
        if (offset == 0) {
            GetUnitDetailsReq req = new GetUnitDetailsReq();
            req.cat_id = cat_id;

            App.getInstance().requestJsonDataGet(req, new unitListener(), new MyErrorListener(stateView));
        }

        GetCategoryListReq newsListReq = new GetCategoryListReq();
        newsListReq.cat_id = cat_id;
        newsListReq.offset = offset;
        newsListReq.number = number;

        App.getInstance().requestJsonArrayDataGet(newsListReq, new newsListListener(), null);
    }

    @Override
    protected void initListeners() {
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
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
        findViewById(R.id.back).setOnClickListener(this);

        attention.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attention:
                if (!App.getInstance().isLogin()) {
                    startActivity(LoginActivity.newIntent(this, false));
                    return;
                }
                if (!attention.isChecked()) {
                    SetMakeOrderCategoryReq req = new SetMakeOrderCategoryReq();
                    req.cat_id = cat_id;
                    App.getInstance().requestJsonDataGet(req, new makeOrderAuthorListenner(), null);
                } else {
                    SetCancelOrderCategoryReq req = new SetCancelOrderCategoryReq();
                    req.cat_id = cat_id;
                    App.getInstance().requestJsonDataGet(req, new cancelOrderAuthorListenner(), null);
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if ((attention.isChecked() && is_ordered == 0) || (!attention.isChecked() && is_ordered == 1))
            setResult(RESULT_CODE);
        super.onBackPressed();
    }

    private class unitListener implements com.android.volley.Response.Listener<BaseBeanRsp<GetUnitDetailsRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetUnitDetailsRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                addHeadData(response.Info);
            } else stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private void addHeadData(GetUnitDetailsRsp rsp) {
        GlideTools.Glide(this, rsp.thumb, image, R.drawable.bg_morentu_xiaotumoshi);
        name.setText(rsp.catname);
        title.setText(rsp.catname);
//        position.setText(rsp.);
        attention.setText(rsp.is_ordered == 0 ? "关注" : "已关注");
        attention.setChecked(rsp.is_ordered == 1);
        is_ordered = rsp.is_ordered;
        newsNum.setText(String.valueOf(rsp.items));
        viewsNum.setText(String.valueOf(rsp.views));
        attenteNum.setText(String.valueOf(rsp.ordered));
        msg.setText(TextUtils.isEmpty(rsp.brief) ? "" : rsp.brief);
    }

    private class newsListListener implements com.android.volley.Response.Listener<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetFeaturedNewsListRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    adapter.setList(response.Info);
                } else
                    adapter.addAll(response.Info);
            }
            if (offset == 0) recyclerView.refreshComplete();
            else recyclerView.loadMoreComplete();
        }
    }

    private class makeOrderAuthorListenner implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                attention.setText("已关注");
                attention.setChecked(true);
            }
        }
    }

    private class cancelOrderAuthorListenner implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                attention.setText("关注");
                attention.setChecked(false);
            }
        }
    }
}
