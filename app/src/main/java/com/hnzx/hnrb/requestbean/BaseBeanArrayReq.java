package com.hnzx.hnrb.requestbean;

import com.alibaba.fastjson.TypeReference;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;
import com.hnzx.hnrb.responsebean.BaseBeanRsp;

/**
 * Created by Ming on 16/12/20 09:36.
 * Email: mingancai8869@gmail.com
 * 朝看水东流,暮看日西坠。
 */
public abstract class BaseBeanArrayReq<T> {
    public abstract String myAddr();

    public abstract TypeReference<BaseBeanArrayRsp<T>> myTypeReference();

}
