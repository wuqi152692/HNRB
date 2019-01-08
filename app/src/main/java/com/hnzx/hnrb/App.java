package com.hnzx.hnrb;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hnzx.hnrb.network.Algorithm;
import com.hnzx.hnrb.network.FastJsonArrayRequest;
import com.hnzx.hnrb.network.FastJsonRequest;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.network.OkHttpStack;
import com.hnzx.hnrb.requestbean.BaseBeanArrayReq;
import com.hnzx.hnrb.requestbean.BaseBeanReq;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;
import com.hnzx.hnrb.tools.LogUtil;
import com.hnzx.hnrb.tools.SharePreferenceTool;
import com.hnzx.hnrb.view.dialog.ProgressHUD;
import com.iflytek.cloud.SpeechUtility;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class App extends Application {

    /**
     * 用户是否已经登陆
     */
    public static final String SHARE_PREFERENCE_KEY_IS_LOGIN = "SHARE_PREFERENCE_KEY_IS_LOGIN";

    public static final String SHARE_PREFERENCE_KEY_USER_INFO = "SHARE_PREFERENCE_KEY_USER_INFO";

    public static final String SHARE_PREFERENCE_KEY_FONT = "SHARE_PREFERENCE_KEY_FONT";

    public static final String SHARE_PREFERENCE_NOTICE = "SHARE_PREFERENCE_NOTICE";

    public static final int LOGIN_STATUS_ONLINE = 0x01;

    public static final int LOGIN_STATUS_OFFLINE = 0x02;

    private static App app;

    private static UserInfoRsp loginInfo;

    private RequestQueue queue;

    private ProgressHUD mBaseDialog;

    private boolean pushOpen;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("multipart/form-data");

    OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.setLogEnabled(true);
        app = this;
        queue = Volley.newRequestQueue(this, new OkHttpStack());

        fixAutoLayoutMeasure();

        SpeechUtility.createUtility(this, "appid=591d5859");

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,
                "");

        Config.DEBUG = true;
        UMShareAPI.get(this);

        /**
         * 初始化推送
         */
        JPushInterface.init(this);

        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin("wxada55009df1ceaaa", "812c089d512685cae84d8083bfde8d53");
        PlatformConfig.setQQZone("1105124967", "KEYIDlWp0esOd6ApGYB");
        PlatformConfig.setSinaWeibo("1285457612", "8cd9180ac8b25915978c1afc564232b2", "http://www.henandaily.cn");
    }

    /**
     * 反射或者设备真是物理高度（包含状态栏高度）
     */
    private void fixAutoLayoutMeasure() {
        try {
            Class<?> cls = AutoLayoutConifg.class;
            Field f = cls.getDeclaredField("useDeviceSize");
            f.setAccessible(true);//为 true 则表示反射的对象在使用时取消 Java 语言访问检查
            System.out.println(f.get(AutoLayoutConifg.getInstance()));
            f.set(AutoLayoutConifg.getInstance(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void requestJsonDataGet(BaseBeanReq<T> object, Response.Listener<BaseBeanRsp<T>> listener,
                                       Response.ErrorListener errorListener) {

        FastJsonRequest<T> request = new FastJsonRequest<T>(GetData.requestJsonUrlGetClass(object), object.myTypeReference(),
                listener, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setShouldCache(true);

        request.setTag(object);

        queue.add(request);
    }

    public <T> void requestJsonArrayDataGet(BaseBeanArrayReq<T> object, Response.Listener<BaseBeanArrayRsp<T>> listener,
                                            Response.ErrorListener errorListener) {

        FastJsonArrayRequest<T> request = new FastJsonArrayRequest<T>(GetData.requestJsonUrlGet(object), object.myTypeReference(),
                listener, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setShouldCache(true);

        request.setTag(object);

        queue.add(request);
    }

    public <T> void requestJsonDataPost(Map<String, String> mParams, BaseBeanReq<T> object, Response.Listener<BaseBeanRsp<T>> listener,
                                        Response.ErrorListener errorListener) {
        FastJsonRequest<T> request = new FastJsonRequest<T>(mParams, GetData.requestJsonUrlGetClass(object),
                object.myTypeReference(), listener, errorListener);

        request.setShouldCache(true);

        request.setTag(object);

        queue.add(request);
    }

    /**
     * 发布
     *
     * @param url
     * @param content
     * @param files
     * @param callback
     */
    public void doPostPublishMsg(String url, String topic_id, String quoted, String id, String content, List<File> files, Callback callback) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (quoted != null && quoted.length() > 0)
            builder.addFormDataPart("quoted", quoted);
        if (id != null && id.length() > 0)
            builder.addFormDataPart("id", id);
        if (topic_id != null && topic_id.length() > 0)
            builder.addFormDataPart("topic_id", topic_id);
        if (files != null && files.size() > 0) {
            for (File file : files) {
                builder.addFormDataPart("imgs[]", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        if (content != null && content.length() > 0)
            builder.addFormDataPart("content", content);

        MultipartBody requestBody = builder.build();


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(callback);

    }


    /**
     * 上传头像
     *
     * @param myFile
     * @param callback
     */
    public void doUpLoadUserAvatar(File myFile, Callback callback) {
        final UserInfoRsp mUserInfoRsp = App.getInstance().getLoginInfo();
        try {
            StringBuilder mStringBuilder = new StringBuilder();

            mStringBuilder.append("device_type=android&");

            mStringBuilder.append("user_id=" + mUserInfoRsp.user_id + "&");

            String path = GetData.url
                    + GetData.SetPhotoModify
                    + mStringBuilder
                    + "token="
                    + Algorithm
                    .Md5Encrypt(
                            mStringBuilder.append(
                                    App.getInstance().getLoginInfo().auth_key)
                                    .toString(), GetData.encode).toLowerCase();

            LogUtil.e("请求地址：" + path);

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("avatar", myFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, myFile))
                    .build();

            Request request = new Request.Builder().url(path)
                    .post(requestBody)
                    .build();

            mOkHttpClient.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取版本
     *
     * @return 当前应用的版本
     */
    public int getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static App getInstance() {
        return app;
    }

    public void saveLoginUserInfo(UserInfoRsp userInfo) {
        if (userInfo != null) {
            this.loginInfo = userInfo;
            SharePreferenceTool.put(this, SHARE_PREFERENCE_KEY_USER_INFO, JSONObject.toJSONString(userInfo));
            SharePreferenceTool.put(this, SHARE_PREFERENCE_KEY_IS_LOGIN, true);
        } else {
            SharePreferenceTool.clear(this, SHARE_PREFERENCE_KEY_USER_INFO);
        }
    }

    public void cleanLoginUserInfo() {
        SharePreferenceTool.clear(this, SHARE_PREFERENCE_KEY_USER_INFO);
        SharePreferenceTool.clear(this, SHARE_PREFERENCE_KEY_IS_LOGIN);
        SharePreferenceTool.clear(this, SHARE_PREFERENCE_NOTICE);
    }

    public UserInfoRsp getLoginInfo() {
        if (this.loginInfo != null) {
            return this.loginInfo;
        } else {
            String userContent = (String) SharePreferenceTool.get(this, SHARE_PREFERENCE_KEY_USER_INFO, "");
            UserInfoRsp mLoginInfo = JSON.parseObject(userContent, UserInfoRsp.class);
            return mLoginInfo;
        }
    }

    public void setIsLogin(boolean isLogin) {
        SharePreferenceTool.put(this, SHARE_PREFERENCE_KEY_IS_LOGIN, isLogin);
    }

    public boolean isLogin() {
        if (!SharePreferenceTool.contains(this, SHARE_PREFERENCE_KEY_IS_LOGIN)) {
            return false;
        } else {
            return (boolean) SharePreferenceTool.get(this, SHARE_PREFERENCE_KEY_IS_LOGIN, false);
        }
    }

    public void log(String msg) {
        if (BuildConfig.DEBUG)
            Log.e("APP", msg);
    }


    public void setWebFontSize(int appSize) {
        SharePreferenceTool.put(this, SHARE_PREFERENCE_KEY_FONT, appSize);
    }

    public int getWebFontSize() {
        try {
            return (int) SharePreferenceTool.get(this, SHARE_PREFERENCE_KEY_FONT, 14);
        } catch (Exception e) {
            e.printStackTrace();
            return 14;
        }
    }

    public void setPushOpen(boolean pushOpen) {
        this.pushOpen = pushOpen;
    }

    public boolean getPushOpen() {
        return this.pushOpen;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
