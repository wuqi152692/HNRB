package com.hnzx.hnrb.adapter;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.hnzx.hnrb.tools.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/1 0001.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mInflater;

    public BaseAdapter(Context context) {
        super();
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 计算RecycleView 数据内容差异化改变，如使用此方法，则需要重写：
     * {@link #isItemDataModify()}
     * {@link #isSameItemData()}
     *
     * @param newList
     */
    public void notificationDataUpdate(List<T> newList) {
        DiffUtil.DiffResult mDiffResult = DiffUtil.calculateDiff(new DiffCallBack<T>(mList, newList) {
            @Override
            protected boolean isSameData(int oldItemPosition, int newItemPosition) {
                return isSameItemData();
            }

            @Override
            protected boolean isDataModify(int oldItemPosition, int newItemPosition) {
                return isItemDataModify();
            }
        });

        mDiffResult.dispatchUpdatesTo(this);
        mList = newList;
    }

    /**
     * item数据是否为同一个索引
     * 如使用了{@link #notificationDataUpdate(List)} 则需要重写
     *
     * @return
     */
    protected boolean isSameItemData() {
        return false;
    }

    /**
     * item数据是否发生了改变
     * 如使用了{@link #notificationDataUpdate(List)} 则需要重写
     *
     * @return
     */
    protected boolean isItemDataModify() {
        return false;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public T getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    /**
     * 更换list
     */
    public void setList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setList(List<T> list, boolean isDiff) {
        if (null == list) return;
        if (isDiff) {
            notificationDataUpdate(list);
        } else {
            setList(list);
        }
    }

    public void addAll(List<T> list) {
        if (null == mList)
            mList = list;
        else
            mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list, boolean isDiff) {
        if (null == list || list.size() == 0) return;
        List<T> mListNew = new ArrayList<>(mList);
        if (isDiff) {
            mListNew.addAll(list);
            notificationDataUpdate(mListNew);
        } else {
            addAll(list);
        }
    }

    public List<T> getList() {
        return mList;
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
            notifyDataSetChanged();
        }
    }

    public String formatTime(String created) {
        try {
            long dif = System.currentTimeMillis() - DateUtils.stringToDate(created, DateUtils.patternLong).getTime();
            if (dif < 60 * 1000) {
                return dif / 1000 + "秒前";
            } else if (dif < 60 * 60 * 1000) {
                return dif / (60 * 1000) + "分钟前";
            } else if (dif < 24 * 60 * 60 * 1000) {
                return dif / (60 * 60 * 1000) + "小时前";
            } else {
                return created;
            }
        } catch (NullPointerException e) {
            return created;
        }
    }

    public String formatViews(int views) {
        if (views > 10000) {
            float temp = (float) (Math.round(views / 10000.0 * 10) / 10.0);
            return (temp > (int) temp ? temp : (int) temp) + "万";
        } else return String.valueOf(views);
    }
}
