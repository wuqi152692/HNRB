package com.hnzx.hnrb.ui.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.News24HoursAdapter;
import com.hnzx.hnrb.adapter.ReporterNewsListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetAuthorCenterReq;
import com.hnzx.hnrb.requestbean.GetAuthorNewsListReq;
import com.hnzx.hnrb.requestbean.SetCancelOrderAuthorReq;
import com.hnzx.hnrb.requestbean.SetMakeOrderAuthorReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAuthorCenterRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/4/20 0020.
 */

public class ReporterDetailsActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private ReporterNewsListAdapter adapter;

    private ImageView image;
    private TextView name, position, attention, newsNum, viewsNum, attenteNum, msg;

    private int offset = 0;
    private final int number = 10;
    private String author_id;

    @Override
    protected int getLayoutId() {
        author_id = getIntent().getStringExtra(Constant.BEAN);
        return R.layout.activity_news24_hours;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        ((TextView) findViewById(R.id.title)).setText("记者空间");

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.addHeaderView(getHeaderView());

        adapter = new ReporterNewsListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private View getHeaderView() {
        View head = LayoutInflater.from(this).inflate(R.layout.layout_reporter_header, recyclerView, false);
        AutoUtils.auto(head);
        image = (ImageView) head.findViewById(R.id.image);
        name = (TextView) head.findViewById(R.id.name);
        position = (TextView) head.findViewById(R.id.position);
        attention = (TextView) head.findViewById(R.id.attention);
        newsNum = (TextView) head.findViewById(R.id.newsNum);
        viewsNum = (TextView) head.findViewById(R.id.viewsNum);
        attenteNum = (TextView) head.findViewById(R.id.attenteNum);
        msg = (TextView) head.findViewById(R.id.msg);

        return head;
    }

    @Override
    protected void initData() {
        if (offset == 0) {
            GetAuthorCenterReq req = new GetAuthorCenterReq();
            req.author_id = author_id;

            App.getInstance().requestJsonDataGet(req, new authorCenterListener(), new MyErrorListener(stateView));
        }

        GetAuthorNewsListReq newsListReq = new GetAuthorNewsListReq();
        newsListReq.author_id = author_id;
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
                if (attention.getText().equals("关注")) {
                    SetMakeOrderAuthorReq req = new SetMakeOrderAuthorReq();
                    req.author_id = author_id;
                    App.getInstance().requestJsonDataGet(req, new makeOrderAuthorListenner(), null);
                } else {
                    SetCancelOrderAuthorReq req = new SetCancelOrderAuthorReq();
                    req.author_id = author_id;
                    App.getInstance().requestJsonDataGet(req, new cancelOrderAuthorListenner(), null);
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    private class authorCenterListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetAuthorCenterRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetAuthorCenterRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                addHeadData(response.Info);
            } else stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private void addHeadData(GetAuthorCenterRsp rsp) {
        GlideTools.Glide(this, rsp.thumb, image, R.drawable.icon_default_head);
        name.setText(rsp.name);
        position.setText(rsp.rolename);
        attention.setText(rsp.is_ordered == 0 ? "关注" : "已关注");
        newsNum.setText(String.valueOf(rsp.published));
        viewsNum.setText(String.valueOf(rsp.views));
        attenteNum.setText(String.valueOf(rsp.ordered));
        msg.setText(rsp.brief);
    }

    private class newsListListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetLatestNewsRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetLatestNewsRsp> response) {
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
            }
        }
    }

    private class cancelOrderAuthorListenner implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                attention.setText("关注");
            }
        }
    }
}
