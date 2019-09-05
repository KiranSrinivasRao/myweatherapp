package com.fireflies.myweather.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("forecastday")
    private List<ForeCastDays> foreCastDays;

    public List<ForeCastDays> getForeCastDays() {
        return foreCastDays;
    }

    public void setForeCastDays(List<ForeCastDays> foreCastDays) {
        this.foreCastDays = foreCastDays;
    }
}
