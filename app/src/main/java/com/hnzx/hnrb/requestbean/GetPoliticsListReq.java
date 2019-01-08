package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetPoliticsListReq extends BaseBeanArrayReq<GetPoliticsListRsp> {
    public int offset, number;

    @Override
    public String myAddr() {
        return GetData.GetPoliticsList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetPoliticsListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetPoliticsListRsp>>() {
        };
    }
}
