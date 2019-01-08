package com.hnzx.hnrb.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.ui.leftsidebar.CategoryDetailsFragment;
import com.hnzx.hnrb.ui.live.ImageLiveCommentFragment;
import com.hnzx.hnrb.ui.live.ImageLiveHallFragment;
import com.hnzx.hnrb.view.CustomViewpager;

import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class ImageLiveViewPagerAdapter extends FragmentStatePagerAdapter {
    private String rsps;
    private CustomViewpager vp;

    public ImageLiveViewPagerAdapter(FragmentManager fm, String rsps, CustomViewpager vp) {
        super(fm);
        this.rsps = rsps;
        this.vp = vp;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle mBundle = new Bundle();
        mBundle.putString(Constant.BEAN, rsps);
        switch (position) {
            case 1:
                ImageLiveCommentFragment cFragment = ImageLiveCommentFragment.newInstance(vp);
                cFragment.setArguments(mBundle);
                return cFragment;
            default:
                ImageLiveHallFragment fragment = ImageLiveHallFragment.newInstance(vp);
                fragment.setArguments(mBundle);
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
