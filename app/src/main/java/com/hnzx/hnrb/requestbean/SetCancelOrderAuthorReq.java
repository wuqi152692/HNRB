package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/20 0020.
 */

public class SetCancelOrderAuthorReq extends BaseBeanReq<String> {
    public String author_id;

    @Override
    public String myAddr() {
        return GetData.SetCancelOrderAuthor;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>() {
        };
    }
}
