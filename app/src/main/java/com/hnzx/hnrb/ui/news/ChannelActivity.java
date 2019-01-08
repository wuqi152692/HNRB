package com.hnzx.hnrb.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.ChannelAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.channel.helper.ItemDragHelperCallback;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道编辑
 */
public class ChannelActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerView;

    private ArrayList<GetTopCategoryRsp> list;

    public static Intent newIntent(Context con, ArrayList<GetTopCategoryRsp> list) {
        Intent intent = new Intent(con, ChannelActivity.class);
        intent.putParcelableArrayListExtra(Constant.BEAN, list);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        list = getIntent().getParcelableArrayListExtra(Constant.BEAN);
        if (list != null)
            list.remove(list.size() - 1);
        return R.layout.activity_channel;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        recyclerView.setLayoutManager(gridLayoutManager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);

        touchHelper.attachToRecyclerView(recyclerView);

        final ChannelAdapter channelAdapter = new ChannelAdapter(this, touchHelper, list);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                int viewType = channelAdapter.getItemViewType(position);

                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : 4;

            }
        });
        recyclerView.setAdapter(channelAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListeners() {
        findViewById(R.id.close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
        }
    }
}
