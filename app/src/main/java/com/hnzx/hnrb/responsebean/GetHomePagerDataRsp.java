package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetHomePagerDataRsp {

    public List<FocusBean> focus;
    public List<DynamicsBean> dynamics;
    public List<GetFeaturedNewsListRsp> featured;
    public ArrayList<String> tags;

    public static class FocusBean implements Serializable {
        /**
         * title : 最高人民法院工作报告
         * brief : 最高人民法院工作报告
         * thumb : http://content.henandaily.cn/uploadfile/2017/0320/20170320080238305.jpg
         * content_id : content_411-39179-1
         * catname : 时政
         * type_name :
         * content_type : content_content
         * comment : 0
         * views : 0
         * url : http://www.henandaily.cn/content/szheng/sqfbu/2017/0320/38520.html
         * is_link : 0
         * link_url : http://www.henandaily.cn/content/szheng/sqfbu/2017/0320/38520.html
         * updated : 1490930467
         * created : 2017-03-20 07:58
         * internal_type : content_content
         * internal_id : content_27-38520-1
         * cat_id : content_411
         */

        public String title;
        public String brief;
        public String thumb;
        public String content_id;
        public String catname;
        public String type_name;
        public String content_type;
        public int comment;
        public int views;
        public String url;
        public int is_link;
        public String link_url;
        public String updated;
        public String created;
        public String internal_type;
        public String internal_id;
        public String cat_id;
    }

    public static class DynamicsBean implements Serializable {
        /**
         * title : 媒体：火箭军取代第二炮兵成为中国人民解放军第四军种
         * brief : 新华社北京12月31日电　新年前夕，国家主席习近平通过中国国际广播电台、中央人民广播电台、中央电视台，发表了2016年新年贺词
         * thumb : http://125.46.11.87/uploadfile/2016/0126/20160126045659341.png
         * content_id : content_8-16-1
         * catname : 省委关注
         * type_name :
         * content_type : content_hybrid
         * comment : 0
         * views : 0
         * url : http://www.henandaily.cn/content/szheng/swgzhu/2016/0102/16.html
         * is_link : 0
         * link_url :
         * updated : 1455593059
         * created : 2016-01-02 14:12
         * internal_type : none
         * internal_id : 0
         * cat_id : content_8
         * tag : 习近平
         */

        public String title;
        public String brief;
        public String thumb;
        public String content_id;
        public String catname;
        public String type_name;
        public String content_type;
        public int comment;
        public int views;
        public String url;
        public int is_link;
        public String link_url;
        public String updated;
        public String created;
        public String internal_type;
        public String internal_id;
        public String cat_id;
        public String tag;
    }
}
