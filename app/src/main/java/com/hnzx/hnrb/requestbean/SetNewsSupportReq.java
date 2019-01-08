package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/8 0008.
 */

public class SetNewsSupportReq extends BaseBeanReq<String> {
    public String content_id;

    @Override
    public String myAddr() {
        return GetData.SetNewsSupport;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>() {
        };
    }
}
