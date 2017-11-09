package com.farukydnn.weatherplus.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.farukydnn.weatherplus.util.DayWeather;
import com.farukydnn.weatherplus.R;
import com.farukydnn.weatherplus.core.network.dto.FiveDaysCityDTO;


public class DayDetailsListAdapter extends RecyclerView.Adapter<DayDetailsListHolder> {

    private DayWeather dayDetails;
    private Resources res;

    public DayDetailsListAdapter(DayWeather dayDetails) {
        this.dayDetails = dayDetails;
    }

    @Override
    public DayDetailsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_details_list_item, parent, false);

        res = view.getResources();

        return new DayDetailsListHolder(view);
    }

    @Override
    public void onBindViewHolder(DayDetailsListHolder holder, int position) {

        FiveDaysCityDTO hour = dayDetails.getForecasts().get(position);

        holder.forecastTemprature.setText(
                res.getString(R.string.toolbar_temprature, hour.getMain().getTemp(), "°C"));

        holder.forecastDescription.setText(hour.getWeather().get(0).getDescription());

        holder.forecastWind.setText(
                res.getString(R.string.toolbar_wind, hour.getWind().getSpeed(),
                        res.getString(R.string.meters_per_second)));

        holder.forecastPressure.setText(
                res.getString(R.string.toolbar_pressure, hour.getMain().getPressure(), "hPa"));

        holder.forecastHumidity.setText(
                res.getString(R.string.toolbar_humidity, hour.getMain().getHumidity()));

        holder.forecastTime.setText(hour.getTimeString());

        holder.dayIcon.setImageResource(hour.getWeather().get(0).getIcon(true));
    }

    @Override
    public int getItemCount() {
        return dayDetails.getForecasts().size();
    }
}
