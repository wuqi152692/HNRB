package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/5/12 0012.
 */

public class GetMyTaskListRsp implements Serializable {

    /**
     * dayScore : 0
     * lists : [{"name":"每日签到","total":1,"user":0,"status":-1},{"name":"文章阅读","total":6,"user":0,"status":-1},{"name":"互动评论","total":6,"user":0,"status":-1},{"name":"转发次数","total":6,"user":0,"status":-1},{"name":"点赞次数","total":10,"user":0,"status":-1}]
     * rule : http://www.henandaily.cn
     */

    public int dayScore;
    public String rule;
    public List<ListsBean> lists;

    public static class ListsBean {
        /**
         * name : 每日签到
         * total : 1
         * user : 0
         * status : -1
         */

        public String name;
        public int total;
        public int user;
        public int status;
    }
}
