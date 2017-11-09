package com.farukydnn.weatherplus.core.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class CoordDTO implements Parcelable {

    @SerializedName("lon")
    private float lon;

    @SerializedName("lat")
    private float lat;


    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.lon);
        dest.writeFloat(this.lat);
    }

    protected CoordDTO(Parcel in) {
        this.lon = in.readFloat();
        this.lat = in.readFloat();
    }

    public static final Parcelable.Creator<CoordDTO> CREATOR = new Parcelable.Creator<CoordDTO>() {
        @Override
        public CoordDTO createFromParcel(Parcel source) {
            return new CoordDTO(source);
        }

        @Override
        public CoordDTO[] newArray(int size) {
            return new CoordDTO[size];
        }
    };
}
