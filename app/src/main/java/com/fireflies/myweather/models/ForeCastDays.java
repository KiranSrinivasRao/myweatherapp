package com.fireflies.myweather.models;

import com.google.gson.annotations.SerializedName;

public class ForeCastDays {

    @SerializedName("date")
    private String date;

    @SerializedName("date_epoch")
    private String dateEpoch;

    @SerializedName("day")
    private Day day;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getDateEpoch() {
        return dateEpoch;
    }

    public void setDateEpoch(String dateEpoch) {
        this.dateEpoch = dateEpoch;
    }
}
