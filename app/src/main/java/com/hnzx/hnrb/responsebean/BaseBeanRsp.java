package com.hnzx.hnrb.responsebean;

/**
 * Created by Ming on 16/12/20 09:37.
 * Email: mingancai8869@gmail.com
 * 朝看水东流,暮看日西坠。
 */
public class BaseBeanRsp<DATA> {
    public int Status;

    public String Message;

    public DATA Info;

    public boolean isSuccess() {
        return Status == 1;
    }
}
