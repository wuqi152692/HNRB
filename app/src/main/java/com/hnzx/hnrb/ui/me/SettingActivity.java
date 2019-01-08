package com.hnzx.hnrb.ui.me;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnzx.hnrb.App;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.base.BaseActivity;
import com.hnzx.hnrb.tools.DataCleanManager;
import com.hnzx.hnrb.tools.DialogUtil;
import com.hnzx.hnrb.tools.PackageUtil;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.view.SwitchTypeButton;
import com.hnzx.hnrb.view.TopHeadView;
import com.hnzx.hnrb.view.dialog.ProgressHUD;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by FoAng on 17/4/24 下午9:28;
 * 设置页面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TopHeadView mTopHeadView;

    private SwitchTypeButton mTypeButtonMessage;

    private SwitchTypeButton mTypeButtonNick;

    private TextView mTextViewVersionName;

    private TextView mTextViewCache;

    private LinearLayout mLinearLayoutCleanCache;

    private LinearLayout mLinearLayoutFeedBack;

    private LinearLayout mLinearLayoutAbout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mTopHeadView = getViewById(R.id.setting_headView);
        mTypeButtonMessage = getViewById(R.id.setting_switch_message);
        mTypeButtonNick = getViewById(R.id.setting_switch_nickName);
        mLinearLayoutCleanCache = getViewById(R.id.setting_clean_cache);
        mLinearLayoutFeedBack = getViewById(R.id.setting_feed_back);
        mLinearLayoutAbout = getViewById(R.id.setting_about);
        mTextViewCache = getViewById(R.id.setting_cache_tips);
        mTextViewVersionName = getViewById(R.id.setting_version_tips);
    }

    @Override
    protected void initData() {
        mTopHeadView.setHeadTitle("设置");
        mTextViewVersionName.setText("V" + PackageUtil.getVersionName(this));
        try {
            getCacheFileSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initListeners() {
        mLinearLayoutAbout.setOnClickListener(this);
        mLinearLayoutFeedBack.setOnClickListener(this);
        mLinearLayoutCleanCache.setOnClickListener(this);

        configNoticeMode();

        mTypeButtonMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOpen) {
                if (isOpen) {
                    SharePreferenceTool.put(SettingActivity.this, App.SHARE_PREFERENCE_NOTICE, true);
                    JPushInterface.resumePush(SettingActivity.this);
                } else {
                    SharePreferenceTool.put(SettingActivity.this, App.SHARE_PREFERENCE_NOTICE, false);
                    JPushInterface.stopPush(SettingActivity.this);
                }
            }
        });

        mTypeButtonNick.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    /**
     * 配置当前消息通知
     */
    private void configNoticeMode() {
        final boolean isOpenNotice = (boolean) SharePreferenceTool.get(this, App.SHARE_PREFERENCE_NOTICE, true);
        if (isOpenNotice) {
            mTypeButtonMessage.setChecked(true);
        } else {
            mTypeButtonMessage.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        switch (viewId) {
            case R.id.setting_clean_cache:
                DialogUtil.showBaseDialog(this, "正在清理缓存");
                cleanCacheFile();
                break;
            case R.id.setting_feed_back:
                startActivity(FeedBackActivity.newIntent(this, FeedBackActivity.class));
                break;
            case R.id.setting_about:
                startActivity(AboutActivity.newIntent(this));
                break;
        }
    }


    private static final int HANDLER_GET_FILE_SIZES = 0x01;

    private static final int HANDLER_ClEAN_FILE_CACHE = 0x02;

    private Handler cacheHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_GET_FILE_SIZES:
                    String fileSize = (String) msg.obj;
                    mTextViewCache.setText(fileSize);
                    break;
                case HANDLER_ClEAN_FILE_CACHE:
                    cacheHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showCleanCacheFileTips();
                            getCacheFileSize(SettingActivity.this);
                        }
                    }, 1000);
                    break;
            }
        }
    };

    private void getCacheFileSize(final Context mContext) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String cacheSize = DataCleanManager.getTotalCacheSize(mContext);
                    Message message = cacheHandler.obtainMessage();
                    message.what = HANDLER_GET_FILE_SIZES;
                    message.obj = cacheSize;
                    cacheHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void cleanCacheFile() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DataCleanManager.clearAllCache(SettingActivity.this);
                Message message = cacheHandler.obtainMessage();
                message.what = HANDLER_ClEAN_FILE_CACHE;
                cacheHandler.sendMessage(message);
            }
        }.start();
    }

    private ProgressHUD cleanCacheTips;

    private void showCleanCacheFileTips() {
        DialogUtil.dismissDialog();
        if (cleanCacheTips == null) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.icon_done);
            cleanCacheTips = ProgressHUD.create(this)
                    .setCustomView(imageView)
                    .setLabel("缓存已经清理");
        }
        cleanCacheTips.show();
        cacheHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanCacheTips.dismiss();
            }
        }, 1000);
    }
}
