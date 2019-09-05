package com.fireflies.myweather.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Room will create a table with a name WeatherData for our database
 * when we add a "Entity" Annotation
 */
@Entity(tableName = "weather")
public class WeatherEntry {

    /**
     * Room requires one of the field or member variable to have a "PrimaryKey" Annotation
     * setting autoGenerate = true will make Room to generate keys automatically
     * <p>
     * For Date Objects - it's not going to build so need to define TypeConverters,
     * eg: DateConverter class is written to cater the need of SQLite.
     * <p>
     * this class should be added in AppDatabase with @TypeConverters annotation
     * <p>
     * SQLite supports only REAL, INTEGER, TEXT, BLOB, NULL Classes
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String imageLocationURL;
    private String region;
    private String day;
    private double maxTempInC;
    private double minTempInC;

    // Room will always look out for one Constructor, if we have 2 then we should add
    // "Ignore" annotation for one of the Constructor
    @Ignore
    public WeatherEntry(String imageLocationURL, String region, String day, double maxTempInC, double minTempInC) {
        this.imageLocationURL = imageLocationURL;
        this.region = region;
        this.day = day;
        this.maxTempInC = maxTempInC;
        this.minTempInC = minTempInC;
    }

    /**
     * @param id               identifier
     * @param imageLocationURL image location URL  - Uniform Resource Locator
     * @param region           region
     * @param day              day time
     * @param maxTempInC       max temperature in degree centigrade
     * @param minTempInC       min temperature in degree centigrade
     */
    WeatherEntry(int id, String imageLocationURL, String region, String day, double maxTempInC, double minTempInC) {
        this.id = id;
        this.imageLocationURL = imageLocationURL;
        this.region = region;
        this.day = day;
        this.maxTempInC = maxTempInC;
        this.minTempInC = minTempInC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageLocationURL() {
        return imageLocationURL;
    }

    public void setImageLocationURL(String imageLocationURL) {
        this.imageLocationURL = imageLocationURL;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getMaxTempInC() {
        return maxTempInC;
    }

    public void setMaxTempInC(double maxTempInC) {
        this.maxTempInC = maxTempInC;
    }

    public double getMinTempInC() {
        return minTempInC;
    }

    public void setMinTempInC(double minTempInC) {
        this.minTempInC = minTempInC;
    }
}
