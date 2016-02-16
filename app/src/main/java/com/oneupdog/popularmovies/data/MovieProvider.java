package com.oneupdog.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nbjensen on 12/8/15.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int Movie = 100;
    static final int FAVORITE = 101;
    static final int MovieAndFav = 102;


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI , create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIE, Movie);
        matcher.addURI(authority, MovieContract.PATH_FAV, FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_FAV, MovieAndFav);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movie"
            case Movie: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MovieAndFav: {
                //String sql = "Select * from "+ MovieContract.MovieEntry.TABLE_NAME+ " left join "+
                //        MovieContract.FavMovieEntry.TABLE_NAME +
                //        " on " + MovieContract.MovieEntry.TABLE_NAME +"."+ MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                //        " = " + MovieContract.FavMovieEntry.TABLE_NAME +"."+ MovieContract.FavMovieEntry.COLUMN_FAV_ID;
                String sql = "Select * from movies A left join favorites B ON A.movie_id = B.favorite_movie_id;";
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(sql, null);

                break;
            }
            case FAVORITE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case Movie:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case FAVORITE:
                return MovieContract.FavMovieEntry.CONTENT_TYPE;
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case Movie: {
                convertDate(values);
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE: {
                convertDate(values);
                long _id = db.insert(MovieContract.FavMovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case Movie:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsDeleted = db.delete(
                        MovieContract.FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void convertDate(ContentValues values) {
        if (values.containsKey(MovieContract.MovieEntry.COLUMN_DATE)) {
            String sdate = (String)values.get(MovieContract.MovieEntry.COLUMN_DATE);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = df.parse(sdate);
                values.put(MovieContract.MovieEntry.COLUMN_DATE, date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsUpdated;

            switch (match) {
                case Movie:
                    convertDate(values);
                    rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                case FAVORITE:
                    rowsUpdated = db.update(MovieContract.FavMovieEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Movie:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        convertDate(value);
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

}
