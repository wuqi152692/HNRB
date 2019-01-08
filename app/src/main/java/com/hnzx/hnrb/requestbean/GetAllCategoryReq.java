package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetAllCategoryRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/12 0012.
 */

public class GetAllCategoryReq extends BaseBeanArrayReq<GetAllCategoryRsp> {
    @Override
    public String myAddr() {
        return GetData.GetNewsSideBar;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetAllCategoryRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetAllCategoryRsp>>() {
        };
    }
}
