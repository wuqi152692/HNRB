package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/12 0012.
 */

public class GetLatestNewsReq extends BaseBeanArrayReq<GetLatestNewsRsp> {
    public Object offset, number;

    @Override
    public String myAddr() {
        return GetData.GetLatestNews;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>>() {
        };
    }
}
