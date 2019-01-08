package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetTopicCommentMoreRsp;

/**
 * @author: mingancai
 * @Time: 2017/7/7 0007.
 */

public class GetTopicCommentMoreReq extends BaseBeanArrayReq<GetTopicCommentMoreRsp> {
    public Object id, topic_id;

    @Override
    public String myAddr() {
        return GetData.GetTopicCommentMore;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetTopicCommentMoreRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetTopicCommentMoreRsp>>() {
        };
    }
}
