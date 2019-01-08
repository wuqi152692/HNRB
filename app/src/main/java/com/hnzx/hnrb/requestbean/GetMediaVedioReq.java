package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMediaImageRsp;
import com.hnzx.hnrb.responsebean.GetMediaVedioRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class GetMediaVedioReq extends BaseBeanArrayReq<GetMediaVedioRsp> {
    public Object number, offset;

    @Override
    public String myAddr() {
        return GetData.GetMediaVedio;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetMediaVedioRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetMediaVedioRsp>>() {
        };
    }
}
