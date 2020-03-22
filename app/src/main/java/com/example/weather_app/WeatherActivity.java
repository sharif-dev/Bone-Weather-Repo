package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.Service.DarkSky;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {
    public static String nameOfCity;
    public String latitude;
    public String longitude;
    public static RequestQueue requestQueue;
    public static int SUCCESS_GET_WEATHER = 1;
    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("MyHandler", "handleMessage: I'm in");
            if (msg.what == WeatherActivity.SUCCESS_GET_WEATHER){
                setDataForWeatherOfCity((JSONObject)msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        nameOfCity = intent.getStringExtra(MainActivity.NAME_OF_CITY);
        latitude = intent.getStringExtra(MainActivity.LATITUDE);
        longitude = intent.getStringExtra(MainActivity.LONGITUDE);

        nameOfCity = "Tehran";
        latitude = "35.689198";
        longitude = "51.388973";

        nameOfCity = intent.getExtras().getString("nameOfCity");
        latitude = intent.getExtras().getString("latitude");
        longitude = intent.getExtras().getString("longitude");

//        WorkerThread a = new WorkerThread();
//        a.handler = handler;
//        a.run();
//        handler.sendMessage(null);
        DarkSky.create(latitude, longitude, DarkSky.DAILY_TYPE, nameOfCity, handler).run();


    }


    private void setDataForWeatherOfCity(JSONObject data){
        ((TextView)findViewById(R.id.title_weather_page)).setText(WeatherActivity.nameOfCity);
        String[] allDays = new String[]{"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};
        int count = 0;
        try {
            JSONArray dataOfWeather = data.getJSONArray("data");
            for (String currentDay :
                    allDays) {
                JSONObject currentDayData = (JSONObject)dataOfWeather.get(count);
                String highTemperature =
                        Double.toString(currentDayData.getDouble("temperatureHigh"));
                String lowTemperature =
                        Double.toString(currentDayData.getDouble("temperatureLow"));
                int idOfHighTemperature = getResources().getIdentifier(currentDay + "TopTemperature", "id", this.getPackageName());
                int idOfLowTemperature = getResources().getIdentifier(currentDay + "MinTemperature", "id", this.getPackageName());
                ((TextView)findViewById(idOfHighTemperature)).setText(highTemperature);
                ((TextView)findViewById(idOfLowTemperature)).setText(lowTemperature);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
