package com.oneupdog.popularmovies;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
public class MainActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final static String TAG = "MainActivityFragment";
    private GridViewAdapter adapter;
    private boolean mTwoPane;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);

        //find spinner
        Spinner spinner = (Spinner) menu.findItem(R.id.action_sort).getActionView();

        //  create the adapter from a StringArray
        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pref_sort_options,
                R.layout.spinner_item_layout);
        spinner.setAdapter(mSpinnerAdapter);

        int pos = Utils.getSortSelection(getContext());
        spinner.setSelection(pos);

        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        int screenWidth = view.getWidth();
        int screenHeight = view.getHeight();
        Log.i("MyActivity", "screenWidth: " + screenWidth + ", screenHeight: " +screenHeight);
        Log.d(TAG, "Two mane is enabled: "+mTwoPane);

        GridView gridView = (GridView) view.findViewById(R.id.gridview_popular);

        ArrayList list = new ArrayList();

        adapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, list);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                showDetails(position);
            }
        });

        fetchMovieInfo(getActivity());

        return view;
    }

    public void fetchMovieInfo(Context context) {

        FetchPopularMoviesTask task = new FetchPopularMoviesTask();
        task.execute(Utils.getCurrentSortValue(context));
    }

    public void onSortKeyChange() {

        fetchMovieInfo(getActivity());

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent instanceof Spinner) {
            Utils.setSortValue(getContext(), position);
            onSortKeyChange();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void setDualPane(boolean twoPane) {
        mTwoPane = twoPane;
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
                return getPopMoviesFromJason(movieJsonStr);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "JsonException: " + e.toString());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(MovieData[] results) {

            if (results == null) {
                Log.d(LOG_TAG, "Didnt return movie data, results are null...");
                return;
            }

            adapter.clear();
            for (int i = 0; i < results.length; i++)
                adapter.add(results[i]);
            adapter.notifyDataSetChanged();
        }

        private MovieData[] getPopMoviesFromJason(String movieJsonStr) throws JSONException {

            Log.d(TAG, "getJsondata");
            JSONObject moviesJasonObject = new JSONObject(movieJsonStr);
            JSONArray arrayMovies = moviesJasonObject.getJSONArray("results");

            int cnt = arrayMovies.length();
            MovieData[] resultStrs = new MovieData[cnt];

            for (int i = 0; i < cnt; i++) {
                JSONObject jsonObject = arrayMovies.getJSONObject(i);
                MovieData movieData = new MovieData(jsonObject);
                resultStrs[i] = movieData;
            }

            return resultStrs;
        }
    }

    private void showDetails(int position) {

        MovieData movieData = (MovieData)adapter.getItem(position);

        if (mTwoPane) {

            Log.d(TAG, "Dual pane is enabled");
            DetailsActivityFragment fragment = (DetailsActivityFragment)getFragmentManager().findFragmentById(R.id.fragment_details);
            if(fragment != null) {
                fragment.setmMovieData(movieData);
            }
        } else {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra(Constants.MOVIE_DATA, movieData);
            startActivity(intent);
        }

        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_details);
        if(fragment == null) {

        }
    }
}
