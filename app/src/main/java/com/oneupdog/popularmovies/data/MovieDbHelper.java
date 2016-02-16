package com.oneupdog.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oneupdog.popularmovies.data.MovieContract.MovieEntry;
import com.oneupdog.popularmovies.data.MovieContract.FavMovieEntry;


/**
 * Created by nbjensen on 12/8/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    public static int COL_ID = 0;
    public static int COL_MOVIE_ID = 1;
    public static int COL_MOVIE_DATE = 2;
    public static int COL_MOVIE_ORIG_LANG = 3;
    public static int COL_MOVIE_OVERVIEW = 4;
    public static int COL_MOVIE_ORIG_TITLE = 5;
    public static int COL_MOVIE_POPULARITY = 6;
    public static int COL_MOVIE_POSTER_PATH = 7;
    public static int COL_MOVIE_TITLE = 8;
    public static int COL_MOVIE_VIDEO = 9;
    public static int COL_MOVIE_VOTE_AVE = 10;
    public static int COL_MOVIE_VOTE_COUNT = 11;
    public static int COL_MOVIE_ADULT = 12;
    public static int COL_BACKDROP_PATH = 13;

    static final String DATABASE_NAME = "popmovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_ORIG_LANG + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIG_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VIDEO + " INTEGER NOT NULL ," +
                MovieEntry.COLUMN_VOTE_AVE + " REAL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " LONG, " +
                MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL ," +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT  );";

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavMovieEntry.TABLE_NAME + " (" +
                FavMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavMovieEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                FavMovieEntry.COLUMN_ORIG_LANG + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_ORIG_TITLE + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                FavMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_VIDEO + " INTEGER NOT NULL ," +
                FavMovieEntry.COLUMN_VOTE_AVE + " REAL, " +
                FavMovieEntry.COLUMN_VOTE_COUNT + " LONG, " +
                FavMovieEntry.COLUMN_ADULT + " INTEGER NOT NULL ," +
                FavMovieEntry.COLUMN_BACKDROP_PATH + " TEXT  );";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        onCreate(db);
    }

    public static int isFavorite(Context context, long id) {
        String selection = FavMovieEntry.COLUMN_MOVIE_ID + " = " + id;
        Cursor cursor = context.getContentResolver().query(FavMovieEntry.FAV_CONTENT_URI, null, selection, null, null);
        if(cursor != null && cursor.getCount() > 0)
            return 1;

        return 0;
    }
}
