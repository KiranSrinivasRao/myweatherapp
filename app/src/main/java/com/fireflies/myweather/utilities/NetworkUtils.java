package com.fireflies.myweather.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private static String TAG = NetworkUtils.class.getSimpleName();

    private final static String PARAM_QUERY = "q";
    private static final String API_KEY = "4ae33f1faf6048cd82e25133190607";
    private final static String OPEN_MAP_BASE_URL = "http://api.apixu.com/v1/forecast.json?key=" + API_KEY;

    /**
     * Builds the URL used to query OpenMap.
     *
     * @param openMapSearchQuery The keyword that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static java.net.URL buildUrl(String openMapSearchQuery) {
        Uri builtUri = Uri.parse(OPEN_MAP_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, openMapSearchQuery)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }
}