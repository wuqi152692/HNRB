package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetNewsSpecialRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/18 0018.
 */

public class GetNewsSpecialReq extends BaseBeanReq<GetNewsSpecialRsp> {
    public Object special_id;

    @Override
    public String myAddr() {
        return GetData.GetNewsSpecial;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetNewsSpecialRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetNewsSpecialRsp>>() {
        };
    }
}
