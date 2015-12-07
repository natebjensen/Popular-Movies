package com.oneupdog.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.oneupdog.popularmovies.model.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nbjensen on 11/23/15.
 */
public class GridViewAdapter extends ArrayAdapter {

    private static final String  TAG = GridViewAdapter.class.getSimpleName();
    private Context context;
    private int resourceId;
    private int width;
    private boolean mTwoPane;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int resource, ArrayList data) {
        super(context, resource, data);
        this.resourceId = resource;
        this.context = context;
        this.data = data;
        this.width= context.getResources().getDisplayMetrics().widthPixels;

        Log.d(TAG, "Curreent width is: "+ width);
    }

    static class ViewHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.poster);
            holder.imageView.setAdjustViewBounds(true);
            row.setTag(holder);
        }  else {
            holder = (ViewHolder) row.getTag();
        }

        MovieData movieData = (MovieData) data.get(position);
        String link = buildLink(movieData.getPosterPath());
        Log.d(TAG, "link: " + link);
        Picasso.with(context).load(link).into(holder.imageView);

        return row;
    }

    private String buildLink(String posterPath) {
        //"w92", "w154", "w185", "w342", "w500", "w780", or "original"
        return "http://image.tmdb.org/t/p/w92" + posterPath;
    }
}
