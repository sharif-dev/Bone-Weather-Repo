package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.Service.DarkSky;

import java.util.Date;

public class WeatherActivity extends AppCompatActivity {
    public String nameOfCity;
    public String latitude;
    public String longitude;
    public static int SUCCESS_GET_WEATHER = 1;
    public static Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WeatherActivity.SUCCESS_GET_WEATHER){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        nameOfCity = intent.getStringExtra(MainActivity.NAME_OF_CITY);
        latitude = intent.getStringExtra(MainActivity.LATITUDE);
        longitude = intent.getStringExtra(MainActivity.LONGITUDE);

        DarkSky.create(latitude, longitude, DarkSky.DAILY_TYPE).run();
    }
}
