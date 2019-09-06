package com.fireflies.myweather;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.fireflies.myweather.database.AppDatabase;
import com.fireflies.myweather.database.WeatherEntry;
import com.fireflies.myweather.databinding.ActivityCitySearchBinding;
import com.fireflies.myweather.executers.AppExecutors;
import com.fireflies.myweather.interfaces.IParser;
import com.fireflies.myweather.models.Day;
import com.fireflies.myweather.parsers.ParserFactory;
import com.fireflies.myweather.responses.GetWeatherDataResponse;
import com.fireflies.myweather.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class CitySearchActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    /* A constant to save and restore the URL that is being displayed */
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final String TAG = CitySearchActivity.class.getSimpleName();

    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */
    private static final int OPEN_MAP_SEARCH_LOADER = 22;

    // Data binding for the UI
    private ActivityCitySearchBinding binding;

    // AppDatabase instance
    private AppDatabase mDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CitySearchActivity.this, R.layout.activity_city_search);

        mDb = AppDatabase.getsInstance(getApplicationContext());

        if (savedInstanceState != null) {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);

            binding.urlPath.setText(queryUrl);
        }


        binding.startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOpenMapSearchQuery();
            }
        });
        /*
         * Initialize the loader
         */
        LoaderManager.getInstance(this).initLoader(OPEN_MAP_SEARCH_LOADER, null, this);
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using {@link NetworkUtils}) for the openMap repository you'd like to find, displays
     * that URL in a TextView, and finally request that an AsyncTaskLoader performs the GET request.
     */
    private void makeOpenMapSearchQuery() {
        String openMapQuery = binding.urlPath.getText().toString();

        /*
         * If the user didn't enter anything, there's nothing to search for. In the case where no
         * search text was entered but the search button was clicked, we will display a message
         * stating that there is nothing to search for and we will not attempt to load anything.
         *
         * If there is text entered in the search box when the search button was clicked, we will
         * create the URL that will return our openMap search results, display that URL, and then
         * pass that URL to the Loader. The reason we pass the URL as a String is simply a matter
         * of convenience. There are other ways of achieving this same result, but we felt this
         * was the simplest.
         */
        if (TextUtils.isEmpty(openMapQuery)) {
            binding.jsonResponse.setText(getResources().getString(R.string.no_query));
            return;
        }

        URL openMapSearchUrl = NetworkUtils.buildUrl(openMapQuery);
        binding.cityQuery.setText(openMapSearchUrl.toString());

        Log.d(TAG, openMapSearchUrl.toString());

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, openMapSearchUrl.toString());

        /*
         * Now that we've created our bundle that we will pass to our Loader, we need to decide
         * if we should restart the loader (if the loader already existed) or if we need to
         * initialize the loader (if the loader did NOT already exist).
         *
         * We do this by first store the support loader manager in the variable loaderManager.
         * All things related to the Loader go through through the LoaderManager. Once we have a
         * hold on the support loader manager, (loaderManager) we can attempt to access our
         * openMapSearchLoader. To do this, we use LoaderManager's method, "getLoader", and pass in
         * the ID we assigned in its creation. You can think of this process similar to finding a
         * View by ID. We give the LoaderManager an ID and it returns a loader (if one exists). If
         * one doesn't exist, we tell the LoaderManager to create one. If one does exist, we tell
         * the LoaderManager to restart it.
         */
        LoaderManager loaderManager = LoaderManager.getInstance(CitySearchActivity.this);
        Loader<String> openMapSearchLoader = loaderManager.getLoader(OPEN_MAP_SEARCH_LOADER);
        if (openMapSearchLoader == null) {
            loaderManager.initLoader(OPEN_MAP_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(OPEN_MAP_SEARCH_LOADER, queryBundle, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.get_weather) {

            makeOpenMapSearchQuery();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showJsonDataView() {
        /* First, make sure the error is invisible */
        binding.errorMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the JSON data is visible */
        binding.jsonResponse.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        binding.jsonResponse.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        binding.errorMessage.setVisibility(View.VISIBLE);
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            // COMPLETED (1) Create a String member variable called mOpenMapJson that will store the raw JSON
            /* This String will contain the raw JSON from the results of our Open Map search */
            String mOpenMapJson;

            @Override
            protected void onStartLoading() {
                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    return;
                }

                // COMPLETED (2) If mOpenMapJson is not null, deliver that result. Otherwise, force a load
                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                if (mOpenMapJson != null) {
                    deliverResult(mOpenMapJson);
                } else {
                    /*
                     * When we initially begin loading in the background, we want to display the
                     * loading indicator to the user
                     */
                    binding.loadingIndicator.setVisibility(View.VISIBLE);

                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {


                /* Extract the search query from the args using our constant */
                String searchQueryUrlString = null;
                if (args != null) {
                    searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);
                }

                /* If the user didn't enter anything, there's nothing to search for */
                if (TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }

                /* Parse the URL from the passed in String and perform the search */
                try {
                    URL openMapUrl = new URL(searchQueryUrlString);
                    return NetworkUtils.getResponseFromHttpUrl(openMapUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }


            @Override
            public void deliverResult(@Nullable String data) {
                mOpenMapJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        /* When we finish loading, we want to hide the loading indicator from the user. */

        binding.loadingIndicator.setVisibility(View.GONE);
        /*
         * If the results are null, we assume an error has occurred. There are much more robust
         * methods for checking errors, but we wanted to keep this particular example simple.
         */
        if (null == data) {
            showErrorMessage();
        } else {
            binding.jsonResponse.setText(data);


            IParser parser = ParserFactory.getParser();
            GetWeatherDataResponse jsonResponse = parser.fromJson(data, GetWeatherDataResponse.class);
            Day day = jsonResponse.getForecast().getForeCastDays().get(0).getDay();
            String name = jsonResponse.getLocation().getName();
            if (name.isEmpty()) {
                name = jsonResponse.getLocation().getRegion().concat(",\n")
                        .concat(jsonResponse.getLocation().getCountry());
            }

            final WeatherEntry weatherEntry = new WeatherEntry(day.getCondition().getIcon(),
                    name,
                    jsonResponse.getLocation().getLocaltime(),
                    Double.parseDouble(day.getMaxTempInC()),
                    Double.parseDouble(day.getMinTempInC()));

            AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.weatherDao().insertWeather(weatherEntry);
                }
            });


            Log.e(TAG, jsonResponse.getLocation().getRegion());
            showJsonDataView();

            finish();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        /*
         * We aren't using this method in our example application, but we are required to Override
         * it to implement the LoaderCallbacks<String> interface
         */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = binding.urlPath.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);
    }
}
