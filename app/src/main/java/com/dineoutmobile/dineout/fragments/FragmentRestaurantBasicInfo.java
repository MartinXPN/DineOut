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
import com.dineoutmobile.dineout.adapters.AdapterRestaurantBasicInfoGrid;
import com.dineoutmobile.dineout.util.Util;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

import java.util.ArrayList;

public class FragmentRestaurantBasicInfo extends Fragment implements AdapterRestaurantBasicInfoGrid.OnDataRequestedListener {

    public interface OnDataRequestedListener {
        RestaurantFullInfo getRestaurantFullInfo();
    }

    AdapterRestaurantBasicInfoGrid adapterRestaurantBasicInfoGrid;
    OnDataRequestedListener listener;
    ArrayList <RestaurantFullInfo.BasicInfo> basicInfos;
    ArrayList <RestaurantFullInfo.BasicInfoWithLinks> basicInfoWithLinks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d( "BasicInfoF", "onCreate" );
        adapterRestaurantBasicInfoGrid = new AdapterRestaurantBasicInfoGrid( this );
        listener = (OnDataRequestedListener) getActivity();
        basicInfos = listener.getRestaurantFullInfo().getAllBasicInfo();
        basicInfoWithLinks = listener.getRestaurantFullInfo().getAllBasicInfoWithLinks();

        setRetainInstance( true );
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( "BasicInfoF", "onCreateView" );
        View view = inflater.inflate(R.layout.fragment_restaurant_basic_info, container, false);
        int numberOfItems = (int) (Util.getWindowWidth(getActivity()) / getResources().getDimension(R.dimen.restaurant_services_grid_item_size));

        final RecyclerView restaurantBasicInfoGrid = (RecyclerView) view.findViewById( R.id.restaurant_basic_info_grid );
        GridLayoutManager restaurantBasicInfoListLayoutManager = new GridLayoutManager(getActivity(), numberOfItems);
        restaurantBasicInfoListLayoutManager.setAutoMeasureEnabled(true);
        assert restaurantBasicInfoGrid != null;
        restaurantBasicInfoGrid.setHasFixedSize(true);
        restaurantBasicInfoGrid.setNestedScrollingEnabled(false);
        restaurantBasicInfoGrid.setLayoutManager(restaurantBasicInfoListLayoutManager);
        restaurantBasicInfoGrid.setAdapter(adapterRestaurantBasicInfoGrid);
        return view;
    }

    public void notifyDataSetChanged() {
        basicInfos = listener.getRestaurantFullInfo().getAllBasicInfo();
        basicInfoWithLinks = listener.getRestaurantFullInfo().getAllBasicInfoWithLinks();
        adapterRestaurantBasicInfoGrid.notifyDataSetChanged();
    }

    @Override
    public ArrayList<RestaurantFullInfo.BasicInfo> getBasicInfo() {
        return basicInfos;
    }

    @Override
    public ArrayList<RestaurantFullInfo.BasicInfoWithLinks> getBasicInfoWithLinks() {
        return basicInfoWithLinks;
    }
}
