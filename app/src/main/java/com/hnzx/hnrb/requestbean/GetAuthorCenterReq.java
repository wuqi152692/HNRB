package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetAuthorCenterRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/20 0020.
 */

public class GetAuthorCenterReq extends BaseBeanReq<GetAuthorCenterRsp> {
    public String author_id;
    @Override
    public String myAddr() {
        return GetData.GetAuthorCenter;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetAuthorCenterRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetAuthorCenterRsp>>() {
        };
    }
}
