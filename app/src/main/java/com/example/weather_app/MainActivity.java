package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.Model.City;
import com.example.Service.MapBoxSRV;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static City cities[] = new City[0];
    private String mapBoxToken;
    public static RequestQueue requestQueue;
    public static Button b;
    public static ProgressBar pb;
    public static AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        mapBoxToken = getResources().getString(R.string.map_box_token);
        setContentView(R.layout.activity_main);


        b = findViewById(R.id.city_button);
        pb = findViewById(R.id.city_progress);

        autoCompleteTextView = findViewById(R.id.select_city_text_view);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pb.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String pre_city = autoCompleteTextView.getText().toString();
                        MapBoxSRV.getMatchesCities(pre_city, getBaseContext());
                    }
                };
                handler.postDelayed(runnable, 200);
            }
        });
    }

    public static void LoadCities(Context context) {
        Log.d("Load:", String.valueOf(cities.length));
        ArrayAdapter<City> listAdapter = new ArrayAdapter<City>(context, android.R.layout.activity_list_item, cities);
        MainActivity.autoCompleteTextView.setAdapter(listAdapter);
        MainActivity.pb.setVisibility(View.INVISIBLE);

    }

}
