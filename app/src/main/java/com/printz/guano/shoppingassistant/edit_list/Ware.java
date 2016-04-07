package com.printz.guano.shoppingassistant.edit_list;

import android.os.Parcel;
import android.os.Parcelable;

public class Ware implements Comparable<Ware>, Parcelable {

    private int wId;
    private String mName;

    /**
     * The position of the Ware in the shopping list view.
     * This is used to sort loaded wares to maintain position of wares in shopping list view.
     */
    private int mPosition;
    private boolean mIsMarked;

    /**
     * The type of Quantity type e.g. Flask, or Carton etc.
     */
    private String mQuantityType;

    /**
     * The amount of this
     */
    private String mAmount;

    public Ware(int wId, String name, int position, boolean isMarked, String type, String amount) {
        this.wId = wId;
        this.mName = name;
        this.mPosition = position;
        this.mIsMarked = isMarked;
        this.mQuantityType = type;
        this.mAmount = amount;
    }

    public Ware(Parcel in) {
        this.mName = in.readString();
        this.mIsMarked = in.readInt() == 1;
    }

    public int getId() {
        return wId;
    }

    public void setId(int id) {
        this.wId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public boolean isMarked() {
        return mIsMarked;
    }

    public void setMarked(boolean marked) {
        this.mIsMarked = marked;
    }

    public String getQuantityType() {
        return mQuantityType;
    }

    public void setQuantityType(String type) {
        this.mQuantityType = type;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        this.mAmount = amount;
    }

    @Override
    public int compareTo(Ware another) {
        return this.getPosition() - another.getPosition();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(wId);
        dest.writeString(mName);
        dest.writeInt(mPosition);
        dest.writeInt(mIsMarked ? 1 : 0);
        dest.writeString(mQuantityType);
        dest.writeString(mAmount);
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
