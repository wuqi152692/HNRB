package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetNewsDetalisRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/7 0007.
 */

public class GetNewsDetailsReq extends BaseBeanReq<GetNewsDetalisRsp> {
    public Object content_id;
    @Override
    public String myAddr() {
        return GetData.GetNewsDetalis;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetNewsDetalisRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetNewsDetalisRsp>>() {
        };
    }
}
