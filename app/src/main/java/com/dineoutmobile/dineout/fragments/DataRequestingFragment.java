package com.dineoutmobile.dineout.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;


public abstract class DataRequestingFragment extends Fragment {

    public interface OnDataRequestedListener {
        RestaurantFullInfo getRestaurantFullInfo();
    }
    protected OnDataRequestedListener onDataRequestedListener;
    public RestaurantFullInfo getRestaurantFullInfo() {
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
