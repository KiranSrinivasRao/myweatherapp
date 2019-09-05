package com.fireflies.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fireflies.myweather.database.AppDatabase;
import com.fireflies.myweather.database.WeatherEntry;
import com.fireflies.myweather.executers.AppExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class AddWeatherActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddWeatherActivity.class.getSimpleName();
    private static final int RESULT_FROM_SEARCH_CITY = 1453;
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    ///private WeatherAdapter.ItemClickListener itemClickListener;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weather);

        // Set the RecyclerView to its corresponding view
        recyclerView = findViewById(R.id.recycler_view);
        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter and attach it to the RecyclerView
        adapter = new WeatherAdapter(AddWeatherActivity.this);
        recyclerView.setAdapter(adapter);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //  implement swipe to delete

                AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<WeatherEntry> tasks = adapter.getEntries();
                        mDb.weatherDao().deleteWeather(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent searchCityActivity = new Intent(AddWeatherActivity.this, CitySearchActivity.class);
                startActivityForResult(searchCityActivity, RESULT_FROM_SEARCH_CITY);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // With Threading - Executors
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<WeatherEntry> entries = mDb.weatherDao().getAllWeatherEntries();

                adapter.setEntries(entries);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < entries.size(); i++) {
                            Log.e(LOG_TAG, String.valueOf(entries.get(i).getId()));
                            Log.e(LOG_TAG, entries.get(i).getImageLocationURL());
                            Log.e(LOG_TAG, entries.get(i).getRegion());
                            Log.e(LOG_TAG, entries.get(i).getDay());
                            Log.e(LOG_TAG, String.valueOf(entries.get(i).getMaxTempInC()));
                            Log.e(LOG_TAG, String.valueOf(entries.get(i).getMinTempInC()));
                        }
                    }
                });
            }
        });


        // Without Threading and //.allowMainThreadQueries() was enabled in AppDatabase
//        List<WeatherEntry> entries = mDb.weatherDao().getAllWeatherEntries();
//
//        for (int i = 0 ; i < entries.size(); i++) {
//            Log.e(LOG_TAG, String.valueOf(entries.get(i).getId()));
//            Log.e(LOG_TAG, entries.get(i).getImageLocationURL());
//            Log.e(LOG_TAG, entries.get(i).getRegion());
//            Log.e(LOG_TAG, Utils.getDateTimeFromLong(entries.get(i).getDay()));
//            Log.e(LOG_TAG, String.valueOf(entries.get(i).getMaxTempInC()));
//            Log.e(LOG_TAG, String.valueOf(entries.get(i).getMinTempInC()));
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
