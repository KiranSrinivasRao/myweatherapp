package com.fireflies.myweather.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * AppDatabase should always be Abstract and links the Entity and Dao Objects,
 * Also it extends RoomDatabase
 * <p>
 * We should use Singleton pattern (Restricts the instantiation of a class)
 * -- Only one object of the class is created
 * Synchronised for Thread safe operation
 * DATABASE Annotation @Database is added to tell what are the classes has been added in
 * the table, version of the database and export schema is set to false since we are not exporting
 * entities can have multiple classes in our case its just WeatherEntry class
 * <p>
 * Abstract method that returns weatherDao
 */
@Database(entities = {WeatherEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "weatherlist";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        // Queries should be done in a separate thread to avoid locking of UI
                        // we will allow this ONLY TEMPORARILY to see that our DB is working
                        // later comment this allowMainThreadQueries
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract WeatherDao weatherDao();
}
