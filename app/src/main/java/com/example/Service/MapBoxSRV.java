package com.example.Service;


import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.Model.City;
import com.example.weather_app.MainActivity;
import com.example.weather_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapBoxSRV {
    public static void getMatchesCities(String preCityName, final Context context) {
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + preCityName + ".json?access_token=" + context.getResources().getString(R.string.map_box_token);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("features");
                            City[] cities = new City[jsonArray.length()];
                            for (int index = 0; index < jsonArray.length(); index++) {
                                JSONObject jsonCity = ((JSONObject) jsonArray.get(index));
                                City c = new City(jsonCity.getString("text"));
                                String x = jsonCity.getJSONArray("center").get(0).toString();
                                String y = jsonCity.getJSONArray("center").get(1).toString();
                                Pair<String, String> center = new Pair<>(x, y);
                                cities[index] = c;
                            }
                            MainActivity.cities = cities;
                            MainActivity.LoadCities(context);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Errrr", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("fffff", "onErrorResponse: ");
                    }
                });
        MainActivity.requestQueue.add(getRequest);
    }

}
