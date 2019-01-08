package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetCategoryListRsp;
import com.hnzx.hnrb.responsebean.GetFeaturedNewsListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/12 0012.
 */

public class GetCategoryListReq extends BaseBeanArrayReq<GetFeaturedNewsListRsp> {
    public Object cat_id, offset, number;

    @Override
    public String myAddr() {
        return GetData.GetCategoryList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetFeaturedNewsListRsp>>() {
        };
    }
}
