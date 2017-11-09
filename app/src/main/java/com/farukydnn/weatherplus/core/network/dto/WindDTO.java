package com.farukydnn.weatherplus.core.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class WindDTO implements Parcelable {

    @SerializedName("speed")
    private String speed;


    public String getSpeed() {
        return speed;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.speed);
    }

    protected WindDTO(Parcel in) {
        this.speed = in.readString();
    }

    public static final Parcelable.Creator<WindDTO> CREATOR = new Parcelable.Creator<WindDTO>() {
        @Override
        public WindDTO createFromParcel(Parcel source) {
            return new WindDTO(source);
        }

        @Override
        public WindDTO[] newArray(int size) {
            return new WindDTO[size];
        }
    };
}
