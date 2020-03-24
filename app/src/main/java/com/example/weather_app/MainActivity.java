package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    public static String NAME_OF_CITY = "NAME_OF_CITY_INTENT";
    public static String LATITUDE = "LATITUDE_INTENT";
    public static String LONGITUDE = "LONGITUDE_INTENT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        mapBoxToken = getResources().getString(R.string.map_box_token);
        setContentView(R.layout.activity_main);


        pb = findViewById(R.id.city_progress);

        autoCompleteTextView = findViewById(R.id.select_city_text_view);
        autoCompleteTextView.setDropDownBackgroundDrawable(getResources().getDrawable(R.color.autoCompleteDropDown));
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

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("hellhlhlhlhlhl","shit");
                Object item = parent.getItemAtPosition(position);
                if (item instanceof City) {
                    City city = (City) item;
                    Intent intent = new Intent(getBaseContext(), WeatherActivity.class);
                    intent.putExtra(MainActivity.NAME_OF_CITY, city.getName());
                    intent.putExtra(MainActivity.LONGITUDE, city.getCenter().first);
                    intent.putExtra(MainActivity.LATITUDE, city.getCenter().second);
                    startActivity(intent);
                }
            }
        });
    }

    public static void LoadCities(Context context) {
        ArrayAdapter<City> listAdapter = new ArrayAdapter<City>(context, R.layout.city_adapter, cities);
        MainActivity.autoCompleteTextView.setAdapter(listAdapter);
        MainActivity.autoCompleteTextView.showDropDown();
        MainActivity.pb.setVisibility(View.INVISIBLE);
        Log.d("Load:", String.valueOf(cities.length));
    }

}
