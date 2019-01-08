package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetHomePagerDataRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */

public class GetHomePagerDataReq extends BaseBeanReq<GetHomePagerDataRsp> {
    @Override
    public String myAddr() {
        return GetData.GetHomePagerData;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetHomePagerDataRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetHomePagerDataRsp>>() {
        };
    }
}
