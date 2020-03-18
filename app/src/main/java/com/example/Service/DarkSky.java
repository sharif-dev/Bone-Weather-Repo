package com.example.Service;

import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.weather_app.R;
import com.example.weather_app.WeatherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Dictionary;

public class DarkSky implements Runnable {
    public static String DAILY_TYPE = "daily";
    private String latitude;
    private String longitude;
    private String purposeType;
    private JSONArray result;
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

//    public JSONArray getResult() {
//        return result;
//    }

    public static DarkSky create(String latitude, String longitude, String purposeType){
        DarkSky obj =  new DarkSky();
        obj.setLatitude(latitude);
        obj.setLongitude(longitude);
        obj.setPurposeType(purposeType);
        return obj;
    }

    private void getForecastWeatherDaily(){
        String url = "https://api.darksky.net/forecast/" +  R.string.DARK_SKY_TOKEN + "/" +
                latitude + "," + longitude;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            result = response.getJSONArray("daily");
                            message.what = WeatherActivity.SUCCESS_GET_WEATHER;
                            message.obj = result;
                            WeatherActivity.handler.sendMessage(message);
                        } catch (JSONException e) {
                            result = null;
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });

    }


    @Override
    public void run() {
        if (this.purposeType.equals(DarkSky.DAILY_TYPE)){
            getForecastWeatherDaily();
        }
    }
}

