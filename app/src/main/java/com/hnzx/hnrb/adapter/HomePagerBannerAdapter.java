package com.hnzx.hnrb.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class HomePagerBannerAdapter extends PagerAdapter {
    private List<View> views = new ArrayList<>();

    /**
     * 从ViewPager中删除集合中对应索引的View对象
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (views.size() > 2) {
            container.removeView(views.get(position % views.size()));
        }
    }

    @Override
    public int getCount() {
        return views.size() > 1 ? Integer.MAX_VALUE : views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (views.get(position % views.size()).getParent() != null) {
            ((ViewPager) views.get(position % views.size()).getParent()).removeView(views.get(position % views.size()));
        }
        container.addView(views.get(position % views.size()), 0);
        return views.get(position % views.size());
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    public void notifyDataSetChanged(List<View> views) {
        this.views.removeAll(this.views);
        this.views.addAll(views);
        super.notifyDataSetChanged();
    }
}
