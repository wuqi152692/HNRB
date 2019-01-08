package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetPushListReq extends BaseBeanArrayReq<GetLatestNewsRsp> {
    public int offset, number;

    @Override
    public String myAddr() {
        return GetData.GetPushList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>>() {
        };
    }
}
