package com.hnzx.hnrb.ui.government;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseFragment;
import com.hnzx.hnrb.ui.audio.ImageFragment;
import com.hnzx.hnrb.ui.audio.RadioFragment;
import com.hnzx.hnrb.ui.audio.VedioFragment;
import com.hnzx.hnrb.ui.government.governmentbox.GovernmentBoxFragment;
import com.hnzx.hnrb.ui.government.servicelife.ServiceCenterFragment;
import com.hnzx.hnrb.ui.government.square.SquareFragment;

public class GovernmentFragment extends BaseFragment {
    private SlidingTabLayout mTabLayout;
    private ViewPager viewPager;

    @Override
    protected View getContentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_government, container, false);
    }

    @Override
    protected void initViews(View view) {
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
                    SquareFragment squareFragment = new SquareFragment();
                    return squareFragment;
                case 2:
                    ServiceCenterFragment serviceFragment = new ServiceCenterFragment();
                    return serviceFragment;
                default:
                    GovernmentBoxFragment boxFragment = new GovernmentBoxFragment();
                    return boxFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "政库";
                case 1:
                    return "政务广场";
                case 2:
                    return "生活服务";
                default:
                    return "政库";
            }
        }
    }
}
