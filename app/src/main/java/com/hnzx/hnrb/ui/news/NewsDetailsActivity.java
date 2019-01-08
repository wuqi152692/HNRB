package com.hnzx.hnrb.ui.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CheckableImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.adapter.NewsCommentAdapter;
import com.hnzx.hnrb.adapter.NewsDetailsAboutAdapter;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.htmlTools.HtmlParser;
import com.hnzx.hnrb.htmlTools.Js2JavaInterface;
import com.hnzx.hnrb.htmlTools.TWebChromeClient;
import com.hnzx.hnrb.htmlTools.TWebView;
import com.hnzx.hnrb.htmlTools.TWebViewClient;
import com.hnzx.hnrb.network.MyErrorListener;
import com.hnzx.hnrb.requestbean.GetAboutNewsListReq;
import com.hnzx.hnrb.requestbean.GetNewsDetailsReq;
import com.hnzx.hnrb.requestbean.GetNewsHotCommentsReq;
import com.hnzx.hnrb.requestbean.SetAddFavouriteReq;
import com.hnzx.hnrb.requestbean.SetCancelFavouriteReq;
import com.hnzx.hnrb.requestbean.SetCancelOrderCategoryReq;
import com.hnzx.hnrb.requestbean.SetMakeOrderCategoryReq;
import com.hnzx.hnrb.requestbean.SetNewsSupportReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.CommentsBean;
import com.hnzx.hnrb.responsebean.GetAboutNewsListRsp;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;
import com.hnzx.hnrb.responsebean.GetNewsDetalisRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.GetHtmlData;
import com.hnzx.hnrb.tools.GlideTools;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.tools.ScreenUtil;
import com.hnzx.hnrb.tools.WebUtil;
import com.hnzx.hnrb.ui.ADSelect;
import com.hnzx.hnrb.ui.LoginActivity;
import com.hnzx.hnrb.ui.audio.ImageActivity;
import com.hnzx.hnrb.ui.dialog.NewsFontSizeDialog;
import com.hnzx.hnrb.ui.dialog.NewsShareDialog;
import com.hnzx.hnrb.ui.dialog.QRCodeDialog;
import com.hnzx.hnrb.ui.government.square.SquareFragment;
import com.hnzx.hnrb.ui.leftsidebar.ColumnActivity;
import com.hnzx.hnrb.view.MultiStateView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.vov.vitamio.MediaPlayer;

public class NewsDetailsActivity extends BaseActivity implements View.OnClickListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnInfoListener {
    private String content_id;
    private String shareImageUrl;
    private boolean isFromSquare;
    private MultiStateView stateView;
    private XRecyclerView recyclerView;
    private RecyclerView recyclerView_about;
    private TextView from, reporters, pinglun, newsTitle, time, type, subscribe, editor, support, supportNum, hotComment, tv_check, tv_about, reporter_tv0, reporter_tv1, reporter_tv2, reporter_tv3, mTextViewZuTu;
    private CheckableImageButton collect;
    private CheckedTextView readPager;
    private ImageView reporter_iv0, reporter_iv1, reporter_iv2, reporter_iv3, news_details_ad, mImageViewZuTu;
    private View pinglunLayout, reporterLayouts, reporterLayout0, reporterLayout1, reporterLayout2, reporterLayout3, subscribeLayout, mLayoutZuTu;
    private LinearLayout webLayout;

    private TWebView webview;
    private NewsCommentAdapter adapter;
    private NewsDetailsAboutAdapter aboutAdapter;

    //讯飞语音
    private SpeechSynthesizer speechSynthesizer;

    private Map<String, String> params = new HashMap<String, String>();
    private String reporterId0, reporterId1, reporterId2, reporterId3;

    private GetNewsDetalisRsp info;

    public static final String SHARE_IMAGE = "SHARE_IMAGE";

    private QRCodeDialog QRDialog;

