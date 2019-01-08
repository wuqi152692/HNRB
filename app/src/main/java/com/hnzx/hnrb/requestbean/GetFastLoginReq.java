package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/11 0011.
 */

public class GetFastLoginReq extends BaseBeanReq<UserInfoRsp> {
    public String openid;

    @Override
    public String myAddr() {
        return GetData.GetFastLogin;
    }

    @Override
    public TypeReference<BaseBeanRsp<UserInfoRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<UserInfoRsp>>() {
        };
    }
}
