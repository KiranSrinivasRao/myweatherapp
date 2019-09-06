package com.fireflies.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fireflies.myweather.adapters.WeatherAdapter;
import com.fireflies.myweather.database.AppDatabase;
import com.fireflies.myweather.database.WeatherEntry;
import com.fireflies.myweather.executers.AppExecutors;
import com.fireflies.myweather.factories.AddWeatherViewModelFactory;
import com.fireflies.myweather.viewmodel.AddWeatherViewModel;
import com.fireflies.myweather.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class AddWeatherActivity extends AppCompatActivity implements WeatherAdapter.ItemClickListener {

    private static final String LOG_TAG = AddWeatherActivity.class.getSimpleName();
    private static final int RESULT_FROM_SEARCH_CITY = 1453;
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;

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
        adapter = new WeatherAdapter(AddWeatherActivity.this, this);
        recyclerView.setAdapter(adapter);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerView.addItemDecoration(decoration);

        /*
         Adding a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         Swipe Right or Swipe Left will delete the entry from database and refreshes the UI
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
                        List<WeatherEntry> entries = adapter.getEntries();
                        mDb.weatherDao().deleteWeather(entries.get(position));
                        // We don't need to call if we are using LiveData - once the object is deleted from DB
                        // LiveData will notify
                        // setupViewModel();
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

        setupViewModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "+onResume()");

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

    private void setupViewModel() {

        // ViewModel class handles the life cycle events with ease - so we have replaced to avoid memory leaks
        // Retrieve data from ViewModel class
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getEntries()
                .observe(this, new Observer<List<WeatherEntry>>() {
                    @Override
                    public void onChanged(List<WeatherEntry> weatherEntries) {
                        Log.d(LOG_TAG, "Retrieve data from the db using ModelView<LiveData>");
                        adapter.setEntries(weatherEntries);
                    }
                });



        /*// LiveData Encapsulation - removing the executors and adding the Observers for which
        // we need to define Owner of the Lifecycle aware components - for this it's our activity
        final LiveData<List<WeatherEntry>> entries = mDb.weatherDao().getAllWeatherEntries();
        entries.observe(this, new Observer<List<WeatherEntry>>() {
            @Override
            public void onChanged(List<WeatherEntry> weatherEntries) {
                Log.d(LOG_TAG, "Retrieve data from the db using LiveData");
                adapter.setEntries(weatherEntries);
            }
        });*/


        // With Threading - Executors
        /*AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final List<WeatherEntry> entries = mDb.weatherDao().getAllWeatherEntries();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.setEntries(entries);

                        Log.d(LOG_TAG, "Checking weather entries --- only for testing and debugging");

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
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onItemClick(final int itemId) {

        // View Model way of retrieving data from Database
        // Intialize the factory and setup view model for the same
        AddWeatherViewModelFactory factory = new AddWeatherViewModelFactory(mDb, itemId);

        final AddWeatherViewModel addWeatherViewModel = ViewModelProviders
                .of(this, factory).get(AddWeatherViewModel.class);

        addWeatherViewModel.getEntry().observe(AddWeatherActivity.this, new Observer<WeatherEntry>() {
            @Override
            public void onChanged(WeatherEntry weatherEntry) {
                // Remember - We don't need updates so we remove the observer
                addWeatherViewModel.getEntry().removeObserver(this);
                Log.d(LOG_TAG, "Clicked on the item : " + weatherEntry.getId()
                        + ", " + weatherEntry.getRegion());
                Log.d(LOG_TAG, "Receiving database access from ViewModel<LiveData> ");
            }
        });



        /*// Live Data of retrieving data from Database
        final LiveData<WeatherEntry> entry = mDb.weatherDao().loadWeatherEntryByItemId(itemId);
        entry.observe(this, new Observer<WeatherEntry>() {
            @Override
            public void onChanged(WeatherEntry weatherEntry) {
                // Remember - We don't need updates so we remove the observer
                entry.removeObserver(this);
                Log.d(LOG_TAG, "Clicked on the item : " + weatherEntry.getId()
                        + ", "+  weatherEntry.getRegion() );
                Log.d(LOG_TAG, "Receiving database access from LiveData ");
            }
        });*/


        // With Threading - Executors
        /*AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final WeatherEntry entry = mDb.weatherDao().loadWeatherEntryByItemId(itemId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(LOG_TAG, "Clicked on the item : " + entry.getId()
                                + ", "+  entry.getRegion() );
                    }
                });

            }
        });*/

    }
}
