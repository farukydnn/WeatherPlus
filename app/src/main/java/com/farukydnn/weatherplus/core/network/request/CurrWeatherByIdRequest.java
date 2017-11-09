package com.farukydnn.weatherplus.core.network.request;

import android.util.Log;

import com.farukydnn.weatherplus.core.network.enums.RequestURL;

import java.util.List;


public class CurrWeatherByIdRequest extends BaseRequest {

    private static final String TAG = CurrWeatherByIdRequest.class.getSimpleName();

    public CurrWeatherByIdRequest(List<String> params) {
        super(params);
    }

    @Override
    String createUrl(List<String> params) {
        Log.d(TAG, "createUrl: Creating Id Based Url");

        return RequestURL.BaseURL.toString()
                + RequestURL.Weather.toString()
                + RequestURL.QueryId.toString()
                + params.get(0)
                + RequestURL.Units.toString()
                + RequestURL.Lang.toString()
                + RequestURL.AppId.toString();
    }
}
