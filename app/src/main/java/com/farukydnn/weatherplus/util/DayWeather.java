package com.farukydnn.weatherplus.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.farukydnn.weatherplus.core.network.dto.FiveDaysCityDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DayWeather implements Parcelable {

    private static final String TAG = DayWeather.class.getSimpleName();

    private List<FiveDaysCityDTO> daysForecasts;
    private int sortType = 0;


    DayWeather(List<FiveDaysCityDTO> daysForecasts, int sortType) {
        this.daysForecasts = daysForecasts;
        this.sortType = sortType;
    }

    public String getHighestTemperature() {

        if (sortType == DayManager.SORT_BY_TEMPRATURE)
            return daysForecasts.get(0).getMain().getTemp();

        Log.e(TAG, "getHighestTemperature() only works with a dayWeather instance that sorted by temprature!");

        return null;
    }

    public String getLowestTemperature() {

        if (sortType == DayManager.SORT_BY_TEMPRATURE)
            return daysForecasts.get(daysForecasts.size() - 1).getMain().getTemp();

        Log.e(TAG, "getLowestTemperature() only works with a dayWeather instance that sorted by temprature!");

        return null;
    }

    public String getDayName() {
        Date date = new Date(daysForecasts.get(0).getTimeStamp());
        SimpleDateFormat format = new SimpleDateFormat("E", Locale.getDefault());

        return format.format(date).toUpperCase();
    }

    public List<FiveDaysCityDTO> getForecasts() {
        return daysForecasts;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.daysForecasts);
        dest.writeInt(this.sortType);
    }

    protected DayWeather(Parcel in) {
        this.daysForecasts = in.createTypedArrayList(FiveDaysCityDTO.CREATOR);
        this.sortType = in.readInt();
    }

    public static final Parcelable.Creator<DayWeather> CREATOR = new Parcelable.Creator<DayWeather>() {
        @Override
        public DayWeather createFromParcel(Parcel source) {
            return new DayWeather(source);
        }

        @Override
        public DayWeather[] newArray(int size) {
            return new DayWeather[size];
        }
    };
}
