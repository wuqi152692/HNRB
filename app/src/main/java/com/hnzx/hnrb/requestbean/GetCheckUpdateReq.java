package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetCheckUpdateRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/16 0016.
 */

public class GetCheckUpdateReq extends BaseBeanReq<GetCheckUpdateRsp> {
    @Override
    public String myAddr() {
        return GetData.GetCheckUpdate;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetCheckUpdateRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetCheckUpdateRsp>>() {
        };
    }
}
