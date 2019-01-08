package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * Created by FoAng on 17/5/3 下午9:41;
 */
public class SendSmsCodeReq extends BaseBeanReq<Object> {

    public static final String FORWARD_REGISTER = "register";

    public static final String FORWARD_FIND_PWD = "forget_password";

    public static final String FORWARD_CHANGE_MOBILE = "changemobile";

    public Object  mobile;

    public Object forward;

    @Override
    public String myAddr() {
        return GetData.GetSendSmsCode;
    }

    @Override
    public TypeReference<BaseBeanRsp<Object>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<Object>>(){};
    }
}
