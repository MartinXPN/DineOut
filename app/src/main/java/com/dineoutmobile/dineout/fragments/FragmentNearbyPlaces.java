package com.dineoutmobile.dineout.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.models.RestaurantOnMapSchema;
import com.dineoutmobile.dineout.restapi.DataLoader;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class FragmentNearbyPlaces extends FragmentSuperMap {

    private String TAG = "FragNearby";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( TAG, "onCreateView" );
        View view = inflater.inflate(R.layout.fragment_nearby_places, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync( this );
        DataLoader.loadNearbyRestaurants(LanguageUtil.getLanguage(getActivity()).locale, 0f, 0f, null );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onRestaurantsLoaded( ArrayList <RestaurantOnMapSchema> restaurants ) {
        Toast.makeText( getActivity(), restaurants.toString(), Toast.LENGTH_SHORT ).show();
    }


    @Override
    public boolean onMarkerClicked(Marker marker) {
        return false;
    }

    @Override
    public void populateMarkers() {

    }

    @Override
    public void onMyLocationPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)return;
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    public void onMyLocationPermissionDenied() {
        Toast.makeText( getActivity(), "Location is needed to find nearest restaurants", Toast.LENGTH_LONG ).show();
        getActivity().finish();
    }
}
