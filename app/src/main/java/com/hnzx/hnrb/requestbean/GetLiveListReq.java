package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLiveListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/14 0014.
 */

public class GetLiveListReq extends BaseBeanArrayReq<GetLiveListRsp> {
    public Object number, offset;

    @Override
    public String myAddr() {
        return GetData.GetLiveList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetLiveListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetLiveListRsp>>() {
        };
    }
}
