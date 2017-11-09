package com.farukydnn.weatherplus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.farukydnn.weatherplus.R;


class DayDetailsListHolder extends RecyclerView.ViewHolder {

    TextView forecastTemprature, forecastDescription, forecastWind, forecastPressure,
            forecastHumidity, forecastTime;
    ImageView dayIcon;


    DayDetailsListHolder(View itemView) {
        super(itemView);

        forecastTemprature = itemView.findViewById(R.id.item_temprature);
        forecastDescription = itemView.findViewById(R.id.item_description);
        forecastWind = itemView.findViewById(R.id.item_wind);
        forecastPressure = itemView.findViewById(R.id.item_pressure);
        forecastHumidity = itemView.findViewById(R.id.item_humidity);
        forecastTime = itemView.findViewById(R.id.item_forecast_time);
        dayIcon = itemView.findViewById(R.id.item_day_icon);
    }
}
