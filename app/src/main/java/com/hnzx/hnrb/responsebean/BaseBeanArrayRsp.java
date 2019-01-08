package com.hnzx.hnrb.responsebean;

import java.util.ArrayList;

/**
 * Created by Ming on 16/12/20 09:37.
 * Email: mingancai8869@gmail.com
 * 朝看水东流,暮看日西坠。
 */
public class BaseBeanArrayRsp<DATA> {
    public int Status;

    public String Message;

    public ArrayList<DATA> Info;
}
