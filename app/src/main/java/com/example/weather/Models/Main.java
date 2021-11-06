package com.example.weather.Models;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    double temp;

    @SerializedName("temp_min")
    double temp_min;

    @SerializedName("temp_max")
    double temp_max;

    public String getTemp() {
        return String.valueOf(Math.round(temp));
    }

    public String getTemp_min() {
        return String.valueOf(Math.round(temp_min));
    }

    public String getTemp_max() {
        return String.valueOf(Math.round(temp_max));
    }
}
