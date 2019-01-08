package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetActivityDetailsRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/10 0010.
 */

public class GetActivityDetailsReq extends BaseBeanReq<GetActivityDetailsRsp> {
    public String activity_id;

    @Override
    public String myAddr() {
        return GetData.GetActivityDetails;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetActivityDetailsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetActivityDetailsRsp>>() {
        };
    }
}
