package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAdsRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/17 0017.
 */

public class GetAdsIndexReq extends BaseBeanReq<GetAdsRsp> {
    @Override
    public String myAddr() {
        return GetData.GetAdsIndexBean;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetAdsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetAdsRsp>>() {
        };
    }
}
