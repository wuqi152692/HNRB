package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/4/1 0001.
 */

public class GetLiveListRsp implements Serializable {

    /**
     * enabled : 1
     * start_time : 1488523800
     * current_time : 1492141385
     * thumb : http://content.henandaily.cn/uploadfile/2017/0303/20170303020217830.jpg
     * title : 独家直播｜在京豫籍专家、院士、企业家都有谁？他们正为河南发展支招
     * live_id : live_72
     * type : video
     * created : 2017-03-03 14:54
     */

    public int enabled;
    public int start_time;
    public int current_time;
    public String thumb;
    public String title;
    public String live_id;
    public String type;
    public String created;
}
