package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLiveHallListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/14 0014.
 */

public class GetLiveHallListReq extends BaseBeanArrayReq<GetLiveHallListRsp> {
    public Object live_id, offset, number, sortorder;

    @Override
    public String myAddr() {
        return GetData.GetLiveHallList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetLiveHallListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetLiveHallListRsp>>() {
        };
    }
}
