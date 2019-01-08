package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetUnitDetailsRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/20 0020.
 */

public class GetUnitDetailsReq extends BaseBeanReq<com.hnzx.hnrb.responsebean.GetUnitDetailsRsp> {
    public String cat_id;

    @Override
    public String myAddr() {
        return GetData.GetOfficalDetails;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetUnitDetailsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetUnitDetailsRsp>>() {
        };
    }
}
