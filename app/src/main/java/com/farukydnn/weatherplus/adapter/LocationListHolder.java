package com.farukydnn.weatherplus.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farukydnn.weatherplus.R;
import com.farukydnn.weatherplus.interfaces.RecyclerViewClickListener;


class LocationListHolder extends BaseHolder {

    TextView cityName, cityDescription, cityTemprature,
            tomorrowName, tomorrowTemprature,
            secondDayName, secondDayTemprature,
            thirdDayName, thirdDayTemprature,
            fourthDayName, fourthDayTemprature;

    ImageView locationIcon, forecastIcon, tomorrowIcon, secondDayIcon, thirdDayIcon,
            fourthDayIcon;

    LocationListHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView, listener);

        cityName = itemView.findViewById(R.id.city_name);
        cityDescription = itemView.findViewById(R.id.city_description);
        cityTemprature = itemView.findViewById(R.id.city_temprature);
        locationIcon = itemView.findViewById(R.id.location_icon);
        forecastIcon = itemView.findViewById(R.id.forecast_icon);

        LinearLayout tomorrowLayout = itemView.findViewById(R.id.day_tomorrow);
        tomorrowName = tomorrowLayout.findViewById(R.id.city_day_name);
        tomorrowTemprature = tomorrowLayout.findViewById(R.id.city_day_temprature);
        tomorrowIcon = tomorrowLayout.findViewById(R.id.city_day_icon);

        LinearLayout secondDayLayout = itemView.findViewById(R.id.day_second_day);
        secondDayName = secondDayLayout.findViewById(R.id.city_day_name);
        secondDayTemprature = secondDayLayout.findViewById(R.id.city_day_temprature);
        secondDayIcon = secondDayLayout.findViewById(R.id.city_day_icon);

        LinearLayout thirdDayLayout = itemView.findViewById(R.id.day_third_day);
        thirdDayName = thirdDayLayout.findViewById(R.id.city_day_name);
        thirdDayTemprature = thirdDayLayout.findViewById(R.id.city_day_temprature);
        thirdDayIcon = thirdDayLayout.findViewById(R.id.city_day_icon);

        LinearLayout fourthDayLayout = itemView.findViewById(R.id.day_fourth_day);
        fourthDayName = fourthDayLayout.findViewById(R.id.city_day_name);
        fourthDayTemprature = fourthDayLayout.findViewById(R.id.city_day_temprature);
        fourthDayIcon = fourthDayLayout.findViewById(R.id.city_day_icon);
    }
}