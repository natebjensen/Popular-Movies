package com.oneupdog.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.oneupdog.popularmovies.data.MovieDbHelper;
import com.oneupdog.popularmovies.model.MovieData;
import com.squareup.picasso.Picasso;

/**
 * Created by nbjensen on 2/10/16.
 */
public class GridCursorAdapter extends CursorAdapter {


    public static class ViewHolder {
        public final ImageView imageView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.poster);
            imageView.setAdjustViewBounds(true);
        }
    }

    public GridCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String path = cursor.getString(MovieDbHelper.COL_MOVIE_POSTER_PATH);
        String link = buildLink(path);
        Picasso.with(context).load(link).into(holder.imageView);
    }

    private String buildLink(String posterPath) {
        //"w92", "w154", "w185", "w342", "w500", "w780", or "original"
        return "http://image.tmdb.org/t/p/w185" + posterPath;
    }

    @Override
    public MovieData getItem(int position) {
        Cursor cursor = (Cursor) super.getItem(position);
        return new MovieData(cursor);
    }
}
