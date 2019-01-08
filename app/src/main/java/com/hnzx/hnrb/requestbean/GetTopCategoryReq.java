package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetTopCategoryRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetTopCategoryReq extends BaseBeanArrayReq<GetTopCategoryRsp> {
    @Override
    public String myAddr() {
        return GetData.GetTopCategory;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetTopCategoryRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetTopCategoryRsp>>() {
        };
    }
}
