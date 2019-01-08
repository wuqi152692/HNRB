package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetUserSignHistoryRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/12 0012.
 */

public class GetUserSignHistoryReq extends BaseBeanReq<GetUserSignHistoryRsp> {
    @Override
    public String myAddr() {
        return GetData.GetUserSignHistory;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetUserSignHistoryRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetUserSignHistoryRsp>>() {
        };
    }
}
