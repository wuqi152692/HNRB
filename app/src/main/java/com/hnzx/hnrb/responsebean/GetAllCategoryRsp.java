package com.hnzx.hnrb.responsebean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author: mingancai
 * @Time: 2017/4/12 0012.
 */
@DatabaseTable
public class GetAllCategoryRsp implements Serializable {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    public String catid;
    @DatabaseField
    public String timestamp;
    @DatabaseField
    public String name;
    @DatabaseField
    public String image;
    @DatabaseField
    public int is_ordered;

    @Override
    public String toString() {
        return "GetAllCategoryRsp{" +
                "catid='" + catid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", is_ordered=" + is_ordered +
                '}';
    }
}
