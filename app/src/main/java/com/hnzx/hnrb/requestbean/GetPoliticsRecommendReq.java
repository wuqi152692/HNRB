package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsRecommendRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetPoliticsRecommendReq extends BaseBeanArrayReq<GetPoliticsRecommendRsp> {
    public int offset = 0, number = 8;

    @Override
    public String myAddr() {
        return GetData.GetPoliticsRecommend;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetPoliticsRecommendRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetPoliticsRecommendRsp>>() {
        };
    }
}
