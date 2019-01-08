package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.UserScanAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.MyViewsReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.MyViewsRsp;
import com.hnzx.hnrb.view.SlideOnePageGallery;

import java.util.ArrayList;
import java.util.Calendar;

public class UserScanActivity extends BaseActivity implements View.OnClickListener {
    private TextView yearTv, monthTv, viewsNum, commentsNum, supportsNum;
    private SlideOnePageGallery slideGallery;
    private UserScanAdapter adapter;
    private int month, year;
    private int time;
    ArrayList<Integer> data = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_scan;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("我的阅历");

        yearTv = (TextView) findViewById(R.id.year);
        monthTv = (TextView) findViewById(R.id.month);
        viewsNum = (TextView) findViewById(R.id.viewsNum);
        commentsNum = (TextView) findViewById(R.id.commentNum);
        supportsNum = (TextView) findViewById(R.id.supportNum);

        slideGallery = (SlideOnePageGallery) findViewById(R.id.slideGallery);

        Calendar cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
        time = year * 100 + month;

        slideGallery.setSelection(month - 1);

        yearTv.setText(year + "年");
        monthTv.setText(month + "月");
    }

    @Override
    protected void initData() {
        for (int i = 1; i < 13; i++)
            data.add(i);

        adapter = new UserScanAdapter(this);

        slideGallery.setAdapter(adapter);

        adapter.addData(data, 0);

        slideGallery.setSelection(month - 1);

        getData(time);
    }

    private void getData(int time) {
        final MyViewsReq req = new MyViewsReq();
        req.time = time;

        App.getInstance().requestJsonDataGet(req, new Response.Listener<BaseBeanRsp<MyViewsRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<MyViewsRsp> response) {
                if (response != null && response.Status == 1 && response.Info != null) {
                    if (response.Info.comments == 0 && response.Info.views == 0 && response.Info.support == 0) {
                        showToast("您这个月份尚未留下足迹");
                        commentsNum.setText("0条");
                        supportsNum.setText("0条");
                        viewsNum.setText("0次");
                    } else {
                        commentsNum.setText(String.valueOf(response.Info.comments) + "条");
                        supportsNum.setText(String.valueOf(response.Info.support) + "条");
                        viewsNum.setText(String.valueOf(response.Info.views) + "次");
                    }
                } else
                    showToast("您这个月份尚未留下足迹");
            }
        }, null);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        slideGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.addData(data, position);
                month = position + 1;
                int time = year * 100 + (position + 1);
                yearTv.setText(year + "年");
                monthTv.setText((position + 1) + "月");
                getData(time);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
