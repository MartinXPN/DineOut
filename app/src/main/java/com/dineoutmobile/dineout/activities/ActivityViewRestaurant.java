package com.dineoutmobile.dineout.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dineoutmobile.dineout.MapsActivity;
import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantDetailsGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantDetailsList;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.viewpagerindicator.CirclePageIndicator;

public class ActivityViewRestaurant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /// make collapsing toolbar show title only when it is collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle("Երևան Պանդոկ");
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor( Color.WHITE );


        /// initialize restaurant photo pager
        final ViewPager restaurantPhotoPager = (ViewPager) findViewById( R.id.restaurant_photos_pager );
        assert restaurantPhotoPager != null;
        restaurantPhotoPager.setAdapter( new AdapterRestaurantImagePager(this) );

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById( R.id.indicator );
        assert pageIndicator != null;
        pageIndicator.setViewPager( restaurantPhotoPager );


        /// initialize restaurant details grid
        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());  /// TODO replace 120 by getResources().getDimension...
        GridLayoutManager restaurantDescriptionGridLayoutManager = new GridLayoutManager(this, (int) (getWindowManager().getDefaultDisplay().getWidth() / ht_px));
        restaurantDescriptionGridLayoutManager.setAutoMeasureEnabled( true );
        final RecyclerView restaurantDetailsGrid = (RecyclerView) findViewById( R.id.restaurant_details_grid );
        assert restaurantDetailsGrid != null;
        restaurantDetailsGrid.setHasFixedSize( true );
        restaurantDetailsGrid.setNestedScrollingEnabled( false );
        restaurantDetailsGrid.setLayoutManager(restaurantDescriptionGridLayoutManager);
        restaurantDetailsGrid.setAdapter( new AdapterRestaurantDetailsGrid(this) );


        /// initialize restaurant details list
        LinearLayoutManager restaurantDescriptionListLayoutManager = new LinearLayoutManager( this );
        restaurantDescriptionListLayoutManager.setAutoMeasureEnabled( true );
        final RecyclerView restaurantDetailsList = (RecyclerView) findViewById( R.id.restaurant_details_list );
        assert restaurantDetailsList != null;
        restaurantDetailsList.setHasFixedSize( true );
        restaurantDetailsList.setNestedScrollingEnabled( false );
        restaurantDetailsList.setLayoutManager(restaurantDescriptionListLayoutManager);
        restaurantDetailsList.setAdapter( new AdapterRestaurantDetailsList(this) );

        /// initialize 360 photo button
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_show_directions);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        /// initialize google maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {


                final GoogleMap mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(ActivityViewRestaurant.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityViewRestaurant.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        Toast.makeText(ActivityViewRestaurant.this, "hello", Toast.LENGTH_SHORT).show();

                        Location myLocation = mMap.getMyLocation();
                        if (myLocation != null) {

                            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                        }
                        return true;
                    }
                });
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == android.R.id.home )   { finish();     return true; }
        return super.onOptionsItemSelected(item);
    }
}
