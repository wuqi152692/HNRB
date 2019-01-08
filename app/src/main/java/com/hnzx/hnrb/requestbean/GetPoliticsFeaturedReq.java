package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetPoliticsFeaturedRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/27 0027.
 */

public class GetPoliticsFeaturedReq extends BaseBeanReq<GetPoliticsFeaturedRsp> {
    @Override
    public String myAddr() {
        return GetData.getPoliticsFeatured;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetPoliticsFeaturedRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetPoliticsFeaturedRsp>>() {
        };
    }
}
