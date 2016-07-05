package com.dineoutmobile.dineout.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;


public class FragmentRestaurantBackgroundPager extends DataRequestingFragment implements AdapterRestaurantImagePager.OnDataRequestedListener {

    @Override
    public ArrayList<String> getRestaurantBackgroundPhotos() {
        return getRestaurantFullInfo().backgroundPhotoURLs;
    }


    private String TAG = "FragPager";
    AdapterRestaurantImagePager adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d( TAG, "onCreate" );
        adapter = new AdapterRestaurantImagePager( this );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_restaurant_background_pager, container, false);


        /// initialize restaurant photo pager
        final ViewPager restaurantPhotoPager = (ViewPager) rootView.findViewById(R.id.restaurant_photos_pager);
        assert restaurantPhotoPager != null;
        if( restaurantPhotoPager.getAdapter() == null )
            restaurantPhotoPager.setAdapter(adapter);

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        assert pageIndicator != null;
        pageIndicator.setViewPager(restaurantPhotoPager);

        return rootView;
    }

    @Override
    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
