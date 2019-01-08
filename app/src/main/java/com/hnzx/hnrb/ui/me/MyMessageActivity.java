package com.hnzx.hnrb.ui.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.MyMessageListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetUserMessageReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetUserMessageRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class MyMessageActivity extends BaseActivity implements View.OnClickListener{
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private MyMessageListAdapter adapter;
    private final int number = 10;
    private int offset = 0;
    private int unReadNum = 0;
    private View lookMore;

    public static final String MESSAGE_TIMESTAMP = "MESSAGE_TIMESTAMP";

    public static Intent newIntent(Context con, int unReadNum) {
        Intent intent = new Intent(con, MyMessageActivity.class);
        intent.putExtra(Constant.BEAN, unReadNum);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        unReadNum = getIntent().getIntExtra(Constant.BEAN, 0);
        return R.layout.activity_my_message;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("消息");
        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        RecycleViewTool.getVerticalRecyclerView(recyclerView, this, false);
        lookMore = getFootView();
        recyclerView.setFootView(lookMore);
        recyclerView.setPullRefreshEnabled(false);

        adapter = new MyMessageListAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        GetUserMessageReq req = new GetUserMessageReq();
        req.offset = offset;
        req.number = unReadNum;

        App.getInstance().requestJsonArrayDataGet(req, new dataLIstener(), new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                lookMoreData();
            }

            @Override
            public void onLoadMore() {
                if (lookMore.getVisibility() == View.VISIBLE) return;
                offset += number;
                lookMoreData();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0x02);
                finish();
            }
        });

        lookMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setPullRefreshEnabled(true);
                lookMore.setVisibility(View.GONE);
                offset = unReadNum;
                lookMoreData();
            }
        });
    }

    private void lookMoreData() {
        GetUserMessageReq req = new GetUserMessageReq();
        req.offset = offset;
        req.number = number;

        App.getInstance().requestJsonArrayDataGet(req, new dataLIstener(), new MyErrorListener(stateView));
    }

    public View getFootView() {
        TextView footView = new TextView(this);
        footView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        footView.setTextColor(Color.parseColor("#191919"));
        footView.setTextSize(15);
        footView.setGravity(Gravity.CENTER);
        footView.setPadding(0, 18, 0, 18);
        footView.setText(Html.fromHtml("点击查看更多回复<font color='#8a8a8a'> >>"));
        return footView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    private class dataLIstener implements Response.Listener<BaseBeanArrayRsp<GetUserMessageRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetUserMessageRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    if (response.Info != null && response.Info.size() < number)
                        recyclerView.setNoMore(true);
                    if (response.Info != null && response.Info.size() > 0)
                        SharePreferenceTool.put(MyMessageActivity.this, MESSAGE_TIMESTAMP, response.Info.get(0).timestamp);
                } else {
                    adapter.addAll(response.Info);
                    recyclerView.loadMoreComplete();
                    if (response.Info == null || response.Info.size() < number)
                        recyclerView.setNoMore(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(0x02);
        finish();
    }
}
