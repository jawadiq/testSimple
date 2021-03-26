package com.example.testsimple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testsimple.apis.Apis;
import com.example.testsimple.apis.Constans;
import com.example.testsimple.models.Main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = "MainActivity";
    double lat, lng;
    LocationManager locationManager;
    Location location;
    String provider;
    Main main = new Main();
    int permissions = 0;
    TextView adress,weather,cityReg;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adress = findViewById(R.id.addRess);
        weather = findViewById(R.id.weather);
        cityReg=findViewById(R.id.cityReg);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                  Manifest.permission.ACCESS_FINE_LOCATION,
Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
            },permissions);
        }
        location = locationManager.getLastKnownLocation(provider);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.INTERNET
            },permissions);
        }
        locationManager.requestLocationUpdates(provider,400,1,this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        adress.append(String.valueOf(lat)+String.valueOf(lng));
        new GetWeather().execute(Constans.apiRequest(String.valueOf(lat),String.valueOf(lng)));
//        new GetWeather().execute(Constans.apiRequest(String.valueOf(35.5252),String.valueOf(18.1515)));

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                adress.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
    private class GetWeather extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = strings[0];
            Apis request = new Apis();
            stream = request.getHttpData(urlString);
            return stream;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("Error")) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                return;
            }
            Gson gson = new Gson();
            Type type = new TypeToken<Main>() {
            }.getType();
            main = gson.fromJson(s, type);
            cityReg.setText(main.getTimezone());
            weather.append("Time: "+Constans.unitTimeToDate(main.getCurrently().getTime()));
            weather.append(System.getProperty("line.separator"));
            weather.append("Summary:"+main.getCurrently().getSummary());
            weather.append(System.getProperty("line.separator"));
            weather.append("Image: "+main.getCurrently().getIcon());//.... To be Converted to image
            weather.append(System.getProperty("line.separator"));
            weather.append("Precep Intensity: "+main.getCurrently().getPrecipIntensity().toString());
            weather.append(System.getProperty("line.separator"));
            weather.append("Probability :"+main.getCurrently().getPrecipProbability().toString());
            weather.append(System.getProperty("line.separator"));
            weather.append("Temprature: "+main.getCurrently().getTemperature().toString());
            weather.append(System.getProperty("line.separator"));
            weather.append("App Temprature: "+main.getCurrently().getApparentTemperature().toString());
            weather.append(System.getProperty("line.separator"));
            weather.append("Dew Point: "+main.getCurrently().getDewPoint().toString());
            weather.append(System.getProperty("line.separator"));
            weather.append("Humidity: "+main.getCurrently().getHumidity().toString());
            weather.append(System.getProperty("line.separator"));
            weather.append("Ozone: "+main.getCurrently().getOzone().toString());
        }
    }


}