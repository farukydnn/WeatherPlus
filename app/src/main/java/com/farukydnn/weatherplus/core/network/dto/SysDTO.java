package com.farukydnn.weatherplus.core.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SysDTO implements Parcelable {

    @SerializedName("country")
    private String country;

    @SerializedName("sunrise")
    private long sunrise;

    @SerializedName("sunset")
    private long sunset;


    public String getCountry() {
        return country;
    }

    public String getSunrise() {
        Date date = new Date(sunrise * 1000);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return format.format(date);
    }

    public String getSunset() {
        Date date = new Date(sunset * 1000);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return format.format(date);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.country);
        dest.writeLong(this.sunrise);
        dest.writeLong(this.sunset);
    }

    protected SysDTO(Parcel in) {
        this.country = in.readString();
        this.sunrise = in.readLong();
        this.sunset = in.readLong();
    }

    public static final Parcelable.Creator<SysDTO> CREATOR = new Parcelable.Creator<SysDTO>() {
        @Override
        public SysDTO createFromParcel(Parcel source) {
            return new SysDTO(source);
        }

        @Override
        public SysDTO[] newArray(int size) {
            return new SysDTO[size];
        }
    };
}
