package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMediaRadioRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class GetMediaRadioReq extends BaseBeanArrayReq<GetMediaRadioRsp> {
    public Object offset, number;

    @Override
    public String myAddr() {
        return GetData.GetMediaAudio;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetMediaRadioRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetMediaRadioRsp>>() {
        };
    }
}
