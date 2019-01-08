package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author: mingancai
 * @Time: 2017/7/7 0007.
 */

public class GetTopicCommentMoreRsp implements Serializable {

    /**
     * avatar : http://content.henandaily.cn/uploadfile/avatar/1/1/47/9006d268f9411966771e5b5ff952d846d6.jpg
     * is_vip : 0
     * username : Melody
     * content : 搞好关系
     * support : 0
     * imgs : []
     * comment_id : 18973
     * created : 3小时前
     */

    public String avatar;
    public int is_vip;
    public String username;
    public String content;
    public String replied;
    public int support;
    public int comment_id;
    public String created;
    public ArrayList<String> imgs;
    public boolean isSupport;
}
