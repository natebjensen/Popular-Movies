package com.oneupdog.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.oneupdog.popularmovies.BuildConfig;
import com.oneupdog.popularmovies.model.MovieData;

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


/**
 * MainActivity fragment containing the gridview.
 */
public class MainActivityFragment extends Fragment {

    private final static String TAG = "MainActivityFragment";
    private GridViewAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragmeny, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridview_popular);

        ArrayList list = new ArrayList();

        adapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, list);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //"popularity.desc"
        FetchPopularMoviesTask task = new FetchPopularMoviesTask();
        task.execute("popularity.desc");

        return view;
    }

    public class FetchPopularMoviesTask extends AsyncTask<String, Void, MovieData[]> {

        private final String LOG_TAG = FetchPopularMoviesTask.class.getSimpleName();

        @Override
        protected MovieData[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {

                final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
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
                Log.e(LOG_TAG, "Error ", e);
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
                return getPopMoviesFromJason(movieJsonStr);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "JsonException: "+e.toString());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(MovieData[] results) {

            if(results == null) {
                return;
            }

            for(int i =0; i < results.length; i++)
                adapter.add(results[i]);
            adapter.notifyDataSetChanged();
        }
    }


   private MovieData[] getPopMoviesFromJason(String movieJsonStr) throws JSONException {

        Log.d(TAG, "getJsondata");
        JSONObject moviesJasonObject = new JSONObject(movieJsonStr);
        JSONArray arrayMovies = moviesJasonObject.getJSONArray("results");

        int cnt = arrayMovies.length();
       MovieData[] resultStrs = new MovieData[cnt];

        for(int i=0; i < cnt; i++){
            JSONObject jsonObject = arrayMovies.getJSONObject(i);
            MovieData movieData = new MovieData(jsonObject);
            resultStrs[i] = movieData;
        }

        return resultStrs;
    }
}
