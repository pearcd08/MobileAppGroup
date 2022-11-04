package com.example.mobileappgroup.rest;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mobileappgroup.Models.Location;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationControllerRESTAPI implements retrofit2.Callback<Location> {
    final static String BASE_URL = "https://us1.locationiq.com/v1/";
//final static String BASE_URL = "https://api.geoapify.com/v1/geocode/";
    String access_token = "pk.612147813bfe7b2abec533b018eb2010"; //locationIQ
  //  String access_token = "315a0006500d4770b86bb0b789271b72"; // geoapify
    String format = "json";
    private static Location location;
//    List<Location> locationList;
//    Location location = new Location();
    Context context;
    TextView textview;

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
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<Location> call, Response<Location> response) {
        if (response.isSuccessful()) {
            location = response.body();
            location.setCity(location.getAddress().get("city").toString());
            location.setCounty(location.getAddress().get("county").toString());
            location.setState(location.getAddress().get("state").toString());
            location.setCountry(location.getAddress().get("country").toString());
            StringBuilder locationDescription = new StringBuilder();
            if (location.getCity() != null){
                locationDescription.append(location.getCity()).append(", ");
            }
            if (location.getCounty() != null){
                locationDescription.append(location.getCounty()).append(", ");
            }
            if (location.getState() != null){
                locationDescription.append(location.getState()).append(", ");
            }
            if (location.getCountry() != null){
                locationDescription.append(location.getCountry());
            }
            textview.setText(locationDescription);

//            locationList = locations.getValue();
            Log.d("RESPONSE_CODE", String.valueOf(response.code()));
            Log.d("RESPONSE_BODY", location.toString());

//            if (locations.size() == 1) {
//                Location location = new Location();
//                location.setCity(locations.getValue(0).getCity());
//                location.setCounty(locations.getValue(0).getCounty());
//                location.setCountry(locations.getValue(0).getCountry());
//            } else {
//                Log.d("RESPONSE_BODY", response.body().toString());
            //}
        }
    }

    @Override
    public void onFailure(Call<Location> call, Throwable t) {
        t.printStackTrace();
        Log.d("ERROR MSG", t.getMessage().toString());
        Log.d("LOCATION_ERROR", "Error getting location");
    }

    public Location getLocation() {
        return location;
    }
}
