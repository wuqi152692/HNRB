package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * Created by FoAng on 17/5/16 下午3:39;
 */

public class ModifySexReq extends BaseBeanReq<String>{
    @Override
    public String myAddr() {
        return GetData.SetSexModify;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>(){};
    }
}
