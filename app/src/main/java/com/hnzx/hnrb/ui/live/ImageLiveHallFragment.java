package com.hnzx.hnrb.ui.live;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.ImageLiveHallAdapter;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.GetLiveHallListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLiveHallListRsp;
import com.hnzx.hnrb.view.CustomViewpager;
import com.hnzx.hnrb.view.pulltorefresh.PullToRefreshLayout;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * 图文直播 直播大厅
 */
public class ImageLiveHallFragment extends BaseFragment {
    public XRecyclerView recyclerView;
    private ImageLiveHallAdapter adapter;
    private String live_id;
    private int offset = 0;
    private final int number = 10;
    private CustomViewpager vp;
    private IntentFilter sortFilter, refreshFilter;
    private PullToRefreshLayout refreshLayout;

    public static ImageLiveHallFragment newInstance(CustomViewpager vp) {
        ImageLiveHallFragment fragment = new ImageLiveHallFragment();
        fragment.vp = vp;
        return fragment;
    }

    public static ImageLiveHallFragment newInstance(PullToRefreshLayout refreshLayout) {
        ImageLiveHallFragment fragment = new ImageLiveHallFragment();
        fragment.refreshLayout = refreshLayout;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sortFilter == null)
            sortFilter = new IntentFilter("sort");
        mActivity.registerReceiver(sortReceiver, sortFilter);
        if (refreshFilter == null)
            refreshFilter = new IntentFilter("refresh");
        mActivity.registerReceiver(refreshReceiver, refreshFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sortFilter != null)
            mActivity.unregisterReceiver(sortReceiver);
        if (refreshFilter != null)
            mActivity.unregisterReceiver(refreshReceiver);
    }

    private BroadcastReceiver sortReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sortorder = sortorder == 1 ? 0 : 1;
            offset = 0;
            initDatas();
        }
    };

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
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        live_id = getArguments().getString(Constant.BEAN);
        View view = inflater.inflate(R.layout.fragment_image_live_hall, container, false);
        if (vp != null)
            vp.setObjectForPosition(view, 0);
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

        adapter = new ImageLiveHallAdapter(mActivity);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initListeners() {

    }

    int sortorder = 1;

    @Override
    protected void initDatas() {
        GetLiveHallListReq req = new GetLiveHallListReq();
        req.live_id = live_id;
        req.offset = offset;
        req.number = number;
        req.sortorder = sortorder;

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), null);
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetLiveHallListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetLiveHallListRsp> response) {
            if (response != null && response.Status == 1) {
                if (offset == 0) {
                    adapter.setList(response.Info);
                    recyclerView.scrollToPosition(0);
                    if (refreshLayout != null)
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    if (response.Info != null && response.Info.size() > 0) {
                        adapter.addAll(response.Info);
                        if (refreshLayout != null)
                            refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    } else if (refreshLayout != null)
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }
            }
        }
    }
}
