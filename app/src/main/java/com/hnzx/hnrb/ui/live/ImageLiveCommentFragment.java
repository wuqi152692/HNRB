package com.hnzx.hnrb.ui.live;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.LiveCommentAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.GetLiveDiscussListReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.CommentsBean;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;
import com.hnzx.hnrb.view.CustomViewpager;
import com.hnzx.hnrb.view.pulltorefresh.PullToRefreshLayout;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;
import java.util.Map;

/**
 * 图文直播 互动评论
 *
 * @author: mingancai
 * @Time: 2017/4/1 0001.
 */

public class ImageLiveCommentFragment extends BaseFragment {
    private XRecyclerView recyclerView;
    private LiveCommentAdapter adapter;
    private static CustomViewpager vp;
    private String live_id;
    private int offset = 0;
    private final int number = 10;
    private PullToRefreshLayout refreshLayout;
    private IntentFilter refreshFilter;

    public static ImageLiveCommentFragment newInstance(CustomViewpager vp) {
        ImageLiveCommentFragment fragment = new ImageLiveCommentFragment();
        fragment.vp = vp;
        return fragment;
    }

    public static ImageLiveCommentFragment newInstance(PullToRefreshLayout refreshLayout) {
        ImageLiveCommentFragment fragment = new ImageLiveCommentFragment();
        fragment.refreshLayout = refreshLayout;
        return fragment;
    }

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        live_id = getArguments().getString(Constant.BEAN);
        View view = inflater.inflate(R.layout.fragment_image_live_hall, container, false);
        if (vp != null)
            vp.setObjectForPosition(view, 1);
        return view;
    }

    @Override
    protected void initViews(View contentView) {
        recyclerView = (XRecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new LiveCommentAdapter(getActivity(), live_id);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshFilter == null)
            refreshFilter = new IntentFilter("refresh");
        mActivity.registerReceiver(refreshReceiver, refreshFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refreshFilter != null)
            mActivity.unregisterReceiver(refreshReceiver);
    }

    private BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(Constant.BEAN, true)) {
                offset = 0;
                initDatas();
            } else {
                offset += number;
                initDatas();
            }
        }
    };

    @Override
    protected void initListeners() {
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)) {
                    recyclerView.setNestedScrollingEnabled(true);
                }
            }
        });
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    protected void initDatas() {
        GetLiveDiscussListReq req = new GetLiveDiscussListReq();
        req.live_id = live_id;
        req.offset = offset;
        req.number = number;

        App.getInstance().requestJsonDataGet(req, new commentListListener(), null);

        recyclerView.setNestedScrollingEnabled(false);
    }

    private class commentListListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsCommentRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsCommentRsp> response) {
            if (response != null && response.Status == 1) {
                List<String> ids = response.Info.ids;
                if (offset == 0) {
                    refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    if (ids != null && ids.size() > 0) {
                        adapter.addMap(JSON.parseObject(response.Info.discussion, new TypeReference<Map<String, CommentsBean>>() {
                        }), offset);
                        adapter.setList(ids);
                    }
                } else {
                    if (ids != null && ids.size() > 0) {
                        adapter.addMap(JSON.parseObject(response.Info.discussion, new TypeReference<Map<String, CommentsBean>>() {
                        }), offset);
                        adapter.addAll(ids);
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    } else
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }
            }
        }
    }
}
