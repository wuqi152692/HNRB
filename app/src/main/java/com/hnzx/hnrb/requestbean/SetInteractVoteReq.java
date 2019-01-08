package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.network.GetData;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.SetInteractVoteRsp;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class SetInteractVoteReq extends BaseBeanArrayReq<SetInteractVoteRsp> {
    public Object vote_id, vote_option, device_token;

    @Override
    public String myAddr() {
        return GetData.SetInteractVote;
    }

    @Override
    public TypeReference<BaseBeanArrayRsp<SetInteractVoteRsp>> myTypeReference() {
        return new TypeReference<BaseBeanArrayRsp<SetInteractVoteRsp>>() {
        };
    }
}
