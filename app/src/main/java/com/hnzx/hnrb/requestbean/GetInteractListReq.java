package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetInteractListRsp;
import com.hnzx.hnrb.responsebean.GetMediaImageRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class GetInteractListReq extends BaseBeanArrayReq<GetInteractListRsp> {
    public Object device_token, offset, number;

    @Override
    public String myAddr() {
        return GetData.GetInteractList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetInteractListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetInteractListRsp>>() {
        };
    }
}
