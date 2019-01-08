package com.hnzx.hnrb.ui.dialog;

import android.content.ActivityNotFoundException;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.HomePagerBannerAdapter;
import com.hnzx.hnrb.base.BaseDialogFragment;
import com.hnzx.hnrb.responsebean.GetAdsRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.ADSelect;
import com.hnzx.hnrb.ui.news.NewsListFragment;
import com.hnzx.hnrb.ui.web.WebActivity;
import com.hnzx.hnrb.view.AutoScrollViewPager;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class ADPopupDialog extends BaseDialogFragment {
    private View update, close;
    private TextView bannerTitle, bannerIndex, bannerTotal;
    private AutoScrollViewPager viewPager;
    private static List<GetAdsRsp> _data;

    private HomePagerBannerAdapter bannerAdapter;

    private int position = 0;

    private autoPagerChangeListener autoPC = new autoPagerChangeListener();

    public static ADPopupDialog newInstance(List<GetAdsRsp> data) {
        ADPopupDialog dialog = new ADPopupDialog();
        _data = data;
        return dialog;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_ads_popup;
    }

    @Override
    protected void initViews(View contentView) {
        AutoUtils.auto(contentView);
        update = contentView.findViewById(R.id.update);
        close = contentView.findViewById(R.id.close);

        bannerTitle = (TextView) contentView.findViewById(R.id.bannerTitle);
        bannerIndex = (TextView) contentView.findViewById(R.id.bannerIndex);
        bannerTotal = (TextView) contentView.findViewById(R.id.bannerTotal);

        viewPager = (AutoScrollViewPager) contentView.findViewById(R.id.viewPager);

        bannerAdapter = new HomePagerBannerAdapter();
        viewPager.setAdapter(bannerAdapter);
    }

    @Override
    protected void initListeners() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dismiss();
                    if (_data.get(position % _data.size()) != null)
                        ADSelect.goWhere(mActivity, _data.get(position), false);
                } catch (Exception exception) {
                    showToast("链接有误");
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initDatas() {
        bannerTitle.setText(_data.get(viewPager.getCurrentItem() % _data.size()).title);
        bannerIndex.setText(String.valueOf(viewPager.getCurrentItem() % _data.size() + 1));
        bannerTotal.setText(String.valueOf(_data.size()));
        List<View> views = new ArrayList<>();
        for (final GetAdsRsp focu : _data) {
            ImageView iv = new ImageView(mActivity);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideTools.Glide(mActivity, focu.thumb, iv, R.drawable.bg_morentu_datumoshi);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (focu != null)
                        ADSelect.goWhere(mActivity, focu, false);
                }
            });
            views.add(iv);
        }
        bannerAdapter.notifyDataSetChanged(views);
        viewPager.setVisibility(View.VISIBLE);
        if (views.size() > 1) {
            viewPager.setInterval(4 * 1000);
            viewPager.startAutoScroll(3 * 1000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager == null)
            return;
        viewPager.removeOnPageChangeListener(autoPC);
        viewPager.setOnPageChangeListener(autoPC);
        if (viewPager != null && viewPager.getChildCount() > 0) {
            viewPager.setInterval(4 * 1000);
            viewPager.startAutoScroll(3 * 1000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (viewPager == null)
            return;
        viewPager.removeOnPageChangeListener(autoPC);
        viewPager.stopAutoScroll();
    }

    class autoPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            bannerTitle.setText(_data.get(viewPager.getCurrentItem() % _data.size()).title);
            bannerIndex.setText(String.valueOf(viewPager.getCurrentItem() % _data.size() + 1));
        }
    }
}
