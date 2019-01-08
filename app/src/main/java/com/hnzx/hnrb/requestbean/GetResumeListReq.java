package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetResumeListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class GetResumeListReq extends BaseBeanArrayReq<GetResumeListRsp> {
    public Object number, offset;

    @Override
    public String myAddr() {
        return GetData.GetResumeList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetResumeListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetResumeListRsp>>() {
        };
    }
}