    private String NEWS_DETAILS_SAVE_KEY = "news_details";

    private JCVideoPlayerStandard standard;

    private int screenWidth;

    @Override
    protected int getLayoutId() {
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        return R.layout.activity_news_details;
    }

    @Override
    public boolean supportSlideBack() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void initViews(Bundle savedInstanceState) {
        screenWidth = ScreenUtil.getScreenWidth(this);
        content_id = getIntent().getStringExtra(Constant.BEAN);
        NEWS_DETAILS_SAVE_KEY = NEWS_DETAILS_SAVE_KEY + content_id;
        shareImageUrl = getIntent().getStringExtra(SHARE_IMAGE);
        isFromSquare = getIntent().getBooleanExtra(SquareFragment.FROM_SQUARE, false);

        stateView = (MultiStateView) findViewById(R.id.stateView);
        stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
        stateView.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.reload_data).setOnClickListener(this);
        stateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.error_reload_data).setOnClickListener(this);

        recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);

        adapter = new NewsCommentAdapter(this, content_id);
        recyclerView.setAdapter(adapter);
        recyclerView.addHeaderView(getHeaderView());
        recyclerView.setFootView(getFooterView());

        pinglunLayout = findViewById(R.id.pinglunLayout);
        pinglun = (TextView) findViewById(R.id.pinglun);

        if (!App.getInstance().isNetworkConnected(this)) {
            new sqliteAsync().execute();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private View getHeaderView() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_newsdetails_header, recyclerView, false);
        AutoUtils.auto(header);
        readPager = (CheckedTextView) header.findViewById(R.id.readPager);
        from = (TextView) header.findViewById(R.id.from);
        reporters = (TextView) header.findViewById(R.id.reporters);
        newsTitle = (TextView) header.findViewById(R.id.newsTitle);
        time = (TextView) header.findViewById(R.id.time);
        type = (TextView) header.findViewById(R.id.type);
        subscribe = (TextView) header.findViewById(R.id.subscribe);
        editor = (TextView) header.findViewById(R.id.editor);
        support = (TextView) header.findViewById(R.id.support);
        supportNum = (TextView) header.findViewById(R.id.supportNum);
        hotComment = (TextView) header.findViewById(R.id.hotComment);

        webLayout = (LinearLayout) header.findViewById(R.id.webLayout);
        webLayout.removeAllViews();
        webview = new TWebView(this);
        webview.addJavascriptInterface(new Js2JavaInterface(this), HtmlParser.Js2JavaInterfaceName);
        WebUtil.setWebView(webview, this);
        webview.getSettings().setUseWideViewPort(false);
        webview.getSettings().setDefaultFontSize(App.getInstance().getWebFontSize());
        webview.setWebViewClient(new TWebViewClient(this, webview, stateView, true));
        webview.setWebChromeClient(new TWebChromeClient());
        webview.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        webLayout.addView(webview);
        int fontSize = App.getInstance().getWebFontSize();
        webview.getSettings().setTextZoom(fontSize == 12 ? 100 : (fontSize == 14 ? 115 : (fontSize == 16 ? 130 : 145)));

        subscribeLayout = header.findViewById(R.id.subscribeLayout);

        reporter_iv0 = (ImageView) header.findViewById(R.id.reporter_iv0);
        reporter_iv1 = (ImageView) header.findViewById(R.id.reporter_iv1);
        reporter_iv2 = (ImageView) header.findViewById(R.id.reporter_iv2);
        reporter_iv3 = (ImageView) header.findViewById(R.id.reporter_iv3);

        reporter_tv3 = (TextView) header.findViewById(R.id.reporter_tv3);
        reporter_tv2 = (TextView) header.findViewById(R.id.reporter_tv2);
        reporter_tv1 = (TextView) header.findViewById(R.id.reporter_tv1);
        reporter_tv0 = (TextView) header.findViewById(R.id.reporter_tv0);

        reporterLayout0 = header.findViewById(R.id.reporterLayout0);
        reporterLayout1 = header.findViewById(R.id.reporterLayout1);
        reporterLayout2 = header.findViewById(R.id.reporterLayout2);
        reporterLayout3 = header.findViewById(R.id.reporterLayout3);
        reporterLayouts = header.findViewById(R.id.reporterLayouts);

        news_details_ad = (ImageView) header.findViewById(R.id.news_details_ad);

        mLayoutZuTu = header.findViewById(R.id.mLayoutZuTu);
        mImageViewZuTu = (ImageView) header.findViewById(R.id.mImageViewZuTu);
        mTextViewZuTu = (TextView) header.findViewById(R.id.mTextViewZuTu);

        standard = (JCVideoPlayerStandard) header.findViewById(R.id.standard);
        standard.setAllControlsVisible(View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        standard.backButton.setVisibility(View.GONE);

        standard.setLayoutParams(new LinearLayout.LayoutParams(-1, screenWidth * 9 / 16));
        header.requestFocus();
        return header;
    }

    private View getFooterView() {
        View foot = LayoutInflater.from(this).inflate(R.layout.layout_newsdetails_footer, recyclerView, false);
        recyclerView_about = (RecyclerView) foot.findViewById(R.id.recyclerView_about);
        recyclerView_about.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

        aboutAdapter = new NewsDetailsAboutAdapter(this);
        recyclerView_about.setAdapter(aboutAdapter);
        tv_about = (TextView) foot.findViewById(R.id.tv_about);
        tv_check = (TextView) foot.findViewById(R.id.tv_check);
        tv_check.setOnClickListener(this);

        return foot;
    }

    @Override
    protected void initData() {
        //新闻详情
        GetNewsDetailsReq detailsReq = new GetNewsDetailsReq();
        detailsReq.content_id = content_id;
        App.getInstance().requestJsonDataGet(detailsReq, new detailsListener(), App.getInstance().isNetworkConnected(this) || isReload ? new MyErrorListener(stateView) : null);
        isReload = false;
        //热门评论
        GetNewsHotCommentsReq commentsReq = new GetNewsHotCommentsReq();
        commentsReq.content_id = content_id;
        App.getInstance().requestJsonDataGet(commentsReq, new commentsListener(), null);
        //相关新闻
        GetAboutNewsListReq aboutReq = new GetAboutNewsListReq();
        aboutReq.content_id = content_id;
        App.getInstance().requestJsonArrayDataGet(aboutReq, new AboutNewsListListener(), null);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.refreshComplete();
            }
        }, 500);
    }

    @Override
    protected void initListeners() {
        readPager.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.fontSize).setOnClickListener(this);
        collect = (CheckableImageButton) findViewById(R.id.collect);
        collect.setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        support.setOnClickListener(this);
        type.setOnClickListener(this);
        subscribe.setOnClickListener(this);
        reporterLayout0.setOnClickListener(this);
        reporterLayout1.setOnClickListener(this);
        reporterLayout2.setOnClickListener(this);
        reporterLayout3.setOnClickListener(this);
        pinglun.setOnClickListener(this);
        tv_check.setOnClickListener(this);
        findViewById(R.id.shuo).setOnClickListener(this);
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                recyclerView.stopScroll();
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (result == null)
                    return false;
                int type = result.getType();
                if (type == WebView.HitTestResult.UNKNOWN_TYPE)
                    return false;
                if (type == WebView.HitTestResult.EDIT_TEXT_TYPE)
                    return true;
                if (type == WebView.HitTestResult.IMAGE_TYPE) {
                    String imageUrl = result.getExtra();
                    QRDialog = QRCodeDialog.newInstance(imageUrl);
                    QRDialog.show(getFragmentManager(), "QRDialog");
                }
                return false;
            }
        });

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readPager:
                goReadPager();
                break;
            case R.id.subscribe:
                goToSubscribe();
                break;
            case R.id.type:
                goToColumnActivity();
                break;
            case R.id.tv_check:
                goToCommentActivity();
                break;
            case R.id.pinglun:
                goToCommentActivity();
                break;
            case R.id.fontSize:
                fontSize();
                break;
            case R.id.collect:
                collect();
                break;
            case R.id.share:
                share();
                break;
            case R.id.support:
                newsSupport();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.reporterLayout0:
                Map<String, String> params0 = new HashMap<>();
                params0.put(Constant.BEAN, reporterId0);
                IntentUtil.startActivity(this, ReporterDetailsActivity.class, params0);
                break;
            case R.id.reporterLayout1:
                Map<String, String> params1 = new HashMap<>();
                params1.put(Constant.BEAN, reporterId1);
                IntentUtil.startActivity(this, ReporterDetailsActivity.class, params1);
                break;
            case R.id.reporterLayout2:
                Map<String, String> params2 = new HashMap<>();
                params2.put(Constant.BEAN, reporterId2);
                IntentUtil.startActivity(this, ReporterDetailsActivity.class, params2);
                break;
            case R.id.reporterLayout3:
                Map<String, String> params3 = new HashMap<>();
                params3.put(Constant.BEAN, reporterId3);
                IntentUtil.startActivity(this, ReporterDetailsActivity.class, params3);
                break;
            case R.id.news_details_ad:
                if (info.guanggao != null)
                    ADSelect.goWhere(this, info.guanggao, false);
                break;
            case R.id.shuo:
                goToCommentActivity();
                break;
            case R.id.mImageViewZuTu:
                Intent zutu = new Intent(this, ImageActivity.class);
                zutu.putExtra(ImageActivity.DATA_KEY, content_id);
                startActivity(zutu);
                break;
            case R.id.error_reload_data:
            case R.id.reload_data:
                stateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                isReload = true;
                initData();
                break;
        }
    }

    boolean isReload;

    private void goToSubscribe() {
        if (App.getInstance().isLogin()) {
            if (subscribe.getText().toString().equals("订阅")) {
                SetMakeOrderCategoryReq req = new SetMakeOrderCategoryReq();
                req.cat_id = params.get(Constant.BEAN);
                App.getInstance().requestJsonDataGet(req, new orderCategoryListener(), null);
            } else {
                SetCancelOrderCategoryReq req = new SetCancelOrderCategoryReq();
                req.cat_id = params.get(Constant.BEAN);
                App.getInstance().requestJsonDataGet(req, new orderCategoryListener(), null);
            }
        } else {
            // 如未的登录 跳转到登录页面
            startActivity(LoginActivity.newIntent(this, false));
        }
    }

    private void goToColumnActivity() {
        IntentUtil.startActivity(this, ColumnActivity.class, params);
    }

    private void goReadPager() {
        if (readStrs.size() == 0) {
            showTopToast("播报内容为空", false);
            return;
        }
        readPager.setChecked(!readPager.isChecked());
        readPager.setText(readPager.isChecked() ? "播报中" : "播报");
        if (readPager.isChecked()) {//设置参数
            // 清空参数
            speechSynthesizer.setParameter(SpeechConstant.PARAMS, null);
            // 根据合成引擎设置相应参数

            speechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyu");//voicer
            //设置合成语速
            speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            speechSynthesizer.setParameter(SpeechConstant.VOLUME, "50");

            //设置播放器音频流类型
            speechSynthesizer.setParameter(SpeechConstant.STREAM_TYPE, "3");
            // 设置播放合成音频打断音乐播放，默认为true
            speechSynthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            speechSynthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            speechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
            mTtsListener.add(new MySynthesizerListener(0));
            int code = speechSynthesizer.startSpeaking(readStrs.get(0), mTtsListener.get(0));
            if (code != ErrorCode.SUCCESS) {
                showTopToast("语音合成失败,错误码: " + code, false);
                readPager.setChecked(!readPager.isChecked());
                readPager.setText(readPager.isChecked() ? "播报中" : "播报");
            }
        } else
            speechSynthesizer.pauseSpeaking();
    }

    private void goToCommentActivity() {
        Intent it = new Intent(this, CommentActivity.class);
        it.putExtra(Constant.BEAN, content_id);
        startActivity(it);
    }

    NewsFontSizeDialog fontDialog;

    private void fontSize() {
        if (fontDialog == null)
            fontDialog = NewsFontSizeDialog.newInstance(webview);
        fontDialog.show(getFragmentManager(), getLocalClassName());
    }

    @SuppressLint("RestrictedApi")
    private void collect() {
        if (!App.getInstance().isLogin()) {
            startActivity(LoginActivity.newIntent(this, LoginActivity.class));
            return;
        }
        if (collect.isChecked()) {
            SetCancelFavouriteReq req = new SetCancelFavouriteReq();
            req.content_id = content_id;
            App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
        } else {
            SetAddFavouriteReq req = new SetAddFavouriteReq();
            req.content_id = content_id;
            App.getInstance().requestJsonDataGet(req, new addCollectListener(), null);
        }
    }

    NewsShareDialog dialog;

    private void share() {
        if (info == null) {
            showTopToast("分享失败", false);
            return;
        }
        if (dialog == null || dialog.isAdded())
            dialog = NewsShareDialog.newInstance(info.title, info.title, shareImageUrl, info.url);
        dialog.show(getFragmentManager(), getLocalClassName());
    }

    private boolean isSupport;

    private void newsSupport() {
        if (isSupport)
            showTopToast("您已点过赞", false);
        else {
            SetNewsSupportReq req = new SetNewsSupportReq();
            req.content_id = content_id;

            App.getInstance().requestJsonDataGet(req, new supportListener(), null);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private class detailsListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsDetalisRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsDetalisRsp> response) {
            if (response != null && response.Status == 1) {
                info = response.Info;
                addDataToView();
                new saveDataAsync(info).execute();
            } else stateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @SuppressLint("RestrictedApi")
    private void addDataToView() {
        webview.loadData(info.content, "text/html", "UTF-8");
        subscribeLayout.setVisibility(isFromSquare ? View.GONE : View.VISIBLE);
        params.put(Constant.BEAN, info.cat_id);
        reporterLayout0.setVisibility(View.VISIBLE);
        reporterLayout0.setOnClickListener(this);
        if (info.author != null && info.author.size() > 0) {
            reporterLayouts.setVisibility(View.VISIBLE);
            StringBuilder reporterNames = new StringBuilder();
            for (int i = 0; i < info.author.size(); i++) {
                GetNewsDetalisRsp.AuthorBean bean = info.author.get(i);
                reporterNames.append(bean.name + " ");
                if (i == 0) {
                    reporterLayout0.setVisibility(View.VISIBLE);
                    GlideTools.Glide(this, info.author.get(i).avatar, reporter_iv0, R.drawable.reporter_cricle_light);
                    reporter_tv0.setText(bean.name);
                    reporterId0 = bean.author_id;
                } else if (i == 1) {
                    reporterLayout1.setVisibility(View.VISIBLE);
                    GlideTools.Glide(this, info.author.get(i).avatar, reporter_iv1, R.drawable.reporter_cricle_light);
                    reporter_tv1.setText(bean.name);
                    reporterId1 = bean.author_id;
                } else if (i == 2) {
                    reporterLayout2.setVisibility(View.VISIBLE);
                    GlideTools.Glide(this, info.author.get(i).avatar, reporter_iv2, R.drawable.reporter_cricle_light);
                    reporter_tv2.setText(bean.name);
                    reporterId2 = bean.author_id;
                } else {
                    reporterLayout3.setVisibility(View.VISIBLE);
                    GlideTools.Glide(this, info.author.get(i).avatar, reporter_iv3, R.drawable.reporter_cricle_light);
                    reporter_tv3.setText(bean.name);
                    reporterId3 = bean.author_id;
                }
            }
            if (reporterNames.toString().trim().length() > 2)
                reporters.setText("记者:" + reporterNames.toString());
        }

        newsTitle.setText(info.title);
        time.setText(info.inputtime);
        editor.setText("编辑：" + info.editor);
        supportNum.setText(info.support);
        collect.setChecked(info.is_favor == 1);
        if (info.shown_more == 1) {
            subscribe.setText(info.is_ordered == 1 ? "已订阅" : "订阅");
            type.setText(info.catname);
            type.setOnClickListener(this);
            subscribe.setOnClickListener(this);
        } else subscribeLayout.setVisibility(View.GONE);
        from.setText(info.copyfrom);
        pinglun.setText(String.valueOf(info.comment));
        if (info.guanggao != null && info.guanggao.thumb != null) {
            news_details_ad.setVisibility(View.VISIBLE);
            GlideTools.Glide(this, info.guanggao.thumb, news_details_ad, R.drawable.bg_morentu_datumoshi);
            news_details_ad.setOnClickListener(this);
        } else news_details_ad.setVisibility(View.GONE);

        setReadStrs(GetHtmlData.HtmlToText(info.content));

        if (info.zutu != null && info.zutu.size() > 0) {
            GlideTools.GlideRounded(this, info.zutu.get(0).img, mImageViewZuTu, R.drawable.bg_morentu_datumoshi, 0);
            mTextViewZuTu.setText("共" + info.zutu.size() + "张");
            mImageViewZuTu.setOnClickListener(this);
            mLayoutZuTu.setVisibility(View.VISIBLE);
        }
        if (info.myvideo != null && info.myvideo.size() > 0) {
            GlideTools.Glide(this, info.myvideo.get(0).filethumb, standard.thumbImageView, R.drawable.bg_morentu_datumoshi);
            standard.setUp(info.myvideo.get(0).filepath, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            standard.setVisibility(View.VISIBLE);
        }
        if (info.mylive != null && info.mylive.size() > 0) {
            GlideTools.Glide(this, info.mylive.get(0).filethumb, standard.thumbImageView, R.drawable.bg_morentu_datumoshi);
            standard.setUp(info.mylive.get(0).filepath, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
            JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            standard.setVisibility(View.VISIBLE);
        }
    }

    private class commentsListener implements com.android.volley.Response.Listener<com.hnzx.hnrb.responsebean.BaseBeanRsp<com.hnzx.hnrb.responsebean.GetNewsCommentRsp>> {
        @Override
        public void onResponse(BaseBeanRsp<GetNewsCommentRsp> response) {
            if (response != null && response.Status == 1 && response.Info != null) {
                List<String> ids = response.Info.ids;
                if (ids != null && ids.size() > 0) {
                    adapter.addMap(JSON.parseObject(response.Info.comments, new TypeReference<Map<String, CommentsBean>>() {
                    }), 0);
                    adapter.setList(ids);
                } else {
                    hotComment.setVisibility(View.GONE);
                    tv_check.setVisibility(View.GONE);
                }
            } else {
                hotComment.setVisibility(View.GONE);
                tv_check.setVisibility(View.GONE);
            }
        }
    }

    private class AboutNewsListListener implements com.android.volley.Response.Listener<BaseBeanArrayRsp<com.hnzx.hnrb.responsebean.GetAboutNewsListRsp>> {
        @Override
        public void onResponse(BaseBeanArrayRsp<GetAboutNewsListRsp> response) {
            if (response != null && response.Status == 1) {
                if (response.Info == null || response.Info.size() == 0)
                    tv_about.setVisibility(View.GONE);
                else aboutAdapter.addAll(response.Info);
            } else tv_about.setVisibility(View.GONE);
        }
    }

    //讯飞语音合成
    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTopToast("初始化失败,错误码：" + code, true);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    private List<MySynthesizerListener> mTtsListener = new ArrayList<>();

    /**
     * 合成回调监听。
     */
    private class MySynthesizerListener implements SynthesizerListener {
        private int position;

        public MySynthesizerListener(int position) {
            this.position = position;
        }

        @Override
        public void onSpeakBegin() {
            if (position == 0)
                showTopToast("开始播放", false);
        }

        @Override
        public void onSpeakPaused() {
//				Toast.makeText(NewsDetailsActivity.this,"暂停播放",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSpeakResumed() {
//				Toast.makeText(NewsDetailsActivity.this,"继续播放",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            if (position > 1 && percent == 100) {
                mTtsListener.get(position + 1).onSpeakBegin();
            }
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (position + 1 >= readStrs.size()) {
                if (error == null) {
                    showTopToast("播放完成", true);
                } else if (error != null) {
                    showTopToast(error.getPlainDescription(true), true);
                }
                readPager.setText("语音播报");
                readPager.setChecked(false);
            } else {
                mTtsListener.add(new MySynthesizerListener(position + 1));
                speechSynthesizer.startSpeaking(readStrs.get(position + 1), mTtsListener.get(position + 1));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    }

    ;

    private class supportListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                isSupport = true;
                showTopToast("点赞成功", true);
                supportNum.setText(String.valueOf(Integer.parseInt(supportNum.getText().toString()) + 1));
            } else showTopToast("点赞失败", false);
        }
    }

    private class addCollectListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {

        @SuppressLint("RestrictedApi")
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            if (response != null && response.Status == 1) {
                showTopToast(collect.isChecked() ? "取消收藏成功" : "添加收藏成功", true);
                collect.setChecked(!collect.isChecked());
            } else
                showTopToast(collect.isChecked() ? "取消收藏失败" : "添加收藏失败", false);
        }
    }

    private class orderCategoryListener implements com.android.volley.Response.Listener<BaseBeanRsp<String>> {
        @Override
        public void onResponse(BaseBeanRsp<String> response) {
            boolean isSubscribed = subscribe.getText().toString().equals("订阅");
            if (response != null && response.Status == 1) {
                showTopToast(isSubscribed ? "添加订阅成功" : "添加订阅成功", true);
                subscribe.setText(isSubscribed ? "已订阅" : "订阅");
            } else
                showTopToast(isSubscribed ? "添加订阅失败" : "取消订阅失败", true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            speechSynthesizer.destroy();
            webview.removeAllViews();
            webview.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> readStrs = new ArrayList<>();

    private void setReadStrs(String readStr) {
        int readLenth = 0;
        while (readLenth < readStr.length()) {
            readStrs.add(readStr.substring(readLenth, (readLenth + 2048 >= readStr.length() ? readStr.length() - 1 : readLenth + 2048)));
            readLenth += 2048;
        }
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        GetNewsDetalisRsp data;

        saveDataAsync(GetNewsDetalisRsp data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(String... params) {
            CDUtil.saveObject(data, NEWS_DETAILS_SAVE_KEY);
            return null;
        }
    }

    private class sqliteAsync extends AsyncTask<String, Integer, String> {

        GetNewsDetalisRsp data = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            data = (GetNewsDetalisRsp) CDUtil.readObjectJust(NEWS_DETAILS_SAVE_KEY);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (data != null) {
                info = data;
                addDataToView();
            } else stateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (JCVideoPlayer.backPress())
                return;
        } catch (Exception e) {
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (JCMediaManager.instance().mediaPlayer.isPlaying())
                JCMediaManager.instance().mediaPlayer.pause();
            webview.getClass().getMethod("onPause").invoke(webview, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            webview.getClass().getMethod("onResume").invoke(webview, (Object[]) null);
            if (JCMediaManager.instance().mediaPlayer.isPlaying())
                JCMediaManager.instance().mediaPlayer.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
