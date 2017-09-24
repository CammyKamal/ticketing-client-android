package com.chandigarhadmin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by harendrasinghbisht on 23/09/17.
 */

public class UniqueId  implements Parcelable{
    @SerializedName("id")
    @Expose
    private String id;

    protected UniqueId(Parcel in) {
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UniqueId> CREATOR = new Creator<UniqueId>() {
        @Override
        public UniqueId createFromParcel(Parcel in) {
            return new UniqueId(in);
        }

        @Override
        public UniqueId[] newArray(int size) {
            return new UniqueId[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}