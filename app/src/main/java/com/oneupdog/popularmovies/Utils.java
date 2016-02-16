package com.oneupdog.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

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
        if (value == null) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.pref_sort_key), value);
        editor.commit();
    }


    public static int getSortSelection(Context context) {
        int retValue = 0;

        String value = getCurrentSortValue(context);

        if (value == null)
            return retValue;

        if (value.equals((context.getString(R.string.pref_value_mostpopular)))) {
            retValue = 0;
        } else if (value.equals((context.getString(R.string.pref_value_toprated)))) {
            retValue = 1;
        } else if (value.equals(("favorites"))) {
            retValue = 2;
        }

        return retValue;
    }

    public static void setSortValue(Context context, int position) {
        String value = context.getString(R.string.pref_value_mostpopular);

        if (position == 0) {
            value = context.getString(R.string.pref_value_mostpopular);
        } else if (position == 1) {
            value = context.getString(R.string.pref_value_toprated);
        } else if (position == 2) {
            value = "favorites";
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void playYouTubeVideo(Context context, String videoId) {
        //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+video)));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
        intent.putExtra("VIDEO_ID", videoId);
        context.startActivity(intent);
    }
}
