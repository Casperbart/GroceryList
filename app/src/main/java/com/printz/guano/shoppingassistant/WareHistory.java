package com.printz.guano.shoppingassistant;

import android.os.Parcel;
import android.os.Parcelable;

public class WareHistory implements Comparable<WareHistory>, Parcelable {

    private int _id;
    private String mName;
    private Integer mCount;

    public WareHistory(int id, String name, int count) {
        this._id = id;
        this.mCount = count;
        this.mName = name;
    }

    public WareHistory(Parcel in) {
        this._id = in.readInt();
        this.mName = in.readString();
        this.mCount = in.readInt();
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Integer getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    @Override
    public int compareTo(WareHistory another) {
        return this.getCount() - another.getCount();
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;

        if (o instanceof WareHistory) {
            String otherName = ((WareHistory) o).getName();
            isEqual = mName.equals(otherName);
        }

        return isEqual;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(mName);
        dest.writeInt(mCount);
    }

    public static final Parcelable.Creator<WareHistory> CREATOR = new Parcelable.Creator<WareHistory>() {
        public WareHistory createFromParcel(Parcel in) {
            return new WareHistory(in);
        }

        public WareHistory[] newArray(int size) {
            return new WareHistory[size];
        }
    };
}
