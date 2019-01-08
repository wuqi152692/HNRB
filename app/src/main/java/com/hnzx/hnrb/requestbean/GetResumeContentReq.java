package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetResumeContentRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/5 0005.
 */

public class GetResumeContentReq extends BaseBeanArrayReq<GetResumeContentRsp> {
    public Object resume_id;

    @Override
    public String myAddr() {
        return GetData.GetResumeContent;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetResumeContentRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetResumeContentRsp>>() {
        };
    }
}
