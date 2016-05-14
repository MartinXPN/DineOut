package com.dineoutmobile.dineout.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantBasicInfoGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantServicesGrid;
import com.dineoutmobile.dineout.util.LockableNestedScrollView;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;
import com.dineoutmobile.dineout.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityViewRestaurant extends AppCompatActivity implements RestaurantFullInfo.DataLoading {


    private RestaurantFullInfo restaurantInfo = new RestaurantFullInfo(this);
    private AdapterRestaurantServicesGrid adapterRestaurantServicesGrid = new AdapterRestaurantServicesGrid(this, restaurantInfo);
    private AdapterRestaurantBasicInfoGrid adapterRestaurantBasicInfoGrid = new AdapterRestaurantBasicInfoGrid(this, restaurantInfo);
    private AdapterRestaurantImagePager adapterRestaurantImagePager = new AdapterRestaurantImagePager(this, restaurantInfo);
    private long id;
    private boolean isLoaded = false;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        /// initialize restaurant information
        Bundle extras = getIntent().getExtras();
        id = extras.getLong(Util.Tags.BUNDLE_RESTAURANT_ID);
        Log.d("ActivityVR", "created, id = " + id);
        if (!isLoaded) {
            Log.d("Data", "is not loaded");
            restaurantInfo.loadData(id);
            isLoaded = true;
        }

        /// initialize actionbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        initialize();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == android.R.id.home )   { finish();     return true; }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataLoaded() {
        adapterRestaurantServicesGrid.notifyDataSetChanged();
        adapterRestaurantBasicInfoGrid.notifyDataSetChanged();
        adapterRestaurantImagePager.notifyDataSetChanged();
        Log.d( "ActivityVR", "data loaded" );
        initialize();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            initialize();
        }
    }


    public void initialize() {


        View.OnTouchListener enableScrollingOnTouch = new View.OnTouchListener() {
            final LockableNestedScrollView nestedScrollView = (LockableNestedScrollView) findViewById( R.id.nestedscrollview );

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nestedScrollView.setScrollingEnabled( true );
                return false;
            }
        };
        View.OnTouchListener disableScrollingOnTouch = new View.OnTouchListener() {

            final LockableNestedScrollView nestedScrollView = (LockableNestedScrollView) findViewById( R.id.nestedscrollview );

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nestedScrollView.setScrollingEnabled( false );
                return false;
            }
        };


        /// initialize restaurant photo pager
        final ViewPager restaurantPhotoPager = (ViewPager) findViewById(R.id.restaurant_photos_pager);
        assert restaurantPhotoPager != null;
        restaurantPhotoPager.setAdapter(adapterRestaurantImagePager);
        restaurantPhotoPager.setOnTouchListener( enableScrollingOnTouch );

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        assert pageIndicator != null;
        pageIndicator.setViewPager(restaurantPhotoPager);
        pageIndicator.setOnTouchListener( enableScrollingOnTouch );




        /// initialize reservation button
        final FloatingActionButton reserve = (FloatingActionButton) findViewById(R.id.reserve);
        assert reserve != null;
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityViewRestaurant.this, "NO!", Toast.LENGTH_SHORT).show();
            }
        });

        /// initialize call button
        final FloatingActionButton call = (FloatingActionButton) findViewById(R.id.call);
        assert call != null;
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + restaurantInfo.phoneNumber));
                startActivity(intent);
            }
        });





        /// make collapsing toolbar show title only when it is collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle( restaurantInfo.name );
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor( Color.WHITE );




        /// initialize restaurant logo
        final CircleImageView restaurantLogo = (CircleImageView) findViewById( R.id.restaurant_logo );
        assert restaurantLogo != null;
        restaurantLogo.setOnTouchListener( enableScrollingOnTouch );
        Picasso.with(this)
                .load( Util.getImageURL( restaurantInfo.logoURL ) )
                .placeholder( ContextCompat.getDrawable( this,R.drawable.placeholder ) )
                .resizeDimen(R.dimen.restaurant_logo_size, R.dimen.restaurant_logo_size)
                .centerCrop()
                .into(restaurantLogo);


        /// initialize restaurant name
        final TextView restaurantName = (TextView) findViewById( R.id.restaurant_name );
        assert restaurantName != null;
        restaurantName.setOnTouchListener( enableScrollingOnTouch );
        restaurantName.setText( restaurantInfo.name );

        /// initialize restaurant rating
        final RatingBar restaurantRating = (RatingBar) findViewById( R.id.restaurant_rating );
        assert restaurantRating != null;
        restaurantRating.setOnTouchListener( enableScrollingOnTouch );
        if( restaurantInfo.rating <= 5 )
            restaurantRating.setRating( restaurantInfo.rating );

        /// initialize restaurant descriptionResId
        final TextView restaurantDescription = (TextView) findViewById( R.id.restaurant_description );
        assert restaurantDescription != null;
        restaurantDescription.setOnTouchListener( enableScrollingOnTouch );
        restaurantDescription.setText( restaurantInfo.description );




        int numberOfItems = (int) (Util.getWindowWidth( this ) / getResources().getDimension( R.dimen.restaurant_services_grid_item_size));
        Log.d( "ActivityVR", "grid numberOfItems = " + numberOfItems );
        Log.d( "ActivityVR", "grid item width = " + getResources().getDimension( R.dimen.restaurant_services_grid_item_size) );

        /// initialize restaurant basic info grid
        GridLayoutManager restaurantDescriptionListLayoutManager = new GridLayoutManager(this, numberOfItems);
        restaurantDescriptionListLayoutManager.setAutoMeasureEnabled( true );
        final RecyclerView restaurantBasicInfoGrid = (RecyclerView) findViewById( R.id.restaurant_basic_info_grid );
        assert restaurantBasicInfoGrid != null;
        restaurantBasicInfoGrid.setHasFixedSize( true );
        restaurantBasicInfoGrid.setNestedScrollingEnabled( false );
        restaurantBasicInfoGrid.setLayoutManager( restaurantDescriptionListLayoutManager );
        restaurantBasicInfoGrid.setAdapter( adapterRestaurantBasicInfoGrid );
        restaurantBasicInfoGrid.setOnTouchListener( enableScrollingOnTouch );

        /// initialize restaurant services grid
        GridLayoutManager restaurantDescriptionGridLayoutManager = new GridLayoutManager(this, numberOfItems);
        restaurantDescriptionGridLayoutManager.setAutoMeasureEnabled( true );
        final RecyclerView restaurantServicesGrid = (RecyclerView) findViewById( R.id.restaurant_details_grid );
        assert restaurantServicesGrid != null;
        restaurantServicesGrid.setHasFixedSize( true );
        restaurantServicesGrid.setNestedScrollingEnabled( false );
        restaurantServicesGrid.setLayoutManager( restaurantDescriptionGridLayoutManager );
        restaurantServicesGrid.setAdapter( adapterRestaurantServicesGrid );
        restaurantServicesGrid.setOnTouchListener( enableScrollingOnTouch );





        /// initialize current address
        Button currentAddress = (Button) findViewById(R.id.restaurant_current_address);
        assert currentAddress != null;
        currentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("asd", "clicked");
            }
        });
        currentAddress.setOnTouchListener( enableScrollingOnTouch );



        /// initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(-34, 151);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                if (ActivityCompat.checkSelfPermission(ActivityViewRestaurant.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityViewRestaurant.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions( ActivityViewRestaurant.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);
            }
        });

        /// don't scroll when working with map and scroll otherwise
        Button fixScrolling = (Button) findViewById( R.id.fix_scrolling);
        assert fixScrolling != null;
        fixScrolling.setOnTouchListener( disableScrollingOnTouch );



        /// initialize the whole scrollView
        final LockableNestedScrollView nestedScrollView = (LockableNestedScrollView) findViewById(R.id.nestedscrollview);
        assert nestedScrollView != null;
        nestedScrollView.setOnScrollChangeListener(new LockableNestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0) {
                    reserve.hide();
                    call.hide();
                } else if (scrollY - oldScrollY < 0) {
                    reserve.show();
                    call.show();
                }
            }
        });
    }
}
