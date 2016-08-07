package com.dineoutmobile.dineout.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.models.ReservationSchema;

import java.util.ArrayList;

public class AdapterReservedRestaurantsList extends BaseAdapter {


    Context context;
    ArrayList <ReservationSchema> reservedRestaurants = new ArrayList<>();

    public AdapterReservedRestaurantsList(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.reserved_restaurant_list_item, parent, false);
        return convertView;
    }
}
