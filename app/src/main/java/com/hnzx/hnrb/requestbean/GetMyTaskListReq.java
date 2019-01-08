package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;
import com.hnzx.hnrb.responsebean.GetMyTaskListRsp;

/**
 * @author: mingancai
 * @Time: 2017/5/12 0012.
 */

public class GetMyTaskListReq extends BaseBeanReq<GetMyTaskListRsp> {
    @Override
    public String myAddr() {
        return GetData.GetMyTaskList;
    }

    @Override
    public TypeReference<BaseBeanRsp<GetMyTaskListRsp>> myTypeReference() {
        return new TypeReference<BaseBeanRsp<GetMyTaskListRsp>>() {
        };
    }
}
