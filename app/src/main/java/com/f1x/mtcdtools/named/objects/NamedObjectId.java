package com.f1x.mtcdtools.named.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by f1x on 2017-02-11.
 */

public class NamedObjectId implements Parcelable {
    public NamedObjectId(String name) {
        mName = name;
        mHashCode = name.toUpperCase().hashCode();
    }

    public NamedObjectId(Parcel parcel) {
        mName = parcel.readString();
        mHashCode = mName.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        return (other instanceof NamedObjectId) && mHashCode == ((NamedObjectId)other).mHashCode;
    }

    @Override
    public int hashCode() {
        return mHashCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
    }

    public static final Parcelable.Creator<NamedObjectId> CREATOR
            = new Parcelable.Creator<NamedObjectId>() {
        public NamedObjectId createFromParcel(Parcel in) {
            return new NamedObjectId(in);
        }

        public NamedObjectId[] newArray(int size) {
            return new NamedObjectId[size];
        }
    };

    private String mName;
    private int mHashCode;
}
