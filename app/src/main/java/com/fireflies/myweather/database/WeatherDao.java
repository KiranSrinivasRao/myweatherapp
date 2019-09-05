package com.fireflies.myweather.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Creating a  Weather Data access object with @DAO Annotation
 * CRUD Operations
 * INSERT Annotation @Insert
 * QUERY Annotation @Query
 * UPDATE Annotation @Update
 * DELETE Annotation @Delete
 * We can request Objects back from the Database makes the ROOM - ORM (Object Relational Mapping)
 * <p>
 * in place of conflict - replace is the strategy we have chosen during Update
 */
@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather ORDER BY id")
    List<WeatherEntry> getAllWeatherEntries();

    @Insert
    void insertWeather(WeatherEntry weatherEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeather(WeatherEntry weatherEntry);

    @Delete
    void deleteWeather(WeatherEntry weatherEntry);


}
