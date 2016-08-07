package com.dineoutmobile.dineout.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.dineoutmobile.dineout.models.RestaurantSchema;


public abstract class DataRequestingFragment extends Fragment {

    public interface OnDataRequestedListener {
        RestaurantSchema getRestaurantFullInfo();
    }
    protected OnDataRequestedListener onDataRequestedListener;
    public RestaurantSchema getRestaurantFullInfo() {
        return onDataRequestedListener.getRestaurantFullInfo();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance( true );
        onDataRequestedListener = (OnDataRequestedListener) getActivity();
    }

    public abstract void notifyDataSetChanged();
}
