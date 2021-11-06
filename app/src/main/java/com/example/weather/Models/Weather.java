package com.example.weather.Models;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("main")
    String main;

    @SerializedName("description")
    String description;

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }
}