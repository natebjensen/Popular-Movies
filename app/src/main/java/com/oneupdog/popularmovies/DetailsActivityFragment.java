package com.oneupdog.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneupdog.popularmovies.model.MovieData;
import com.squareup.picasso.Picasso;

/**
 * Created by nbjensen on 12/4/15.
 */
public class DetailsActivityFragment extends Fragment {

    private final static String TAG = "DetailsActivityFragment";
    private GridViewAdapter adapter;
    private MovieData mMovieData;
    private TextView releaseDate;
    private TextView headerTitle;
    private TextView summary;
    private ImageView posterImage;
    private TextView userRating;

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

        headerTitle = (TextView) view.findViewById(R.id.header_title);
        releaseDate = (TextView) view.findViewById(R.id.release_date);
        posterImage = (ImageView) view.findViewById(R.id.poster_details);
        userRating = (TextView) view.findViewById(R.id.user_rating);
        summary = (TextView) view.findViewById(R.id.summary);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        if(savedInstanceState == null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            if(bundle == null)
                return;

            MovieData movieData = (MovieData) bundle.get(Constants.MOVIE_DATA);
            setmMovieData(movieData);
        }
    }


    public void setmMovieData(MovieData movie) {
        mMovieData = movie;
        Log.d(TAG, "gettting moving data: " + mMovieData);

        headerTitle.setText(mMovieData.getOriginalTitle());
        releaseDate.setText(mMovieData.getReleaseDate());
        userRating.setText(mMovieData.getVoteAverage()+ "/10");
        summary.setText(mMovieData.getOverview());

        String link = Utils.buildLink(mMovieData.getPosterPath());
        Picasso.with(getContext()).load(link).into(posterImage);
    }
}
