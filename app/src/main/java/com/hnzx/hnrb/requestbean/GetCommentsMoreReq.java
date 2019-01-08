package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetTopicCommentMoreRsp;

/**
 * @author: mingancai
 * @Time: 2017/7/10 0010.
 */

public class GetCommentsMoreReq extends BaseBeanArrayReq<GetTopicCommentMoreRsp> {
    public Object content_id, id;

    @Override
    public String myAddr() {
        return GetData.GetCommentsMore;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetTopicCommentMoreRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetTopicCommentMoreRsp>>() {
        };
    }
}
