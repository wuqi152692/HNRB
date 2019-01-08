package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/10 0010.
 */

public class SetUserDingyueColumnReq extends BaseBeanReq<String> {
    public String cat_id;

    @Override
    public String myAddr() {
        return GetData.SetUserDingyueColumn;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>() {
        };
    }
}
