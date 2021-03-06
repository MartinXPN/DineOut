package com.dineoutmobile.dineout.adapters;


import android.app.Fragment;
import android.widget.BaseAdapter;

import com.dineoutmobile.dineout.models.RestaurantPreviewSchema;

import java.util.ArrayList;

public abstract class AdapterSuperRestaurantList extends BaseAdapter {


    public interface OnDataRequestedListener {
        ArrayList <RestaurantPreviewSchema> getRestaurants();
    }

    protected OnDataRequestedListener listener;
    protected Fragment parentFragment;



    public AdapterSuperRestaurantList( Fragment parentFragment ) {

        this.parentFragment = parentFragment;
        listener = (OnDataRequestedListener) parentFragment;
    }



    @Override
    public int getCount() {
        return listener.getRestaurants().size();
    }

    @Override
    public Object getItem(int position) {
        return listener.getRestaurants().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
