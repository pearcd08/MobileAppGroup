package com.example.mobileappgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
//import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobileappgroup.rest.LocationControllerRESTAPI;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class NewPost2nd extends AppCompatActivity implements View.OnClickListener {

    TextView et_newPost2_location, et_newPost2_caption;
    Button btn_newPost2_back, btn_newPost2_next;
    String imageUri, locationStr, caption;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    com.example.mobileappgroup.Models.Location locationAPIResult;
    double latitude;
    double longitude;

    public static final int LOCATION_REQUEST_CHECK_SETTINGS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post2nd);

        Intent intent = getIntent();
        imageUri = intent.getStringExtra("Image Uri");

        et_newPost2_location = findViewById(R.id.et_newPost2_location);
        et_newPost2_caption = findViewById(R.id.et_newPost2_caption);
        btn_newPost2_back = findViewById(R.id.btn_newPost2_back);
        btn_newPost2_next = findViewById(R.id.btn_newPost2_next);

        btn_newPost2_back.setOnClickListener(this);
        btn_newPost2_next.setOnClickListener(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == btn_newPost2_back.getId()) {
            finish();
        } else if (view.getId() == btn_newPost2_next.getId()) {
            locationStr = et_newPost2_location.getText().toString();
            caption = et_newPost2_caption.getText().toString();
            Intent intent = new Intent(this, ConfirmPost.class);
            intent.putExtra("Image Uri", imageUri);
            intent.putExtra("Location", locationStr);
            intent.putExtra("Longitude",longitude);
            intent.putExtra("Latitude", latitude);
            intent.putExtra("Caption", caption);
            startActivity(intent);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            Log.d("LINE_INFO", "hit line 331");
            if (isLocationEnabled()) {
                Log.d("LINE_INFO", "hit line 333");
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d("LINE_INFO", "hit line 337");
                        Location location = task.getResult();
                        if (location == null) {
                            Log.d("LINE_INFO", "hit line 340");
                            requestNewLocationData();
                        } else {
                            Log.d("LINE_INFO", "hit line 344");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("LINE_INFO", "hit line 350");
                            LocationControllerRESTAPI locationControllerRESTAPI = new LocationControllerRESTAPI();
                            locationControllerRESTAPI.start(getApplicationContext(), latitude, longitude, et_newPost2_location);
                                Log.d("LINE_INFO", "hit line 352");
                        }
                    }
                });
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, LOCATION_REQUEST_CHECK_SETTINGS);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2500);
        locationRequest.setNumUpdates(1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            Log.d("LOCATION_COORDS", String.valueOf(location.getLongitude()));
            Log.d("LOCATION_COORDS", String.valueOf(location.getLatitude()));
        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CHECK_SETTINGS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}