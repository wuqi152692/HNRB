package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetHighDynamicListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/13 0013.
 */

public class GetHighDynamicListReq extends BaseBeanArrayReq<GetHighDynamicListRsp> {
    public Object index, offset, number;

    @Override
    public String myAddr() {
        return GetData.GetHighDynamicList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetHighDynamicListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetHighDynamicListRsp>>() {
        };
    }
}
