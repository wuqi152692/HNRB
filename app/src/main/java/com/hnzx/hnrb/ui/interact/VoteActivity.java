package com.hnzx.hnrb.ui.interact;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsCommentAdapter;
import com.hnzx.hnrb.adapter.TopicCommentListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.htmlTools.TDownloadListener;
import com.hnzx.hnrb.htmlTools.TWebChromeClient;
import com.hnzx.hnrb.htmlTools.TWebView;
import com.hnzx.hnrb.htmlTools.TWebViewClient;
import com.hnzx.hnrb.requestbean.GetNewsCommentsReq;
import com.hnzx.hnrb.requestbean.GetNewsHotCommentsReq;
import com.hnzx.hnrb.requestbean.GetTopicCommentReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.CommentsBean;
import com.hnzx.hnrb.responsebean.GetInteractListRsp;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;
import com.hnzx.hnrb.responsebean.GetTopicInfoRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.tools.WebUtil;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.ui.news.CommentActivity;
import com.hnzx.hnrb.ui.news.NewsDetailsActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;
import java.util.Map;

public class VoteActivity extends BaseActivity implements View.OnClickListener {
    public static final String DATA_KEY = "datakey";
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private NewsCommentAdapter adapter;

    private ImageView other;
    private TextView pinglun;
    private TWebView webView;
    private GetInteractListRsp intentData;

    @Override
    protected int getLayoutId() {
        intentData = (GetInteractListRsp) getIntent().getSerializableExtra(DATA_KEY);
        if (intentData == null)
            finish();
        return R.layout.activity_topic_detail;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("投票");
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
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.addHeaderView(getHeaderView());
        recyclerView.setFootView(getFootView());
        adapter = new NewsCommentAdapter(this, intentData.type_id);
        recyclerView.setAdapter(adapter);

        if (App.getInstance().isLogin())
            synCookies(".henandaily.cn");

        webView.loadUrl(intentData.link_url);

        pinglun.setText(intentData.views > 99 ? "99+" : String.valueOf(intentData.views));
    }

    private View getFootView() {
        View footer = LayoutInflater.from(this).inflate(R.layout.layout_vote_footer, recyclerView, false);
        AutoUtils.auto(footer);
        footer.setOnClickListener(this);
        return footer;
    }

    private void synCookies(String url) {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, App.getInstance().getLoginInfo().cookie);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private AutoLinearLayout getHeaderView() {
        AutoLinearLayout header = (AutoLinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_vote_header, recyclerView, false);
        AutoUtils.auto(header);
        webView = new TWebView(this);
        WebUtil.setWebView(webView, this);
        webView.setDownloadListener(new TDownloadListener());
        webView.setWebChromeClient(new TWebChromeClient());
        webView.setWebViewClient(new TWebViewClient(this, webView, false));
        header.addView(webView, 0);
        header.requestFocus();
        return header;
    }

    @Override
    protected void initData() {
        //热门评论
        GetNewsHotCommentsReq commentsReq = new GetNewsHotCommentsReq();
        commentsReq.content_id = intentData.type_id;
        App.getInstance().requestJsonDataGet(commentsReq, new commentListener(), null);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.pinglunLayout).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        other.setOnClickListener(this);

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
            case R.id.pinglunLayout:
                goToCommentActivity();
                break;
            case R.id.tv_check:
                goToCommentActivity();
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

    private void goToCommentActivity() {
        Intent it = new Intent(this, CommentActivity.class);
        it.putExtra(Constant.BEAN, intentData.type_id);
        startActivity(it);
    }

    NewsShareDialog dialog;

    private void share() {
        if (intentData == null) {
            showToast("分享失败");
            return;
        }
        if (dialog == null || dialog.isAdded())
            dialog = NewsShareDialog.newInstance(intentData.title, intentData.brief, intentData.thumb, intentData.link_url);
        dialog.show(getFragmentManager(), getLocalClassName());
    }

    private class commentListener implements Response.Listener<BaseBeanRsp<GetNewsCommentRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsCommentRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                recyclerView.refreshComplete();
                List<String> ids = response.Info.ids;
                if (ids != null && ids.size() > 0) {
                    adapter.addMap(JSON.parseObject(response.Info.comments, new TypeReference<Map<String, CommentsBean>>() {
                    }), 0);
                    adapter.setList(ids);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
