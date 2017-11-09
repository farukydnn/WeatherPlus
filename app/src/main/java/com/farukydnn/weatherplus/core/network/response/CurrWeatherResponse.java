package com.farukydnn.weatherplus.core.network.response;

import com.farukydnn.weatherplus.core.network.dto.MainDTO;
import com.farukydnn.weatherplus.core.network.dto.SysDTO;
import com.farukydnn.weatherplus.core.network.dto.WeatherDTO;
import com.farukydnn.weatherplus.core.network.dto.WindDTO;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CurrWeatherResponse extends BaseResponse {

    @SerializedName("sys")
    private SysDTO sysDTO;

    @SerializedName("weather")
    private List<WeatherDTO> weatherDTO;

    @SerializedName("main")
    private MainDTO mainDTO;

    @SerializedName("wind")
    private WindDTO windDTO;

    @SerializedName("name")
    private String cityName;

    @SerializedName("id")
    private String id;

    @SerializedName("dt")
    private long lastUpdateTime;

    public SysDTO getSys() {
        return sysDTO;
    }

    public List<WeatherDTO> getWeather() {
        return weatherDTO;
    }

    public MainDTO getMain() {
        return mainDTO;
    }

    public WindDTO getWind() {
        return windDTO;
    }

    public String getCityId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getLastUpdateTime() {
        Date date = new Date(lastUpdateTime * 1000);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());

        return format.format(date);
    }

    public void setCityName(String name) {
        this.cityName = name;
    }

}
