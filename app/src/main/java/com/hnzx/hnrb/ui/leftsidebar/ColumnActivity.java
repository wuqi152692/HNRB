package com.hnzx.hnrb.ui.leftsidebar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.ColumnViewPagerAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.db.Dao.GetAllCategoryRspDao;
import com.hnzx.hnrb.responsebean.GetAllCategoryRsp;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.view.MultiStateView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

public class ColumnActivity extends BaseActivity implements View.OnClickListener {
    private View topLayout;
    private TextView title;
    private SlidingTabLayout tabLayout;
    private MultiStateView stateView;
    private ViewPager viewPager;
    private ColumnViewPagerAdapter adapter;
    private GetAllCategoryRspDao dao;
    private List<GetAllCategoryRsp> datas;
    private String catid;

    public static Intent newIntence(Context con, String catid) {
        Intent intent = new Intent(con, ColumnActivity.class);
        intent.putExtra(Constant.BEAN, catid);
        return intent;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean supportSlideBack() {
        //嵌套viewpager 需要禁止侧滑
        return false;
    }

    @Override
    protected int getLayoutId() {
        catid = getIntent().getStringExtra(Constant.BEAN);
        dao = new GetAllCategoryRspDao(this);
        datas = dao.queryAll();
        return R.layout.activity_column;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        title = (TextView) findViewById(R.id.title);
        AutoUtils.auto(title);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);

        tabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);

        topLayout = findViewById(R.id.topLayout);
        AutoUtils.auto(topLayout);

        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        adapter = new ColumnViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        if (datas != null) {
            for (int i = 0; i < datas.size(); i++) {
                GetAllCategoryRsp data = datas.get(i);
                if (data.catid.equals(catid)) {
                    title.setText(data.name);
                    stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    adapter.notifyData(datas);
                    tabLayout.setViewPager(viewPager);
                    tabLayout.setCurrentTab(i);
                    return;
                }
            }
        } else
            stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    protected void initListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(datas.get(position).name);
                ShowOrHide(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                IntentUtil.startActivityForResult(this, ColumnListActivity.class, 0x01);
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

    public void ShowOrHide(boolean show) {
        topLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理栏目返回
        if (resultCode == 0x01) {
            String cat_id = data.getStringExtra(Constant.BEAN);
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).catid.equals(cat_id)) {
                    tabLayout.setCurrentTab(i);
                    return;
                }
            }
        }
    }

    public void updateData(GetAllCategoryRsp rsp) {
        dao.update(rsp);
    }

    public GetAllCategoryRsp queryByCatid(GetAllCategoryRsp rsp) {
        List<GetAllCategoryRsp> data = dao.queryByCatid("catid", rsp.catid);
        if (data != null && data.size() > 1) {
            for (GetAllCategoryRsp rsps : data) {
                System.err.println(rsps.toString());
            }
            return data.get(data.size() - 1);
        } else return null;
    }
}
