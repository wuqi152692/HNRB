package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/14 0014.
 */

public class GetLiveDiscussListReq extends BaseBeanReq<GetNewsCommentRsp> {
    public Object live_id, offset, number;

    @Override
    public String myAddr() {
        return GetData.GetLiveDiscussList;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetNewsCommentRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetNewsCommentRsp>>() {
        };
    }
}
