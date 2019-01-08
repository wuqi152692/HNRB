package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetUserMessageRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class GetUserMessageReq extends BaseBeanArrayReq<GetUserMessageRsp> {
    public Object number, offset;

    @Override
    public String myAddr() {
        return GetData.GetUserMessage;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetUserMessageRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetUserMessageRsp>>() {
        };
    }
}
