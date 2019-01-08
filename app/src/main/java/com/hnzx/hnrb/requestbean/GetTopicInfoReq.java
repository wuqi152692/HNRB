package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetTopicInfoRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/7 0007.
 */

public class GetTopicInfoReq extends BaseBeanReq<GetTopicInfoRsp> {
    public Object topic_id;

    @Override
    public String myAddr() {
        return GetData.GetTopicInfo;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetTopicInfoRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetTopicInfoRsp>>() {
        };
    }
}
