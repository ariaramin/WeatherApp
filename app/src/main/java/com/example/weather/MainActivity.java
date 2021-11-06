package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Models.APIClient;
import com.example.weather.Models.APIEndpoint;
import com.example.weather.Models.APIResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView iconImage;
    TextView tempText, weatherText, cityText, maxTempText, minTempText, sunriseText, sunsetText;
    EditText searchText;

    Dialog loadingDialog;
    private static final int PERMISSION_ID = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String API_KEY = "eb17e86a2342d4673802116cc69bc4de";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconImage = findViewById(R.id.iconImageView);
        tempText = findViewById(R.id.tempTextView);
        weatherText = findViewById(R.id.weatherTextView);
        cityText = findViewById(R.id.locationTextView);
        maxTempText = findViewById(R.id.maxTempTextView);
        minTempText = findViewById(R.id.minTempTextView);
        sunriseText = findViewById(R.id.sunriseTextView);
        sunsetText = findViewById(R.id.sunsetTextView);
        searchText = findViewById(R.id.searchEditText);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Check if permissions granted
        if (permissionGranted()) {
            getLastLocation();
        } else {
            checkPermission();
        }

        loadingDialog = ProgressDialog.show(this, "",
                "Loading...", true);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getLocationWeather(editable.toString());
            }
        });
    }

    // Get location weather by coordinates
    private void getLocationWeather(String lat, String lon) {
        APIEndpoint request = APIClient.getAPIClient().create(APIEndpoint.class);
        request.getWeather(lat, lon, API_KEY).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.body() != null) {
                    APIResponse result = response.body();
                    setIcon(result.getWeather().get(0).getMain(), getTime());
                    tempText.setText(result.getMain().getTemp() + "°");
                    weatherText.setText(result.getWeather().get(0).getDescription());
                    cityText.setText(result.getCity() + "," + result.getSys().getCountry());
                    maxTempText.setText(result.getMain().getTemp_max() + "°");
                    minTempText.setText(result.getMain().getTemp_min() + "°");
                    sunriseText.setText(getTimezoneTime(result.getSys().getSunrise(), result.getTimezone()));
                    sunsetText.setText(getTimezoneTime(result.getSys().getSunset(), result.getTimezone()));
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Get location weather by city
    private void getLocationWeather(String location) {
        APIEndpoint request = APIClient.getAPIClient().create(APIEndpoint.class);
        request.getWeather(location, API_KEY).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if (response.body() != null) {
                    APIResponse result = response.body();
                    setIcon(result.getWeather().get(0).getMain(), getTime());
                    tempText.setText(result.getMain().getTemp() + "°");
                    weatherText.setText(result.getWeather().get(0).getDescription());
                    cityText.setText(result.getCity() + "," + result.getSys().getCountry());
                    maxTempText.setText(result.getMain().getTemp_max() + "°");
                    minTempText.setText(result.getMain().getTemp_min() + "°");
                    sunriseText.setText(getTimezoneTime(result.getSys().getSunrise(), result.getTimezone()));
                    sunsetText.setText(getTimezoneTime(result.getSys().getSunset(), result.getTimezone()));
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Get time by timezone
    private String getTimezoneTime(int unixTime, int offset) {
        Date dt = new Date((unixTime + offset) * 1000L);
        return new SimpleDateFormat("HH:mm a").format(dt);
    }

    // Set icon by weather status
    private void setIcon(String main, String time) {
        switch (main.toLowerCase()) {
            case "clouds":
                iconImage.setImageResource(R.drawable.cloud);
                break;

            case "clear":
                if (time.equals("PM")) {
                    iconImage.setImageResource(R.drawable.sun);
                } else {
                    iconImage.setImageResource(R.drawable.moon);
                }
                break;

            case "rain":
                if (time.equals("PM")) {
                    iconImage.setImageResource(R.drawable.light_rain);
                } else {
                    iconImage.setImageResource(R.drawable.light_rain_night);
                }
                break;

            case "haze":
                iconImage.setImageResource(R.drawable.haze);
                break;

            case "fog":
                if (time.equals("PM")) {
                    iconImage.setImageResource(R.drawable.haze);
                } else {
                    iconImage.setImageResource(R.drawable.fog);
                }
                break;

            case "snow":
                iconImage.setImageResource(R.drawable.snow);
                break;

            case "storm":
                iconImage.setImageResource(R.drawable.thunderstorm);
                break;

            case "sleet":
                iconImage.setImageResource(R.drawable.sleet);
                break;
        }
    }

    private String getTime() {
        String time = new SimpleDateFormat("a").format(new Date());
        return time;
    }

    // Get location coordinates
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (permissionGranted()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        String lat = String.format("%.4f", location.getLatitude());
                        String lon = String.format("%.4f", location.getLongitude());
                        getLocationWeather(lat, lon);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Please enable your location.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
            }
        } else {
            checkPermission();
        }
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @SuppressLint("NewApi")
    private boolean permissionGranted() {
        return (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    // Check permission
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permissionGranted()) {
                String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, PERMISSION_ID);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            for (int gResult :
                    grantResults) {
                if (gResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}