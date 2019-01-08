package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/13 0013.
 */

public class GetSearchHotWordsReq extends BaseBeanArrayReq<String> {
    @Override
    public String myAddr() {
        return GetData.getSearchHotWords;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<String>>() {
        };
    }
}
