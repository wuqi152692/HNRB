package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 *
 * 修改密码
 * Created by FoAng on 17/5/15 下午4:02;
 */
public class ModifyPwdReq extends BaseBeanReq<String> {

    @Override
    public String myAddr() {
        return GetData.SetChangePassword;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>(){};
    }
}
