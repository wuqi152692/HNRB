package com.hnzx.hnrb.ui.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.requestbean.FeedBackReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.tools.PackageUtil;
import com.hnzx.hnrb.view.TopHeadView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by FoAng on 17/4/24 下午9:27;
 * 反馈页面
 */
public class FeedBackActivity extends BaseActivity {

    private TopHeadView mTopHeadView;

    private EditText mEditTextContent;

    private TextView mTextViewVersionName;

    private Button mButtonSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feed_back;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTopHeadView = getViewById(R.id.feed_back_headView);
        mEditTextContent = getViewById(R.id.feed_back_content);
        mTextViewVersionName = getViewById(R.id.feed_back_currentVersion);
        mButtonSubmit = getViewById(R.id.feed_back_submit);
    }

    @Override
    protected void initData() {
        mTopHeadView.setHeadTitle("反馈");
        mTextViewVersionName.setText(TextUtils.isEmpty(PackageUtil.getVersionName(this)) ? "" :
                "V" + PackageUtil.getVersionName(this));
    }

    @Override
    protected void initListeners() {
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedBack();
            }
        });
    }

    private void submitFeedBack() {
        final String feedContent = mEditTextContent.getEditableText().toString().trim();
        if (TextUtils.isEmpty(feedContent)) {
            showTopToast("随便说点什么吧", true);
            return;
        }
        FeedBackReq feedBackReq = new FeedBackReq();

        Map<String, String> feedParams = new HashMap<String, String>() {{
            put("content", feedContent);
        }};

        App.getInstance().requestJsonDataPost(feedParams, feedBackReq, new Response.Listener<BaseBeanRsp<String>>() {
            @Override
            public void onResponse(BaseBeanRsp<String> response) {
                if (response != null && response.Status == 1) {
                    showTopToast("反馈成功，感谢您的反馈", true);
                    mEditTextContent.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FeedBackActivity.this.finish();
                        }
                    }, 500);
                } else {
                    showTopToast(response != null && !TextUtils.isEmpty(response.Message) ? response.Message :
                            "反馈失败", false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showTopToast("反馈失败", false);
            }
        });

    }

}
