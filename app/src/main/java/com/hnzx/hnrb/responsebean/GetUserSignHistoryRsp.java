package com.hnzx.hnrb.responsebean;

import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/5/12 0012.
 */

public class GetUserSignHistoryRsp {

    /**
     * date : 2017年5月
     * list : [{"day_no":1,"is_signed":0,"is_today":0,"week_no":1},{"day_no":2,"is_signed":0,"is_today":0,"week_no":2},{"day_no":3,"is_signed":0,"is_today":0,"week_no":3},{"day_no":4,"is_signed":0,"is_today":0,"week_no":4},{"day_no":5,"is_signed":0,"is_today":0,"week_no":5},{"day_no":6,"is_signed":0,"is_today":0,"week_no":6},{"day_no":7,"is_signed":0,"is_today":0,"week_no":0},{"day_no":8,"is_signed":0,"is_today":0,"week_no":1},{"day_no":9,"is_signed":0,"is_today":0,"week_no":2},{"day_no":10,"is_signed":0,"is_today":0,"week_no":3},{"day_no":11,"is_signed":0,"is_today":0,"week_no":4},{"day_no":12,"is_signed":0,"is_today":1,"week_no":5},{"day_no":13,"is_signed":0,"is_today":0,"week_no":6},{"day_no":14,"is_signed":0,"is_today":0,"week_no":0},{"day_no":15,"is_signed":0,"is_today":0,"week_no":1},{"day_no":16,"is_signed":0,"is_today":0,"week_no":2},{"day_no":17,"is_signed":0,"is_today":0,"week_no":3},{"day_no":18,"is_signed":0,"is_today":0,"week_no":4},{"day_no":19,"is_signed":0,"is_today":0,"week_no":5},{"day_no":20,"is_signed":0,"is_today":0,"week_no":6},{"day_no":21,"is_signed":0,"is_today":0,"week_no":0},{"day_no":22,"is_signed":0,"is_today":0,"week_no":1},{"day_no":23,"is_signed":0,"is_today":0,"week_no":2},{"day_no":24,"is_signed":0,"is_today":0,"week_no":3},{"day_no":25,"is_signed":0,"is_today":0,"week_no":4},{"day_no":26,"is_signed":0,"is_today":0,"week_no":5},{"day_no":27,"is_signed":0,"is_today":0,"week_no":6},{"day_no":28,"is_signed":0,"is_today":0,"week_no":0},{"day_no":29,"is_signed":0,"is_today":0,"week_no":1},{"day_no":30,"is_signed":0,"is_today":0,"week_no":2},{"day_no":31,"is_signed":0,"is_today":0,"week_no":3}]
     */

    public String date;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * day_no : 1
         * is_signed : 0
         * is_today : 0
         * week_no : 1
         */

        public int day_no;
        public int is_signed;
        public int is_today;
        public int week_no;
    }
}
