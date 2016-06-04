package com.dineoutmobile.dineout.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.google.android.gms.maps.MapView;

public class FragmentRestaurantMap extends FragmentMapSuper {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync( this );

        return view;
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
