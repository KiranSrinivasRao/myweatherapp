package com.fireflies.myweather.interfaces;

import java.io.Reader;
import java.lang.reflect.Type;

public interface IParser {

    /**
     * Abstract method to parse Json data
     *
     * @param reader          Reader instance
     * @param classOfResponse java.lang.Class<T> instance
     * @return T
     */
    @SuppressWarnings({"unused", "SameReturnValue"})
    <T> T fromJson(Reader reader, Class<T> classOfResponse);

    /**
     * Abstract method to parse Json data
     *
     * @param string          String value
     * @param classOfResponse java.lang.Class<T> instance
     * @return T
     */
    <T> T fromJson(String string, Class<T> classOfResponse);

    /**
     * Abstract method to parse Json data
     *
     * @param string         String value
     * @param collectionType java.lang.reflect.Type instance
     * @return T
     */
    <T> T fromJson(String string, Type collectionType);

    /**
     * @param src Object
     * @return String value
     */
    String toJson(Object src);

    /**
     * @param src Object
     * @return String value
     */
    String toJsonWithExposeAnnotation(Object src);
}
