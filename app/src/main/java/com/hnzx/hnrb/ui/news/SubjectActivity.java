package com.hnzx.hnrb.ui.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.SubjectDetailsAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetNewsSpecialReq;
import com.hnzx.hnrb.requestbean.SetAddFavouriteReq;
import com.hnzx.hnrb.requestbean.SetCancelFavouriteReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.responsebean.GetNewsSpecialRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.view.FlowLayout;
import com.hnzx.hnrb.view.MultiStateView;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/18 0018.
 */

public class SubjectActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView stateView;
    private ExpandableListView listView;
    private ImageView image;
    private TextView title, date, views, msg, indicator;
    private FlowLayout layout;
    private CheckBox collect;

    private SubjectDetailsAdapter adapter;

    private String special_id;
    private GetNewsSpecialRsp info;

    private String SUBJECT_DATA;

    @Override
    protected int getLayoutId() {
        special_id = getIntent().getStringExtra(Constant.BEAN);
        SUBJECT_DATA = "subject_" + special_id;
        return R.layout.activity_subject;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        indicator = (TextView) findViewById(R.id.indicator);
        AutoUtils.auto(indicator);

        collect = (CheckBox) findViewById(R.id.collect);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        listView = (ExpandableListView) findViewById(R.id.listView);
        listView.addHeaderView(getHeaderView());
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);

        listView.requestFocus();

        adapter = new SubjectDetailsAdapter(this);
        listView.setAdapter(adapter);

        if (!App.getInstance().isNetworkConnected(this))
            new sqliteAsync().execute();
    }

    private View getHeaderView() {
        View head = LayoutInflater.from(this).inflate(R.layout.layout_subject_header, listView, false);
        AutoUtils.auto(head);
        image = (ImageView) head.findViewById(R.id.image);
        title = (TextView) head.findViewById(R.id.title);
        date = (TextView) head.findViewById(R.id.date);
        views = (TextView) head.findViewById(R.id.views);
        msg = (TextView) head.findViewById(R.id.msg);

        layout = (FlowLayout) head.findViewById(R.id.flowLayout);
        return head;
    }

    @Override
    protected void initData() {
        GetNewsSpecialReq req = new GetNewsSpecialReq();
        req.special_id = special_id;

        App.getInstance().requestJsonDataGet(req, new dataListener(), App.getInstance().isNetworkConnected(this) || !isFirstRun ? new MyErrorListener(stateView) : null);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        collect.setOnClickListener(this);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final ExpandableListView listView = (ExpandableListView) view;
                int npos = view.pointToPosition(0, 0);// 其实就是firstVisibleItem
                if (npos == AdapterView.INVALID_POSITION)// 如果第一个位置值无效
                    return;
                long position = listView.getExpandableListPosition(npos) + 1;
                int groupPosition = ExpandableListView.getPackedPositionGroup(position);// 获取第一行group的id

                if (groupPosition < 0)
                    return;
                if (firstVisibleItem > 0) {
                    indicator.setVisibility(View.VISIBLE);
                } else {
                    indicator.setVisibility(View.GONE);// 隐藏指示器
                    return;
                }

                if (groupPosition != indicatorGroupId) {// 如果指示器显示的不是当前group
                    indicatorGroupId = groupPosition;
                    App.getInstance().log("position=" + indicatorGroupId);
                    indicator.setText(adapter.getGroup(groupPosition).name);
                }
                if (indicatorGroupId == -1) // 如果此时grop的id无效，则返回
                    return;
            }
        });
    }

    private int indicatorGroupId = -1;

    NewsShareDialog dialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.collect:
                collect();
                break;
            case R.id.share:
                if (info == null) {
                    showTopToast("分享失败", false);
                    return;
                }
                if (dialog == null || dialog.isAdded())
                    dialog = NewsShareDialog.newInstance(info.title, info.title, info.thumb, info.url);
                dialog.show(getFragmentManager(), getLocalClassName());
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                isFirstRun = false;
                initData();
                break;
        }
    }

    private void collect() {
        if (!App.getInstance().isLogin()) {
            startActivity(LoginActivity.newIntent(this, LoginActivity.class));
            return;
        }
        if (collect.isChecked()) {
            SetAddFavouriteReq req = new SetAddFavouriteReq();
            req.content_id = special_id;
            App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
        } else {
            SetCancelFavouriteReq req = new SetCancelFavouriteReq();
            req.content_id = special_id;
            App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsSpecialRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsSpecialRsp> response) {
            if (response != null && response.Status == 1) {
                info = response.Info;
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                addData();
                adapter.addDataList(response.Info.lists);
                for (int i = 0; i < adapter.getGroupCount(); i++)
                    listView.expandGroup(i);
                new saveDataAsync().execute();
            } else stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private void addData() {
        GlideTools.Glide(this, info.thumb, image, R.drawable.bg_morentu_datumoshi);
        title.setText(info.title);
        date.setText(info.created);
        views.setText(String.valueOf(info.views));
        msg.setText(info.brief);

        for (int i = 0; i < info.lists.size(); i++) {
            GetNewsSpecialRsp.ListsBeanX list = info.lists.get(i);
            TextView child = new TextView(this);
            child.setTextColor(getResources().getColor(android.R.color.black));
            child.setText(list.name);
            child.setTextSize(16);
            child.setPadding(14, 10, 14, 12);
            child.setGravity(Gravity.CENTER);
            child.setBackgroundResource(R.drawable.news_special_btn_style);
            child.setOnClickListener(new selectItemClickListener(i));
            layout.addView(child);
        }
    }

    private class selectItemClickListener implements View.OnClickListener {
        private int index;

        public selectItemClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            listView.setSelectedGroup(index);
        }
    }

    private class addCollectListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                showToast(collect.isChecked() ? "添加收藏成功" : "取消收藏成功");
            } else showToast(collect.isChecked() ? "添加收藏失败" : "取消收藏失败");
        }
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            CDUtil.saveObject(info, SUBJECT_DATA);
            return null;
        }
    }

    private class sqliteAsync extends AsyncTask<String, Integer, GetNewsSpecialRsp> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetNewsSpecialRsp doInBackground(String... params) {
            return (GetNewsSpecialRsp) CDUtil.readObjectJust(SUBJECT_DATA);
        }

        @Override
        protected void onPostExecute(GetNewsSpecialRsp listRsps) {
            if (listRsps != null) {
                info = listRsps;
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                addData();
                adapter.addDataList(info.lists);
                for (int i = 0; i < adapter.getGroupCount(); i++)
                    listView.expandGroup(i);
            } else stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}
