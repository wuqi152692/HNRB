package com.hnzx.hnrb.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetAllCategoryRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.ui.leftsidebar.CategoryDetailsFragment;
import com.hnzx.hnrb.ui.live.ImageLiveHallFragment;
import com.hnzx.hnrb.ui.news.HomePagerFragment;
import com.hnzx.hnrb.ui.news.NewsListFragment;
import com.hnzx.hnrb.ui.news.NewsSubjectListFragment;

import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class ColumnViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<GetAllCategoryRsp> rsps;

    public ColumnViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return rsps.get(position).name;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Constant.BEAN, this.rsps.get(position));
        switch (this.rsps.get(position).catid) {
            default:
                CategoryDetailsFragment fragment = new CategoryDetailsFragment();
                fragment.setArguments(mBundle);
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return rsps == null ? 0 : rsps.size();
    }

    public void notifyData(List<GetAllCategoryRsp> rsps) {
        this.rsps = rsps;
        notifyDataSetChanged();
    }
}
