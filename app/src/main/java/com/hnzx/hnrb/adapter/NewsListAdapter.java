package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.ui.NewsSelect;
import com.hnzx.hnrb.ui.leftsidebar.ColumnActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class NewsListAdapter extends BaseAdapter<GetFeaturedNewsListRsp> {
    private int itemType = 1;
    private boolean isHideChannelName;
    private boolean isFromRuZhu;

    private NewsListScrollItemAdapter newsListScrollItemAdapter;


    public NewsListAdapter(Context context, boolean isHideChannelName) {
        super(context);
        this.isHideChannelName = isHideChannelName;
    }

    public NewsListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {


        if (getItem(position).title.startsWith("王国生")){
            return 3;
        }
        return itemType;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("itemType", "viewType: "+viewType);

        if (viewType == 1) {
            View mBingImageItem = mInflater.inflate(R.layout.layout_news_big_image_item, parent, false);
            AutoUtils.auto(mBingImageItem);
            return new ViewHolder(mBingImageItem);

        } else if (viewType == 3){

            // 第四个item 设置成可以滑动的recycleView
            View mScrollImageItem = mInflater.inflate(R.layout.scrollrecycle,parent,false);
            AutoUtils.auto(mScrollImageItem);
            return new ScrollViewHolder(mScrollImageItem);

        } else {
            View mSmallImageItem = mInflater.inflate(R.layout.layout_news_small_image_item, parent, false);
            AutoUtils.auto(mSmallImageItem);
            return new ViewHolder(mSmallImageItem);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("relation_s", "onBindViewHolder: ");

        final GetFeaturedNewsListRsp relation = getItem(position);

        if (holder instanceof ScrollViewHolder){
            ScrollViewHolder sHolder = (ScrollViewHolder) holder;


            Log.d("relations", "------------ ");


            /// 初始化嵌套的recycle
            /////////////////////////////////////////////////

            newsListScrollItemAdapter = new NewsListScrollItemAdapter(mContext);
            sHolder.mRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
            sHolder.mRecycle.setAdapter(newsListScrollItemAdapter);


            /////////////////////////////////////////////////


        } else {


            ViewHolder mHolder = (ViewHolder) holder;



            Log.d("relations", "" + relation);
            Log.d("relations", "" + relation.content_id);

            GlideTools.GlideRounded(mContext, relation.thumb, mHolder.image, itemType == 1 ?
                    R.drawable.bg_morentu_datumoshi : R.drawable.bg_morentu_xiaotumoshi, 8);
            mHolder.title.setText(relation.title);
            if (isHideChannelName || TextUtils.isEmpty(relation.topname)) {
                mHolder.type.setText(Html.fromHtml("<font color='#4990e2'>" + relation.catname + "</font>"));
            } else {
                if (relation.topname != null && relation.topname.length() > 0) {
                    if (relation.catname != null && relation.catname.length() > 0)
                        mHolder.type.setText(Html.fromHtml(relation.topname + " | <font color='#4990e2'>" + relation.catname + "</font>"));
                    else
                        mHolder.type.setText(relation.topname);
                }
            }
            mHolder.time.setText(relation.created);
            mHolder.look.setText(formatViews(relation.views));
            if (itemType != 1) {
                if (relation.content_type.equals("content_zutu")) {
                    mHolder.news_image_type.setVisibility(View.VISIBLE);
                    mHolder.image_num.setVisibility(View.VISIBLE);
                    mHolder.image_num.setText(relation.zutu_total);
                } else if (relation.content_type.equals("content_video")) {
                    mHolder.news_video_time.setVisibility(View.VISIBLE);
                    mHolder.news_video_type.setVisibility(View.VISIBLE);
                    mHolder.news_video_time.setText(relation.video_duration);
                }
            }


            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFromRuZhu)
                        NewsSelect.goWhere(mContext, relation.content_id, relation.is_link, relation.link_url, relation.internal_type,
                                relation.internal_id, relation.content_type, relation.thumb, true);
                    else {
                        Log.d("relations", "onClick:  ---------------    "+relation.content_id);
                        NewsSelect.goWhere(mContext, relation.content_id, relation.is_link, relation.link_url, relation.internal_type,
                                relation.internal_id, relation.content_type, relation.thumb);
                    }
                }
            });
//        mHolder.type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(ColumnActivity.newIntence(mContext, relation.cat_id));
//            }
//        });


        }


    }

    public void setFromRuZhu(boolean b) {
        this.isFromRuZhu = b;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image, news_image_type, news_video_type;
        private TextView title, type, time, look, image_num, news_video_time;

        public ViewHolder(View itemView) {
            super(itemView);

            Log.d("relation_s", "ViewHolder: ");

            title = (TextView) itemView.findViewById(R.id.title);
            type = (TextView) itemView.findViewById(R.id.type);
            time = (TextView) itemView.findViewById(R.id.time);
            look = (TextView) itemView.findViewById(R.id.look);
            image = (ImageView) itemView.findViewById(R.id.image);
            if (itemType != 1) {
                news_image_type = (ImageView) itemView.findViewById(R.id.news_image_type);
                news_video_type = (ImageView) itemView.findViewById(R.id.news_video_type);
                image_num = (TextView) itemView.findViewById(R.id.image_num);
                news_video_time = (TextView) itemView.findViewById(R.id.news_video_time);
            }


        }
    }







    /**
     * 更新显示样式
     *
     * @param itemType
     */
    public void notifyItemStyle(int itemType) {
        this.itemType = itemType;
        notifyDataSetChanged();
    }


    /**
     *
     *
     *
     * recycleView
     *
     *
     * */

    private class ScrollViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView mRecycle;

        public ScrollViewHolder(View itemView) {
            super(itemView);

            mRecycle = itemView.findViewById(R.id.scrollRecycle);
        }
    }
}
