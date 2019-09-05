package com.fireflies.myweather.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    /**
     * Get Day from the epoch
     *
     * @param input epoch
     * @return which day in a week - Monday or Tuesday or Wednesday or Thursday or Friday or Saturday or Sunday
     */
    public static String getDayFromString(String input) {

        Date dayLong = new Date(Long.parseLong(input) * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dayLong);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Get human readable date time format
     *
     * @param input long
     * @return String with the format dd MMMM yyyy hh:mm:ss
     */
    public static String getDateTimeFromLong(long input) {

        Date dayLong = new Date((input) * 1000);
        String pattern = "dd MMMM yyyy hh:mm:ss";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dayLong);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

        return simpleDateFormat.format(calendar.getTime());
    }

}
