package com.oneupdog.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.oneupdog.popularmovies.data.MovieContract;
import com.oneupdog.popularmovies.data.MovieDbHelper;
import com.oneupdog.popularmovies.model.MovieData;
import com.oneupdog.popularmovies.model.Review;
import com.oneupdog.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbjensen on 12/4/15.
 */
public class DetailsActivityFragment extends Fragment {

    private final static String TAG = "DetailsActivityFragment";
    private MovieData mMovieData;
    private TextView releaseDate;
    private TextView headerTitle;
    private TextView summary;
    private ImageView posterImage;
    private TextView userRating;
    private Button favorite;
    private ListView trailerListView;
    private ListView reviewListView;
    private LinearLayout trailerLayout;
    private LinearLayout reviewLayout;
    private ReviewArrayAdapter reviewAdapter;
    private ScrollView scrollView;

    public DetailsActivityFragment() {
        //setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        scrollView = (ScrollView) view.findViewById(R.id.details_layout);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setVisibility(View.INVISIBLE);

        headerTitle = (TextView) view.findViewById(R.id.header_title);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        posterImage = (ImageView) view.findViewById(R.id.poster_details);
        userRating = (TextView) view.findViewById(R.id.user_rating);
        summary = (TextView) view.findViewById(R.id.summary);
        favorite = (Button) view.findViewById(R.id.favorite);

        trailerLayout = (LinearLayout) view.findViewById(R.id.trailerLayout);
        reviewLayout = (LinearLayout) view.findViewById(R.id.reviewLayout);
        //trailerListView = (ListView) view.findViewById(R.id.trailerList);
        reviewListView = (ListView) view.findViewById(R.id.reviewList);

        reviewAdapter = new ReviewArrayAdapter(getActivity(), R.layout.review_item, null);
        reviewListView.setAdapter(reviewAdapter);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = mMovieData.getId();
                if (mMovieData.isFavorite()) {
                    mMovieData.setFavorite(false);
                    getContext().getContentResolver().delete(MovieContract.FavMovieEntry.FAV_CONTENT_URI, MovieContract.FavMovieEntry.COLUMN_MOVIE_ID + " = " + id, null);
                } else {
                    mMovieData.setFavorite(true);
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_ADULT, mMovieData.isAdult());
                    values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, mMovieData.getBackDropPath());
                    values.put(MovieContract.MovieEntry.COLUMN_DATE, mMovieData.getReleaseDate());
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovieData.getId());
                    values.put(MovieContract.MovieEntry.COLUMN_ORIG_LANG, mMovieData.getOriginalLanguage());
                    values.put(MovieContract.MovieEntry.COLUMN_ORIG_TITLE, mMovieData.getOriginalTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovieData.getOverview());
                    values.put(MovieContract.MovieEntry.COLUMN_POPULARITY, mMovieData.getPopularity());
                    values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovieData.getPosterPath());
                    values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovieData.getTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_VIDEO, mMovieData.isVideo());
                    values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVE, mMovieData.getVoteAverage());
                    values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, mMovieData.getVoteCount());
                    getContext().getContentResolver().insert(MovieContract.FavMovieEntry.FAV_CONTENT_URI, values);
                }
                favorite.setText(mMovieData.isFavorite() ? "Remove Favorite" : "Mark Favorite");
            }
        });

        return view;
    }

    public void fetchTrailerInfo(Context context) {
        if(mMovieData==null)
            return;

        FetchTrailersTask task = new FetchTrailersTask();
        task.execute(""+mMovieData.getId());
    }

    public void fetchReviewInfo(Context context) {
        if(mMovieData==null)
            return;

        FetchReviewsTask task = new FetchReviewsTask();
        task.execute(""+mMovieData.getId());
    }


    private void createTrailers(LinearLayout container, Trailer[] results) {
        for(final Trailer item : results) {

            View movieTrailerItem = LayoutInflater.from(getActivity()).inflate(
                    R.layout.trailer_item, null);

            movieTrailerItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.playYouTubeVideo(getActivity(), item.getKey());
                }
            });

            TextView mMovieTrailerTitle = (TextView) movieTrailerItem.findViewById(R.id.textView);
            mMovieTrailerTitle.setText(item.getName());

            container.addView(movieTrailerItem);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle == null)
            return;

        if(savedInstanceState != null) {
            //get the movie object saved off
        }

        MovieData movieData = (MovieData) bundle.get(Constants.MOVIE_DATA);
        setmMovieData(movieData);
    }


    public void setmMovieData(MovieData movie) {
        mMovieData = movie;
        Log.d(TAG, "gettting moving data: " + mMovieData);

        headerTitle.setText(mMovieData.getOriginalTitle());
        releaseDate.setText(mMovieData.getReleaseDate());
        userRating.setText(mMovieData.getVoteAverage()+ "/10");
        summary.setText(mMovieData.getOverview());
        int fav = MovieDbHelper.isFavorite(getContext(), mMovieData.getId());
        mMovieData.setFavorite((fav == 1));
        String sFavorite = (mMovieData.isFavorite()) ? "Remove Favorite" : "Mark Favorite";
        favorite.setText(sFavorite);

        String link = Utils.buildLink(mMovieData.getPosterPath());
        Picasso.with(getContext()).load(link).into(posterImage);

        fetchTrailerInfo(getActivity());
        fetchReviewInfo(getActivity());
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, Trailer[]> {

        private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

        @Override
        protected Trailer[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {

                final String BASE_URL = "https://api.themoviedb.org/3/movie/"+params[0]+"/videos";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.POPULAR_MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // empty
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.d(LOG_TAG, movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOEXception Error ", e.getCause());
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getTrailersFromJason(movieJsonStr);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "JsonException: " + e.toString());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Trailer[] results) {

            if (results == null) {
                Log.d(LOG_TAG, "Didnt return movie data, results are null...");
                return;
            }

            trailerLayout.removeAllViewsInLayout();
            createTrailers(trailerLayout, results);
        }

        private Trailer[] getTrailersFromJason(String movieJsonStr) throws JSONException {

            Log.d(TAG, "getJsondata");
            JSONObject moviesJasonObject = new JSONObject(movieJsonStr);
            JSONArray arrayMovies = moviesJasonObject.getJSONArray("results");

            Trailer[] resultStrs = new Trailer[arrayMovies.length()];

            for (int i = 0; i < arrayMovies.length(); i++) {
                JSONObject jsonObject = arrayMovies.getJSONObject(i);
                Trailer trailer = new Trailer(jsonObject);
                resultStrs[i] = trailer;
            }

            return resultStrs;
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, Review[]> {

        private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

        @Override
        protected Review[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {

                final String BASE_URL = "https://api.themoviedb.org/3/movie/"+params[0]+"/reviews";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.POPULAR_MOVIE_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // empty
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.d(LOG_TAG, movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOEXception Error ", e.getCause());
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getReviewsFromJason(movieJsonStr);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "JsonException: " + e.toString());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Review[] results) {

            if (results == null) {
                Log.d(LOG_TAG, "Didnt return movie data, results are null...");
                return;
            }

            reviewAdapter.clear();
            for (int i = 0; i < results.length; i++) {
                reviewAdapter.add(results[i]);
            }
            reviewAdapter.notifyDataSetChanged();
            Utils.setListViewHeightBasedOnChildren(reviewListView);

            scrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }, 300);
        }

        private Review[] getReviewsFromJason(String movieJsonStr) throws JSONException {

            Log.d(TAG, "getJsondata");
            JSONObject moviesJasonObject = new JSONObject(movieJsonStr);
            JSONArray arrayMovies = moviesJasonObject.getJSONArray("results");

            Review[] resultStrs = new Review[arrayMovies.length()];

            for (int i = 0; i < arrayMovies.length(); i++) {
                JSONObject jsonObject = arrayMovies.getJSONObject(i);
                Review review = new Review(jsonObject);
                resultStrs[i] = review;
            }

            return resultStrs;
        }
    }
}
