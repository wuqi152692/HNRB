package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/27 0027.
 */

public class GetCategoryRsp implements Serializable {

    /**
     * name : 时政
     * catid : content_21
     * children : [{"cat_id":"content_23","catname":"大国政事"},{"cat_id":"content_8","catname":"省委关注"},{"cat_id":"content_27","catname":"授权发布"},{"cat_id":"content_246","catname":"热点要闻"},{"cat_id":"content_126","catname":"会声会色"},{"cat_id":"content_127","catname":"中岳观察"},{"cat_id":"content_128","catname":"金水河观澜"},{"cat_id":"content_129","catname":"清风中原"},{"cat_id":"content_130","catname":"人事任免"}]
     */

    public String name;
    public String catid;
    public List<ChildrenBean> children;

    public static class ChildrenBean {
        /**
         * cat_id : content_23
         * catname : 大国政事
         */

        public String cat_id;
        public String catname;
    }
}
