package com.farukydnn.weatherplus.core.network;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.farukydnn.weatherplus.app.AppController;
import com.farukydnn.weatherplus.core.network.interfaces.OnResponse;
import com.farukydnn.weatherplus.core.network.response.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class VolleyManager implements Response.ErrorListener, Response.Listener<String> {

    private static final String TAG = VolleyManager.class.getSimpleName();

    private OnResponse onResponse = null;
    private Class<? extends BaseResponse> responseClass = null;

    public void sendRequest(String url, OnResponse onResponse, Class<? extends BaseResponse> responseClass) {
        Log.d(TAG, "sendRequest: " + url);

        this.onResponse = onResponse;
        this.responseClass = responseClass;

        StringRequest stringRequest = new StringRequest(url, this, this);

        AppController.getInstance().addToRequestQueue(stringRequest, responseClass.getSimpleName());
    }

    public void cancelAllRequests(Object tag) {
        Log.d(TAG, "cancelAllRequests: " + tag);

        AppController.getInstance().cancelPendingRequests(tag);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse: Request failed via " + responseClass.getSimpleName());

        onResponse.onErrorResponse(error, responseClass);
    }

    @Override
    public void onResponse(String response) {

        GsonBuilder gsonBuilder = new GsonBuilder();

        Gson gson = gsonBuilder.create();

        if (response.startsWith("{")) {
            Log.d(TAG, "onResponse: Response received successfully via " + responseClass.getSimpleName());

            BaseResponse baseResponse = gson.fromJson(response, responseClass);
            onResponse.onSuccessResponse(baseResponse);
        } else // TODO Support JsonArray (Not necessary for this application)
            Log.d(TAG, "onResponse: Unsupported response type (JsonArray)");

    }
}
