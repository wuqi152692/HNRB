package com.hnzx.hnrb.responsebean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.requestbean.BaseBeanReq;

/**
 * 关于
 * Created by FoAng on 17/5/10 下午4:56;
 */

public class GetAboutReq extends BaseBeanReq<String> {

    @Override
    public String myAddr() {
        return GetData.GetAppAboutData;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>(){};
    }
}
