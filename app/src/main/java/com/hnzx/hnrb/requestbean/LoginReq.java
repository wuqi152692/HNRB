package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;

/**
 *
 * 用户登录
 * Created by FoAng on 17/5/8 下午1:54;
 */

public class LoginReq extends BaseBeanReq<UserInfoRsp> {

    @Override
    public String myAddr() {
        return GetData.GetMemberLogin;
    }

    @Override
    public TypeReference<BaseBeanRsp<UserInfoRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<UserInfoRsp>>(){};
    }
}
