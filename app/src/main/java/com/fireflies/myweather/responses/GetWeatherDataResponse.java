package com.fireflies.myweather.responses;


import com.fireflies.myweather.models.Current;
import com.fireflies.myweather.models.Forecast;
import com.fireflies.myweather.models.Location;
import com.google.gson.annotations.SerializedName;

public class GetWeatherDataResponse extends Response {

    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private Current current;

    @SerializedName("forecast")
    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public Location getLocation() {
        return location;
    }


    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }
}
