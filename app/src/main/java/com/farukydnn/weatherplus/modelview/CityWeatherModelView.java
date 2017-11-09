package com.farukydnn.weatherplus.modelview;

import android.os.Parcel;
import android.os.Parcelable;

import com.farukydnn.weatherplus.core.network.dto.FiveDaysCityDTO;
import com.farukydnn.weatherplus.core.network.dto.MainDTO;
import com.farukydnn.weatherplus.core.network.dto.SysDTO;
import com.farukydnn.weatherplus.core.network.dto.WeatherDTO;
import com.farukydnn.weatherplus.core.network.dto.WindDTO;
import com.farukydnn.weatherplus.core.network.model.CityWeatherModel;
import com.farukydnn.weatherplus.core.network.response.CurrWeatherResponse;
import com.farukydnn.weatherplus.core.network.response.FiveDaysWeatherResponse;

import java.util.List;


public class CityWeatherModelView implements Parcelable {

    private SysDTO sysDTO;
    private List<WeatherDTO> weatherDTOs;
    private MainDTO mainDTO;
    private WindDTO windDTO;

    private String id, cityName, lastUpdateTime;

    private List<FiveDaysCityDTO> fiveDaysCityDTOs;

    private boolean isCurrentLocation;
    private int cursorIndex;

    public CityWeatherModelView(CityWeatherModel model) {

        CurrWeatherResponse currWeatherResponse = model.getCurrentWeather();
        FiveDaysWeatherResponse fiveDaysWeatherResponse = model.getFiveDaysWeather();

        sysDTO = currWeatherResponse.getSys();
        weatherDTOs = currWeatherResponse.getWeather();
        mainDTO = currWeatherResponse.getMain();
        windDTO = currWeatherResponse.getWind();

        id = currWeatherResponse.getCityId();
        cityName = currWeatherResponse.getCityName();
        lastUpdateTime = currWeatherResponse.getLastUpdateTime();

        fiveDaysCityDTOs = fiveDaysWeatherResponse.getForecastList();

        cursorIndex = model.getCursorIndex();
        isCurrentLocation = model.isCurrentLocation();
    }


    public SysDTO getSysDTO() {
        return sysDTO;
    }

    public List<WeatherDTO> getWeatherDTOs() {
        return weatherDTOs;
    }

    public MainDTO getMainDTO() {
        return mainDTO;
    }

    public WindDTO getWindDTO() {
        return windDTO;
    }

    public String getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public List<FiveDaysCityDTO> getFiveDaysCityDTOs() {
        return fiveDaysCityDTOs;
    }

    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }

    public int getCursorIndex() {
        return this.cursorIndex;
    }

    public void setCursorIndex(int position) {
        this.cursorIndex = position;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sysDTO, flags);
        dest.writeTypedList(this.weatherDTOs);
        dest.writeParcelable(this.mainDTO, flags);
        dest.writeParcelable(this.windDTO, flags);
        dest.writeString(this.id);
        dest.writeString(this.cityName);
        dest.writeString(this.lastUpdateTime);
        dest.writeTypedList(this.fiveDaysCityDTOs);
        dest.writeByte(this.isCurrentLocation ? (byte) 1 : (byte) 0);
        dest.writeInt(this.cursorIndex);
    }

    protected CityWeatherModelView(Parcel in) {
        this.sysDTO = in.readParcelable(SysDTO.class.getClassLoader());
        this.weatherDTOs = in.createTypedArrayList(WeatherDTO.CREATOR);
        this.mainDTO = in.readParcelable(MainDTO.class.getClassLoader());
        this.windDTO = in.readParcelable(WindDTO.class.getClassLoader());
        this.id = in.readString();
        this.cityName = in.readString();
        this.lastUpdateTime = in.readString();
        this.fiveDaysCityDTOs = in.createTypedArrayList(FiveDaysCityDTO.CREATOR);
        this.isCurrentLocation = in.readByte() != 0;
        this.cursorIndex = in.readInt();
    }

    public static final Parcelable.Creator<CityWeatherModelView> CREATOR
            = new Parcelable.Creator<CityWeatherModelView>() {
        @Override
        public CityWeatherModelView createFromParcel(Parcel source) {
            return new CityWeatherModelView(source);
        }

        @Override
        public CityWeatherModelView[] newArray(int size) {
            return new CityWeatherModelView[size];
        }
    };
}
