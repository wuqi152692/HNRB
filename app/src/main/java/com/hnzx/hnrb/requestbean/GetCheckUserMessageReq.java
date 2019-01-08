package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class GetCheckUserMessageReq extends BaseBeanReq<String> {
    public Object timestamp;

    @Override
    public String myAddr() {
        return GetData.GetCheckUserMessage;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>() {
        };
    }
}
