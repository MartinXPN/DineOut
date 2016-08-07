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
import com.dineoutmobile.dineout.models.AddressSchema;
import com.dineoutmobile.dineout.models.RestaurantSchema;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentRestaurantMap extends FragmentSuperMap {

    public interface OnDataRequestedListener {
        RestaurantSchema getRestaurantFullInfo();
    }
    OnDataRequestedListener onDataRequestedListener;
    public RestaurantSchema getRestaurantFullInfo() {
        return onDataRequestedListener.getRestaurantFullInfo();
    }

    public interface OnMapInteractionListener {
        void onNewAddressSelected();
    }
    OnMapInteractionListener onMapInteractionListener;

    private String TAG = "FragRestMap";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onDataRequestedListener = (OnDataRequestedListener) getActivity();
        onMapInteractionListener = (OnMapInteractionListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( TAG, "onCreateView" );
        View view = inflater.inflate(R.layout.fragment_restaurant_map, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        return view;
    }


    public void notifyDataSetChanged() {
        if( map != null )
            populateMarkers();
    }

    public boolean isSamePosition(LatLng first, LatLng second ) {
        return first.latitude == second.latitude && first.longitude == second.longitude;
    }


    @Override
    public boolean onMarkerClicked(Marker marker) {

        if( isSamePosition( getRestaurantFullInfo().currentAddress.latLng, marker.getPosition() ) )
            return false;

        for( AddressSchema address : getRestaurantFullInfo().allAddresses ) {
            if( isSamePosition( address.latLng, marker.getPosition() ) ) {
                getRestaurantFullInfo().currentAddress = address;
                onMapInteractionListener.onNewAddressSelected();
                break;
            }
        }
        return false;
    }

    @Override
    public void populateMarkers() {

        AddressSchema currentAddress = getRestaurantFullInfo().currentAddress;
        map.addMarker( new MarkerOptions().position( currentAddress.latLng ).title( currentAddress.name ) );
        for(AddressSchema address : getRestaurantFullInfo().allAddresses ) {

            if( isSamePosition( address.latLng, currentAddress.latLng ) )
                continue;

            map.addMarker( new MarkerOptions()
                    .position( address.latLng )
                    .title( address.name )
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) );
        }
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
