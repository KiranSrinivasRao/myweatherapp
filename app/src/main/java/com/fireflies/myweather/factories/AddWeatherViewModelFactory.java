package com.fireflies.myweather.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.fireflies.myweather.database.AppDatabase;
import com.fireflies.myweather.viewmodel.AddWeatherViewModel;

public class AddWeatherViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase db;
    private final int itemId;

    public AddWeatherViewModelFactory(AppDatabase appDatabase, int itemId) {
        this.db = appDatabase;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new AddWeatherViewModel(db, itemId);
    }
}
