package com.farukydnn.weatherplus;

import com.android.volley.VolleyError;
import com.farukydnn.weatherplus.core.network.interfaces.CityModelRequestListener;
import com.farukydnn.weatherplus.core.network.interfaces.OnResponse;
import com.farukydnn.weatherplus.core.network.model.CityWeatherModel;
import com.farukydnn.weatherplus.core.network.response.BaseResponse;
import com.farukydnn.weatherplus.core.network.response.CurrWeatherResponse;
import com.farukydnn.weatherplus.core.network.response.FiveDaysWeatherResponse;
import com.farukydnn.weatherplus.modelview.CityWeatherModelView;


public class CityModelRequest implements OnResponse {

    private CityWeatherModel cityWeather;
    private boolean isCurrWeatherArrived, isFiveDaysWeatherArrived;
    private boolean isFailed;

    private CityModelRequestListener mCallBack;


    public CityModelRequest(int cursorIndex, String shownName, boolean isCurrentLocation,
                            CityModelRequestListener mCallBack) {

        cityWeather = new CityWeatherModel();

        cityWeather.setCursorIndex(cursorIndex);
        cityWeather.setCurrentLocation(isCurrentLocation);

        if (shownName != null)
            cityWeather.setShownName(shownName);

        this.mCallBack = mCallBack;
    }

    @Override
    public void onSuccessResponse(BaseResponse baseResponse) {

        if (baseResponse instanceof CurrWeatherResponse) {

            CurrWeatherResponse response = (CurrWeatherResponse) baseResponse;

            cityWeather.setCurrentWeather(response);

            isCurrWeatherArrived = true;

        } else if (baseResponse instanceof FiveDaysWeatherResponse) {

            FiveDaysWeatherResponse response = (FiveDaysWeatherResponse) baseResponse;

            cityWeather.setFiveDaysWeather(response);

            isFiveDaysWeatherArrived = true;

        }

        checkReponseOK();
    }

    @Override
    public void onErrorResponse(VolleyError error, Class<? extends BaseResponse> responseClass) {

        if (!isFailed) {
            isFailed = true;
            mCallBack.onCityResponseError(error, responseClass);
        }
    }

    private void checkReponseOK() {

        if (isCurrWeatherArrived && isFiveDaysWeatherArrived) {

            CityWeatherModelView modelView = new CityWeatherModelView(cityWeather);

            mCallBack.onCityResponseArive(modelView);
        }
    }
}
