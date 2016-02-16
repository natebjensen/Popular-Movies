package com.oneupdog.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oneupdog.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbjensen on 2/12/16.
 */
public class ReviewArrayAdapter extends ArrayAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private int resourceId;

    public ReviewArrayAdapter(Context context, int resource, List<Review> data) {
        super(context, resource, 0);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        row = mInflater.inflate(resourceId, parent, false);
        TextView nameView = (TextView) row.findViewById(R.id.reviewNameId);
        TextView contentView = (TextView) row.findViewById(R.id.reviewContentId);

        Review review = (Review) getItem(position);
        nameView.setText(review.getAuthor());
        contentView.setText(review.getContent());

        return row;
    }
}
