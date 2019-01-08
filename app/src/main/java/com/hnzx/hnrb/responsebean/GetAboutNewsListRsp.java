package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/4/10 0010.
 */

public class GetAboutNewsListRsp implements Serializable {
    public String content_id, content_type, title, link_url, thumb;
    public int is_link;
    /**
     * catname : 授权发布
     * topname : 时政
     * created : 03-21
     * views : 0
     */

    public String catname;
    public String topname;
    public String created;
    public int views;
}
