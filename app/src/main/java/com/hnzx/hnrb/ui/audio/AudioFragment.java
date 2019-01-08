package com.hnzx.hnrb.ui.audio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseFragment;
import com.zhy.autolayout.utils.AutoUtils;

public class AudioFragment extends BaseFragment {

    private SlidingTabLayout mTabLayout;
    private ViewPager viewPager;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_auto, container, false);
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView.findViewById(R.id.title));

        mTabLayout = (SlidingTabLayout) contentView.findViewById(R.id.tabLayout);

        viewPager = (ViewPager) contentView.findViewById(R.id.viewPager);
    }

    @Override
    protected void initListeners() {
    }

    @Override
    protected void initDatas() {
        pagerAdapter adapter = new pagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        mTabLayout.setViewPager(viewPager);
        adapter.notifyDataSetChanged();
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    ImageFragment imageFragment = new ImageFragment();
                    return imageFragment;
                case 2:
                    RadioFragment radioFragment = new RadioFragment();
                    return radioFragment;
                default:
                    VedioFragment vedioFragment = new VedioFragment();
                    return vedioFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "视频";
                case 1:
                    return "图片";
                case 2:
                    return "音频";
                default:
                    return "视频";
            }
        }
    }
}
