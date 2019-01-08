package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMyActivityRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class GetMyActivityReq extends BaseBeanArrayReq<GetMyActivityRsp> {
    public int offset, number;

    @Override
    public String myAddr() {
        return GetData.GetMyActivity;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetMyActivityRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetMyActivityRsp>>() {
        };
    }
}
