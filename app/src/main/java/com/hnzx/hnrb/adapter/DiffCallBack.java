package com.hnzx.hnrb.adapter;

import android.support.v7.util.DiffUtil;

import com.hnzx.hnrb.ui.news.NewsDetailsActivity;

import java.util.List;

/**
 * Created by FoAng on 17/4/11 上午10:44;
 */

public abstract class DiffCallBack<T> extends DiffUtil.Callback {

    protected List<T> mOldList, mNewList;

    public DiffCallBack(List<T> mOldList, List<T> mNewList) {
        this.mNewList = mNewList;
        this.mOldList = mOldList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return isSameData(oldItemPosition, newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return isDataModify(oldItemPosition, newItemPosition);
    }

    /**
     * 是否是同一个数据对象
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    protected abstract boolean isSameData(int oldItemPosition, int newItemPosition);

    /**
     * 数据对象内容是否发生了改变
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    protected abstract boolean isDataModify(int oldItemPosition, int newItemPosition);
}
