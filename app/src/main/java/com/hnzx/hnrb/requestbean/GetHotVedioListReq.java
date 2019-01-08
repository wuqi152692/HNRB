package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMediaVedioRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class GetHotVedioListReq extends BaseBeanArrayReq<GetMediaVedioRsp> {
    @Override
    public String myAddr() {
        return GetData.GetHotVedioList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetMediaVedioRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetMediaVedioRsp>>() {
        };
    }
}
