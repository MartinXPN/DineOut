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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantAddressesList;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantBasicInfoGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantServicesGrid;
import com.dineoutmobile.dineout.util.LockableNestedScrollView;
import com.dineoutmobile.dineout.util.NumberPicker;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;
import com.dineoutmobile.dineout.util.Util;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityViewRestaurant extends     AppCompatActivity
                                    implements  RestaurantFullInfo.DataLoading,
                                                AdapterRestaurantAddressesList.OnAddressSelectedListener {


    private RestaurantFullInfo restaurantInfo = new RestaurantFullInfo(this);
    private AdapterRestaurantServicesGrid adapterRestaurantServicesGrid = new AdapterRestaurantServicesGrid(this, restaurantInfo);
    private AdapterRestaurantBasicInfoGrid adapterRestaurantBasicInfoGrid = new AdapterRestaurantBasicInfoGrid(this, restaurantInfo);
    private AdapterRestaurantImagePager adapterRestaurantImagePager = new AdapterRestaurantImagePager(this, restaurantInfo);
    private AdapterRestaurantAddressesList adapterRestaurantAddressesList = new AdapterRestaurantAddressesList( this, restaurantInfo );
    private RecyclerView restaurantAddressList;
    private Button restaurantCurrentAddress;
    private boolean isLoaded = false;
    private boolean isExpanded = false;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        /// initialize restaurant information
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong(Util.Tags.BUNDLE_RESTAURANT_ID);
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
    public void onBackPressed() {
        final FABToolbarLayout reserveLayout = (FABToolbarLayout) findViewById( R.id.fabtoolbar );
        assert reserveLayout != null;

        if( reserveLayout.isToolbar() ) {
            reserveLayout.hide();

            final LinearLayout cancelReservation = (LinearLayout) findViewById( R.id.cancel_reservation );
            assert cancelReservation != null;
            cancelReservation.setVisibility( View.GONE );

            final FloatingActionButton reserveButton = (FloatingActionButton) findViewById(R.id.reserve);
            assert reserveButton != null;
            reserveButton.show();

            final FloatingActionButton call = (FloatingActionButton) findViewById(R.id.call);
            assert call != null;
            call.show();
        }
        else {
            super.onBackPressed();
        }
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
        adapterRestaurantAddressesList.notifyDataSetChanged();
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

    public void collapseAddressList() {
        isExpanded = false;
        restaurantAddressList.setVisibility( View.GONE );
        restaurantCurrentAddress.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_expand, 0);
    }
    public void expandAddressList() {
        isExpanded = true;
        restaurantAddressList.setVisibility( View.VISIBLE );
        restaurantCurrentAddress.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.drawable.ic_collapse, 0);
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



        final LinearLayout cancelReservation = (LinearLayout) findViewById( R.id.cancel_reservation );
        assert cancelReservation != null;


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

        /// initialize reservation button
        final FloatingActionButton reserveButton = (FloatingActionButton) findViewById(R.id.reserve);
        assert reserveButton != null;
        final FABToolbarLayout reserveLayout = (FABToolbarLayout) findViewById( R.id.fabtoolbar );
        assert reserveLayout != null;
        final RelativeLayout contentReserve = (RelativeLayout) findViewById( R.id.content_reserve );
        assert contentReserve != null;
        contentReserve.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( reserveLayout.isFab() ) {
                    cancelReservation.setVisibility( View.VISIBLE );
                    reserveLayout.show();
                    call.hide();
                }
            }
        });

        /// initialize person picker
        final NumberPicker numberOfPeople = (NumberPicker) findViewById( R.id.number_of_people_picker);
        assert numberOfPeople != null;
        numberOfPeople.setMinValue( 1 );
        numberOfPeople.setMaxValue( 11 );
        numberOfPeople.setWrapSelectorWheel( false );
        String[] people = new String[11];
        for( int i=1; i <= 10; i++ ) people[i-1] = String.valueOf( i );
        people[10] = "11+";
        numberOfPeople.setDisplayedValues( people );


        /// initialize date picker
        final NumberPicker datePicker = (NumberPicker) findViewById( R.id.date_picker);
        assert datePicker != null;
        datePicker.setMinValue( 1 );
        datePicker.setMaxValue( 4 );
        datePicker.setWrapSelectorWheel( false );
        String[] date = new String[4];
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        for (int i = 0; i < 4; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            date[i] = sdf.format(calendar.getTime());
        }
        datePicker.setDisplayedValues( date );

        /// initialize time picker
        final NumberPicker timePicker = (NumberPicker) findViewById( R.id.time_picker);
        assert timePicker != null;
        timePicker.setMinValue( 1 );
        timePicker.setMaxValue( 48 );
        timePicker.setWrapSelectorWheel( false );
        String[] time = new String[48];
        for (int i = 0; i < 48; i++)
            time[i] = String.valueOf( i / 2 ) + ( i%2 == 0 ? ":00" : ":30" );
        timePicker.setDisplayedValues( time );




        /// initialize reservation layout
        final LinearLayout reservationLayout = (LinearLayout) findViewById( R.id.reserve_layout );
        assert reservationLayout != null;

        /// initialize reservation button
        final Button reserveRestaurant = (Button) findViewById( R.id.reserve_restaurant_button );
        assert  reserveRestaurant != null;
        reserveRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( ActivityViewRestaurant.this, "Your reservation was successful", Toast.LENGTH_LONG ).show();
                call.hide();
                reserveLayout.hide();
                reserveButton.hide();
                cancelReservation.setVisibility( View.GONE );

                Intent i = new Intent( ActivityViewRestaurant.this, ActivityPanoramaView.class );
                i.putExtra( Util.Tags.BUNDLE_RESTAURANT_NAME, restaurantInfo.name );
                i.putExtra( Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LAT, restaurantInfo.currentAddress.latLng.latitude );
                i.putExtra( Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LNG, restaurantInfo.currentAddress.latLng.longitude );
                startActivity( i );
            }
        });

        /// initialize reservation cancel layout
        cancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /// because of the animation delay
                /// reservation layout may not be fast enough to turn to toolbar before out click
                if( reserveLayout.isToolbar() ) {
                    reserveLayout.hide();
                    reserveButton.show();
                    call.show();
                    cancelReservation.setVisibility(View.GONE);
                }
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

        /// initialize restaurant basic info grid
        GridLayoutManager restaurantBasicInfoListLayoutManager = new GridLayoutManager(this, numberOfItems);
        restaurantBasicInfoListLayoutManager.setAutoMeasureEnabled( true );
        final RecyclerView restaurantBasicInfoGrid = (RecyclerView) findViewById( R.id.restaurant_basic_info_grid );
        assert restaurantBasicInfoGrid != null;
        restaurantBasicInfoGrid.setHasFixedSize( true );
        restaurantBasicInfoGrid.setNestedScrollingEnabled( false );
        restaurantBasicInfoGrid.setLayoutManager(restaurantBasicInfoListLayoutManager);
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





        /// initialize addresses
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this );
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        restaurantAddressList = (RecyclerView) findViewById( R.id.restaurant_addresses_list );
        assert restaurantAddressList != null;
        restaurantAddressList.setHasFixedSize( true );
        restaurantAddressList.setNestedScrollingEnabled( false );
        restaurantAddressList.setLayoutManager( linearLayoutManager );
        restaurantAddressList.setAdapter( adapterRestaurantAddressesList );
        restaurantAddressList.setOnTouchListener( enableScrollingOnTouch );


        restaurantCurrentAddress = (Button) findViewById( R.id.restaurant_current_address );
        assert restaurantCurrentAddress != null;
        restaurantCurrentAddress.setText( restaurantInfo.currentAddress.name == null ? "" : restaurantInfo.currentAddress.name );
        restaurantCurrentAddress.setOnTouchListener( enableScrollingOnTouch );
        restaurantCurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                if( isExpanded )    expandAddressList();
                else                collapseAddressList();
            }
        });
        if( isExpanded )    expandAddressList();
        else                collapseAddressList();







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

        Button navigateInGoogleMaps = (Button) findViewById( R.id.fix_scrolling );
        assert navigateInGoogleMaps != null;
        navigateInGoogleMaps.setOnTouchListener( disableScrollingOnTouch );


        /// initialize the whole scrollView
        final LockableNestedScrollView nestedScrollView = (LockableNestedScrollView) findViewById(R.id.nestedscrollview);
        assert nestedScrollView != null;
        nestedScrollView.setOnScrollChangeListener(new LockableNestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                /// move down
                if (scrollY - oldScrollY > 0) {
                    reserveLayout.hide();
                    reserveButton.hide();
                    call.hide();
                }
                /// move up
                else if (scrollY - oldScrollY < 0) {
                    if( !reserveButton.isShown() )  reserveButton.show();
                    if( reserveLayout.isFab() )     call.show();
                }
            }
        });
    }


    @Override
    public void onAddressSelected(int position) {
        collapseAddressList();
        restaurantCurrentAddress.setText( restaurantInfo.currentAddress.name );
    }
}
