package com.printz.guano.shoppingassistant.edit_list;

import android.os.Parcel;
import android.os.Parcelable;

public class Ware implements Parcelable {

    private String mName;
    private boolean mMarked;

    public Ware(String name) {
        this.mName = name;
    }

    public Ware(Parcel in) {
        this.mName = in.readString();
        this.mMarked = in.readInt() == 1;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean isMarked() {
        return mMarked;
    }

    public void setMarked(boolean marked) {
        this.mMarked = marked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mMarked ? 1 : 0);
    }

    public static final Parcelable.Creator<Ware> CREATOR = new Parcelable.Creator<Ware>() {
        public Ware createFromParcel(Parcel in) {
            return new Ware(in);
        }

        public Ware[] newArray(int size) {
            return new Ware[size];
        }
    };
}
