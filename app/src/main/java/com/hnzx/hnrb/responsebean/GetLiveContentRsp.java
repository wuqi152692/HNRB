package com.hnzx.hnrb.responsebean;

import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/14 0014.
 */

public class GetLiveContentRsp {

    /**
     * joined : 1
     * type : proceeding
     * live_id : live_71
     * comment : 22
     * thumb : http://content.henandaily.cn/uploadfile/2017/0301/20170301084352156.jpg
     * title : 两会大直播｜国务院总理李克强答中外记者问
     * url : http://www.henandaily.cn/live/71.html
     * brief : 2017年全国两会，我们用持续滚动播报的方式，为您带来河南报业全媒体记者采自会场内外的新鲜资讯、各路权威媒体发布的两会热闻。河南日报客户端“两会大直播”，随时帮您直达两会现场。
     * host : {"avatar":"http://content.henandaily.cn/uploadfile/2017/0302/20170302014121718.png","username":"河南日报客户端","hoster_id":0}
     * myvideo : [{"filename":"人大代表的2017小目标","filepath":"uploadfile/myvideo/20170308/1488988664440.mp4","filethumb":"http://content.henandaily.cn/uploadfile/2017/0309/20170309120158851.jpg","index":1,"total":1}]
     * mylive : [{"filename":"","filepath":"http://livecdn1.news.cn/Live_MajorEvent04Phone/manifest.m3u8","filepath1":"http://livecdn1.news.cn/Live_MajorEvent04Phone/manifest.m3u8","filethumb":""}]
     */

    public int joined;
    public String type;
    public String live_id;
    public String comment;
    public String thumb;
    public String title;
    public String url;
    public String brief;
    public HostBean host;
    public List<MyvideoBean> myvideo;
    public List<MyliveBean> mylive;

    public static class HostBean {
        /**
         * avatar : http://content.henandaily.cn/uploadfile/2017/0302/20170302014121718.png
         * username : 河南日报客户端
         * hoster_id : 0
         */

        public String avatar;
        public String username;
        public int hoster_id;
    }

    public static class MyvideoBean {
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

    public static class MyliveBean {
        /**
         * filename :
         * filepath : http://livecdn1.news.cn/Live_MajorEvent04Phone/manifest.m3u8
         * filepath1 : http://livecdn1.news.cn/Live_MajorEvent04Phone/manifest.m3u8
         * filethumb :
         */

        public String filename;
        public String filepath;
        public String filepath1;
        public String filethumb;
    }
}
