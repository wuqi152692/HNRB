package com.hnzx.hnrb.ui.audio;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.PictureDetailsListAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetNewsDetailsReq;
import com.hnzx.hnrb.requestbean.SetAddFavouriteReq;
import com.hnzx.hnrb.requestbean.SetCancelFavouriteReq;
import com.hnzx.hnrb.requestbean.SetNewsSupportReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetNewsDetalisRsp;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.ui.news.CommentActivity;
import com.hnzx.hnrb.ui.news.NewsDetailsActivity;
import com.hnzx.hnrb.view.HackyViewPager;
import com.hnzx.hnrb.view.ImageScrollView;
import com.hnzx.hnrb.view.MultiStateView;
import com.umeng.socialize.UMShareAPI;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends BaseActivity implements View.OnClickListener {
    public static final String DATA_KEY = "datakey";

    private TextView title, message;
    private ImageView other;
    private MultiStateView mMultiStateView;
    private HackyViewPager pager;
    private ImageScrollView scroll;
    private View bottomLayout, topLayout;
    private CheckBox support, comment, collect;
    private TextView supportNum, commentNum, pagerNum;
    private GetNewsDetalisRsp rsp;

    private PictureDetailsListAdapter adapter;

    private String content_id;
    private boolean isSupport;

    @Override
    protected int getLayoutId() {
        content_id = getIntent().getStringExtra(DATA_KEY);
        return R.layout.activity_image;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        title = (TextView) findViewById(R.id.title);
        other = (ImageView) findViewById(R.id.other);
        other.setVisibility(View.VISIBLE);

        mMultiStateView = (MultiStateView) findViewById(R.id.mMultiStateView);
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mMultiStateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);
        pager = (HackyViewPager) findViewById(R.id.pager);
        scroll = (ImageScrollView) findViewById(R.id.scroll);
        bottomLayout = findViewById(R.id.bottomLayout);
        topLayout = findViewById(R.id.layout);

        message = (TextView) findViewById(R.id.message);

        support = (CheckBox) findViewById(R.id.support);
        comment = (CheckBox) findViewById(R.id.comment);
        collect = (CheckBox) findViewById(R.id.collect);

        supportNum = (TextView) findViewById(R.id.supportNum);
        commentNum = (TextView) findViewById(R.id.commentNum);
        pagerNum = (TextView) findViewById(R.id.pagerNum);
    }

    @Override
    protected void initData() {
        adapter = new PictureDetailsListAdapter(new photoTapListener());
        pager.setAdapter(adapter);

        GetNewsDetailsReq req = new GetNewsDetailsReq();
        req.content_id = content_id;

        App.getInstance().requestJsonDataGet(req, new dataListener(), new MyErrorListener(mMultiStateView));
    }

    @Override
    protected void initListeners() {
        findViewById(R.id.back).setOnClickListener(this);
        other.setOnClickListener(this);
        support.setOnClickListener(this);
        comment.setOnClickListener(this);
        commentNum.setOnClickListener(this);
        collect.setOnClickListener(this);
        pager.setOnPageChangeListener(new pageCHangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.other:
                share();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.support:
                imageSupport();
                break;
            case R.id.comment:
                goToCommentActivity();
                break;
            case R.id.commentNum:
                goToCommentActivity();
                break;
            case R.id.collect:
                collect();
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
                break;
        }
    }

    private void goToCommentActivity() {
        Intent it = new Intent(this, CommentActivity.class);
        it.putExtra(Constant.BEAN, content_id);
        startActivity(it);
    }

    private void collect() {
        if (App.getInstance().isLogin()) {
            collect.setChecked(!collect.isChecked());
            if (collect.isChecked()) {
                SetAddFavouriteReq req = new SetAddFavouriteReq();
                req.content_id = content_id;

                App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
            } else {
                SetCancelFavouriteReq req = new SetCancelFavouriteReq();
                req.content_id = content_id;

                App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
            }
        } else startActivity(LoginActivity.newIntent(this, false));
    }

    private void imageSupport() {
        if (isSupport)
            showToast("您已点过赞");
        else {
            SetNewsSupportReq req = new SetNewsSupportReq();
            req.content_id = content_id;
            App.getInstance().requestJsonDataGet(req, new supportListener(), null);
        }
    }

    NewsShareDialog dialog;

    private void share() {
        if (rsp == null) {
            showToast("分享失败");
            return;
        }
        if (dialog == null || dialog.isAdded())
            dialog = NewsShareDialog.newInstance(rsp.title, rsp.title, "", rsp.url);
        dialog.show(getFragmentManager(), getLocalClassName());
    }


    private class dataListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsDetalisRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsDetalisRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null && response.Info.zutu != null && response.Info.zutu.size() > 0) {
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                title.setText(response.Info.title);
                commentNum.setText(String.valueOf(response.Info.comment));
                pagerNum.setText(1 + "/" + response.Info.zutu.size());
                message.setText(response.Info.zutu.get(0).brief);
                supportNum.setText(response.Info.support);
                adapter.addPictureData(response.Info.zutu);
                collect.setChecked(response.Info.is_favor == 1);
                rsp = response.Info;
            } else mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    private class photoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            pagerNum.setVisibility(scroll.isShown() ? View.GONE : View.VISIBLE);
            topLayout.setVisibility(scroll.isShown() ? View.GONE : View.VISIBLE);
            bottomLayout.setVisibility(scroll.isShown() ? View.GONE : View.VISIBLE);
            scroll.setVisibility(scroll.isShown() ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private class pageCHangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            GetNewsDetalisRsp.ZutuBean zutu = rsp.zutu.get(position);
            pagerNum.setText(zutu.index + "/" + zutu.total);
            message.setText(zutu.brief);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class supportListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                isSupport = true;
                support.setChecked(true);
                showToast("点赞成功");
                supportNum.setText(Integer.parseInt(rsp.support) + 1 + "");
            }
        }
    }

    private class addCollectListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {

        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                showToast(!collect.isChecked() ? "添加收藏成功" : "取消收藏成功");
                collect.setChecked(!collect.isChecked());
            } else showToast(!collect.isChecked() ? "添加收藏失败" : "取消收藏失败");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
