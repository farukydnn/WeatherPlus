package com.farukydnn.weatherplus.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkManager {

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }
}
