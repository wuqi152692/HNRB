package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetTopTenListReq extends BaseBeanArrayReq<GetLatestNewsRsp> {
    @Override
    public String myAddr() {
        return GetData.GetTop10List;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>>() {
        };
    }
}
