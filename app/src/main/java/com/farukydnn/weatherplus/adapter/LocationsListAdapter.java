package com.farukydnn.weatherplus.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.farukydnn.weatherplus.core.network.dto.FiveDaysCityDTO;
import com.farukydnn.weatherplus.modelview.CityWeatherModelView;
import com.farukydnn.weatherplus.util.DayManager;
import com.farukydnn.weatherplus.util.DayWeather;
import com.farukydnn.weatherplus.R;
import com.farukydnn.weatherplus.interfaces.RecyclerViewClickListener;

import java.util.Collections;
import java.util.List;


public class LocationsListAdapter extends RecyclerView.Adapter<LocationListHolder> {

    private RecyclerViewClickListener mListener;
    private List<CityWeatherModelView> mCities;


    public LocationsListAdapter(List<CityWeatherModelView> cityList, RecyclerViewClickListener listener) {
        mCities = cityList;
        mListener = listener;
    }

    public void updateContent(List<CityWeatherModelView> cityList) {
        mCities = cityList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mCities.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(CityWeatherModelView item, int position) {
        mCities.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int source, int target) {
        Collections.swap(mCities, source, target);
        notifyItemMoved(source, target);
    }

    @Override
    public LocationListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.locations_list_item, viewGroup, false);

        return new LocationListHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(LocationListHolder viewHolder, int position) {

        CityWeatherModelView currWeather = mCities.get(position);
        List<FiveDaysCityDTO> fiveDaysWeather = currWeather.getFiveDaysCityDTOs();

        viewHolder.cityName.setText(currWeather.getCityName() + ", " + currWeather.getSysDTO().getCountry());
        viewHolder.cityTemprature.setText(currWeather.getMainDTO().getTemp() + "°C");
        viewHolder.cityDescription.setText(currWeather.getWeatherDTOs().get(0).getDescription());
        viewHolder.forecastIcon.setImageResource(currWeather.getWeatherDTOs().get(0).getIcon(true));

        if (mCities.get(position).isCurrentLocation())
            viewHolder.locationIcon.setVisibility(View.VISIBLE);
        else
            viewHolder.locationIcon.setVisibility(View.GONE);

        DayManager dayManager = new DayManager(fiveDaysWeather, DayManager.SORT_BY_TEMPRATURE);

        setDayInfo(viewHolder.tomorrowName, viewHolder.tomorrowTemprature, viewHolder.tomorrowIcon,
                dayManager.getTomorrow());

        setDayInfo(viewHolder.secondDayName, viewHolder.secondDayTemprature, viewHolder.secondDayIcon,
                dayManager.getSecondDay());

        setDayInfo(viewHolder.thirdDayName, viewHolder.thirdDayTemprature, viewHolder.thirdDayIcon,
                dayManager.getThirdDay());

        setDayInfo(viewHolder.fourthDayName, viewHolder.fourthDayTemprature, viewHolder.fourthDayIcon,
                dayManager.getFourthDay());
    }


    @Override
    public int getItemCount() {
        return mCities.size();
    }

    private void setDayInfo(TextView dayName, TextView temprature, ImageView icon, DayWeather day) {

        dayName.setText(day.getDayName());

        String text = day.getHighestTemperature() + "°C " + day.getLowestTemperature() + "°C";

        Spannable tempratureText = new SpannableString(text);
        tempratureText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                text.indexOf("°C") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        temprature.setText(tempratureText);

        icon.setImageResource(day.getForecasts().get(0).getWeather().get(0).getIcon(false));
    }
}