package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * 找回密码重置
 * Created by FoAng on 17/5/9 下午2:36;
 */

public class FindPwdReq extends BaseBeanReq<String> {

    public String mobile, new_password, mobile_verify;

    public FindPwdReq(String mobile, String new_password, String mobile_verify) {
        this.mobile = mobile;
        this.new_password = new_password;
        this.mobile_verify = mobile_verify;
    }

    @Override
    public String myAddr() {
        return GetData.SetResetForgetPassword;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>(){};
    }
}
