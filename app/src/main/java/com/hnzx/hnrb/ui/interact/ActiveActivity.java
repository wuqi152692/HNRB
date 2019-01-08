package com.hnzx.hnrb.ui.interact;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.htmlTools.TDownloadListener;
import com.hnzx.hnrb.htmlTools.TWebChromeClient;
import com.hnzx.hnrb.htmlTools.TWebView;
import com.hnzx.hnrb.htmlTools.TWebViewClient;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetActivityAddReq;
import com.hnzx.hnrb.requestbean.GetActivityDetailsReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetActivityDetailsRsp;
import com.hnzx.hnrb.tools.DateUtils;
import com.hnzx.hnrb.tools.GetHtmlData;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.WebUtil;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.view.MultiStateView;
import com.umeng.socialize.UMShareAPI;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActiveActivity extends BaseActivity implements View.OnClickListener {
    public static final String DATA_KEY = "datakey";
    private MultiStateView stateView;

    private ImageView image, other;
    private TextView activityTitle, number, startDate, startTime, endDate, endTime, date, address;
    private EditText nameET, telET;
    private TextView signUp;
    private FrameLayout webLayout;
    private TWebView msgWeb;
    private String activity_id;

    @Override
    protected int getLayoutId() {
        activity_id = getIntent().getStringExtra(DATA_KEY);
        return R.layout.activity_active;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void initViews(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("活动");
        other = (ImageView) findViewById(R.id.other);
        other.setVisibility(View.VISIBLE);
        image = (ImageView) findViewById(R.id.image);
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        date = (TextView) findViewById(R.id.date);
        address = (TextView) findViewById(R.id.address);
        number = (TextView) findViewById(R.id.number);
        startDate = (TextView) findViewById(R.id.startDate);
        startTime = (TextView) findViewById(R.id.startTime);
        endDate = (TextView) findViewById(R.id.endDate);
        endTime = (TextView) findViewById(R.id.endTime);

        nameET = (EditText) findViewById(R.id.nameET);
        telET = (EditText) findViewById(R.id.telET);

        signUp = (TextView) findViewById(R.id.signUp);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        webLayout = (FrameLayout) findViewById(R.id.webLayout);

        msgWeb = new TWebView(this);
        WebUtil.setWebView(msgWeb, this);
        msgWeb.setDownloadListener(new TDownloadListener());
        msgWeb.setWebChromeClient(new TWebChromeClient());
        msgWeb.setWebViewClient(new TWebViewClient(this, msgWeb));
        msgWeb.getSettings().setDefaultFontSize(16);

        webLayout.addView(msgWeb, -1, -2);
    }

    @Override
    protected void initData() {
        getRefresh();
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        other.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    public void getRefresh() {
        GetActivityDetailsReq req = new GetActivityDetailsReq();
        req.activity_id = activity_id;

        App.getInstance().requestJsonDataGet(req, new activityInfoListener(), new MyErrorListener(stateView));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:
                signUp();
                break;
            case R.id.other:
                share();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    private void signUp() {
        if (telET.getText().toString().trim().length() < 1) {
            showTopToast("请输入电话号码", true);
            return;
        }
        if (nameET.getText().toString().trim().length() < 1) {
            showTopToast("请输入姓名", true);
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("mobile", telET.getText().toString().trim());
        params.put("username", nameET.getText().toString().trim());
        GetActivityAddReq req = new GetActivityAddReq();
        req.activity_id = activity_id;

        App.getInstance().requestJsonDataPost(params, req, new activityAddListener(), null);
    }

    NewsShareDialog dialog;

    private void share() {
        if (dialog == null) {
            showTopToast("分享失败", false);
            return;
        }
        dialog.show(getFragmentManager(), getLocalClassName());
    }

    private class activityInfoListener implements Response.Listener<BaseBeanRsp<GetActivityDetailsRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetActivityDetailsRsp> response) {
            if (response != null && response.Status == 1) {
                stateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                addTopicInfoToView(response.Info);
                dialog = NewsShareDialog.newInstance(response.Info.title, GetHtmlData.HtmlToText(response.Info.brief), response.Info.thumb, response.Info.url);
            } else stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private void addTopicInfoToView(GetActivityDetailsRsp info) {
        GlideTools.Glide(this, info.thumb, image, R.drawable.bg_morentu_datumoshi);

        activityTitle.setText(info.title);
        date.setText(info.created);
        address.setText("地址：" + info.address);
        number.setText(info.remained + "");
        msgWeb.loadData(info.brief);
        Date sd = DateUtils.stringToDate(info.start_time, DateUtils.patternLong);
        startDate.setText(DateUtils.dateToString(sd, "yyyy-MM-dd"));
        startTime.setText(DateUtils.dateToString(sd, DateUtils.patternHHmm));

        Date ed = DateUtils.stringToDate(info.end_time, DateUtils.patternLong);
        endDate.setText(DateUtils.dateToString(ed, "yyyy-MM-dd"));
        endTime.setText(DateUtils.dateToString(ed, DateUtils.patternHHmm));

        if (info.type.equals("over")) {
            signUp.setBackgroundColor(Color.parseColor("#a6a6a6"));
            signUp.setText("已结束");
            signUp.setEnabled(false);
            telET.setEnabled(false);
            nameET.setEnabled(false);
        } else if (info.type.equals("coming")) {
            signUp.setBackgroundColor(Color.parseColor("#ffa6a9"));
            signUp.setText("即将开始");
            signUp.setEnabled(false);
            telET.setEnabled(false);
            nameET.setEnabled(false);
        } else {
            telET.setEnabled(true);
            nameET.setEnabled(true);
            if (info.is_joined == 1) {
                signUp.setBackgroundColor(Color.parseColor("#ffa6a9"));
                signUp.setText("已报名");
                signUp.setEnabled(false);
                telET.setEnabled(false);
                nameET.setEnabled(false);
            } else {
                signUp.setEnabled(true);
                telET.setEnabled(true);
                nameET.setEnabled(true);
            }
        }
    }

    private class activityAddListener implements Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                number.setText(String.valueOf(Integer.parseInt(number.getText().toString()) - 1));
                showTopToast("报名成功", true);
                signUp.setText("已参与");
                signUp.setEnabled(false);
                signUp.setClickable(false);
                telET.setEnabled(false);
                nameET.setEnabled(false);
            } else showTopToast(response != null ? response.Message : "报名失败", false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
