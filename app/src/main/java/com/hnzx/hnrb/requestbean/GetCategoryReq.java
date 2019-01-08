package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetCategoryRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/27 0027.
 */

public class GetCategoryReq extends BaseBeanArrayReq<GetCategoryRsp> {
    @Override
    public String myAddr() {
        return GetData.GetCategory;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetCategoryRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetCategoryRsp>>() {
        };
    }
}
