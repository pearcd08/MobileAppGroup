package com.example.mobileappgroup.rest;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.mobileappgroup.Models.Location;
import com.example.mobileappgroup.Models.Locations;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationControllerRESTAPI {
    final static String BASE_URL = "https://us1.locationiq.com/v1/";
    String access_token = "pk.612147813bfe7b2abec533b018eb2010"; //locationIQ
    String format = "json";
    private static Location locationObj;
    Context context;
    TextView textview;
    Locations locations;

    public void start(Context context, Double latitude, Double longitude, TextView view) {
        this.context = context;
        this.textview = view;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LocationRESTAPI locationRESTAPI = retrofit.create(LocationRESTAPI.class);
        Call<Location> call = locationRESTAPI.getLocation(access_token, latitude, longitude, format);
        Log.d("LON", longitude.toString());
        Log.d("LAT", latitude.toString());
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                if (response.isSuccessful()) {
                        locationObj = response.body();
                        locationObj.setCity(locationObj.getAddress().get("city").toString());
                        locationObj.setCounty(locationObj.getAddress().get("county").toString());
                        locationObj.setState(locationObj.getAddress().get("state").toString());
                        locationObj.setCountry(locationObj.getAddress().get("country").toString());
                        StringBuilder locationDescription = new StringBuilder();
                        if (locationObj.getCity() != null) {
                            locationDescription.append(locationObj.getCity()).append(", ");
                        }
                        if (locationObj.getCounty() != null) {
                            locationDescription.append(locationObj.getCounty()).append(", ");
                        }
                        if (locationObj.getState() != null) {
                            locationDescription.append(locationObj.getState()).append(", ");
                        }
                        if (locationObj.getCountry() != null) {
                            locationDescription.append(locationObj.getCountry());
                        }
                        textview.setText(locationDescription);
                    }

                    Log.d("RESPONSE_CODE", String.valueOf(response.code()));
                    Log.d("RESPONSE_BODY", locationObj.toString());
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                t.printStackTrace();
                Log.d("ERROR MSG", t.getMessage().toString());
                Log.d("LOCATION_ERROR", "Error getting location");
            }
        });

    }

    public void start(Context context, String location){
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LocationRESTAPI locationRESTAPI = retrofit.create(LocationRESTAPI.class);
        Call<Locations> call = locationRESTAPI.getLocation(access_token, location, format);
        Log.d("LOCATION", location);
        call.enqueue(new Callback<Locations>() {
            @Override
            public void onResponse(Call<Locations> call, Response<Locations> response) {
                if (response.isSuccessful()) {
                    locations = response.body();
                    List<Location> locationList = locations.getValue();
                    if (locationList != null) {
                        locationObj = locationList.get(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Locations> call, Throwable t) {
                t.printStackTrace();
                Log.d("ERROR MSG", t.getMessage().toString());
                Log.d("LOCATION_ERROR", "Error getting location");
            }
        });
    }


    public Location getLocation() {
        return locationObj;
    }
}
