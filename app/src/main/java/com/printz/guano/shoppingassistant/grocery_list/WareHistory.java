package com.printz.guano.shoppingassistant.grocery_list;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class WareHistory implements Comparable<WareHistory>, Parcelable {

    private int mHistoryId;
    private String mName;
    private Integer mCount;

    public WareHistory(int mHistoryId, String name, int count) {
        this.mHistoryId = mHistoryId;
        this.mCount = count;
        this.mName = name;
    }

    private WareHistory(Parcel in) {
        this.mHistoryId = in.readInt();
        this.mName = in.readString();
        this.mCount = in.readInt();
    }

    public int getId() {
        return mHistoryId;
    }

    public void setId(int id) {
        this.mHistoryId = id;
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

    public void incrementCount() {
        this.mCount++;
    }

    @Override
    public int compareTo(@NonNull WareHistory another) {
        return this.getCount() - another.getCount();
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (object instanceof WareHistory) {
            String otherName = ((WareHistory) object).getName();
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
        dest.writeInt(mHistoryId);
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

    /**
     * Required for autocomplete dropdown to display name.
     * @return Returns name of the WareHistory
     */
    @Override
    public String toString() {
        return mName;
    }
}
