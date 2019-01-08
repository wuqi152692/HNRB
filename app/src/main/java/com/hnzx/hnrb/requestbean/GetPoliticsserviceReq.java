package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsserviceRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetPoliticsserviceReq extends BaseBeanArrayReq<GetPoliticsserviceRsp> {
    @Override
    public String myAddr() {
        return GetData.GetPoliticsservice;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetPoliticsserviceRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetPoliticsserviceRsp>>() {
        };
    }
}
