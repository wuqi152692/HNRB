package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetLatestNewsRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/20 0020.
 */

public class GetAuthorNewsListReq extends BaseBeanArrayReq<GetLatestNewsRsp> {
    public String author_id;
    public int offset, number;

    @Override
    public String myAddr() {
        return GetData.GetAuthorNewsList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetLatestNewsRsp>>() {
        };
    }
}
