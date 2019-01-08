package com.hnzx.hnrb.responsebean;

import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class GetVideoRelationsRsp {

    /**
     * keyword : 荣誉
     * relations : [{"title":"你的样子","thumb":"http://content.henandaily.cn/uploadfile/2017/0122/20170122103207587.png","content_id":"content_415-39202-1"},{"title":"人大代表的小目标","thumb":"http://content.henandaily.cn/uploadfile/2017/0309/20170309120158851.jpg","content_id":"content_415-39201-1"}]
     */

    public String keyword;
    public List<RelationsBean> relations;

    public static class RelationsBean {
        /**
         * title : 你的样子
         * thumb : http://content.henandaily.cn/uploadfile/2017/0122/20170122103207587.png
         * content_id : content_415-39202-1
         */

        public String title;
        public String thumb;
        public String content_id;
    }
}
