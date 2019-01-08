package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/7 0007.
 */

public class GetNewsDetalisRsp implements Serializable {
    /**
     * title : 全省政协开展水污染防治攻坚专项民主监督
     * description : 全省政协开展水污染防治攻坚专项民主监督
     * url : http://www.henandaily.cn/content/szheng/swgzhu/2017/0322/DIIHH.html
     * inputtime : 2017-03-22 07:32
     * support : 0
     * reporters :
     * type : hndaily
     * content : <div style='font-size:18px;color:#696969;line-height:175%'></div>
     * zutu : []
     * myvideo : []
     * mylive : []
     * copyfrom : 河南日报
     * allow_comment : 1
     * voteid : 0
     * content_id : content_116-38899-1
     * catname : 叶冬松
     * cat_id : content_116
     * author : []
     * editor :  马小刚
     * comment : 0
     * form_url :
     * is_favor : 0
     */

    public String title;
    public String brief;
    public String url;
    public String inputtime;
    public String support;
    public String reporters;
    public String type;
    public String content;
    public String copyfrom;
    public int allow_comment;
    public String voteid;
    public String content_id;
    public String catname;
    public String cat_id;
    public String editor;
    public int comment;
    public int shown_more;
    public String form_url;
    public int is_favor;
    public int is_ordered;
    public List<ZutuBean> zutu;
    public List<MyvideoBean> myvideo;
    public List<MyliveBean> mylive;
    public List<AuthorBean> author;
    public GetAdsRsp guanggao;

    public static class ZutuBean implements Serializable {
        /**
         * title :
         * link : #
         * img : http://manager.henandaily.cn/uploadfile/2017/0404/20170404072424860.jpg
         * index : 1
         * total : 4
         * brief :
         */

        public String title;
        public String link;
        public String img;
        public int index;
        public int total;
        public String brief;
    }

    public static class MyvideoBean implements Serializable {
        /**
         * filename : 人大代表的2017小目标
         * filepath : uploadfile/myvideo/20170308/1488988664440.mp4
         * filethumb : http://content.henandaily.cn/uploadfile/2017/0309/20170309120158851.jpg
         * index : 1
         * total : 1
         */

        public String filename;
        public String filepath;
        public String filethumb;
        public int index;
        public int total;
    }

    public static class MyliveBean implements Serializable {
        /**
         * filename : 直播
         * filepath : http://www.henandaily.cn/uploadfile/myvideo/20170308/1488988664440.mp4
         * filethumb : http://manager.henandaily.cn/uploadfile/2017/0404/20170404072423600.jpg
         */

        public String filename;
        public String filepath;
        public String filethumb;
    }

    public static class AuthorBean implements Serializable {
        /**
         * avatar :
         * name : 平萍
         * author_id : author_92
         * type_name : 作者
         */

        public String avatar;
        public String name;
        public String author_id;
        public String type_name;
    }
}
