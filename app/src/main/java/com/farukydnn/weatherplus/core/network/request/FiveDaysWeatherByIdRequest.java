package com.farukydnn.weatherplus.core.network.request;

import android.util.Log;

import com.farukydnn.weatherplus.core.network.enums.RequestURL;


import java.util.List;

public class FiveDaysWeatherByIdRequest extends BaseRequest {

    private static final String TAG = FiveDaysWeatherByIdRequest.class.getSimpleName();

    public FiveDaysWeatherByIdRequest(List<String> params) {
        super(params);
    }

    @Override
    String createUrl(List<String> params) {
        Log.d(TAG, "createUrl: Creating Id Based Url");

        return RequestURL.BaseURL.toString()
                + RequestURL.Forecast.toString()
                + RequestURL.QueryId.toString()
                + params.get(0)
                + RequestURL.Units.toString()
                + RequestURL.Lang.toString()
                + RequestURL.AppId.toString();
    }
}
