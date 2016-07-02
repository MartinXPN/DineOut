package com.dineoutmobile.dineout.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantServicesGrid;
import com.dineoutmobile.dineout.util.Util;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

import java.util.ArrayList;


public class FragmentRestaurantServices extends Fragment implements AdapterRestaurantServicesGrid.OnDataRequestedListener {


    public interface OnDataRequestedListener {
        RestaurantFullInfo getRestaurantFullInfo();
    }

    private String TAG = "FragServices";
    OnDataRequestedListener listener;
    ArrayList <RestaurantFullInfo.Services> services;
    AdapterRestaurantServicesGrid adapterRestaurantServicesGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d( TAG, "onCreate" );
        adapterRestaurantServicesGrid = new AdapterRestaurantServicesGrid( this );
        listener = (OnDataRequestedListener) getActivity();
        services = listener.getRestaurantFullInfo().getAllServices();

        setRetainInstance( true );
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( TAG, "onCreateView" );
        View view = inflater.inflate(R.layout.fragment_restaurant_services, container, false);
        int numberOfItems = (int) (Util.getWindowWidth(getActivity()) / getResources().getDimension(R.dimen.restaurant_services_grid_item_size));

        final RecyclerView restaurantServicesGrid = (RecyclerView) view.findViewById( R.id.restaurant_services_grid );
        GridLayoutManager restaurantServicesLayoutManager = new GridLayoutManager(getActivity(), numberOfItems);
        restaurantServicesLayoutManager.setAutoMeasureEnabled(true);
        assert restaurantServicesGrid != null;
        restaurantServicesGrid.setHasFixedSize(true);
        restaurantServicesGrid.setNestedScrollingEnabled(false);
        restaurantServicesGrid.setLayoutManager(restaurantServicesLayoutManager);
        restaurantServicesGrid.setAdapter(adapterRestaurantServicesGrid);
        return view;
    }

    public void notifyDataSetChanged() {
        services = listener.getRestaurantFullInfo().getAllServices();
        adapterRestaurantServicesGrid.notifyDataSetChanged();
    }

    @Override
    public ArrayList<RestaurantFullInfo.Services> getServices() {
        return services;
    }
}
