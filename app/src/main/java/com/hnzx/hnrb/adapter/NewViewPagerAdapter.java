package com.hnzx.hnrb.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hnzx.hnrb.ui.news.HomePagerFragment;

/**
 * @author: mingancai
 * @Time: 2017/3/15 0015.
 */

public class NewViewPagerAdapter extends FragmentStatePagerAdapter {
    public NewViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public Fragment getItem(int position) {

        // mobilecolumnid == 6是自媒体 ，17是图集，97是直播，其他都是新闻列表
        Bundle mBundle = new Bundle();

//        mBundle.putSerializable(BaseConstant.BEAN, this.rsps.get(position));

        switch (0) {
            case 97:
//                LiveListFragment liveListFragment=new LiveListFragment();
//
//                liveListFragment.setArguments(mBundle);
//
//                return  liveListFragment;
//            //图集
//            case 84:
//                NewsPictureListFragment pictureListFragment = new NewsPictureListFragment();
//
//                pictureListFragment.setArguments(mBundle);
//
//                return pictureListFragment;
////                //视频
//            case 98:
//                NewsVideoListFragment videoListFragment = new NewsVideoListFragment();
//
//                videoListFragment.setArguments(mBundle);
//
//                return videoListFragment;
                //普通新闻列表
            default:
                HomePagerFragment newsListFragment = new HomePagerFragment();

                newsListFragment.setArguments(mBundle);

                return newsListFragment;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    public void notifyData() {

        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
