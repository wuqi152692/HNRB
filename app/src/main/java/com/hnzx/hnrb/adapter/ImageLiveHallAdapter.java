package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetLiveHallListRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.news.NewsScanBigImageActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author: mingancai
 * @Time: 2017/4/1 0001.
 */

public class ImageLiveHallAdapter extends BaseAdapter<GetLiveHallListRsp> {
    private static final int IMAGE_TYPE = 1, IMAGES_TYPE = 2, VEDIO_TYPE = 3;

    public ImageLiveHallAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        GetLiveHallListRsp rsp = getItem(position);
        return rsp.myvideo != null && rsp.myvideo.size() > 0 ? VEDIO_TYPE : ((rsp.imgs != null && rsp.imgs.size() > 1) ?
                IMAGES_TYPE :
                IMAGE_TYPE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        parent.removeAllViews();
        switch (viewType) {
            case IMAGE_TYPE:
                return new ImageViewHolder(mInflater.inflate(R.layout.layout_image_live_hall_image_item, parent, false));
            case IMAGES_TYPE:
                return new ImagesViewHolder(mInflater.inflate(R.layout.layout_image_live_hall_images_item, parent, false));
            case VEDIO_TYPE:
                return new VedioViewHolder(mInflater.inflate(R.layout.layout_image_live_hall_vedio_item, parent, false));
            default:
                return new ImageViewHolder(mInflater.inflate(R.layout.layout_image_live_hall_image_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GetLiveHallListRsp rsp = getItem(position);
        switch (getItemViewType(position)) {
            case IMAGE_TYPE:
                final ImageViewHolder iHolder = (ImageViewHolder) holder;
                if (rsp.imgs == null || rsp.imgs.size() < 1) {//无图
                    iHolder.imageLayout.setVisibility(View.GONE);
                } else {//单张图
                    iHolder.imageLayout.setVisibility(View.VISIBLE);
                    final String imageUrl = rsp.imgs.get(0);
                    GlideTools.Glide(mContext, imageUrl, iHolder.bigImg, R.drawable.bg_morentu_datumoshi);
                    if (imageUrl.toLowerCase().endsWith(".gif")) {
                        iHolder.gif.setVisibility(View.VISIBLE);
                        iHolder.gif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GlideTools.GlideGif(mContext, imageUrl, iHolder.bigImg, R.drawable.bg_morentu_datumoshi);
                            }
                        });
                    }
                    iHolder.bigImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            scanBigImage(rsp.imgs, 0);
                        }
                    });
                }
                GlideTools.Glide(mContext, rsp.avatar, iHolder.headImg, R.drawable.icon_default_round_head);
                iHolder.name.setText(rsp.username);
                iHolder.time.setText(rsp.created);
                iHolder.msg.setText(Html.fromHtml(rsp.content));
                break;
            case IMAGES_TYPE:
                ImagesViewHolder isHolder = (ImagesViewHolder) holder;
                for (int i = 0; i < rsp.imgs.size(); i++) {
                    final int imagePosition = i;
                    if (i == 0) {
                        GlideTools.Glide(mContext, rsp.imgs.get(i), isHolder.image0, R.drawable.bg_morentu_xiaotumoshi);
                        isHolder.image0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scanBigImage(rsp.imgs, imagePosition);
                            }
                        });
                    } else if (i == 1) {
                        GlideTools.Glide(mContext, rsp.imgs.get(i), isHolder.image1, R.drawable.bg_morentu_xiaotumoshi);
                        isHolder.image1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scanBigImage(rsp.imgs, imagePosition);
                            }
                        });
                    } else if (i == 2) {
                        isHolder.image2.setVisibility(View.VISIBLE);
                        GlideTools.Glide(mContext, rsp.imgs.get(i), isHolder.image2, R.drawable.bg_morentu_xiaotumoshi);
                        isHolder.image2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scanBigImage(rsp.imgs, imagePosition);
                            }
                        });
                    } else if (i == 3) {
                        isHolder.image3.setVisibility(View.VISIBLE);
                        GlideTools.Glide(mContext, rsp.imgs.get(i), isHolder.image3, R.drawable.bg_morentu_xiaotumoshi);
                        isHolder.image3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scanBigImage(rsp.imgs, imagePosition);
                            }
                        });
                    }
                }
                GlideTools.Glide(mContext, rsp.avatar, isHolder.headImg, R.drawable.icon_default_round_head);
                isHolder.name.setText(rsp.username);
                isHolder.time.setText(rsp.created);
                isHolder.msg.setText(Html.fromHtml(rsp.content));
                break;
            case VEDIO_TYPE:
                VedioViewHolder vHolder = (VedioViewHolder) holder;
                GlideTools.Glide(mContext, rsp.avatar, vHolder.headImg, R.drawable.icon_default_round_head);
                vHolder.name.setText(rsp.username);
                vHolder.time.setText(rsp.created);
                vHolder.msg.setText(Html.fromHtml(rsp.content));
                vHolder.player.setUp(rsp.myvideo.get(0).filepath, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
                GlideTools.Glide(mContext, rsp.myvideo.get(0).filethumb, vHolder.player.thumbImageView, R.drawable.bg_morentu_datumoshi);
                break;
        }
    }

    protected class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView headImg, bigImg;
        private TextView name, time, msg;
        private View gif, imageLayout;

        public ImageViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            headImg = (ImageView) itemView.findViewById(R.id.headImg);
            bigImg = (ImageView) itemView.findViewById(R.id.bigImg);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            msg = (TextView) itemView.findViewById(R.id.msg);
            gif = itemView.findViewById(R.id.gif);
            imageLayout = itemView.findViewById(R.id.imageLayout);
        }
    }

    protected class ImagesViewHolder extends RecyclerView.ViewHolder {
        private ImageView headImg, image0, image1, image2, image3;
        private TextView name, time, msg;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            headImg = (ImageView) itemView.findViewById(R.id.headImg);
            image0 = (ImageView) itemView.findViewById(R.id.image0);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            msg = (TextView) itemView.findViewById(R.id.msg);
        }
    }

    protected class VedioViewHolder extends RecyclerView.ViewHolder {
        private ImageView headImg;
        private TextView name, time, msg;
        private JCVideoPlayerStandard player;

        public VedioViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            headImg = (ImageView) itemView.findViewById(R.id.headImg);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            msg = (TextView) itemView.findViewById(R.id.msg);
            player = (JCVideoPlayerStandard) itemView.findViewById(R.id.player);
        }
    }

    private void scanBigImage(ArrayList<String> imgSrc, int position) {
        Intent intent = new Intent(mContext, NewsScanBigImageActivity.class);
        intent.putStringArrayListExtra(Constant.BEAN, imgSrc);
        intent.putExtra(NewsScanBigImageActivity.SHOW_POSITION, position);
        mContext.startActivity(intent);
    }
}
