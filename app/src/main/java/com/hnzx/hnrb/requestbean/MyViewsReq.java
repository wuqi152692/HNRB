package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.MyViewsRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/12 0012.
 */

public class MyViewsReq extends BaseBeanReq<MyViewsRsp> {
    public Object time;

    @Override
    public String myAddr() {
        return GetData.GetMyViews;
    }

    @Override
    public TypeReference<BaseBeanRsp<MyViewsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<MyViewsRsp>>() {
        };
    }
}
