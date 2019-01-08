package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAdsRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/17 0017.
 */

public class GetAdsLoginReq extends BaseBeanReq<GetAdsRsp> {
    @Override
    public String myAddr() {
        return GetData.GetAdsLogin;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetAdsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetAdsRsp>>() {
        };
    }
}
