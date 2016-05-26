package com.dineoutmobile.dineout.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.Util;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

public class ActivityPanoramaView extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback  {

    private LatLng currentPosition = new LatLng( 40.1743643,44.5050949 );


    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama_view);
        Bundle extras = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        assert toolbar != null;
        toolbar.setTitle( extras.getString( Util.Tags.BUNDLE_RESTAURANT_NAME ) );
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //double latitude = extras.getDouble( Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LAT );
        //double longitude = extras.getDouble( Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LNG );
        //currentPosition = new LatLng( latitude, longitude );


        SupportStreetViewPanoramaFragment streetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync( this );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == android.R.id.home )   { finish();     return true; }
        return super.onOptionsItemSelected(item);
    }

    private static final LatLng SAN_FRAN = new LatLng(37.765927, -122.449972);
    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(currentPosition);
        streetViewPanorama.setPanningGesturesEnabled( true );
        streetViewPanorama.setUserNavigationEnabled( true );
        streetViewPanorama.setZoomGesturesEnabled( false );
        streetViewPanorama.setStreetNamesEnabled( true );

        StreetViewPanoramaLocation location = streetViewPanorama.getLocation();
        if (location != null && location.links != null) {
            streetViewPanorama.setPosition(location.links[0].panoId);
        }
    }
}
