package com.farukydnn.weatherplus;

import com.farukydnn.weatherplus.core.network.interfaces.OnResponse;
import com.farukydnn.weatherplus.core.network.request.CurrWeatherByIdRequest;
import com.farukydnn.weatherplus.core.network.request.CurrWeatherByLocationRequest;
import com.farukydnn.weatherplus.core.network.request.FiveDaysWeatherByIdRequest;
import com.farukydnn.weatherplus.core.network.request.FiveDaysWeatherByLocationRequest;
import com.farukydnn.weatherplus.core.network.response.CurrWeatherResponse;
import com.farukydnn.weatherplus.core.network.response.FiveDaysWeatherResponse;
import com.farukydnn.weatherplus.core.ui.RequestActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SendRequest {

    public void sendCurrWeatherRequestByLocation(RequestActivity requestActivity, String lat,
                                                 String lon, OnResponse onResponse) {

        List<String> params = Arrays.asList(lat, lon);
        CurrWeatherByLocationRequest request = new CurrWeatherByLocationRequest(params);

        requestActivity.sendRequest(request.getUrl(), onResponse, CurrWeatherResponse.class);
    }

    public void sendCurrWeatherRequestById(RequestActivity requestActivity, String cityId,
                                             OnResponse onResponse) {

        List<String> params = Collections.singletonList(cityId);
        CurrWeatherByIdRequest request = new CurrWeatherByIdRequest(params);

        requestActivity.sendRequest(request.getUrl(), onResponse, CurrWeatherResponse.class);
    }

    public void sendFiveDaysWeatherRequestByLocation(RequestActivity requestActivity, String lat,
                                                 String lon, OnResponse onResponse) {

        List<String> params = Arrays.asList(lat, lon);
        FiveDaysWeatherByLocationRequest request = new FiveDaysWeatherByLocationRequest(params);

        requestActivity.sendRequest(request.getUrl(), onResponse, FiveDaysWeatherResponse.class);
    }

    public void sendFiveDaysWeatherRequestById(RequestActivity requestActivity, String cityId,
                                        OnResponse onResponse) {

        List<String> params = Collections.singletonList(cityId);
        FiveDaysWeatherByIdRequest request = new FiveDaysWeatherByIdRequest(params);

        requestActivity.sendRequest(request.getUrl(), onResponse, FiveDaysWeatherResponse.class);
    }

    public void cancelAllRequests(RequestActivity requestActivity, Object tag) {

        requestActivity.cancelAllRequests(tag);
    }
}