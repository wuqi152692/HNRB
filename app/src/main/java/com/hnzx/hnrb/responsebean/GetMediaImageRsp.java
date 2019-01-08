package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class GetMediaImageRsp implements Serializable {

    /**
     * title : 组图测试3
     * thumb : http://manager.henandaily.cn/uploadfile/2017/0404/20170404072423678.jpg
     * total : 3
     * content_id : content_414-39199-1
     * views : 0
     * created : 2017-04-04 19:42
     */

    public String title;
    public String thumb;
    public int total;
    public String content_id;
    public int views;
    public String created;
}
