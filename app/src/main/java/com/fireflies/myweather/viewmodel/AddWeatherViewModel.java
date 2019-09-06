package com.fireflies.myweather.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.fireflies.myweather.database.AppDatabase;
import com.fireflies.myweather.database.WeatherEntry;

public class AddWeatherViewModel extends ViewModel {

    private LiveData<WeatherEntry> entry;

    public AddWeatherViewModel(AppDatabase db, int itemId) {
        entry = db.weatherDao().loadWeatherEntryByItemId(itemId);
    }

    public LiveData<WeatherEntry> getEntry() {
        return entry;
    }
}
