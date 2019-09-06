package com.fireflies.myweather.database;


import androidx.lifecycle.LiveData;
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
 * Room has SQL Validation @ Compile time  - if any simple errors is also identified and caught during compile time
 * Run time would take a long time to identify a bug
 * <p>
 * in place of conflict - replace is the strategy we have chosen during Update
 *
 * Android Architecture Component
 * - LiveData is wrapped around the objects in order to know
 * the database changes (Notified using Observer pattern)
 *
 * By default LiveData will run outside of the main thread - because of which we can avoid Executors
 */
@Dao
public interface WeatherDao {

    @Query("SELECT * FROM weather ORDER BY id")
    LiveData<List<WeatherEntry>> getAllWeatherEntries();

    @Insert
    void insertWeather(WeatherEntry weatherEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeather(WeatherEntry weatherEntry);

    @Delete
    void deleteWeather(WeatherEntry weatherEntry);

    @Query("SELECT * FROM weather WHERE id = :itemId")
    LiveData<WeatherEntry> loadWeatherEntryByItemId(int itemId);


}
