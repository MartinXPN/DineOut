package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;


public class AdapterRestaurantGrid extends BaseAdapter {

    ViewHolder holder;
    Context context;

    public AdapterRestaurantGrid( Context context ) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate( R.layout.restaurant_grid_item, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


    private static class ViewHolder {
        ImageView logo;
        TextView name;
        TextView rating;

        ViewHolder( View v ) {
            logo = (ImageView) v.findViewById( R.id.restaurant_logo );
            name = (TextView ) v.findViewById( R.id.restaurant_name );
            rating = (TextView) v.findViewById( R.id.restaurant_rating_number );
        }
    }
}
