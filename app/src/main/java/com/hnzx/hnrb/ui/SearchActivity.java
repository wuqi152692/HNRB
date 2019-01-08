package com.hnzx.hnrb.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.SearchAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.requestbean.GetSearchHotWordsReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.view.FlowLayout;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private XRecyclerView recyclerView;
    private EditText editText;
    private FlowLayout hotLayout;
    private TextView clear;

    private SearchAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("搜索");

        editText = (EditText) findViewById(R.id.editText);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addHeaderView(getHeaderView());
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(false);

        adapter = new SearchAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private View getHeaderView() {
        View head = LayoutInflater.from(this).inflate(R.layout.layout_search_header, recyclerView, false);
        clear = (TextView) head.findViewById(R.id.clear);
        hotLayout = (FlowLayout) head.findViewById(R.id.hotLayout);
        return head;
    }

    @Override
    protected void initData() {
        App.getInstance().requestJsonArrayDataGet(new GetSearchHotWordsReq(), new Response.Listener<BaseBeanArrayRsp<String>>() {
            @Override
            public void onResponse(BaseBeanArrayRsp<String> response) {
                if (response != null && response.Status == 1 && response.Info != null) {
                    for (final String name : response.Info) {
                        TextView cat = new TextView(SearchActivity.this);
                        cat.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
                        cat.setPadding(20, 10, 20, 10);
                        cat.setBackgroundColor(Color.parseColor("#f2f2f2"));
                        cat.setTextColor(Color.parseColor("#131313"));
                        cat.setTextSize(15);
                        cat.setText(name);
                        cat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goSearch(name);
                            }
                        });
                        hotLayout.addView(cat);
                    }
                }
            }
        }, null);

        searchMsg = getSearchMsg();
        adapter.setList(searchMsg);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        clear.setOnClickListener(this);
        findViewById(R.id.goSearch).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goSearch:
                if (!TextUtils.isEmpty(editText.getText()))
                    goSearch(editText.getText().toString());
                else showTopToast("请输入搜索关键词", false);
                break;
            case R.id.clear:
                searchMsg = null;
                SharePreferenceTool.remove(this, SEARCH_HISTORY_KEY);
                adapter.setList(null);
                showTopToast("搜索记录清除成功", true);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void goSearch(String searchValues) {
        saveSearchMsg(searchValues);
        Map<String, String> params = new HashMap<>();
        params.put(Constant.BEAN, searchValues);
        IntentUtil.startActivity(this, SearchResultActivity.class, params);
    }

    private final String SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY";
    private List<String> searchMsg;

    private List<String> getSearchMsg() {
        String strSearchHistory = (String) SharePreferenceTool.get(this, SEARCH_HISTORY_KEY, "");
        return JSON.parseArray(strSearchHistory, String.class);
    }

    private void saveSearchMsg(String key) {
        if (searchMsg != null) {
            if (searchMsg.size() >= 10)
                searchMsg.remove(9);
            for (String name : searchMsg) {
                if (name.equals(key)) {
                    //移动到列表头部
                    searchMsg.remove(name);
                    searchMsg.add(name);
                    return;
                }
            }
            searchMsg.add(0, key);
        } else {
            searchMsg = new ArrayList<>();
            searchMsg.add(key);
        }
        String sss = JSON.toJSONString(searchMsg);
        SharePreferenceTool.put(this, SEARCH_HISTORY_KEY, sss);
        adapter.setList(searchMsg);
    }
}
