package com.farukydnn.weatherplus.core.network.model;

import com.farukydnn.weatherplus.core.network.response.CurrWeatherResponse;
import com.farukydnn.weatherplus.core.network.response.FiveDaysWeatherResponse;


public class CityWeatherModel {

    private CurrWeatherResponse currentWeather;
    private FiveDaysWeatherResponse fiveDaysWeather;

    private int cursorIndex;
    private boolean isCurrentLocation;
    private String shownName;


    public void setCursorIndex(int cursorIndex) {
        this.cursorIndex = cursorIndex;
    }

    public void setCurrentLocation(boolean isCurrentLocation) {
        this.isCurrentLocation = isCurrentLocation;
    }

    public void setShownName(String name) {
        this.shownName = name;
    }

    public void setCurrentWeather(CurrWeatherResponse currentWeather) {
        if (shownName != null)
            currentWeather.setCityName(shownName);

        this.currentWeather = currentWeather;
    }

    public void setFiveDaysWeather(FiveDaysWeatherResponse fiveDaysWeather) {
        this.fiveDaysWeather = fiveDaysWeather;
    }

    public CurrWeatherResponse getCurrentWeather() {
        return currentWeather;
    }

    public FiveDaysWeatherResponse getFiveDaysWeather() {
        return fiveDaysWeather;
    }

    public int getCursorIndex() {
        return cursorIndex;
    }

    public boolean isCurrentLocation() {
        return isCurrentLocation;
    }
}
