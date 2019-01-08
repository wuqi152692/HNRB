package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class GetInteractListRsp implements Serializable {
    /**
     * 活动
     * remained : 50
     * views : 5
     * type_name : 进行中
     * start_md : 04-06
     * start_hm : 09:20
     * created : 2017-04-05 09:18
     * title : 清明节踏春
     * thumb : http://manager.henandaily.cn/uploadfile/2017/0404/20170404072423600.jpg
     * type : activity
     * type_id : activity_8
     */

    public int remained;
    public int views;
    public String type_name;
    public String start_md;
    public String start_hm;
    public String created;
    public String title;
    public String thumb;
    public String type;
    public String type_id;

    /**
     * 话题
     * joined : 2
     */

    public int joined;


    /**
     * is_link : 0
     * link_url :
     * brief : 你喜欢狗狗吗
     * option1 : 喜欢
     * option2 : 不喜欢
     * option1_number : 16
     * option2_number : 0
     * start_time : 04-07 12:00
     * end_time : 04-10 12:00
     * is_voted : 1
     */

    public int is_link;
    public String link_url;
    public String brief;
    public String option1;
    public String option2;
    public int option1_number;
    public int option2_number;
    public String start_time;
    public String end_time;
    public int is_voted;
}
