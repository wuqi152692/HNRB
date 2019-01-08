package com.hnzx.hnrb.responsebean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/10 0010.
 */

public class CommentsBean {

    /**
     * avatar : http://manager.henandaily.cn/uploadfile/avatar/1/1/1/90e528008fbb3f81eed49cb2112a0b938f.jpg
     * is_vip : 0
     * username : hello
     * content : 话题评论1
     * support : 10
     * imgs : []
     * comment_id : 9
     * created : 2017-04-05 09:59
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
    public String type;
    public String position;
}
