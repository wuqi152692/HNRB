package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.SignlogAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.GetUserSignHistoryReq;
import com.hnzx.hnrb.requestbean.UCenterSignReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetUserSignHistoryRsp;
import com.hnzx.hnrb.tools.LogUtil;

import java.util.List;

public class UserSignActivity extends BaseActivity implements View.OnClickListener {
    private TextView year, month;
    private GridView nsGridView;
    private SignlogAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_sign;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("签到");
        year = (TextView) findViewById(R.id.year);
        month = (TextView) findViewById(R.id.month);

        nsGridView = (GridView) findViewById(R.id.gridView);

        adapter = new SignlogAdapter();

        nsGridView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        App.getInstance().requestJsonDataGet(new GetUserSignHistoryReq(), new Response.Listener<BaseBeanRsp<GetUserSignHistoryRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<GetUserSignHistoryRsp> response) {
                if (response != null && response.Status == 1) {
                    GetUserSignHistoryRsp.ListBean dayNull = new GetUserSignHistoryRsp.ListBean();
                    dayNull.day_no = 0;
                    List<GetUserSignHistoryRsp.ListBean> list = response.Info.list;
                    if (list != null && list.get(0).week_no != 0) {
                        int position = list.get(0).week_no;
                        for (int i = 0; i < position; i++) {
                            list.add(i, dayNull);
                        }
                    }
                    if (list != null && list.size() % 7 > 0) {
                        int position = 7 - list.size() % 7;
                        for (int i = 0; i < position; i++) {
                            list.add(dayNull);
                        }
                    }
                    adapter.addAll(response.Info.list);
                    String date = response.Info.date;
                    year.setText((date.split("年")[0]) + "年");
                    month.setText(date.split("年")[1]);
                }
            }
        }, null);
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.sign).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign:
                App.getInstance().requestJsonDataGet(new UCenterSignReq(), new Response.Listener<BaseBeanRsp<String>>() {
                    @Override
                    public void onResponse(BaseBeanRsp<String> response) {
                        if (response != null && response.Status == 1) {
                            showTopToast("签到成功", true);
                            initData();
                        } else {
                            showTopToast("签到失败" + (TextUtils.isEmpty(response.Message) ? "" : ":" + response.Message), false);
                        }
                    }
                }, null);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
