package com.hnzx.hnrb.ui.me;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.App;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;

import java.util.ArrayList;
import java.util.HashMap;

public class ResiterUpLoadTask extends AsyncTask<String, Integer, String> {
    private Context con;
    private ArrayList<String> uploadFiles;
    private ProgressDialog dialog;
    private HashMap<String, String> map;
    private String fileKey;

    public ResiterUpLoadTask(Context con, ArrayList<String> uploadFiles, HashMap<String, String> map, String fileKey) {
        this.con = con;
        this.uploadFiles = uploadFiles;
        this.map = map;
        this.fileKey = fileKey;
    }

    @Override
    protected void onPreExecute() {
        if (!App.getInstance().isNetworkConnected(con)) {
            ((BindMobileActivity) con).showToast("网络不可用");
            this.cancel(true);
        }
        dialog = new ProgressDialog(con, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle("正在上传...");
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
//        ImageUpload upLoad = new ImageUpload(con);
        return "";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.cancel();
        try {
            BaseBeanRsp<UserInfoRsp> bean = JSON.parseObject(result,
                    new TypeReference<BaseBeanRsp<UserInfoRsp>>() {
                    });
            if (bean != null && bean.Status == 1) {
                App.getInstance().saveLoginUserInfo(bean.Info);
                ((BindMobileActivity) con).showToast("登录成功");
//                Intent intent = new Intent(ActivityUser.ACTION);
//                con.sendBroadcast(intent);
                ((Activity) con).finish();
            } else
                ((BindMobileActivity) con).showToast(result != null && bean.Message != null ? bean.Message : "第三方登录失败");
        } catch (Exception e) {
            e.printStackTrace();
            ((BindMobileActivity) con).showToast("第三方登录失败");
        }
    }
}
