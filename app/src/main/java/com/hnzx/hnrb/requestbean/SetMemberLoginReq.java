package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.SetMemberLoginRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/10 0010.
 */

public class SetMemberLoginReq extends BaseBeanReq<SetMemberLoginRsp> {
    @Override
    public String myAddr() {
        return GetData.GetMemberLogin;
    }

    @Override
    public TypeReference<BaseBeanRsp<SetMemberLoginRsp>> myTypeReference() {
        return null;
    }
}
