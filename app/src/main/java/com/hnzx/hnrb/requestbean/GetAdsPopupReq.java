package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetAdsRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/28 0028.
 */

public class GetAdsPopupReq extends BaseBeanArrayReq<GetAdsRsp> {
    @Override
    public String myAddr() {
        return GetData.GetAdsPopup;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetAdsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetAdsRsp>>() {
        };
    }
}
