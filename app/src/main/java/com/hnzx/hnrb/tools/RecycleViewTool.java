package com.hnzx.hnrb.tools;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.hnzx.hnrb.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * @author: mingancai
 * @Time: 2017/4/1 0001.
 */

public class RecycleViewTool {

    /**
     * return setting recyclerview to vertical list
     *
     * @param view
     */
    public static void getVerticalRecyclerView(XRecyclerView view, Context context) {
        getVerticalRecyclerView(view, context, true);
    }

    public static void getVerticalRecyclerView(XRecyclerView recyclerView, Context context, boolean isAddDeliverLine) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayout.VERTICAL, false));
        if (isAddDeliverLine)
            recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
    }


    /**
     * return setting recyclerview to grid
     *
     * @param view
     */
    public static XRecyclerView getGridRecyclerView(XRecyclerView view, Context context, int spanCount) {
        view.setLayoutManager(new GridLayoutManager(context, spanCount));
//        view.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));
        return view;
    }
}
