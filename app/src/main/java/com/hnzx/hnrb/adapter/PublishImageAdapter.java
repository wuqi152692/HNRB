package com.hnzx.hnrb.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.PermissionCheckUtil;
import com.hnzx.hnrb.ui.live.PublishActivity;
import com.hnzx.hnrb.view.photopicker.PhotoPicker;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author: mingancai
 * @Time: 2017/5/19 0019.
 */

public class PublishImageAdapter extends BaseAdapter<String> {
    private final int IMAGE_TYPE = 0;
    private final int ADD_TYPE = 1;

    public PublishImageAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position == mList.size() - 1 ? ADD_TYPE : IMAGE_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (IMAGE_TYPE == viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.layout_publish_image_item, parent, false));
        }
        return new AddViewHolder(mInflater.inflate(R.layout.layout_publish_add_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == IMAGE_TYPE) {
            ViewHolder mHolder = (ViewHolder) holder;
            GlideTools.Glide(mContext, getItem(position), mHolder.image, R.drawable.bg_morentu_xiaotumoshi);
            mHolder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            if (position == 3)
                holder.itemView.setVisibility(View.GONE);
            else
                holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionCheckUtil.getInstance(mContext).checkPermission((Activity) mContext, new PermissionCheckUtil.CheckListener() {
                        @Override
                        public void isPermissionOn() {
                            // 选择图片
                            PhotoPicker.builder()
                                    .setPhotoCount(3 - position)
                                    .setShowCamera(true)
                                    .setShowGif(false)
                                    .setPreviewEnabled(false)
                                    .start((Activity) mContext, PhotoPicker.REQUEST_CODE);
                        }

                        @Override
                        public void isPermissionNo() {
                            ((BaseActivity) mContext).showTopToast("请开启权限", false);
                        }
                    }, PermissionCheckUtil.PERMISSION_SD_STORAGE);
                }
            });
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private View del;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            del = itemView.findViewById(R.id.del);
        }
    }

    private class AddViewHolder extends RecyclerView.ViewHolder {
        public AddViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
        }
    }
}
