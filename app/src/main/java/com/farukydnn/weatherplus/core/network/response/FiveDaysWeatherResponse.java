package com.farukydnn.weatherplus.core.network.response;

import com.farukydnn.weatherplus.core.network.dto.FiveDaysCityDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class FiveDaysWeatherResponse extends BaseResponse {

    @SerializedName("list")
    private List<FiveDaysCityDTO> forecastList;


    public List<FiveDaysCityDTO> getForecastList() {
        return forecastList;
    }
}
