package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetResumeRelationRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class GetResumeRelationReq extends BaseBeanArrayReq<GetResumeRelationRsp> {
    public Object resume_id, offset, number;

    @Override
    public String myAddr() {
        return GetData.GetResumeRelation;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetResumeRelationRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetResumeRelationRsp>>() {
        };
    }
}
