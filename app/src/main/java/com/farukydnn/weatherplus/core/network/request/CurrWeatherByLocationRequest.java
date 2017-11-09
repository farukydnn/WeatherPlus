package com.farukydnn.weatherplus.core.network.request;

import android.util.Log;

import com.farukydnn.weatherplus.core.network.enums.RequestURL;

import java.util.List;


public class CurrWeatherByLocationRequest extends BaseRequest {

    private static final String TAG = CurrWeatherByLocationRequest.class.getSimpleName();

    public CurrWeatherByLocationRequest(List<String> params) {
        super(params);
    }

    @Override
    String createUrl(List<String> params) {
        Log.d(TAG, "createUrl: Creating Location Based Url");

        return RequestURL.BaseURL.toString()
                + RequestURL.Weather.toString()
                + RequestURL.QueryLat.toString()
                + params.get(0)
                + RequestURL.QueryLon.toString()
                + params.get(1)
                + RequestURL.Units.toString()
                + RequestURL.Lang.toString()
                + RequestURL.AppId.toString();
    }

}
