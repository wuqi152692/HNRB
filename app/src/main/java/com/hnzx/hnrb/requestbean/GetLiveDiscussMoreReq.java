package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetTopicCommentMoreRsp;

/**
 * @author: mingancai
 * @Time: 2017/7/10 0010.
 */

public class GetLiveDiscussMoreReq extends BaseBeanArrayReq<GetTopicCommentMoreRsp> {
    public Object live_id, id;

    @Override
    public String myAddr() {
        return GetData.GetLiveDiscussMore;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetTopicCommentMoreRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetTopicCommentMoreRsp>>() {
        };
    }
}
