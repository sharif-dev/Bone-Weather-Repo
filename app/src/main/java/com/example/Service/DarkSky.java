package com.example.Service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.weather_app.MainActivity;
import com.example.weather_app.R;
import com.example.weather_app.WeatherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Dictionary;

public class DarkSky implements Runnable {
    public static String DAILY_TYPE = "daily";
    private static final String DARK_SKY_TOKEN  = "3e8755a5390d0908c7e0949f881510dc";
    private String latitude;
    private String longitude;
    private String purposeType;
    private JSONArray result;
    private String nameOfCity;
    private Handler handler;
    private int what;
    private Message message;

    private DarkSky() {
    }

    private void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    private void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    private void setPurposeType(String purposeType) {
        this.purposeType = purposeType;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setNameOfCity(String nameOfCity) {
        this.nameOfCity = nameOfCity;
    }
//    public JSONArray getResult() {
//        return result;
//    }

    public static DarkSky create(String latitude, String longitude, String purposeType,
                                 String nameOfCity, Handler handler){
        DarkSky obj =  new DarkSky();
        obj.setLatitude(latitude);
        obj.setLongitude(longitude);
        obj.setPurposeType(purposeType);
        obj.setNameOfCity(nameOfCity);
        obj.setHandler(handler);
        return obj;
    }

    private void getForecastWeatherDaily(){
        String url = "https://api.darksky.net/forecast/" + DarkSky.DARK_SKY_TOKEN  +
                "/" + latitude + "," + longitude;
        Log.d("URL", "getForecastWeatherDaily: " + url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            message = new Message();
                            message.what = WeatherActivity.SUCCESS_GET_WEATHER;
                            message.obj = response.get(DarkSky.DAILY_TYPE);
                            handler.sendMessage(message);
                            Log.d("VOLLEY", "onResponse: OK");
                        } catch (Exception e) {
                            result = null;
                            e.printStackTrace();
                            Log.d("VOLLEY", "onResponse: SHIT");
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });
        WeatherActivity.requestQueue.add(getRequest);
    }


    @Override
    public void run() {
        if (this.purposeType.equals(DarkSky.DAILY_TYPE)){
            getForecastWeatherDaily();
        }
    }
}

