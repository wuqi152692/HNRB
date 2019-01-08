package com.hnzx.hnrb.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: mingancai
 * @Time: 2017/4/27 0027.
 */

public class GetPoliticsFeaturedRsp {

    public List<ShujixinxiangBean> shujixinxiang;
    public List<JingpintuijianBean> jingpintuijian;

    public static class ShujixinxiangBean implements Serializable {
        /**
         * thumb : http://content.henandaily.cn/uploadfile/2017/0426/20170426043909122.jpg
         * url : http://wsxfdt.hnxf.gov.cn/sjxx/
         * name : 书记信箱
         */

        public String thumb;
        public String url;
        public String name;
    }

    public static class JingpintuijianBean implements Serializable {
        /**
         * name : 空气质量
         * thumb : http://content.henandaily.cn/uploadfile/2016/0314/20160314025943576.png
         * url : http://pm25.moji.com/
         */

        public String name;
        public String thumb;
        public String url;
    }
}
