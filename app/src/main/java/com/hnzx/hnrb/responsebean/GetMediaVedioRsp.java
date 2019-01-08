package com.hnzx.hnrb.responsebean;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class GetMediaVedioRsp implements Serializable {
    /**
     * video_id : 6
     * title : 人大代表的2017小目标
     * thumb : http://content.henandaily.cn/uploadfile/2017/0309/20170309120158851.jpg
     * views : 2
     * url : http://manager.henandaily.cn/uploadfile/myvideo/20170308/1488988664440.mp4
     * duration : 0:0
     * created : 2017-03-09 00:02
     */

    public String video_id;
    public String title;
    public String thumb;
    public int views;
    public String url;
    public String duration;
    public String created;
    public String myvideo;
}
