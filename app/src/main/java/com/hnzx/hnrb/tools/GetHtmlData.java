package com.hnzx.hnrb.tools;

import java.util.regex.Pattern;

public class GetHtmlData {

    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


    public static String getHtmlData2(String source) {
        return "<html><head><style type=\"text/css\">img{max-width:100% !important;height:auto;} </style></head><body style=\"text-align:justify;\">" + source
                + "</body></html>";
    }

    public static String getHtmlData3(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}body{text-align:justify;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public static String getSingleColumn(String source) {
        return "<html><head><style type=\"text/css\">img{max-width:100%;height:auto;} </style></head><body style=\"text-align:justify;text-justify:inter-ideograph;\">" + source
                + "</body><script>  function text(e){e=e.childNodes||e;for(var j=0;j<e.length;j++){if(e[j].nodeType!=1){e[j].nodeValue=e[j].nodeValue.split(\"\").join(\" \")}else{text(e[j].childNodes)}}}var boxs=document.getElementsByTagName(\"p\");for(var p in boxs) {var box=boxs[p];box.style.letterSpacing = '-.15em';text(box)}</script></html>";
    }

    //正则 HTML 标签 得到 纯文本
    public static String HtmlToText(String inputString) {
        String htmlStr = inputString; //含html标签的字符串
        String textStr = "";
        Pattern p_script;
        java.util.regex.Matcher m_script;
        Pattern p_style;
        java.util.regex.Matcher m_style;
        Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签
            htmlStr = htmlStr.replaceAll("&nbsp;", "");
            htmlStr = htmlStr.replaceAll("&mdash;", "");
            htmlStr = htmlStr.replaceAll("&ldquo;", "");
            htmlStr = htmlStr.replaceAll("&rdquo;", "");
            textStr = htmlStr;
        } catch (Exception e) {

        }
        return textStr;//返回文本字符串
    }
}
