package com.hnzx.hnrb.ui.interact;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.TopicCommentListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetTopicCommentReq;
import com.hnzx.hnrb.requestbean.GetTopicInfoReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.CommentsBean;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;
import com.hnzx.hnrb.responsebean.GetTopicInfoRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsCommentDialog;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;
import java.util.Map;

public class TopicDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String DATA_KEY = "datakey";
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private TopicCommentListAdapter adapter;

    private ImageView image, headPic, other;
    private TextView topicTitle, info, founderValue, date, pinglun;
    private String topic_id;
    private final int number = 10;
    private int offset = 0;
    private GetTopicInfoRsp rsp;

    private IntentFilter filter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            offset = 0;
            getRefresh();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (filter == null)
            filter = new IntentFilter(TopicCommentActivity.ACTION);
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
        topic_id = getIntent().getStringExtra(DATA_KEY);
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("话题");
        other = (ImageView) findViewById(R.id.other);
        other.setVisibility(View.VISIBLE);
        pinglun = (TextView) findViewById(R.id.pinglun);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setFootViewText("正在加载", "没有更多评论啦");

        recyclerView.addHeaderView(getHeaderView());
        adapter = new TopicCommentListAdapter(this, topic_id);
        recyclerView.setAdapter(adapter);
    }

    private View getHeaderView() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_topic_detials_header, null, false);
        AutoUtils.auto(header);

        image = (ImageView) header.findViewById(R.id.image);
        headPic = (ImageView) header.findViewById(R.id.headPic);

        topicTitle = (TextView) header.findViewById(R.id.topicTitle);
        info = (TextView) header.findViewById(R.id.info);
        founderValue = (TextView) header.findViewById(R.id.founderValue);
        date = (TextView) header.findViewById(R.id.date);
        return header;
    }

    @Override
    protected void initData() {
        getRefresh();
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.pinglunLayout).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        other.setOnClickListener(this);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                getRefresh();
            }

            @Override
            public void onLoadMore() {
                offset += number;
                getLoadMore();
            }
        });
    }

    public void getRefresh() {
        GetTopicInfoReq req = new GetTopicInfoReq();
        req.topic_id = topic_id;

        App.getInstance().requestJsonDataGet(req, new topicInfoListener(), new MyErrorListener(stateView));
        //评论数据获取

        GetTopicCommentReq commentReq = new GetTopicCommentReq();
        commentReq.topic_id = topic_id;
        commentReq.number = number;
        commentReq.offset = offset;

        App.getInstance().requestJsonDataGet(commentReq, new commentListener(), null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pinglunLayout:
                if (!App.getInstance().isLogin()) {
                    startActivity(LoginActivity.newIntent(this, false));
                    return;
                }
                startActivity(TopicCommentActivity.newIntent(this, topic_id));
                break;
            case R.id.other:
                share();
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

    NewsShareDialog dialog;

    private void share() {
        if (rsp == null) {
            showToast("分享失败");
            return;
        }
        if (dialog == null || dialog.isAdded())
            dialog = NewsShareDialog.newInstance(rsp.title, rsp.info, rsp.thumb, rsp.url);
        dialog.show(getFragmentManager(), getLocalClassName());
    }

    public void getLoadMore() {
        GetTopicCommentReq commentReq = new GetTopicCommentReq();
        commentReq.topic_id = topic_id;
        commentReq.number = number;
        commentReq.offset = offset;

        App.getInstance().requestJsonDataGet(commentReq, new commentListener(), null);
    }

    private class topicInfoListener implements Response.Listener<BaseBeanRsp<GetTopicInfoRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetTopicInfoRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                rsp = response.Info;
                addTopicInfoToView();
            } else stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private void addTopicInfoToView() {
        GlideTools.Glide(this, rsp.thumb, image, R.drawable.bg_morentu_datumoshi);
        GlideTools.GlideRound(this, rsp.avatar, headPic, R.drawable.reporter_cricle_dark);

        topicTitle.setText(rsp.title);
        this.info.setText(rsp.info);
        founderValue.setText(rsp.sponsor);
        date.setText(rsp.created);

        pinglun.setText(rsp.comments.length() > 2 ? "99+" : rsp.comments);
    }

    private class commentListener implements Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsCommentRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsCommentRsp> response) {
            if (offset == 0) recyclerView.refreshComplete();
            else recyclerView.loadMoreComplete();
            if (response != null && response.Status == 1) {
                List<String> ids = response.Info.ids;
                if (offset == 0) {
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
            } else recyclerView.setNoMore(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
