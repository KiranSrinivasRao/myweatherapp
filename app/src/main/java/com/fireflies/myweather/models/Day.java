package com.fireflies.myweather.models;

import com.google.gson.annotations.SerializedName;

public class Day {

    @SerializedName("avgtemp_c")
    private String avgTempInc;

    @SerializedName("maxtemp_c")
    private String maxTempInC;
    @SerializedName("mintemp_c")
    private String minTempInC;
    @SerializedName("condition")
    private Condition condition;

    public String getAvgTempInc() {
        return avgTempInc;
    }

    public void setAvgTempInc(String avgTempInc) {
        this.avgTempInc = avgTempInc;
    }

    public String getMaxTempInC() {
        return maxTempInC;
    }

    public void setMaxTempInC(String maxTempInC) {
        this.maxTempInC = maxTempInC;
    }

    public String getMinTempInC() {
        return minTempInC;
    }

    public void setMinTempInC(String minTempInC) {
        this.minTempInC = minTempInC;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
