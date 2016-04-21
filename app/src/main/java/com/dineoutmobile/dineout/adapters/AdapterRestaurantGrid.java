package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.databasehelpers.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;


public class AdapterRestaurantGrid extends BaseAdapter {

    ViewHolder holder;
    Context context;
    private static DatabaseHelper database;
    private static ArrayList<String> restaurantNames = new ArrayList<>();
    private static ArrayList<Integer> restaurantIDs = new ArrayList<>();

    public AdapterRestaurantGrid( Context context ) {

        this.context = context;

        /// DatabaseHelper is single-tone
        database = DatabaseHelper.getInstance( context );
        try                     { database.createDataBase(); }
        catch (IOException e)   { e.printStackTrace(); }

        restaurantNames = database.getRestaurantNames( restaurantIDs );
    }

    @Override
    public void notifyDataSetChanged() {
        restaurantNames = database.getRestaurantNames(restaurantIDs);
        super.notifyDataSetChanged();
    }


    public int CalcRestaurantID(int position){

        return restaurantIDs.get(position);
    }


    @Override
    public int getCount() {
        return restaurantNames.size();
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

        ( (ViewHolder) convertView.getTag() ).name.setText( restaurantNames.get( position ) );

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
