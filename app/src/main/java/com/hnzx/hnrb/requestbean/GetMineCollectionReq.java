package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMineCollectionRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/15 0015.
 */

public class GetMineCollectionReq extends BaseBeanArrayReq<GetMineCollectionRsp> {
    public int offset, number;

    @Override
    public String myAddr() {
        return GetData.GetMineCollection;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetMineCollectionRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetMineCollectionRsp>>() {
        };
    }
}
