package com.fireflies.myweather.parsers;

import com.fireflies.myweather.interfaces.IParser;

public class ParserFactory {

    private static String TAG = ParserFactory.class.getSimpleName();

    /**
     * Creates Gson parser instance by default
     */
    public static IParser getParser() {
        return GsonParser.getGsonInstance();
    }
}
