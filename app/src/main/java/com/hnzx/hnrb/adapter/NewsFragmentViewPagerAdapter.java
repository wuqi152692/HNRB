package com.hnzx.hnrb.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;
import com.hnzx.hnrb.ui.news.HomePagerFragment;
import com.hnzx.hnrb.ui.news.NewsListFragment;
import com.hnzx.hnrb.ui.news.NewsSubjectListFragment;
import com.hnzx.hnrb.ui.news.NewsSubscribeListFragment;
import com.hnzx.hnrb.ui.news.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class NewsFragmentViewPagerAdapter extends FragmentStatePagerAdapter implements RecyclerViewScrollListener {
    private List<GetTopCategoryRsp> rsps;
    private View topLayou;

    public NewsFragmentViewPagerAdapter(FragmentManager fm, View topLayout) {
        super(fm);
        this.topLayou = topLayout;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return rsps.get(position).catname;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(Constant.BEAN, this.rsps.get(position));
        switch (this.rsps.get(position).cat_id) {
            case "头条":
                HomePagerFragment homePagerFragment = new HomePagerFragment();
                homePagerFragment.setRecyclerViewScrollListener(this);
                homePagerFragment.setArguments(mBundle);

                return homePagerFragment;
            case "especial":
                NewsSubjectListFragment subjectListFragment = new NewsSubjectListFragment();
                subjectListFragment.setRecyclerViewScrollListener(this);
                subjectListFragment.setArguments(mBundle);

                return subjectListFragment;
            case "mine_order":
                NewsSubscribeListFragment subscribeListFragment = new NewsSubscribeListFragment();
                subscribeListFragment.setRecyclerViewScrollListener(this);
                subscribeListFragment.setArguments(mBundle);
                return subscribeListFragment;
            default:
                NewsListFragment newsListFragment = new NewsListFragment();
                newsListFragment.setRecyclerViewScrollListener(this);
                newsListFragment.setArguments(mBundle);

                return newsListFragment;
        }
    }

    @Override
    public int getCount() {
        return rsps == null ? 0 : rsps.size();
    }

    public void notifyData(List<GetTopCategoryRsp> rsps) {
        this.rsps = showList(rsps);
        notifyDataSetChanged();
    }

    private List<GetTopCategoryRsp> showList(List<GetTopCategoryRsp> rsps) {
        List<GetTopCategoryRsp> list = new ArrayList<>();
        for (GetTopCategoryRsp item : rsps) {
            if (item.isdefault == 1)
                list.add(item);
        }
        return list;
    }

    int totalY = 0;

    @Override
    public void upScroll(int dy) {
//        if (topLayou.getAlpha() == 0 && topLayou.getVisibility() == View.GONE)
//            return;
//        totalY += dy;
//        if (totalY <= 80)
//            topLayou.setAlpha((float) ((float) (80 - totalY) / 80.00));
//        else {
//            topLayou.setAlpha(0f);
//            topLayou.setVisibility(View.GONE);
//        }
        if (topLayou.getVisibility() == View.VISIBLE)
            srollHide();
    }

    @Override
    public void downScroll(int dy) {
//        if (totalY > 0) {
//            topLayou.setAlpha(1.0f);
//            topLayou.setVisibility(View.VISIBLE);
//            totalY = 0;
//        }
        if (topLayou.getVisibility() == View.GONE)
            scrollShow();
    }

    public void srollHide() {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                topLayou.setVisibility(View.GONE);
            }
        });
        animation.setDuration(300);
        topLayou.startAnimation(animation);
    }

    public void scrollShow() {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                topLayou.setVisibility(View.VISIBLE);
            }
        });
        animation.setDuration(300);
        topLayou.startAnimation(animation);
    }
}
