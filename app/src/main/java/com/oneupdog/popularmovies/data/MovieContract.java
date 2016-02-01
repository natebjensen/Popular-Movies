package com.oneupdog.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by nbjensen on 12/8/15.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.oneupdog.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    // This code is from the Udacity Sunsine App
    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /**
     * Defines a movie entry for the db table
     */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // Column with the foreign key into the location table.
        public static final String COLUMN_REVIEW_KEY = "review_id";
        //id - movie id
        public static final String COLUMN_MOVIE_ID = "movie_id";
        //originalLanguage
        public static final String COLUMN_ORIG_LANG = "orig_lang";
        //originalTitle
        public static final String COLUMN_ORIG_TITLE = "orig_title";
        //overview
        public static final String COLUMN_OVERVIEW = "overview";
        // releaseDate - stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";
        //posterPath
        public static final String COLUMN_POSTER_PATH = "poster_path";
        // popularity
        public static final String COLUMN_POPULARITY = "popularity";
        // title
        public static final String COLUMN_TITLE = "title";
        // video
        public static final String COLUMN_VIDEO = "video";
        // voteAverage
        public static final String COLUMN_VOTE_AVE = "vote_ave";
        // voteCount
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        // adult
        public static final String COLUMN_ADULT = "adult";
        //  backDropPath
        public static final String COLUMN_BACKDROP_PATH = "back_drop_path";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
