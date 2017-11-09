package com.farukydnn.weatherplus.core.network.interfaces;

import com.android.volley.VolleyError;
import com.farukydnn.weatherplus.core.network.response.BaseResponse;
import com.farukydnn.weatherplus.modelview.CityWeatherModelView;


public interface CityModelRequestListener {

    void onCityResponseArive(CityWeatherModelView model);

    void onCityResponseError(VolleyError error, Class<? extends BaseResponse> responseClass);
}
