package com.fireflies.myweather.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fireflies.myweather.database.AppDatabase;
import com.fireflies.myweather.database.WeatherEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final static String LOG_TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<WeatherEntry>> entries;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase mDb = AppDatabase.getsInstance(application);
        entries = mDb.weatherDao().getAllWeatherEntries();
        Log.d(LOG_TAG, "Actively retrieving the entries from the Database");
    }

    public LiveData<List<WeatherEntry>> getEntries() {
        return entries;
    }


}
