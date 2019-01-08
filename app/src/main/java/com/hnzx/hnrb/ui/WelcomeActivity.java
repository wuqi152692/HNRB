package com.hnzx.hnrb.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;
import com.hnzx.hnrb.constant.Constant;
import com.hnzx.hnrb.network.Algorithm;
import com.hnzx.hnrb.requestbean.GetAdsIndexReq;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAdsRsp;
import com.hnzx.hnrb.tools.CDUtil;
import com.hnzx.hnrb.tools.IntentUtil;
import com.hnzx.hnrb.tools.PackageUtil;
import com.hnzx.hnrb.tools.PermissionCheckUtil;
import com.hnzx.hnrb.tools.SharePreferenceTool;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPluginPlatformInterface;


public class WelcomeActivity extends AppCompatActivity {
    private TextView mTextViewTime;
    private ImageView image;
    private final String VERSIONNAME = "VERSIONNAME";
    private final int GO_HOME = 1;
    private final int GO_GUIDE = 2;

    private Timer timer;
    private TimeTask task;

    private GetAdsRsp ads;

    private static final String WELCOME_AD = "welcome_ad";

    private JPluginPlatformInterface pHuaweiPushInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pHuaweiPushInterface = new JPluginPlatformInterface(this.getApplicationContext());
        setTheme(R.style.WelcomeTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mTextViewTime = (TextView) findViewById(R.id.time);
        image = (ImageView) findViewById(R.id.image);
        if (App.getInstance().getPushOpen())
            finish();
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPhoneStatusPermission();
            else {
                if (!SharePreferenceTool.contains(WelcomeActivity.this, Constant.DEVICE_ID)) {
                    SharePreferenceTool.put(WelcomeActivity.this, Constant.DEVICE_ID, getDeviceID());
                }
                getSchemeIntent();
                initListener();
            }
        }
    }

    private void requestPhoneStatusPermission() {
        PermissionCheckUtil.getInstance(this).checkPermission(this, new PermissionCheckUtil.CheckListener() {
            @Override
            public void isPermissionOn() {
                if (!SharePreferenceTool.contains(WelcomeActivity.this, Constant.DEVICE_ID)) {
                    SharePreferenceTool.put(WelcomeActivity.this, Constant.DEVICE_ID, getDeviceID());
                }
                getSchemeIntent();
                initListener();
            }

            @Override
            public void isPermissionNo() {
                WelcomeActivity.this.finish();
            }
        }, PermissionCheckUtil.PERMISSION_COMMON);
    }


    private void initListener() {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ads != null) {
                    ADSelect.goWhere(WelcomeActivity.this, ads, true);
                }
            }
        });

        mTextViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals((String) SharePreferenceTool.get(WelcomeActivity.this, VERSIONNAME, ""),
                        PackageUtil.getVersionName(WelcomeActivity.this))) {
                    mHandler.sendEmptyMessage(GO_HOME);
                } else {
                    mHandler.sendEmptyMessage(GO_GUIDE);
                }
            }
        });
    }

    private void initDatas() {
        App.getInstance().requestJsonDataGet(new GetAdsIndexReq(), new Response.Listener<BaseBeanRsp<GetAdsRsp>>() {
            @Override
            public void onResponse(BaseBeanRsp<GetAdsRsp> response) {
                if (response != null && response.Status == 1) {
                    new saveDataAsync(response.Info).execute();
                    ads = response.Info;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && WelcomeActivity.this.isDestroyed())
                        return;
                    Glide.with(WelcomeActivity.this)
                            .load(response.Info.thumb)
                            .crossFade()
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(getResources().getDrawable(R.drawable.bg_welcome_guide))
                            .into(new GlideDrawableImageViewTarget(image) {
                                @Override
                                public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                    super.onResourceReady(drawable, anim);
                                    if (drawable != null && !WelcomeActivity.this.isFinishing()) {
                                        if (timer != null) {
                                            timer.cancel();
                                            mHandler.removeMessages(GO_HOME);
                                            mHandler.removeMessages(GO_GUIDE);
                                        }
                                        image.setImageDrawable(drawable);
                                        mTextViewTime.setVisibility(View.VISIBLE);
                                        task = new TimeTask(7000, 1000);
                                        task.start();
                                    }
                                }
                            });
                }
            }
        }, null);
    }


    public String getDeviceID() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return System.currentTimeMillis() + "hndaily";
            }
            String device_id = tm.getDeviceId();
            if (device_id == null && device_id.length() < 2) {
                device_id = System.currentTimeMillis() + "hndaily";
            }
            try {
                device_id = Algorithm.Md5Encrypt(device_id, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return device_id;
        } catch (Exception e) {
            return System.currentTimeMillis() + "hndaily";
        }
    }

    /**
     * scheme处理
     */
    void getSchemeIntent() {
        if (null != getIntent().getScheme() && getIntent().getScheme().contains("hnrb")) {
            String url = getIntent().getDataString();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())
                return;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (App.getInstance().isNetworkConnected(WelcomeActivity.this))
                        initDatas();
                    else new sqliteAsync().execute();
                    goToHome();
                }
            }, 2000);
        }
    }

    private class TimeTask extends CountDownTimer {

        public TimeTask(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            final long time = millisUntilFinished / 1000;
            mTextViewTime.post(new Runnable() {
                @Override
                public void run() {
                    mTextViewTime.setText(time + " 跳过");
                }
            });
        }

        @Override
        public void onFinish() {
            mTextViewTime.post(new Runnable() {
                @Override
                public void run() {
                    mTextViewTime.setText(0 + " 跳过");
                }
            });
            if (TextUtils.equals((String) SharePreferenceTool.get(WelcomeActivity.this, VERSIONNAME, ""),
                    PackageUtil.getVersionName(WelcomeActivity.this))) {
                mHandler.sendEmptyMessage(GO_HOME);
            } else {
                mHandler.sendEmptyMessage(GO_GUIDE);
            }
        }
    }

    // 延迟两秒进入
    protected void goToHome() {

        timer = new Timer();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (task != null) {
                    task.cancel();
                    mHandler.removeMessages(GO_HOME);
                    mHandler.removeMessages(GO_GUIDE);
                }
                if (TextUtils.equals((String) SharePreferenceTool.get(WelcomeActivity.this, VERSIONNAME, ""),
                        PackageUtil.getVersionName(WelcomeActivity.this)))
                    mHandler.sendEmptyMessage(GO_HOME);
                else
                    mHandler.sendEmptyMessage(GO_GUIDE);
            }
        }, 2000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    IntentUtil.startActivity(WelcomeActivity.this, MainActivity.class);
                    break;
                case GO_GUIDE:
                    IntentUtil.startActivity(WelcomeActivity.this, GuideActivity.class);
                    break;
            }
            if (task != null)
                task.cancel();
            if (timer != null)
                timer.cancel();
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
        if (task != null)
            task.cancel();
    }

    private class saveDataAsync extends AsyncTask<String, Integer, String> {

        GetAdsRsp data;

        saveDataAsync(GetAdsRsp data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(String... params) {
            CDUtil.saveObject(data, WELCOME_AD);
            return null;
        }
    }

    private class sqliteAsync extends AsyncTask<String, Integer, String> {

        GetAdsRsp data = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            data = (GetAdsRsp) CDUtil.readObjectJust(WELCOME_AD);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (data != null) {
                ads = data;
                Glide.with(WelcomeActivity.this)
                        .load(data.thumb)
                        .crossFade()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(getResources().getDrawable(R.drawable.bg_welcome_guide))
                        .into(image);
                if (timer != null) {
                    timer.cancel();
                    mHandler.removeMessages(GO_HOME);
                    mHandler.removeMessages(GO_GUIDE);
                }
                mTextViewTime.setVisibility(View.VISIBLE);
                task = new TimeTask(7000, 1000);
                task.start();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //适配华为HMS SDK增加的调用
        if (requestCode == JPluginPlatformInterface.JPLUGIN_REQUEST_CODE) {
            pHuaweiPushInterface.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //适配华为HMS SDK增加的调用
        pHuaweiPushInterface.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //适配华为HMS SDK增加的调用
        pHuaweiPushInterface.onStop(this);
    }
}
