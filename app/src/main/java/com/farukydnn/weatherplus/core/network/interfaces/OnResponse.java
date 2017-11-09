package com.farukydnn.weatherplus.core.network.interfaces;

import com.android.volley.VolleyError;
import com.farukydnn.weatherplus.core.network.response.BaseResponse;


public interface OnResponse {

    void onSuccessResponse(BaseResponse baseResponse);

    void onErrorResponse(VolleyError error, Class<? extends BaseResponse> responseClass);
}
