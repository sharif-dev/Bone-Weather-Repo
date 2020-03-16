package com.example.Model;

import android.util.Pair;

import androidx.annotation.NonNull;

public class City {
    private String name;
    private Pair<String, String> center;

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pair<String, String> getCenter() {
        return center;
    }

    public void setCenter(Pair<String, String> center) {
        this.center = center;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
