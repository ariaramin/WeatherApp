package com.example.weather.Models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIEndpoint {

    @GET("weather?units=metric")
    Call<APIResponse> getWeather(@Query("q") String city,
                                 @Query("appid") String API_KEY);

    @GET("weather?units=metric")
    Call<APIResponse> getWeather(@Query("lat") String lat,
                                 @Query("lon") String lon,
                                 @Query("appid") String API_KEY);
}
