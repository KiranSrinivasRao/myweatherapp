package com.fireflies.myweather.models;

import com.google.gson.annotations.SerializedName;

public class Condition {

    @SerializedName("text")
    private String text;
    @SerializedName("icon")
    private String icon;
    @SerializedName("code")
    private String code;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
