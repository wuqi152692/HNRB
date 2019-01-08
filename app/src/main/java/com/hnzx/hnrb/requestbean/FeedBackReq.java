package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 *
 * 用户反馈请求
 * Created by FoAng on 17/5/10 下午4:37;
 */
public class FeedBackReq extends BaseBeanReq<String> {

    @Override
    public String myAddr() {
        return GetData.SetUserFeedBack;
    }

    @Override
    public TypeReference<BaseBeanRsp<String>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<String>>(){};
    }
}
