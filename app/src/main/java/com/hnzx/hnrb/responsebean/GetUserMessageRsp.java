package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class GetUserMessageRsp implements Serializable {
    public boolean isSupport;

    /**
     * comment_id : 12474
     * created : 2017-04-10 15:28
     * support : 0
     * content : 今天星期日
     * content_id : content_27-39124-1
     * timestamp : 1491809298
     * avatar : http://125.46.11.56/uploadfile/avatar/1/1/47/90237f9eb88db134d2ce73a5c23e340c0a.jpg
     * is_vip : 0
     * username : Melody
     */

    public String comment_id;
    public String created;
    public String support;
    public String content;
    public String content_id;
    public String timestamp;
    public String avatar;
    public int is_vip;
    public String username;
}
