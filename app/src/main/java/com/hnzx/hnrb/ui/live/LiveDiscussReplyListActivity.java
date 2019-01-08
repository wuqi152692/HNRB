package com.hnzx.hnrb.ui.live;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.LiveDiscussReplyListAdapter;
import com.hnzx.hnrb.adapter.TopicCommentReplyListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetCommentsMoreReq;
import com.hnzx.hnrb.requestbean.GetLiveDiscussMoreReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetTopicCommentMoreRsp;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsCommentDialog;
import com.hnzx.hnrb.ui.interact.TopicCommentActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

public class LiveDiscussReplyListActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private EditText comment_et;
    private LiveDiscussReplyListAdapter adapter;

    private String content_id;
    private int id;

    private static final String CONTENT_ID = "contentid", ID = "id";

    public static Intent newIntent(Context context, String content_id, int id) {
        Intent intent = new Intent(context, LiveDiscussReplyListActivity.class);
        intent.putExtra(CONTENT_ID, content_id);
        intent.putExtra(ID, id);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        try {
            content_id = getIntent().getStringExtra(CONTENT_ID);
            id = getIntent().getIntExtra(ID, 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return R.layout.activity_topic_comment_reply_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("评论");
        comment_et = (EditText) findViewById(R.id.comment_et);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        View emptyView = stateView.getView(MultiStateView.VIEW_STATE_EMPTY);
        ((TextView) emptyView.findViewById(R.id.guide)).setText("还没有评论回复");
        ((TextView) emptyView.findViewById(R.id.type)).setText("快来抢沙发吧");
        emptyView.findViewById(R.id.reload_data).setVisibility(View.GONE);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);

        adapter = new LiveDiscussReplyListAdapter(this, content_id, String.valueOf(id));
        recyclerView.setAdapter(adapter);

        recyclerView.setFootViewText("正在加载", "没有更多回复啦");
    }

    @Override
    protected void initData() {
        GetLiveDiscussMoreReq req = new GetLiveDiscussMoreReq();
        req.id = id;
        req.live_id = content_id;

        App.getInstance().requestJsonArrayDataGet(req, new Response.Listener<BaseBeanArrayRsp<GetTopicCommentMoreRsp>>() {
            @Override
            public void onResponse(BaseBeanArrayRsp<GetTopicCommentMoreRsp> response) {
                recyclerView.refreshComplete();
                if (response != null && response.Status == 1) {
                    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.setList(response.Info);
                    recyclerView.refreshComplete();
                    recyclerView.setNoMore(true);
                } else stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }, new MyErrorListener(stateView));
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.pinglunLayout).setOnClickListener(this);
        comment_et.setOnClickListener(this);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.comment_et:
            case R.id.pinglunLayout:
                if (!App.getInstance().isLogin()) {
                    startActivity(LoginActivity.newIntent(this, false));
                    return;
                }
                NewsCommentDialog dialog = NewsCommentDialog.newInstance(content_id, "", NewsCommentDialog.LIVE_COMMENT, "");
                dialog.show(getFragmentManager(), getLocalClassName());
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }
}
