package com.oneupdog.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nbjensen on 12/3/15.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static String getCurrentSortValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String value = prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_value_mostpopular));

        Log.d(TAG, "SortValue: " + value);
        return value;
    }

    public static void setCurrentSortValue(Context context, String value) {
        if(value == null) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_sort_key), value);
        editor.commit();
    }


    public static int getSortSelection(Context context) {
        int retValue =0;

        String value = getCurrentSortValue(context);

        if(value == null)
            return retValue;

        if(value.equals((context.getString(R.string.pref_value_mostpopular)))) {
            retValue = 0;
        } else if(value.equals((context.getString(R.string.pref_value_toprated)))) {
            retValue = 1;
        }

        return retValue;
    }

    public static void setSortValue(Context context, int position) {
        String value = context.getString(R.string.pref_value_mostpopular);

        if(position == 0) {
            value = context.getString(R.string.pref_value_mostpopular);
        } else if(position == 1) {
            value = context.getString(R.string.pref_value_toprated);
        }

        setCurrentSortValue(context, value);
    }

    public static String buildLink(String posterPath) {
        return "http://image.tmdb.org/t/p/w150" + posterPath;
    }

    public static String convertToDate(long time) {

        Date date = new Date(time);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(date);
    }
}
