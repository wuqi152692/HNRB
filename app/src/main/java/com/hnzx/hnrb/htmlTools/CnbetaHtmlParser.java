package com.hnzx.hnrb.htmlTools;

import android.content.Context;
import android.webkit.WebView;

import com.hnzx.hnrb.tools.GetHtmlData;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CnbetaHtmlParser extends HtmlParser {


    public CnbetaHtmlParser(WebView webView, String url,
                            Context context) {
        super(webView, url, context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String handleDocument(Document doc) {
//        Elements imgsE = doc.getElementsByTag("img");
//        for (Element element : imgsE) {
//            String image = element.attr("ori_link");
//            element.attr("src", image);
//        }
        Elements audioE = doc.getElementsByTag("audio");
        for (Element element : audioE) {
            String video = element.attr("src");
            element.attr("src", video);
        }
//        Elements vediosE = doc.getElementsByTag("video");
//        for (Element element : vediosE) {
//            String video = element.attr("src");
//            element.attr("src", video);
//        }
        return GetHtmlData.getHtmlData2(doc.html());//會造成複製的時候出現空格
    }

}
