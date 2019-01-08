package com.hnzx.hnrb.ui.government.square;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.GovernmentListAdapter;
import com.hnzx.hnrb.adapter.ServiceCenterAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetPoliticsListReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsListRsp;
import com.hnzx.hnrb.tools.RecycleViewTool;
import com.hnzx.hnrb.ui.leftsidebar.LeftMenuFragment;
import com.hnzx.hnrb.ui.me.LoginFragment;
import com.hnzx.hnrb.ui.news.HighDynamicFragment;
import com.hnzx.hnrb.ui.news.NewsFragment;
import com.hnzx.hnrb.view.MultiStateView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Method;
import java.util.List;

public class GovernmentListActivity extends BaseActivity implements View.OnClickListener {
    private SlidingTabLayout mTabLayout;
    private ViewPager viewPager;

    private List<GetPoliticsListRsp> rsp;

    private int position = -1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UnitDetailsActivity.RESULT_CODE) {
            initData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_government_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);

        ((TextView) findViewById(R.id.title)).setText("机构列表");
        AutoUtils.auto(findViewById(R.id.title));
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
    }

    @Override
    protected void initData() {
        GetPoliticsListReq req = new GetPoliticsListReq();

        App.getInstance().requestJsonArrayDataGet(req, new dataListener(), null);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GovernmentListActivity.this.position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(UnitDetailsActivity.RESULT_CODE);
        super.onBackPressed();
    }

    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetPoliticsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetPoliticsListRsp> response) {
            if (response != null && response.Status == 1) {
                rsp = response.Info;

                viewPager.setAdapter(new pagerAdapter((getSupportFragmentManager())));

                mTabLayout.setViewPager(viewPager);
                if (position != -1 && position != 0)
                    viewPager.setCurrentItem(position);
            }
        }
    }

    private class pagerAdapter extends FragmentPagerAdapter {
        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return rsp == null ? 0 : rsp.size();
        }

        @Override
        public Fragment getItem(int position) {
            GovernmentListFragment fragment = new GovernmentListFragment();
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    bundle.putSerializable(Constant.BEAN, rsp.get(0));
                    break;
                case 1:
                    bundle.putSerializable(Constant.BEAN, rsp.get(1));
                    break;
                case 2:
                    bundle.putSerializable(Constant.BEAN, rsp.get(2));
                    break;
                case 3:
                    bundle.putSerializable(Constant.BEAN, rsp.get(3));
                    break;
            }
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            if (position != -1)
                removeFragment(container, position);
            return super.instantiateItem(container, position);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return rsp.get(position).name;
        }
    }

    private void removeFragment(ViewGroup container, int index) {
        FragmentManager fm = getSupportFragmentManager();
        String tag = getFragmentTag(container.getId(), index);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null)
            return;
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
        ft = null;
        fm.executePendingTransactions();
    }

    private String getFragmentTag(int viewId, int index) {
        try {
            Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
            Class<?>[] parameterTypes = {int.class, long.class};
            Method method = cls.getDeclaredMethod("makeFragmentName",
                    parameterTypes);
            method.setAccessible(true);
            String tag = (String) method.invoke(this, viewId, index);
            return tag;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
