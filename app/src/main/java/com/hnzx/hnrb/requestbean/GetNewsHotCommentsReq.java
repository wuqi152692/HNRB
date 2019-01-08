package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetNewsCommentRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/10 0010.
 */

public class GetNewsHotCommentsReq extends BaseBeanReq<GetNewsCommentRsp> {
    public Object content_id;

    @Override
    public String myAddr() {
        return GetData.GetNewsHotComment;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetNewsCommentRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetNewsCommentRsp>>() {
        };
    }
}
