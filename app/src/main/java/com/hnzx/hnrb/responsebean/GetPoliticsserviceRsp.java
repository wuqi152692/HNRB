package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetPoliticsserviceRsp implements Serializable {

    /**
     * name : 政务
     * children : [{"name":"空气质量","logo":"http://content.henandaily.cn/uploadfile/2016/0314/20160314025943576.png","url":"http://pm25.moji.com/","is_link":"1"},{"name":"航班查询","logo":"http://content.henandaily.cn/uploadfile/2016/0314/20160314025934281.png","url":"http://flights.ctrip.com/schedule/","is_link":"1"},{"name":"生活服务","logo":"http://content.henandaily.cn/uploadfile/2016/0314/20160314025923923.png","url":"http://m.map.haosou.com","is_link":"1"},{"name":"车票查询","logo":"http://content.henandaily.cn/uploadfile/2016/0314/20160314025905706.png","url":"http://m.hn96520.com","is_link":"1"},{"name":"电话本","logo":"http://content.henandaily.cn/uploadfile/2016/0314/20160314025844599.png","url":"","is_link":"0"},{"name":"律兜","logo":"http://content.henandaily.cn/uploadfile/2017/0208/20170208023353135.jpg","url":"http://www.ilvdo.com/newmob/consultstep1?type=app-hnrb","is_link":"0"}]
     */

    public String name;
    public List<ChildrenBean> children;

    public static class ChildrenBean implements Serializable {
        /**
         * name : 空气质量
         * logo : http://content.henandaily.cn/uploadfile/2016/0314/20160314025943576.png
         * url : http://pm25.moji.com/
         * is_link : 1
         */

        public String name;
        public String logo;
        public String url;
        public String is_link;
    }
}
