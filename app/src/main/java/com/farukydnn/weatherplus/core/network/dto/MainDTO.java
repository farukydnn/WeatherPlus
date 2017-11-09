package com.farukydnn.weatherplus.core.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class MainDTO implements Parcelable {

    @SerializedName("temp")
    private float temp;

    @SerializedName("pressure")
    private float pressure;

    @SerializedName("humidity")
    private int humidity;


    public String getTemp() {
        return String.valueOf((int) temp);
    }

    public String getPressure() {
        return String.valueOf((int) pressure);
    }

    public String getHumidity() {
        return String.valueOf(humidity);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.temp);
        dest.writeFloat(this.pressure);
        dest.writeInt(this.humidity);
    }

    protected MainDTO(Parcel in) {
        this.temp = in.readFloat();
        this.pressure = in.readFloat();
        this.humidity = in.readInt();
    }

    public static final Parcelable.Creator<MainDTO> CREATOR = new Parcelable.Creator<MainDTO>() {
        @Override
        public MainDTO createFromParcel(Parcel source) {
            return new MainDTO(source);
        }

        @Override
        public MainDTO[] newArray(int size) {
            return new MainDTO[size];
        }
    };
}
