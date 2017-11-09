package com.farukydnn.weatherplus.util;

import android.util.Log;

import com.farukydnn.weatherplus.core.network.dto.FiveDaysCityDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DayManager {

    public static final int NO_SHORT = 0;
    public static final int SORT_BY_TEMPRATURE = 1;

    private static final String TAG = DayManager.class.getSimpleName();

    private final int sortType;
    private DayWeather today, tomorrow, secondDay, thirdDay, fourthDay;


    public DayManager(List<FiveDaysCityDTO> forecastList, int sortType) {
        Log.d(TAG, "Starting to parse FiveDaysWeather data");

        this.sortType = sortType;

        Calendar now = Calendar.getInstance();
        Calendar forecastTime = Calendar.getInstance();

        int startPosition = 0;

        for (int day = 0; day < 5; day++) {
            Log.d(TAG, "Creating " + day + ". day forecast list");

            List<FiveDaysCityDTO> list = new ArrayList<>();

            now.setTimeInMillis(forecastList.get(startPosition).getTimeStamp());

            for (int position = startPosition; position < forecastList.size(); position++) {

                forecastTime.setTimeInMillis(forecastList.get(position).getTimeStamp());

                if (isSameDay(now, forecastTime)) {
                    handleList(list, forecastList.get(position));

                } else {
                    startPosition = position;
                    break;
                }

            }

            switch (day) {
                case 0:
                    today = new DayWeather(list, sortType);
                    break;

                case 1:
                    tomorrow = new DayWeather(list, sortType);
                    break;

                case 2:
                    secondDay = new DayWeather(list, sortType);
                    break;

                case 3:
                    thirdDay = new DayWeather(list, sortType);
                    break;

                case 4:
                    fourthDay = new DayWeather(list, sortType);
                    break;
            }

            Log.d(TAG, day + ". day forecast list successfully created.");
        }

    }

    private boolean isSameDay(Calendar day, Calendar compareTo) {
        return day.get(Calendar.DAY_OF_YEAR) == compareTo.get(Calendar.DAY_OF_YEAR);
    }

    private void handleList(List<FiveDaysCityDTO> addTo, FiveDaysCityDTO forecast) {

        switch (sortType) {
            case NO_SHORT:
                addTo.add(forecast);
                break;

            case SORT_BY_TEMPRATURE:
                addToListSortedByTemprature(addTo, forecast);
                break;
        }
    }

    private void addToListSortedByTemprature(List<FiveDaysCityDTO> addTo, FiveDaysCityDTO forecast) {
        int position = 0;

        if (!addTo.isEmpty()) {

            int addTemp = Integer.parseInt(forecast.getMain().getTemp());

            do {
                int baseTemp = Integer.parseInt(addTo.get(position).getMain().getTemp());

                if (addTemp < baseTemp)
                    position++;
                else
                    break;
            }
            while (position < addTo.size());

        }

        addTo.add(position, forecast);
    }

    public DayWeather getToday() {
        return today;
    }

    public DayWeather getTomorrow() {
        return tomorrow;
    }

    public DayWeather getSecondDay() {
        return secondDay;
    }

    public DayWeather getThirdDay() {
        return thirdDay;
    }

    public DayWeather getFourthDay() {
        return fourthDay;
    }
}