package com.printz.guano.shoppingassistant.grocery_list;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Ware implements Comparable<Ware>, Parcelable {

    private int mWareId;
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

    public Ware(int mWareId, String name, int position, boolean isMarked, String type, String amount) {
        this.mWareId = mWareId;
        this.mName = name;
        this.mPosition = position;
        this.mIsMarked = isMarked;
        this.mQuantityType = type;
        this.mAmount = amount;
    }

    private Ware(Parcel in) {
        this.mName = in.readString();
        this.mIsMarked = in.readInt() == 1;
    }

    public int getId() {
        return mWareId;
    }

    public void setId(int id) {
        this.mWareId = id;
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

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        this.mAmount = amount;
    }

    public String getType() {
        return mQuantityType;
    }

    public void setType(String type) {
        this.mQuantityType = type;
    }

    public boolean isAmountSet() {
        return !mAmount.equals("-");
    }

    @Override
    public int compareTo(@NonNull Ware another) {
        return this.getPosition() - another.getPosition();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mWareId);
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
