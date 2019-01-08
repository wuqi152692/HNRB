package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/13 0013.
 */

public class GetSearchDataReq extends BaseBeanArrayReq<GetFeaturedNewsListRsp> {
    public Object offset, number, content;

    @Override
    public String myAddr() {
        return GetData.getSearchData;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>>() {
        };
    }
}
