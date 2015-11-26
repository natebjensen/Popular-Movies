package com.oneupdog.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nbjensen on 11/23/15.
 */
public class GridViewAdapter extends ArrayAdapter {

    private final Context context;
    private final int resourceId;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int resource, ArrayList data) {
        super(context, resource, data);
        this.resourceId = resource;
        this.context = context;
        this.data = data;
    }

    static class ViewHolder {
        TextView titleView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.titleView = (TextView) row.findViewById(R.id.text);
            row.setTag(holder);
        }  else {
            holder = (ViewHolder) row.getTag();
        }

        String item = (String) data.get(position);
        holder.titleView.setText(item);

        return row;
    }
}
