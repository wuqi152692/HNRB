package com.hnzx.hnrb.htmlTools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.webkit.WebView;

import com.hnzx.hnrb.network.Algorithm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class HtmlParser extends AsyncTask<Void, Void, String> {

    private String mContent;
    private WebView webView;
    private Context mContext;
    public static String Js2JavaInterfaceName = "JsUseJava";
    public List<String> imgUrls = new ArrayList<String>();

    public HtmlParser(WebView wevView, String url, Context context) {
        this.webView = wevView;
        mContent = url;
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {

        Document doc = null;
        imgUrls.clear();

        doc = Jsoup.parse(mContent);


        if (doc == null)
            return null;

        try {
            handleImageClickEvent(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String htmlText = handleDocument(doc);

        return htmlText;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    private void handleImageClickEvent(Document doc) throws Exception {

        Elements es = doc.getElementsByTag("img");

        for (Element e : es) {
            String imgUrl = e.attr("src");
            imgUrls.add(imgUrl);
            String imgName;
            File file = new File(imgUrl);
            imgName = file.getName();

            String filePath = Environment.getExternalStorageDirectory() + DownloadWebImgTask.location + Algorithm.Md5Encrypt(imgUrl, "UTF-8") + ".bin";
            e.attr("src", "file:///android_asset/web_logo.png");
            e.attr("src_link", "file:///" + filePath);
            e.attr("onerror", "this.src='file:///android_asset/web_logo.png'");
            e.attr("ori_link", imgUrl);
            Element parent = e.parent();
            if ((parent == null || !"a".equals(parent.nodeName())) || !imgName.endsWith(".gif")) {
                String str = "window." + Js2JavaInterfaceName + ".setImgSrc('"
                        + filePath + "')";
                e.attr("onclick", str);
            }
        }
    }

    protected abstract String handleDocument(Document doc);

    @Override
    protected void onPostExecute(String result) {
        webView.loadDataWithBaseURL(null, result, "text/html", "utf-8", null);
        super.onPostExecute(result);
    }

}
