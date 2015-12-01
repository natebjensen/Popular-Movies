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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MovieData {


    private boolean adult;
    private String backDropPath;
    private int[] genreIds;
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

    public boolean isAdult() {
        return adult;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

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


}
