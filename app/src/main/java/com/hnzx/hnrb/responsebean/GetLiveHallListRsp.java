package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/14 0014.
 */

public class GetLiveHallListRsp implements Serializable {

    /**
     * type : host
     * comment_id : 4902
     * username : 两会发布
     * position :
     * avatar : http://content.henandaily.cn/uploadfile/2017/0302/20170302025829539.jpg
     * user_id : 0
     * support : 0
     * created : 2017-03-15 13:22
     * content : 新华网、中国政府网：本场直播到此结束，谢谢。
     * imgs : ["http://content.henandaily.cn/uploadfile/2017/0302/20170302025829539.jpg","http://content.henandaily.cn/uploadfile/2017/0302/20170302025829539.jpg"]
     * myvideo : [{"filename":"人大代表的2017小目标","filepath":"uploadfile/myvideo/20170308/1488988664440.mp4","filethumb":"http://content.henandaily.cn/uploadfile/2017/0309/20170309120158851.jpg","index":1,"total":1}]
     * mylive : [{"filename":"","filepath":"http://livecdn1.news.cn/Live_MajorEvent04Phone/manifest.m3u8","filepath1":"http://livecdn1.news.cn/Live_MajorEvent04Phone/manifest.m3u8","filethumb":""}]
     */

    public String type;
    public String comment_id;
    public String username;
    public String position;
    public String avatar;
    public String user_id;
    public String support;
    public String created;
    public String content;
    public ArrayList<String> imgs;
    public List<MyvideoBean> myvideo;
    public List<MyliveBean> mylive;

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
