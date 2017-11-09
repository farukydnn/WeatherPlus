package com.farukydnn.weatherplus.core.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FiveDaysCityDTO implements Parcelable {

    @SerializedName("weather")
    private List<WeatherDTO> weatherDTO;

    @SerializedName("main")
    private MainDTO mainDTO;

    @SerializedName("wind")
    private WindDTO windDTO;

    @SerializedName("dt")
    private long timeStamp;


    public List<WeatherDTO> getWeather() {
        return weatherDTO;
    }

    public MainDTO getMain() {
        return mainDTO;
    }

    public WindDTO getWind() {
        return windDTO;
    }

    public long getTimeStamp() {
        return timeStamp * 1000;
    }

    public String getTimeString() {
        Date date = new Date(getTimeStamp());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());

        return format.format(date);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.weatherDTO);
        dest.writeParcelable(this.mainDTO, flags);
        dest.writeParcelable(this.windDTO, flags);
        dest.writeLong(this.timeStamp);
    }

    public FiveDaysCityDTO() {
    }

    protected FiveDaysCityDTO(Parcel in) {
        this.weatherDTO = in.createTypedArrayList(WeatherDTO.CREATOR);
        this.mainDTO = in.readParcelable(MainDTO.class.getClassLoader());
        this.windDTO = in.readParcelable(WindDTO.class.getClassLoader());
        this.timeStamp = in.readLong();
    }

    public static final Parcelable.Creator<FiveDaysCityDTO> CREATOR = new Parcelable.Creator<FiveDaysCityDTO>() {
        @Override
        public FiveDaysCityDTO createFromParcel(Parcel source) {
            return new FiveDaysCityDTO(source);
        }

        @Override
        public FiveDaysCityDTO[] newArray(int size) {
            return new FiveDaysCityDTO[size];
        }
    };
}
