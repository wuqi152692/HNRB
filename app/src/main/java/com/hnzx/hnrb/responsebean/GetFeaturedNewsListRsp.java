package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class GetFeaturedNewsListRsp implements Serializable {

    /**
     * title : 孔昌生：抓理论学习抓关键环节抓自身建设推动全面从严治党责任落地见效
     * brief : 2月28日，省委组织部以落实全面从严治党责任为主题，举行部中心组（扩大）学习会议。
     * thumb : http://content.henandaily.cn/uploadfile/2017/0301/20170301100415732.jpg
     * content_id : content_412-39191-1
     * catname : 时政
     * type_name :
     * content_type : content_content
     * comment : 4
     * views : 72
     * url : http://www.henandaily.cn/content/szheng/hshse/2017/0301/36133.html
     * is_link : 0
     * link_url : http://www.henandaily.cn/content/szheng/hshse/2017/0301/36133.html
     * updated : 1490930625
     * created : 2017-04-12 16:00
     * internal_type : content_content
     * internal_id : content_126-36133-1
     * cat_id : content_412
     * topname : 高层动态
     */

    public String title = "";
    public String brief = "";
    public String thumb = "";
    public String content_id = "";
    public String catname = "";
    public String type_name = "";
    public String content_type = "";
    public int comment;
    public int views;
    public String url = "";
    public int is_link;
    public String link_url = "";
    public String updated = "";
    public String created = "";
    public String internal_type = "";
    public String internal_id = "";
    public String cat_id = "";
    public String topname = "";
    public String special_id = "";
    public String video_duration = "";
    public String zutu_total = "";

    @Override
    public String toString() {
        return "GetFeaturedNewsListRsp{" +
                "title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", thumb='" + thumb + '\'' +
                ", content_id='" + content_id + '\'' +
                ", catname='" + catname + '\'' +
                ", type_name='" + type_name + '\'' +
                ", content_type='" + content_type + '\'' +
                ", comment=" + comment +
                ", views=" + views +
                ", url='" + url + '\'' +
                ", is_link=" + is_link +
                ", link_url='" + link_url + '\'' +
                ", updated='" + updated + '\'' +
                ", created='" + created + '\'' +
                ", internal_type='" + internal_type + '\'' +
                ", internal_id='" + internal_id + '\'' +
                ", cat_id='" + cat_id + '\'' +
                ", topname='" + topname + '\'' +
                ", special_id='" + special_id + '\'' +
                ", video_duration='" + video_duration + '\'' +
                ", zutu_total='" + zutu_total + '\'' +
                '}';
    }
}
