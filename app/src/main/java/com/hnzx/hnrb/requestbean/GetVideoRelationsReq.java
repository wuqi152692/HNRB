package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetVideoRelationsRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class GetVideoRelationsReq extends BaseBeanReq<GetVideoRelationsRsp> {
    public String content_id;

    @Override
    public String myAddr() {
        return GetData.GetVideoRelations;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetVideoRelationsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetVideoRelationsRsp>>() {
        };
    }
}
