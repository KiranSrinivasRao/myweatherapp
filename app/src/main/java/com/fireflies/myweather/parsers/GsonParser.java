package com.fireflies.myweather.parsers;

import android.util.Log;

import com.fireflies.myweather.interfaces.IParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.lang.reflect.Type;

public class GsonParser implements IParser {
    /**
     * The TAG
     */
    private static final String TAG = GsonParser.class.getSimpleName();

    /* Singleton instance */
    private static GsonParser sGsonInstance;

    /* Gson parser module instance */
    private Gson mGson;

    /* Gson parser module instance without excluding annotation */
    private Gson mGsonCustom;

    /**
     * Default constructor
     */
    private GsonParser() {

    }

    /**
     * Creates  a singleton instance of GsonParser class
     *
     * @return sGsonInstance GsonParser instance
     */
    static GsonParser getGsonInstance() {
        if (sGsonInstance == null) {
            sGsonInstance = new GsonParser();
        }
        return sGsonInstance;
    }

    /**
     * Creates  a single instance of Gson
     *
     * @return mGson Gson instance
     */

    private Gson getGsonObject() {
        if (mGson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            mGson = gsonBuilder.create();
        }

        return mGson;
    }

    /**
     * Creates  a single instance of Gson
     *
     * @return mGson Gson instance
     */
    private Gson getGsonObjectWithExposeAnnotation() {
        if (mGsonCustom == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            mGsonCustom = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
        }
        return mGsonCustom;
    }


    /**
     * Overridden  method to parse Json data
     *
     * @param reader          Reader instance
     * @param classOfResponse java.lang.Class<T> instance
     * @return T
     */
    public <T> T fromJson(Reader reader, Class<T> classOfResponse) {
        // Parse Json data from Reader
        return null;
    }

    /**
     * Overridden  method to parse Json data
     *
     * @param string          String value
     * @param classOfResponse java.lang.Class<T> instance
     * @return T
     */
    @Override
    public <T> T fromJson(String string, Class<T> classOfResponse) {
        Gson gson = getGsonObject();
        T response = null;
        try {
            Log.d(TAG, "+fromJson() - " + classOfResponse);
            response = gson.fromJson(string, classOfResponse);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return response;
    }

    @Override
    public <T> T fromJson(String string, Type collectionType) {
        Gson gson = getGsonObject();
        T response = null;
        try {
            Log.d(TAG, "+fromJson() - " + collectionType);
            response = gson.fromJson(string, collectionType);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return response;
    }

    /**
     * Overridden  method to parse Object
     *
     * @param src object
     * @return String
     */
    @Override
    public String toJson(Object src) {
        Gson gson = getGsonObject();
        String jsonContent = null;
        try {
            jsonContent = gson.toJson(src);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonContent;
    }

    /**
     * Overridden  method to parse Object
     *
     * @param src object
     * @return String
     */
    @Override
    public String toJsonWithExposeAnnotation(Object src) {
        Gson gson = getGsonObjectWithExposeAnnotation();
        String jsonContent = null;
        try {
            jsonContent = gson.toJson(src);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return jsonContent;
    }


}
