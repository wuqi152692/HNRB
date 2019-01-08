package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class GetFeaturedNewsListReq extends BaseBeanArrayReq<GetFeaturedNewsListRsp> {
    public int offset, number;

    @Override
    public String myAddr() {
        return GetData.GetFeaturedNewsList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>>() {
        };
    }
}
