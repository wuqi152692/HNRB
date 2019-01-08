package com.hnzx.hnrb.ui.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAboutReq;
import com.hnzx.hnrb.tools.LogUtil;
import com.hnzx.hnrb.view.TopHeadView;

/**
 * Created by FoAng on 17/4/24 下午9:27;
 * 我的关于页面
 */
public class AboutActivity extends BaseActivity {

    private TopHeadView mTopHeadView;

    private TextView mTextViewContent;

    public static Intent newIntent(Context mContext) {
        Intent mIntent = new Intent();
        mIntent.setClass(mContext, AboutActivity.class);
        return mIntent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTopHeadView = getViewById(R.id.about_headView);
        mTextViewContent = getViewById(R.id.about_content);
    }

    @Override
    protected void initData() {
        mTopHeadView.setHeadTitle("关于");
        getAboutContent();
    }

    @Override
    protected void initListeners() {

    }

    private void getAboutContent() {
        GetAboutReq aboutReq = new GetAboutReq();

        App.getInstance().requestJsonDataGet(aboutReq, new Response.Listener<BaseBeanRsp<String>>() {
            @Override
            public void onResponse(BaseBeanRsp<String> response) {
                if (response != null && response.Status == 1 && response.Info != null) {
                    LogUtil.e(response.Info);
                    try {
                        JSONObject mJsonObject = JSON.parseObject(response.Info);
                        mTextViewContent.setText(Html.fromHtml(mJsonObject.getString("content")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showTopToast(response.Message, false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast(TextUtils.isEmpty(error.getMessage()) ? "" : error.getMessage(), false);
            }
        });
    }
}
