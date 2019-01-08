package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * 绑定手机请求
 * Created by FoAng on 17/5/9 下午6:03;
 */

public class BindMobileReq extends BaseBeanReq<String> {

    @Override
    public String myAddr() {
        return GetData.setEnsureNewPhone;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>(){};
    }
}
