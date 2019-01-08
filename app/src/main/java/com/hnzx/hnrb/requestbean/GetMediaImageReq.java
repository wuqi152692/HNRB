package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetMediaImageRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class GetMediaImageReq extends BaseBeanArrayReq<GetMediaImageRsp> {
    public Object number, offset;

    @Override
    public String myAddr() {
        return GetData.GetMediaImage;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetMediaImageRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetMediaImageRsp>>() {
        };
    }
}
