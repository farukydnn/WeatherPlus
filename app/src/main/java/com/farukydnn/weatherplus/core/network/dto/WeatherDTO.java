package com.farukydnn.weatherplus.core.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.farukydnn.weatherplus.R;
import com.google.gson.annotations.SerializedName;


public class WeatherDTO implements Parcelable {

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;


    public String getDescription() {
        return description.substring(0, 1).toUpperCase()
                + description.substring(1);
    }

    public int getIcon(boolean isBigSize) {
        switch (icon) {
            case "01n":
                if (isBigSize)
                    return R.drawable.img_01n_64dp;
                else
                    return R.drawable.img_01n_24dp;

            case "02d":
                if (isBigSize)
                    return R.drawable.img_02d_64dp;
                else
                    return R.drawable.img_02d_24dp;

            case "02n":
                if (isBigSize)
                    return R.drawable.img_02n_64dp;
                else
                    return R.drawable.img_02n_24dp;

            case "03d":
            case "03n":
                if (isBigSize)
                    return R.drawable.img_03d_64dp;
                else
                    return R.drawable.img_03d_24dp;

            case "04d":
            case "04n":
                if (isBigSize)
                    return R.drawable.img_04d_64dp;
                else
                    return R.drawable.img_04d_24dp;

            case "09d":
            case "09n":
                if (isBigSize)
                    return R.drawable.img_09d_64dp;
                else
                    return R.drawable.img_09d_24dp;

            case "10d":
                if (isBigSize)
                    return R.drawable.img_10d_64dp;
                else
                    return R.drawable.img_10d_24dp;

            case "10n":
                if (isBigSize)
                    return R.drawable.img_10n_64dp;
                else
                    return R.drawable.img_10n_24dp;

            case "11d":
            case "11n":
                if (isBigSize)
                    return R.drawable.img_11d_64dp;
                else
                    return R.drawable.img_11d_24dp;

            case "13d":
            case "13n":
                if (isBigSize)
                    return R.drawable.img_13d_64dp;
                else
                    return R.drawable.img_13d_24dp;

            case "50d":
            case "50n":
                if (isBigSize)
                    return R.drawable.img_50d_64dp;
                else
                    return R.drawable.img_50d_24dp;

            default:
                if (isBigSize)
                    return R.drawable.img_01d_64dp;
                else
                    return R.drawable.img_01d_24dp;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.icon);
    }

    protected WeatherDTO(Parcel in) {
        this.description = in.readString();
        this.icon = in.readString();
    }

    public static final Parcelable.Creator<WeatherDTO> CREATOR = new Parcelable.Creator<WeatherDTO>() {
        @Override
        public WeatherDTO createFromParcel(Parcel source) {
            return new WeatherDTO(source);
        }

        @Override
        public WeatherDTO[] newArray(int size) {
            return new WeatherDTO[size];
        }
    };
}
