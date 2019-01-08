package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class GetMineCollectionRsp implements Serializable {
    /**
     * fav_id : 7384
     * addtime : 2017-05-10 15:46
     * title : 新闻详情
     * content_id : content_246-39206-1
     * catname : 热点要闻
     * views : 648
     * thumb : http://125.46.11.56/uploadfile/2017/0502/20170502082116919.jpg
     * content_type : content_content
     */

    public String fav_id;
    public String addtime;
    public String title;
    public String content_id;
    public int views;
    public String topname;
    public String catname;
    public String thumb;
    public String content_type;
}
