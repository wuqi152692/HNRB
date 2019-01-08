package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetLiveContentRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/14 0014.
 */

public class GetLiveContentReq extends BaseBeanReq<GetLiveContentRsp> {
    public Object live_id;

    @Override
    public String myAddr() {
        return GetData.GetLiveContentById;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetLiveContentRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetLiveContentRsp>>() {
        };
    }
}
