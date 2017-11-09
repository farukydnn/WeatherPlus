package com.farukydnn.weatherplus.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.farukydnn.weatherplus.R;
import com.farukydnn.weatherplus.adapter.DayDetailsPagerAdapter;
import com.farukydnn.weatherplus.modelview.CityWeatherModelView;

public class DayDetailsActivity extends AppCompatActivity {

    private final static String TAG = DayDetailsActivity.class.getSimpleName();

    private final static String DAY_DETAILS = "DAY_DETAILS";

    TextView temprature, description, wind, pressure, humidity, sunrise,
            sunset, lastUpdate;

    ImageView dayIcon;

    CityWeatherModelView dayDetails;


    public static Intent newIntent(Activity callerActivity, Parcelable value) {
        Intent intent = new Intent(callerActivity, DayDetailsActivity.class);
        intent.putExtra(DAY_DETAILS, value);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        dayDetails = getIntent().getExtras().getParcelable(DAY_DETAILS);

        init();
    }

    private void init() {
        Log.d(TAG, "init");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(dayDetails.getCityName()
                    + ", " + dayDetails.getSysDTO().getCountry());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        temprature = findViewById(R.id.toolbar_temprature);
        description = findViewById(R.id.toolbar_description);
        wind = findViewById(R.id.toolbar_wind);
        pressure = findViewById(R.id.toolbar_pressure);
        humidity = findViewById(R.id.toolbar_humidity);
        sunrise = findViewById(R.id.toolbar_sunrise);
        sunset = findViewById(R.id.toolbar_sunset);

        lastUpdate = findViewById(R.id.toolbar_last_update);
        dayIcon = findViewById(R.id.toolbar_day_icon);


        temprature.setText(
                getString(R.string.toolbar_temprature, dayDetails.getMainDTO().getTemp(), "°C"));

        description.setText(dayDetails.getWeatherDTOs().get(0).getDescription());

        wind.setText(getString(R.string.toolbar_wind, dayDetails.getWindDTO().getSpeed(),
                getString(R.string.meters_per_second)));

        pressure.setText(
                getString(R.string.toolbar_pressure, dayDetails.getMainDTO().getPressure(), "hPa"));

        humidity.setText(
                getString(R.string.toolbar_humidity, dayDetails.getMainDTO().getHumidity()));

        sunrise.setText(
                getString(R.string.toolbar_sunrise, dayDetails.getSysDTO().getSunrise()));

        sunset.setText(
                getString(R.string.toolbar_sunset, dayDetails.getSysDTO().getSunset()));

        lastUpdate.setText(
                getString(R.string.toolbar_last_update, dayDetails.getLastUpdateTime()));

        dayIcon.setImageResource(dayDetails.getWeatherDTOs().get(0).getIcon(true));


        DayDetailsPagerAdapter adapter = new DayDetailsPagerAdapter(getSupportFragmentManager(),
                dayDetails.getFiveDaysCityDTOs());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}