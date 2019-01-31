package com.hnzx.hnrb.tools;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.File;

public class QiNiuUpload {
    static Auth auth = Auth.create("0ho_oSPx9dqFHxWrNGT0vqFrKRrpBOK2iv60ZslV", "OiMT4BplQKmMT-cb5vZG6CjYHQ6HrFRybALNu67d");
    static String token = auth.uploadToken("hnrbapp");

    public static void upload(File file, String fileName, final UploadListener listener){
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(file, fileName, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, org.json.JSONObject response) {
                        if(listener!=null)
                            listener.complete(info.isOK());
                        if(info.isOK()) {
                            Log.i("qiniu", "Upload Success");
                        } else {
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                    }
                }, null);
    }

    public static interface UploadListener{
        void complete(boolean isOK);
    }
}
