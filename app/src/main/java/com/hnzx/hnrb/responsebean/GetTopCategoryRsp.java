package com.hnzx.hnrb.responsebean;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author: mingancai
 * @Time: 2017/4/17 0017.
 */
@DatabaseTable
public class GetTopCategoryRsp implements Parcelable {
    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String catname;
    @DatabaseField
    public String cat_id;
    @DatabaseField
    public int isdefault = 1;

    public GetTopCategoryRsp() {
    }

    protected GetTopCategoryRsp(Parcel in) {
        catname = in.readString();
        cat_id = in.readString();
        isdefault = in.readInt();
    }

    public static final Creator<GetTopCategoryRsp> CREATOR = new Creator<GetTopCategoryRsp>() {
        @Override
        public GetTopCategoryRsp createFromParcel(Parcel in) {
            return new GetTopCategoryRsp(in);
        }

        @Override
        public GetTopCategoryRsp[] newArray(int size) {
            return new GetTopCategoryRsp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(catname);
        dest.writeString(cat_id);
        dest.writeInt(isdefault);
    }
}
