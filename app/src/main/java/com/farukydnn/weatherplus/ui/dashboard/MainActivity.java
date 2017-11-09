package com.farukydnn.weatherplus.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;

import com.farukydnn.weatherplus.core.ui.RequestActivity;
import com.farukydnn.weatherplus.R;
import com.farukydnn.weatherplus.interfaces.LocationResultListener;
import com.farukydnn.weatherplus.interfaces.ScreenTouchListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;


public class MainActivity extends RequestActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;

    private LocationResultListener mResultCallBack;
    private ScreenTouchListener mScreenCallBack;

    private LocationCallback mLocationCallback;

    private boolean isUpdatesReceiving;

    private boolean isActivityForeground;

    @Override
    protected void onCreate(Bundle state) {
        Log.d(TAG, "onCreate");
        super.onCreate(state);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        Log.d(TAG, "init");

        isActivityForeground = true;

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.locations);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationCallBack();
    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        Log.d(TAG, "onAttachFragment");
        super.onAttachFragment(fragment);

        if (fragment instanceof CityListFragment)
            mScreenCallBack = (ScreenTouchListener) fragment;
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();

        isActivityForeground = true;
    }

    private void createLocationCallBack() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mResultCallBack.onSuccess(locationResult.getLastLocation());
                stopLocationUpdates();
            }
        };
    }

    private void stopLocationUpdates() {
        if (isUpdatesReceiving) {

            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Location updates stopped");

                            isUpdatesReceiving = false;
                        }
                    });
        }
    }

    @SuppressWarnings("MissingPermission")
    public void getCurrentLocation(LocationResultListener mCallBack) {
        Log.d(TAG, "Choosing location source");

        if (mCallBack == null) {
            throw new NullPointerException("mCallBack can't be null!");
        }

        mResultCallBack = mCallBack;

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful() && task.getResult() != null) {
                            long updateTime = new Date(task.getResult().getTime()).getTime();
                            long now = new Date().getTime();
                            long diff = (now - updateTime) / 60 / 1000;

                            if (diff < 5) {
                                Log.d(TAG, "Location setting from last known location");

                                if (mResultCallBack != null) {
                                    mResultCallBack.onSuccess(task.getResult());
                                }

                                return;
                            }
                        }

                        Log.d(TAG, "Creating new location request");

                        mLocationRequest = new LocationRequest();
                        mLocationRequest.setInterval(1000);
                        mLocationRequest.setFastestInterval(1000);
                        mLocationRequest.setPriority(
                                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                        checkLocationSettings();
                    }
                });
    }

    private void checkLocationSettings() {
        Log.d(TAG, "Checking device's configuration");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);

        LocationSettingsRequest mLocationSettingsRequest = builder.build();

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)

                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {

                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.d(TAG, "All location settings are satisfied");

                        findCurrentLocation();
                    }
                })

                .addOnFailureListener(this, new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();

                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.d(TAG, "Location settings are not satisfied. Attempting to " +
                                        "upgrade location settings");

                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this,
                                            REQUEST_CHECK_SETTINGS);

                                    if (mResultCallBack != null)
                                        mResultCallBack.onConfigurationNeeded();

                                } catch (IntentSender.SendIntentException sie) {
                                    Log.d(TAG, "PendingIntent unable to execute request.");

                                    if (mResultCallBack != null)
                                        mResultCallBack.onError(sie.getMessage());
                                }
                                break;

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, " +
                                        "and cannot be fixed here. Fix in Settings.";
                                Log.d(TAG, errorMessage);

                                if (mResultCallBack != null)
                                    mResultCallBack.onError(errorMessage);
                        }
                    }
                });
    }

    @SuppressWarnings("MissingPermission")
    private void findCurrentLocation() {
        Log.d(TAG, "findCurrentLocation");

        if (mLocationRequest != null) {
            if (isActivityForeground) {
                Log.d(TAG, "Start listening to location updates");

                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback, Looper.myLooper());

                isUpdatesReceiving = true;
            }

            if (mResultCallBack != null)
                mResultCallBack.onProgress();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult");

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d(TAG, "User agreed to make required location settings changes");

                        findCurrentLocation();
                        break;

                    case Activity.RESULT_CANCELED:
                        String errorMessage = "User chose not to make required location settings " +
                                "changes";
                        Log.d(TAG, errorMessage);

                        if (mResultCallBack != null) {
                            mResultCallBack.onError(errorMessage);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mScreenCallBack != null)
                mScreenCallBack.dispatchTouchEvent(ev);
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();

        isActivityForeground = false;
        stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        mResultCallBack = null;
        mScreenCallBack = null;
        super.onDestroy();
    }
}