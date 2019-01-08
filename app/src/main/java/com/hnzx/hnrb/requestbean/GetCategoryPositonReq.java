package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/24 0024.
 */

public class GetCategoryPositonReq extends BaseBeanArrayReq<GetFeaturedNewsListRsp> {
    public String cat_id;

    @Override
    public String myAddr() {
        return GetData.GetCategoryPositon;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>>() {
        };
    }
}
