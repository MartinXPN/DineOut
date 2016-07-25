package com.dineoutmobile.dineout.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;


public abstract class FragmentSuperMap extends Fragment implements OnMapReadyCallback {

    private static final int PERMISSION_CODE_LOCATION = 77;
    MapView mapView;
    GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FragMap", "onCreate");
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (map != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;
            map.setMyLocationEnabled(false);
            map.clear();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



    public abstract boolean onMarkerClicked( Marker marker );
    public abstract void populateMarkers();
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        map = googleMap;
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        populateMarkers();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE_LOCATION);
        }
        else {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return onMarkerClicked( marker );
            }
        });
    }


    public abstract void onMyLocationPermissionGranted();
    public abstract void onMyLocationPermissionDenied();
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)    onMyLocationPermissionGranted();
                else                                                                                    onMyLocationPermissionDenied();
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}