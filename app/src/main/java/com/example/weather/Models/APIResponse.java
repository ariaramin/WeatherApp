package com.example.weather.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class APIResponse {

    @SerializedName("weather")
    List<Weather> weather;

    @SerializedName("main")
    Main main;

    @SerializedName("name")
    String city;

    @SerializedName("sys")
    Sys sys;

    @SerializedName("timezone")
    int timezone;

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public String getCity() {
        return city;
    }

    public Sys getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }
}