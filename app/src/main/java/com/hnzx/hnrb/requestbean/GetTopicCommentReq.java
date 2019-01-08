package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/7 0007.
 */

public class GetTopicCommentReq extends BaseBeanReq<GetNewsCommentRsp> {
    public Object topic_id, offset, number;

    @Override
    public String myAddr() {
        return GetData.GetTopicCommentList;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetNewsCommentRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetNewsCommentRsp>>() {
        };
    }
}
