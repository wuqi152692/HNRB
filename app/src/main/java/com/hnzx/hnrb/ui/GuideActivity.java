package com.hnzx.hnrb.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.tools.LogUtil;
import com.hnzx.hnrb.tools.PackageUtil;
import com.hnzx.hnrb.tools.SharePreferenceTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private final String VERSION_NAME = "VERSIONNAME";
    private static final int CLICK_VIEW_ID = 0x01 << 2;
    private static int[] GUIDE = new int[]{R.drawable.icon_guide_01, R.drawable.icon_guide_02, R.drawable.icon_guide_03};
    private ViewPager mViewPager;
    private GuideAdapter mGuideAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mViewPager = getViewById(R.id.guide_viewPager);
    }

    @Override
    public boolean supportSlideBack() {
        return false;
    }

    @Override
    protected void initData() {
        List<ImageView> mImageViews = new ArrayList<>();
        for (int Loop = 0; Loop < GUIDE.length; Loop++) {
            ImageView mImageView = new ImageView(this);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mImageView.setLayoutParams(mLayoutParams);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageView.setImageDrawable(getResources().getDrawable(GUIDE[Loop]));
            if (Loop == GUIDE.length - 1) mImageView.setId(CLICK_VIEW_ID);
            mImageView.setOnClickListener(this);
            mImageViews.add(mImageView);
        }
        mGuideAdapter = new GuideAdapter(mImageViews);
        mViewPager.setAdapter(mGuideAdapter);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
            case CLICK_VIEW_ID:
                finish();
                SharePreferenceTool.put(GuideActivity.this, VERSION_NAME, PackageUtil.getVersionName(GuideActivity.this));
                startActivity(MainActivity.newIntent(GuideActivity.this, false));
                break;
        }
    }

    private class GuideAdapter extends PagerAdapter {

        private List<ImageView> mImageViewList;

        public GuideAdapter(List<ImageView> mImageViewList) {
            this.mImageViewList = mImageViewList;
        }

        @Override
        public int getCount() {
            return mImageViewList == null ? 0 : mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                container.removeView(mImageViewList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
                super.destroyItem(container, position, object);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }
    }
}
