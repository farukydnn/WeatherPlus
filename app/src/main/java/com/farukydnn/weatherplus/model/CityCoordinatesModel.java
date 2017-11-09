package com.farukydnn.weatherplus.model;

import android.os.Parcel;
import android.os.Parcelable;


public class CityCoordinatesModel implements Parcelable {

    private String name;
    private double lat, lon;

    public CityCoordinatesModel(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
    }

    protected CityCoordinatesModel(Parcel in) {
        this.name = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
    }

    public static final Parcelable.Creator<CityCoordinatesModel> CREATOR
            = new Parcelable.Creator<CityCoordinatesModel>() {
        @Override
        public CityCoordinatesModel createFromParcel(Parcel source) {
            return new CityCoordinatesModel(source);
        }

        @Override
        public CityCoordinatesModel[] newArray(int size) {
            return new CityCoordinatesModel[size];
        }
    };
}