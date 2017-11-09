package com.farukydnn.weatherplus.ui.dashboard;

import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.farukydnn.weatherplus.CityModelRequest;
import com.farukydnn.weatherplus.core.network.response.CurrWeatherResponse;
import com.farukydnn.weatherplus.core.network.response.FiveDaysWeatherResponse;
import com.farukydnn.weatherplus.database.DatabaseHelper;
import com.farukydnn.weatherplus.R;
import com.farukydnn.weatherplus.adapter.LocationsListAdapter;
import com.farukydnn.weatherplus.core.network.interfaces.CityModelRequestListener;
import com.farukydnn.weatherplus.core.network.response.BaseResponse;
import com.farukydnn.weatherplus.core.ui.PermissionActivity;
import com.farukydnn.weatherplus.core.ui.RequestFragment;
import com.farukydnn.weatherplus.adapter.ItemTouchHelperAdapter;
import com.farukydnn.weatherplus.interfaces.ItemTouchHelperListener;
import com.farukydnn.weatherplus.interfaces.LocationResultListener;
import com.farukydnn.weatherplus.interfaces.PermissionListener;
import com.farukydnn.weatherplus.interfaces.ScreenTouchListener;
import com.farukydnn.weatherplus.model.CityCoordinatesModel;
import com.farukydnn.weatherplus.modelview.CityWeatherModelView;
import com.farukydnn.weatherplus.core.ui.RequestActivity;
import com.farukydnn.weatherplus.interfaces.RecyclerViewClickListener;
import com.farukydnn.weatherplus.util.NetworkManager;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class CityListFragment extends RequestFragment implements View.OnClickListener,
        RecyclerViewClickListener, PermissionListener, CityModelRequestListener,
        ItemTouchHelperListener, ScreenTouchListener {

    private final static String TAG = CityListFragment.class.getSimpleName();

    private final static String KEY_IS_REFRESHING = "is-refreshing";
    private final static String KEY_IS_ADD_QUEUE_HANDLING = "is-add-queque-handling";
    private final static String KEY_CURR_NAME_INDEX = "curr-name-index";
    private final static String KEY_CITY_WEATHER_LIST = "city-weather-list";
    private final static String KEY_CITY_ADD_QUEUE = "key-city-add-queue";

    private final static int LOCATION_REQUEST_PERMISSION = 0;
    private final static int LOCATION_NAME_REQUEST = 1;

    private View view;
    private TextView textEmptyList;
    private SwipeRefreshLayout swipeToRefresh;
    private RecyclerView recyclerView;

    private List<CityWeatherModelView> weatherUpdateCache;
    private List<CityWeatherModelView> cityWeatherList;
    private List<CityCoordinatesModel> cityAddQueue;

    private LocationsListAdapter recyclerViewAdapter;

    private boolean isRefreshing;
    private int currCityIndex;

    private boolean isAddQueueHandling;
    private int currNameIndex;

    private Snackbar snackBar;
    private Handler handler;
    private ItemTouchHelperAdapter itemTouchCallBack;


    public CityListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_city_list, container, false);

        init();

        return view;
    }

    private void init() {
        Log.d(TAG, "init");

        weatherUpdateCache = new ArrayList<>();
        cityWeatherList = new ArrayList<>();
        cityAddQueue = new ArrayList<>();

        recyclerViewAdapter = new LocationsListAdapter(cityWeatherList, this);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_location);
        fab.setOnClickListener(this);

        textEmptyList = view.findViewById(R.id.txt_empty_list);

        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerView();
            }
        });

        recyclerView = view.findViewById(R.id.city_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        handler = new Handler();
    }

    @Override
    public void onActivityCreated(Bundle state) {
        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(state);

        if (state != null) {
            isRefreshing = state.getBoolean(KEY_IS_REFRESHING);
            isAddQueueHandling = state.getBoolean(KEY_IS_ADD_QUEUE_HANDLING);

            currNameIndex = state.getInt(KEY_CURR_NAME_INDEX);

            cityWeatherList = state.getParcelableArrayList(KEY_CITY_WEATHER_LIST);

            cityAddQueue = state.getParcelableArrayList(KEY_CITY_ADD_QUEUE);

            updateRecyclerView();
        } else {
            loadCitiesFromDisk();
        }

        itemTouchCallBack = new ItemTouchHelperAdapter(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this, cityWeatherList, true);

        new ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();

        if (isRefreshing) {

            swipeToRefresh.post(new Runnable() {
                @Override
                public void run() {
                    swipeToRefresh.setRefreshing(true);
                    refreshRecyclerView();
                }
            });

        } else if (isAddQueueHandling) {
            makeCityNameWeatherRequest();
        }
    }

    @Override
    public void dispatchTouchEvent(MotionEvent ev) {
        if (snackBar != null && snackBar.getDuration() != Snackbar.LENGTH_INDEFINITE) {
            Log.d(TAG, "Dissmiss snackbar");

            Rect rect = new Rect();
            snackBar.getView().getGlobalVisibleRect(rect);

            if (!rect.contains((int) ev.getX(), (int) ev.getY())) {
                destroySnackbar();
            }
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder holder, int direction, final int position) {

        final CityWeatherModelView deletedItem = cityWeatherList.get(position);
        final String cityName = deletedItem.getCityName();

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper.getInstance(getContext()).deleteCity(deletedItem);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showRestoreBar(cityName, deletedItem, position);
                    }
                });
            }
        }).start();

        recyclerViewAdapter.removeItem(position);

        setUIVisiblity();
    }

    public void showRestoreBar(String cityName, final CityWeatherModelView deletedItem, final int position) {
        snackBar = Snackbar.make(view, getString(R.string.city_remeoved, cityName),
                Snackbar.LENGTH_LONG);

        snackBar.setAction(R.string.UNDO, new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                itemTouchCallBack.setEnabled(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper.getInstance(getContext()).restoreCity(deletedItem);

                        itemTouchCallBack.setEnabled(true);
                    }
                }).start();

                recyclerViewAdapter.restoreItem(deletedItem, position);

                setUIVisiblity();
            }
        })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }

    @Override
    public void onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                       RecyclerView.ViewHolder target) {

        final int sourcePosition = viewHolder.getAdapterPosition();
        final int targetPosition = target.getAdapterPosition();

        final CityWeatherModelView sourceCity = cityWeatherList.get(sourcePosition);
        final CityWeatherModelView targetCity = cityWeatherList.get(targetPosition);

        int temp = targetCity.getCursorIndex();
        targetCity.setCursorIndex(sourceCity.getCursorIndex());
        sourceCity.setCursorIndex(temp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper.getInstance(getContext()).updateCity(sourceCity);
                DatabaseHelper.getInstance(getContext()).updateCity(targetCity);
            }
        }).start();

        recyclerViewAdapter.moveItem(sourcePosition, targetPosition);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_location:

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());

                    startActivityForResult(intent, LOCATION_NAME_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    GoogleApiAvailability.getInstance()
                            .getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                                    LOCATION_NAME_REQUEST).show();

                } catch (GooglePlayServicesNotAvailableException e) {
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOCATION_NAME_REQUEST:

                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Add city name request to queue");

                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    LatLng coords = place.getLatLng();

                    cityAddQueue.add(new CityCoordinatesModel(place.getName().toString(),
                            coords.latitude, coords.longitude));

                    if (!isRefreshing && !isAddQueueHandling) {
                        Log.d(TAG, "Start new city name request queue");

                        makeCityNameWeatherRequest();
                    }

                    isAddQueueHandling = true;

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);

                    Log.d(TAG, status.getStatusMessage());
                }

                break;
        }
    }

    private void loadCitiesFromDisk() {
        Log.d(TAG, "Get city model list from disk");

        DatabaseHelper.getInstance(getContext()).fillCityList(cityWeatherList);

        boolean isNetworkConnected = NetworkManager.isNetworkConnected(getActivity());

        if (!isNetworkConnected) {
            Log.d(TAG, "loadCitiesFromDisk: Loading data from cache");

            updateRecyclerView();

        } else {
            if (!cityWeatherList.isEmpty())
                textEmptyList.setVisibility(View.GONE);

            isRefreshing = true;
        }
    }

    private void refreshRecyclerView() {
        Log.d(TAG, "Start refreshing recyclerview");

        itemTouchCallBack.setEnabled(false);

        destroySnackbar();

        cancelAllRequests();

        isRefreshing = true;

        weatherUpdateCache.clear();

        tryToRefreshFavoriteCities:
        if (!cityWeatherList.isEmpty()) {
            currCityIndex = 0;

            if (cityWeatherList.get(currCityIndex).isCurrentLocation())
                if (++currCityIndex == cityWeatherList.size())
                    break tryToRefreshFavoriteCities;

            makeCityIdWeatherRequest();
            return;
        }

        currentLocationRequest();
    }

    private void makeCityIdWeatherRequest() {
        if (!isAdded())
            return;

        CityWeatherModelView city = cityWeatherList.get(currCityIndex);

        Log.d(TAG, "Updating weather forecast's name is: " + city.getCityName());

        CityModelRequest responseClass = new CityModelRequest(city.getCursorIndex(),
                city.getCityName(), false, this);

        sendRequestInstance.sendCurrWeatherRequestById((RequestActivity) getActivity(),
                city.getId(), responseClass);

        sendRequestInstance.sendFiveDaysWeatherRequestById((RequestActivity) getActivity(),
                city.getId(), responseClass);
    }

    private void makeCityNameWeatherRequest() {
        Log.d(TAG, "Adding weather forecast by coordinates");

        CityCoordinatesModel coords = cityAddQueue.get(currNameIndex);

        snackBar = Snackbar.make(view, R.string.location_adding, Snackbar.LENGTH_INDEFINITE);
        snackBar.show();

        CityModelRequest responseClass = new CityModelRequest(0, coords.getName(), false,
                CityListFragment.this);

        sendRequestInstance.sendCurrWeatherRequestByLocation(
                (RequestActivity) getActivity(),
                Double.toString(coords.getLat()),
                Double.toString(coords.getLon()), responseClass);

        sendRequestInstance.sendFiveDaysWeatherRequestByLocation(
                (RequestActivity) getActivity(),
                Double.toString(coords.getLat()),
                Double.toString(coords.getLon()), responseClass);
    }

    private void currentLocationRequest() {
        Log.d(TAG, "Ask for location permission");

        isRefreshing = false;

        if (!isAdded())
            return;

        Map<String, PermissionActivity.PermissionDialogMessage> messages = new HashMap<>();

        messages.put(Manifest.permission.ACCESS_FINE_LOCATION,
                new PermissionActivity.PermissionDialogMessage(getString(R.string.location_request_title),
                        getString(R.string.location_request_message)));

        requestAppPermissions((PermissionActivity) getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_REQUEST_PERMISSION, messages, CityListFragment.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        switch (requestCode) {
            case LOCATION_REQUEST_PERMISSION:
                Log.d(TAG, "Location permission granted. Requesting weather forecast.");

                isRefreshing = true;

                ((MainActivity) getActivity()).getCurrentLocation(new LocationResultListener() {

                    @Override
                    public void onConfigurationNeeded() {
                        Log.d(TAG, "onConfigurationNeeded");

                        isRefreshing = false;
                    }

                    @Override
                    public void onProgress() {
                        Log.d(TAG, "onProgress");

                        isRefreshing = true;
                    }

                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "onSuccess: location came");

                        CityModelRequest responseClass
                                = new CityModelRequest(DatabaseHelper.CURRENT_LOCATION_CURSOR_INDEX,
                                null, true, CityListFragment.this);

                        sendRequestInstance.sendCurrWeatherRequestByLocation(
                                (RequestActivity) getActivity(),
                                Double.toString(location.getLatitude()),
                                Double.toString(location.getLongitude()), responseClass);

                        sendRequestInstance.sendFiveDaysWeatherRequestByLocation(
                                (RequestActivity) getActivity(),
                                Double.toString(location.getLatitude()),
                                Double.toString(location.getLongitude()), responseClass);
                    }

                    @Override
                    public void onError(String message) {
                        locationRequestCancelled();
                    }
                });

                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, int errorType,
                                    @Nullable List<String> permissionsThisTimeDenied,
                                    @Nullable List<String> permissionsAlwaysDenied) {
        switch (requestCode) {
            case LOCATION_REQUEST_PERMISSION:
                Log.d(TAG, "Location Permission denied.");

                locationRequestCancelled();

                break;
        }
    }

    private void locationRequestCancelled() {
        Log.d(TAG, "locationRequestCancelled");

        isRefreshing = true;

        if (weatherUpdateCache.isEmpty())
            updateDone(false, true);
        else
            updateDone(true, true);
    }

    @Override
    public synchronized void onCityResponseArive(final CityWeatherModelView model) {
        Log.d(TAG, "Weather forecast respond successfully received.");

        if (!model.isCurrentLocation()) {

            if (isRefreshing) {
                weatherUpdateCache.add(model);

                if (++currCityIndex < cityWeatherList.size())
                    makeCityIdWeatherRequest();
                else
                    currentLocationRequest();

            } else {

                boolean isDuplicated = false;

                for (CityWeatherModelView item : cityWeatherList) {

                    if (item.getCityName().equals(model.getCityName())) {
                        isDuplicated = true;
                        break;
                    }
                }

                if (!isDuplicated) {
                    model.setCursorIndex(DatabaseHelper.getCursorIndex());

                    cityWeatherList.add(model);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHelper.getInstance(getContext()).addCity(model);
                        }
                    }).start();

                    updateRecyclerView();
                }

                proceedHandleCityAddQueue();
            }

        } else {
            weatherUpdateCache.add(0, model);
            updateDone(true, false);
        }
    }

    @Override
    public void onCityResponseError(VolleyError error, Class<? extends BaseResponse> responseClass) {

        if (error instanceof NetworkError) {
            Log.d(TAG, "onCityResponseError: " + R.string.no_internet);

            snackBar = Snackbar.make(view, R.string.no_internet, Snackbar.LENGTH_SHORT);
            snackBar.show();

        } else if (error instanceof TimeoutError) {
            Log.d(TAG, "onCityResponseError: " + R.string.connection_timed_out);

            snackBar = Snackbar.make(view, R.string.connection_timed_out, Snackbar.LENGTH_SHORT);
            snackBar.show();

        } else if (error instanceof ServerError) {

            if (isRefreshing) {
                Log.d(TAG, "onCityResponseError: " + R.string.server_not_responding);

                snackBar = Snackbar.make(view, R.string.server_not_responding, Snackbar.LENGTH_SHORT);
                snackBar.show();

            } else {
                Log.d(TAG, "onCityResponseError: " + R.string.location_not_found);

                snackBar = Snackbar.make(view, R.string.location_not_found, Snackbar.LENGTH_SHORT);
                snackBar.show();
            }
        }

        if (isRefreshing) {
            updateDone(false, false);
        } else {
            proceedHandleCityAddQueue();
        }
    }

    private void proceedHandleCityAddQueue() {

        if (++currNameIndex < cityAddQueue.size())
            makeCityNameWeatherRequest();
        else
            cityAddQueueComplete();
    }

    private void cityAddQueueComplete() {
        Log.d(TAG, "City add queue complete");

        destroySnackbar();

        currNameIndex = 0;
        isAddQueueHandling = false;
        cityAddQueue.clear();
    }

    private void updateDone(boolean isUpdateSuccessful, boolean isCurrLocationReqDenied) {
        Log.d(TAG, "updateDone() - isSuccessful: " + isUpdateSuccessful);

        isRefreshing = false;

        if (isUpdateSuccessful) {

            if (!cityWeatherList.isEmpty()) {
                CityWeatherModelView firstItem = cityWeatherList.get(0);

                if (firstItem.isCurrentLocation() && isCurrLocationReqDenied)
                    weatherUpdateCache.add(0, firstItem);
            }

            cityWeatherList.clear();

            cityWeatherList.addAll(weatherUpdateCache);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (CityWeatherModelView model : cityWeatherList) {
                        DatabaseHelper.getInstance(getContext()).updateCity(model);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            freeItemTouchHelper();

                            updateRecyclerView();

                            if (!cityAddQueue.isEmpty())
                                makeCityNameWeatherRequest();
                        }
                    });
                }
            }).start();

        } else {

            freeItemTouchHelper();

            if (!cityAddQueue.isEmpty())
                makeCityNameWeatherRequest();
        }

        weatherUpdateCache.clear();
    }

    private void freeItemTouchHelper() {
        Log.d(TAG, "freeItemTouchHelper");

        swipeToRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeToRefresh.setRefreshing(false);
            }
        });

        itemTouchCallBack.setEnabled(true);
    }

    private void updateRecyclerView() {
        Log.d(TAG, "updateRecyclerView");

        setUIVisiblity();

        if (!cityWeatherList.isEmpty())
            recyclerViewAdapter.updateContent(cityWeatherList);
    }

    private void setUIVisiblity() {
        Log.d(TAG, "setUIVisiblity");

        if (!cityWeatherList.isEmpty())
            textEmptyList.setVisibility(View.GONE);
        else
            textEmptyList.setVisibility(View.VISIBLE);

    }

    private void destroySnackbar() {
        Log.d(TAG, "Dissmiss snackbar");

        if (snackBar != null) {
            snackBar.dismiss();
            snackBar = null;
        }
    }

    @Override
    public void onClick(View view, int position) {
        Log.d(TAG, "Starting DayDetailsActivity with selected city's details");

        Intent showDayDetails =
                DayDetailsActivity.newIntent(getActivity(), cityWeatherList.get(position));

        startActivity(showDayDetails);
    }

    private void cancelAllRequests() {
        if (!isAdded())
            return;

        sendRequestInstance.cancelAllRequests((RequestActivity) getActivity(),
                CurrWeatherResponse.class.getSimpleName());

        sendRequestInstance.cancelAllRequests((RequestActivity) getActivity(),
                FiveDaysWeatherResponse.class.getSimpleName());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_REFRESHING, isRefreshing);
        outState.putBoolean(KEY_IS_ADD_QUEUE_HANDLING, isAddQueueHandling);

        outState.putInt(KEY_CURR_NAME_INDEX, currNameIndex);

        outState.putParcelableArrayList(KEY_CITY_WEATHER_LIST,
                (ArrayList<? extends Parcelable>) cityWeatherList);

        outState.putParcelableArrayList(KEY_CITY_ADD_QUEUE,
                (ArrayList<? extends Parcelable>) cityAddQueue);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: cancel all requests");
        super.onStop();

        cancelAllRequests();
    }
}