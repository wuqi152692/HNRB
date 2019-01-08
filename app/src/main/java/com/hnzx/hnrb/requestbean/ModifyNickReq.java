package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * 修改昵称
 * Created by FoAng on 17/5/10 下午4:14;
 */
public class ModifyNickReq extends BaseBeanReq<String> {

    @Override
    public String myAddr() {
        return GetData.SetNickModify;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>(){};
    }
}
