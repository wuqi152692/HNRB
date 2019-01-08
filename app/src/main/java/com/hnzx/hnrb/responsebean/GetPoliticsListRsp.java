package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetPoliticsListRsp implements Serializable {

    /**
     * name : 厅局
     * tuijian : [{"cat_id":"content_29","catname":"社科在线","thumb":"http://125.46.11.87/uploadfile/2016/0201/20160201043729329.jpg","is_ordered":0,"ordered":0}]
     */

    public String name;
    public List<TuijianBean> tuijian;

    public static class TuijianBean implements Serializable {
        /**
         * cat_id : content_29
         * catname : 社科在线
         * thumb : http://125.46.11.87/uploadfile/2016/0201/20160201043729329.jpg
         * is_ordered : 0
         * ordered : 0
         */

        public String cat_id;
        public String catname;
        public String thumb;
        public int is_ordered;
        public int ordered;
    }
}
