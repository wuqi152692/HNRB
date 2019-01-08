package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.UserInfoRsp;

/**
 * 用户注册请求类
 * Created by FoAng on 17/5/4 下午4:05;
 */
public class RegisterReq extends BaseBeanReq<UserInfoRsp>{

    @Override
    public String myAddr() {
        return GetData.SetMemberRegister;
    }

    @Override
    public TypeReference<BaseBeanRsp<UserInfoRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<UserInfoRsp>>(){};
    }
}
