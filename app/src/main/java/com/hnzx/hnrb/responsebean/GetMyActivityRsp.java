package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class GetMyActivityRsp implements Serializable {
    public String activity_id;
    public String title;
    public String thumb;
    public String type_name;
    public int views;
    public String created;
    public String is_link;
    public String link_url;
}
