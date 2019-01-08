package com.hnzx.hnrb.htmlTools;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

import com.hnzx.hnrb.network.Algorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadWebImgTask extends AsyncTask<String, String, Void> {

    public static final String TAG = "DownloadWebImgTask";
    public WebView webView;
    private OkHttpClient okHttpClient;

    public DownloadWebImgTask(WebView webView) {
        this.webView = webView;
    }

    public static final String location = "/hnrb/";

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    var imgSrc = objs[i].getAttribute(\"src_link\"); "
                + "    var imgOriSrc = objs[i].getAttribute(\"ori_link\"); "
                + " if(imgOriSrc == \"" + values[0] + "\"){ "
                + "    objs[i].setAttribute(\"src\",imgSrc);}" +
                "}" +
                "})()");
    }

    @Override
    protected void onPostExecute(Void result) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    var imgSrc = objs[i].getAttribute(\"src_link\"); " +
                "    objs[i].setAttribute(\"src\",imgSrc);" +
                "}" +
                "})()");

        super.onPostExecute(result);
    }

    @Override
    protected Void doInBackground(String... params) {
        File dir = new File(Environment.getExternalStorageDirectory() + location);
        if (!dir.exists()) {
            dir.mkdir();
        }
        for (final String url : params) {
            try {
                if (url == null)
                    continue;
                String filePath = Environment.getExternalStorageDirectory() + location + Algorithm.Md5Encrypt(url, "UTF-8") + ".bin";
                File file = new File(filePath);

                if (file.exists()) {
                    publishProgress(url);
                    continue;
                }

                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final FileOutputStream outputStream = new FileOutputStream(file);
                Request request = new Request.Builder().url(url).build();
                if (okHttpClient == null)
                    okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream inputStream = null;
                        byte buffer[] = new byte[1024];
                        int bufferLength = 0;

                        try {
                            inputStream = response.body().byteStream();
                            while ((bufferLength = inputStream.read(buffer)) > 0) {
                                outputStream.write(buffer, 0, bufferLength);
                            }
                            outputStream.flush();
                            publishProgress(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (inputStream != null)
                                    inputStream.close();
                            } catch (IOException e) {
                            }
                            try {
                                if (outputStream != null)
                                    outputStream.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}