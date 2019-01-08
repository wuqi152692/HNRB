package com.hnzx.hnrb.ui.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsCommentAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetNewsCommentsReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.CommentsBean;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsCommentDialog;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;
import java.util.Map;

public class CommentActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private EditText comment_et;
    private View replaceLayout;
    private NewsCommentAdapter adapter;

    private String content_id;

    private GetNewsCommentsReq req;
    private final int number = 10;
    private int offset = 0;
    private IntentFilter filter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            offset = 0;
            initData();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (filter == null)
            filter = new IntentFilter(NewsCommentDialog.ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (filter != null)
            unregisterReceiver(receiver);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        content_id = getIntent().getStringExtra(Constant.BEAN);

        ((TextView) findViewById(R.id.title)).setText("全部评论");
        comment_et = (EditText) findViewById(R.id.comment_et);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        View emptyView = stateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        ((TextView) emptyView.findViewById(R.id.guide)).setText("还没有评论");
        ((TextView) emptyView.findViewById(R.id.type)).setText("快来抢沙发吧");
        emptyView.findViewById(R.id.reload_data).setVisibility(View.GONE);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);

        adapter = new NewsCommentAdapter(this, content_id);
        recyclerView.setAdapter(adapter);

        recyclerView.setFootViewText("正在加载", "没有更多评论啦");

        replaceLayout = findViewById(R.id.pinglunLayout);
    }

    @Override
    protected void initData() {
        if (req == null) {
            req = new GetNewsCommentsReq();
            req.content_id = content_id;
            req.number = number;
        }
        req.offset = offset;
        Log.d("http_url", "评论: "+req);
        App.getInstance().requestJsonDataGet(req, new commentListener(), new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        replaceLayout.setOnClickListener(this);
        comment_et.setOnClickListener(this);
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
        replaceLayout.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.comment_et:
            case R.id.pinglunLayout:
                NewsCommentDialog dialog = NewsCommentDialog.newInstance(content_id, "", NewsCommentDialog.NEWS_COMMENT, "");
                dialog.show(getFragmentManager(), getLocalClassName());
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    private class commentListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsCommentRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsCommentRsp> response) {
            if (offset == 0) recyclerView.refreshComplete();
            else recyclerView.loadMoreComplete();
            if (response != null && response.Status == 1) {
                List<String> ids = response.Info.ids;
                if (offset == 0) {
                    stateView.setViewState(response.Info == null || response.Info.ids == null || response.Info.ids.size() < 1 ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);
                    if (ids != null && ids.size() > 0) {
                        adapter.addMap(JSON.parseObject(response.Info.comments, new TypeReference<Map<String, CommentsBean>>() {
                        }), offset);
                        adapter.setList(ids);
                        if (ids.size() < number)
                            recyclerView.setNoMore(true);
                    }
                } else {
                    if (ids != null && ids.size() > 0) {
                        adapter.addMap(JSON.parseObject(response.Info.comments, new TypeReference<Map<String, CommentsBean>>() {
                        }), offset);
                        adapter.addAll(ids);
                    }
                    if (response.Info == null || response.Info.ids == null || response.Info.ids.size() == 0)
                        recyclerView.setNoMore(true);
                }
            }
        }
    }
}
