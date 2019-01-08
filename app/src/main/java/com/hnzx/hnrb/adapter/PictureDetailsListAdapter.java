package com.hnzx.hnrb.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hnzx.hnrb.responsebean.GetNewsDetalisRsp;
import com.hnzx.hnrb.tools.GlideTools;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author: mingancai
 * @Time: 2017/4/18 0018.
 */

public class PictureDetailsListAdapter extends PagerAdapter {

    List<GetNewsDetalisRsp.ZutuBean> pictureLisDetailt = new ArrayList<>();

    PhotoViewAttacher.OnPhotoTapListener listener;


    public PictureDetailsListAdapter(PhotoViewAttacher.OnPhotoTapListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return pictureLisDetailt.size();
    }

    public GetNewsDetalisRsp.ZutuBean getitem(int position) {

        return pictureLisDetailt.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        PhotoView photoView = new PhotoView(container.getContext());

        photoView.setOnPhotoTapListener(listener);

        GlideTools.GlideGif(container.getContext(), pictureLisDetailt.get(position).img, photoView, -1);

        container.addView(photoView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    public void addPictureData(List<GetNewsDetalisRsp.ZutuBean> pictureLisDetailt) {

        this.pictureLisDetailt = pictureLisDetailt;

        notifyDataSetChanged();

    }
}
