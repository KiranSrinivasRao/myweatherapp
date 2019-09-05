package com.fireflies.myweather.models;

import com.google.gson.annotations.SerializedName;

public class Current {

    @SerializedName("temp_c")
    private String tempInc;

    @SerializedName("last_updated")
    private String last_updated;


    public String getTempInc() {
        return tempInc;
    }

    public void setTempInc(String tempInc) {
        this.tempInc = tempInc;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }
}
