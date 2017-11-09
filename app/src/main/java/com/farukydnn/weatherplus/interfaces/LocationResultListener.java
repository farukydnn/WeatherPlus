package com.farukydnn.weatherplus.interfaces;

import android.location.Location;


public interface LocationResultListener {

    void onConfigurationNeeded();

    void onSuccess(Location location);

    void onError(String message);

    void onProgress();
}
