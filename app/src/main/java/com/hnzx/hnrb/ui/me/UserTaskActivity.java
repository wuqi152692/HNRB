package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsCommentAdapter;
import com.hnzx.hnrb.adapter.UserTaskAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetMyTaskListReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetMyTaskListRsp;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.ui.web.WebActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

public class UserTaskActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private UserTaskAdapter adapter;
    private TextView todayPoint;
    private Map<String, String> params = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_task;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("任务");

        stateView = (MultiStateView) findViewById(R.id.stateView);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
        adapter = new UserTaskAdapter(this);
        recyclerView.setAdapter(adapter);

        todayPoint = (TextView) findViewById(R.id.todayPoint);
    }

    @Override
    protected void initData() {
        final GetMyTaskListReq req = new GetMyTaskListReq();

        App.getInstance().requestJsonDataGet(req, new Response.Listener<BaseBeanRsp<GetMyTaskListRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<GetMyTaskListRsp> response) {
                if (response != null && response.Status == 1) {
                    adapter.setList(response.Info.lists);
                    todayPoint.setText(Html.fromHtml("今日获得：<font color='#ff545b'>" + response.Info.dayScore + "</font>积分"));
                    params.put(WebActivity.WEB_URL_KEY, response.Info.rule);
                }
            }
        }, new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.checkPointRules).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkPointRules:
                IntentUtil.startActivity(this, WebActivity.class, params);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
