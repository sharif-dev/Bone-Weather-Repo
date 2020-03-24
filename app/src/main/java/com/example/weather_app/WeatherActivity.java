package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.Service.DarkSky;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.WatchEvent;
import java.util.TooManyListenersException;
import java.util.concurrent.ExecutionException;

public class WeatherActivity extends AppCompatActivity {
    public static String nameOfCity;
    public String latitude;
    public String longitude;
    public static RequestQueue requestQueue;
    public static int SUCCESS_GET_WEATHER = 1;
    public static int FAIL_GET_WEATHER = 2;
    public static int NO_NETWORK = 3;
    private static String CASH_FILE = "mycashData.json";
    public Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("MyHandler", "handleMessage: I'm in");
            if (msg.what == WeatherActivity.SUCCESS_GET_WEATHER){
                try {
                    OutputStreamWriter outputStreamWriter =
                            new OutputStreamWriter(getApplicationContext().openFileOutput(WeatherActivity.CASH_FILE, Context.MODE_PRIVATE));
                    outputStreamWriter.write(msg.obj.toString());
                    outputStreamWriter.close();
//                    JSONObject jsonObject = new JSONObject("asdfasf");
//                    jsonObject.
//                    Log.d("MyHandler", "handleMessage: " + msg.toString());
//                    FileOutputStream f = new FileOutputStream(new File(getExternalCacheDir() +  WeatherActivity.CASH_FILE));
//                    ObjectOutputStream o = new ObjectOutputStream(f);
//                    o.writeObject(msg.obj);
//                    o.close();
//                    f.close();
                    Log.d("MyHandler", "write response of DarkSky on file");
                }catch (Exception e){
                    Log.d("MyHandler", "cant't write on file: " + e.getMessage());
                }
                setDataForWeatherOfCity((JSONObject)msg.obj);
            }else if (msg.what == WeatherActivity.FAIL_GET_WEATHER){
                setContentView(R.layout.error_layout);
            }else if (msg.what == WeatherActivity.NO_NETWORK){
                Toast.makeText(getApplicationContext(), "no network", Toast.LENGTH_LONG)
                        .show();
                try {
//                    FileInputStream fileInputStream = new FileInputStream(new File(WeatherActivity.CASH_FILE));
//                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//                    JSONObject jsonObject = (JSONObject) objectInputStream.readObject();
//                    setDataForWeatherOfCity(jsonObject);
                    String result = "";
                    InputStream inputStream = getApplicationContext().
                            openFileInput(WeatherActivity.CASH_FILE);
                    if (inputStream != null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String data = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((data = bufferedReader.readLine()) != null){
                            stringBuilder.append("\n").append(data);
                        }
                        inputStream.close();
                        result = stringBuilder.toString();
                        Log.d("MyHandler", "handleMessage: show massage from cache");
                        setDataForWeatherOfCity(new JSONObject(result));
                    }
                }catch (Exception e){
                    setContentView(R.layout.error_layout);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_layout);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        try {
            nameOfCity = intent.getStringExtra(MainActivity.NAME_OF_CITY);
            latitude = intent.getStringExtra(MainActivity.LATITUDE);
            longitude = intent.getStringExtra(MainActivity.LONGITUDE);
            ConnectivityManager cm =
                    (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                DarkSky.create(latitude, longitude, DarkSky.DAILY_TYPE, nameOfCity, handler).run();
            }else {
                Message message = new Message();
                message.what = WeatherActivity.NO_NETWORK;
                handler.sendMessageDelayed(message, 500);
            }
        }catch (Exception e){
            setContentView(R.layout.error_layout);
        }
    }

    private void setDataForWeatherOfCity(JSONObject data){
        setContentView(R.layout.weather_main);
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
                String iconWeather = currentDayData.getString("icon").
                        replace('-', '_');
                int idOfHighTemperature = getResources().
                        getIdentifier(currentDay + "TopTemperature",
                                "id", this.getPackageName());
                int idOfLowTemperature = getResources().
                        getIdentifier(currentDay + "MinTemperature",
                                "id", this.getPackageName());
                int idOfIcon = getResources().getIdentifier(currentDay + "Icon",
                        "id", this.getPackageName());
                int idOfImageIcon = getResources().getIdentifier(iconWeather  +"_foreground"
                        , "mipmap", this.getPackageName());
                ((TextView)findViewById(idOfHighTemperature)).setText(highTemperature);
                ((TextView)findViewById(idOfLowTemperature)).setText(lowTemperature);
                ((ImageView)findViewById(idOfIcon)).setImageResource(idOfImageIcon);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}