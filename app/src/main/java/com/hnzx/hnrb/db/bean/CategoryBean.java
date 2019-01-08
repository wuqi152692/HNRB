package com.hnzx.hnrb.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author: ModeCai
 * @Time: 2018-04-04  14:56
 */
@DatabaseTable
public class CategoryBean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    public String catid;
    @DatabaseField
    public String update;
}
