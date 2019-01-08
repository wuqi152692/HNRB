package com.hnzx.hnrb.requestbean;

import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.GetAboutNewsListRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/10 0010.
 */

public class GetAboutNewsListReq extends BaseBeanArrayReq<GetAboutNewsListRsp> {
    public Object content_id;

    @Override
    public String myAddr() {
        return GetData.GetAboutNewsList;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<GetAboutNewsListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<GetAboutNewsListRsp>>() {
        };
    }
}
