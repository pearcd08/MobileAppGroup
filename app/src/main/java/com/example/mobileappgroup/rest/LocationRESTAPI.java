package com.example.mobileappgroup.rest;

import com.example.mobileappgroup.Models.Location;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationRESTAPI {

    @GET("reverse")
    Call<Location> getLocation(@Query("key")String key, @Query("lat") Double lat, @Query("lon") Double lon, @Query("format")String format);
}
