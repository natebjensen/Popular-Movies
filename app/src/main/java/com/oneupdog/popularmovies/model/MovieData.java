package com.oneupdog.popularmovies.model;

/**
 * Created by nbjensen on 11/30/15.
 * //**
 //    "adult": false,
 //            "backdrop_path": "/kvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg",
 //            "genre_ids": [
 //            28,
 //            12,
 //            878
 //            ],
 //            "id": 102899,
 //            "original_language": "en",
 //            "original_title": "Ant-Man",
 //            "overview": "Armed with the astonishing ability to shrink in scale but increase in strength, con-man Scott Lang must embrace his inner-hero and help his mentor, Dr. Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a heist that will save the world.",
 //            "release_date": "2015-08-14",
 //            "poster_path": "/D6e8RJf2qUstnfkTslTXNTUAlT.jpg",
 //            "popularity": 85.211344,
 //            "title": "Ant-Man",
 //            "video": false,
 //            "vote_average": 6.9,
 //            "vote_count": 1745
 // **/

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.oneupdog.popularmovies.Utils;
import com.oneupdog.popularmovies.data.MovieContract;

import org.json.JSONException;
import org.json.JSONObject;

import static com.oneupdog.popularmovies.data.MovieDbHelper.*;

public class MovieData implements Parcelable {

    private long _id; // row ID in database
    private boolean adult;
    private String backDropPath;
    //private int[] genreIds = new int[0];
    private long id;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private double popularity;
    private String title;
    private boolean video;
    private double voteAverage;
    private long voteCount;
    private boolean favorite;

    public MovieData(JSONObject json) throws JSONException {
        adult = json.getBoolean("adult");
        backDropPath = json.getString("backdrop_path");
        //genreIds
        id = json.getLong("id");
        originalLanguage = json.getString("original_language");
        originalTitle = json.getString("original_title");
        overview = json.getString("overview");
        releaseDate = json.getString("release_date");
        posterPath = json.getString("poster_path");
        popularity = json.getDouble("popularity");
        title = json.getString("title");
        video = json.getBoolean("video");
        voteAverage = json.getDouble("vote_average");
        voteCount = json.getLong("vote_count");

    }

    public MovieData(Cursor cursor) {
        _id = cursor.getInt(COL_ID);
        adult = (cursor.getInt(COL_MOVIE_ADULT) == 1);
        backDropPath = cursor.getString(COL_BACKDROP_PATH);
        id = cursor.getInt(COL_MOVIE_ID);
        originalLanguage = cursor.getString(COL_MOVIE_ORIG_LANG);
        originalTitle = cursor.getString(COL_MOVIE_ORIG_TITLE);
        overview = cursor.getString(COL_MOVIE_OVERVIEW);
        long date = cursor.getLong(COL_MOVIE_DATE);
        releaseDate = Utils.convertToDate(date);
        posterPath = cursor.getString(COL_MOVIE_POSTER_PATH);
        popularity = cursor.getDouble(COL_MOVIE_POPULARITY);
        title = cursor.getString(COL_MOVIE_TITLE);
        video = (cursor.getInt(COL_MOVIE_VIDEO) == 1);
        voteAverage = cursor.getDouble(COL_MOVIE_VOTE_AVE);
        voteCount = cursor.getLong(COL_MOVIE_VOTE_COUNT);
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

//    public int[] getGenreIds() {
//        return genreIds;
//    }

    public long getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public boolean isFavorite() { return favorite; }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    //Make the objecct Parceble

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Wtite object to parcel
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(adult ? 1 : 0);
        dest.writeString(backDropPath);
        //dest.writeInt(genreIds.length);
        //dest.writeIntArray(genreIds);
        dest.writeLong(id);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeDouble(popularity);
        dest.writeString(title);
        dest.writeInt(video ? 1 : 0);
        dest.writeDouble(voteAverage);
        dest.writeLong(voteCount);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    /**
     * Constructor for parcel
     *
     * @param in
     */

    public MovieData(Parcel in) {

        adult = (in.readInt() == 1) ? true : false;
        backDropPath = in.readString();
        //genreIds = new int[in.readInt()];
        //in.readIntArray(genreIds);
        id = in.readLong();
        originalLanguage = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        popularity = in.readDouble();
        title = in.readString();
        video = (in.readInt() == 1);
        voteAverage = in.readDouble();
        voteCount = in.readLong();

    }
}
