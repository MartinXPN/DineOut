package com.dineoutmobile.dineout.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantAddressesList;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantBasicInfoGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantServicesGrid;
import com.dineoutmobile.dineout.fragments.FragmentReserveQuestions;
import com.dineoutmobile.dineout.util.LockableNestedScrollView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityViewRestaurant extends AppCompatActivity
        implements RestaurantFullInfo.OnDataLoadedListener,
        AdapterRestaurantAddressesList.OnAddressSelectedListener,
        FragmentReserveQuestions.OnRestaurantReservedListener,
        OnMapReadyCallback {


    private RestaurantFullInfo restaurantInfo = new RestaurantFullInfo(this);
    private AdapterRestaurantServicesGrid adapterRestaurantServicesGrid = new AdapterRestaurantServicesGrid(this, restaurantInfo);
    private AdapterRestaurantBasicInfoGrid adapterRestaurantBasicInfoGrid = new AdapterRestaurantBasicInfoGrid(this, restaurantInfo);
    private AdapterRestaurantImagePager adapterRestaurantImagePager = new AdapterRestaurantImagePager(this, restaurantInfo);
    private AdapterRestaurantAddressesList adapterRestaurantAddressesList = new AdapterRestaurantAddressesList(this, restaurantInfo);
    private RecyclerView restaurantAddressList;
    private Button restaurantCurrentAddress;
    private boolean isLoaded = false;
    private boolean isExpanded = false;
    View.OnTouchListener enableScrollingOnTouch;
    View.OnTouchListener disableScrollingOnTouch;
    GoogleMap map;
    private static final int PERMISSION_CODE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        /// initialize restaurant information
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong(Util.Tags.BUNDLE_RESTAURANT_ID);
        if (!isLoaded) {
            restaurantInfo.loadRestaurantWholeInfo(id);
            isLoaded = true;
        }

        disableScrollingOnTouch = new View.OnTouchListener() {
            final LockableNestedScrollView nestedScrollView = (LockableNestedScrollView) findViewById(R.id.nestedscrollview);

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nestedScrollView.setScrollingEnabled(false);
                return false;
            }
        };
        enableScrollingOnTouch = new View.OnTouchListener() {
            final LockableNestedScrollView nestedScrollView = (LockableNestedScrollView) findViewById(R.id.nestedscrollview);

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nestedScrollView.setScrollingEnabled(true);
                return false;
            }
        };

        /// initialize actionbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeStaticViews();
    }


    @Override
    public void onBackPressed() {
        final FABToolbarLayout reserveLayout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        assert reserveLayout != null;

        if (reserveLayout.isToolbar()) {
            reserveLayout.hide();

            final LinearLayout cancelReservation = (LinearLayout) findViewById(R.id.cancel_reservation);
            assert cancelReservation != null;
            cancelReservation.setVisibility(View.GONE);

            final FloatingActionButton reserveButton = (FloatingActionButton) findViewById(R.id.reserve);
            assert reserveButton != null;
            reserveButton.show();

            final FloatingActionButton call = (FloatingActionButton) findViewById(R.id.call);
            assert call != null;
            call.show();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataLoaded() {
        adapterRestaurantServicesGrid.notifyDataSetChanged();
        adapterRestaurantBasicInfoGrid.notifyDataSetChanged();
        adapterRestaurantImagePager.notifyDataSetChanged();
        adapterRestaurantAddressesList.notifyDataSetChanged();
        Log.d("ActivityVR", "data loaded");
        initializeVariableViews();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            initializeStaticViews();
        }
    }

    public void collapseAddressList() {
        isExpanded = false;
        restaurantAddressList.setVisibility(View.GONE);
        restaurantCurrentAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0);
    }

    public void expandAddressList() {
        isExpanded = true;
        restaurantAddressList.setVisibility(View.VISIBLE);
        restaurantCurrentAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0);
    }


    public void initializeStaticViews() {

        /// initialize restaurant photo pager
        final ViewPager restaurantPhotoPager = (ViewPager) findViewById(R.id.restaurant_photos_pager);
        assert restaurantPhotoPager != null;
        restaurantPhotoPager.setAdapter(adapterRestaurantImagePager);
        restaurantPhotoPager.setOnTouchListener(enableScrollingOnTouch);

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        assert pageIndicator != null;
        pageIndicator.setViewPager(restaurantPhotoPager);
        pageIndicator.setOnTouchListener(enableScrollingOnTouch);


        int numberOfItems = (int) (Util.getWindowWidth(this) / getResources().getDimension(R.dimen.restaurant_services_grid_item_size));

        /// initialize restaurant basic info grid
        GridLayoutManager restaurantBasicInfoListLayoutManager = new GridLayoutManager(this, numberOfItems);
        restaurantBasicInfoListLayoutManager.setAutoMeasureEnabled(true);
        final RecyclerView restaurantBasicInfoGrid = (RecyclerView) findViewById(R.id.restaurant_basic_info_grid);
        assert restaurantBasicInfoGrid != null;
        restaurantBasicInfoGrid.setHasFixedSize(true);
        restaurantBasicInfoGrid.setNestedScrollingEnabled(false);
        restaurantBasicInfoGrid.setLayoutManager(restaurantBasicInfoListLayoutManager);
        restaurantBasicInfoGrid.setAdapter(adapterRestaurantBasicInfoGrid);
        restaurantBasicInfoGrid.setOnTouchListener(enableScrollingOnTouch);

        /// initialize restaurant services grid
        GridLayoutManager restaurantDescriptionGridLayoutManager = new GridLayoutManager(this, numberOfItems);
        restaurantDescriptionGridLayoutManager.setAutoMeasureEnabled(true);
        final RecyclerView restaurantServicesGrid = (RecyclerView) findViewById(R.id.restaurant_details_grid);
        assert restaurantServicesGrid != null;
        restaurantServicesGrid.setHasFixedSize(true);
        restaurantServicesGrid.setNestedScrollingEnabled(false);
        restaurantServicesGrid.setLayoutManager(restaurantDescriptionGridLayoutManager);
        restaurantServicesGrid.setAdapter(adapterRestaurantServicesGrid);
        restaurantServicesGrid.setOnTouchListener(enableScrollingOnTouch);


        /// initialize addresses list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        restaurantAddressList = (RecyclerView) findViewById(R.id.restaurant_addresses_list);
        assert restaurantAddressList != null;
        restaurantAddressList.setHasFixedSize(true);
        restaurantAddressList.setNestedScrollingEnabled(false);
        restaurantAddressList.setLayoutManager(linearLayoutManager);
        restaurantAddressList.setAdapter(adapterRestaurantAddressesList);
        restaurantAddressList.setOnTouchListener(enableScrollingOnTouch);


        /// initialize current address
        restaurantCurrentAddress = (Button) findViewById(R.id.restaurant_current_address);
        assert restaurantCurrentAddress != null;
        restaurantCurrentAddress.setText(restaurantInfo.currentAddress == null ? "" : restaurantInfo.currentAddress.name);
        restaurantCurrentAddress.setOnTouchListener(enableScrollingOnTouch);
        restaurantCurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                if (isExpanded) expandAddressList();
                else collapseAddressList();
            }
        });
        if (isExpanded) expandAddressList();
        else collapseAddressList();


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
        final FABToolbarLayout reserveLayout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        assert reserveLayout != null;
        final LinearLayout cancelReservation = (LinearLayout) findViewById(R.id.cancel_reservation);
        assert cancelReservation != null;

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (reserveLayout.isFab()) {
                    cancelReservation.setVisibility(View.VISIBLE);
                    reserveLayout.show();
                    call.hide();
                }
            }
        });


        /// initialize reservation cancel layout
        cancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /// because of the animation delay
                /// reservation layout may not be fast enough to turn to toolbar before our click
                if (reserveLayout.isToolbar()) {

                    reserveLayout.hide();
                    reserveButton.show();
                    call.show();
                    cancelReservation.setVisibility(View.GONE);

                    /// hide keyboard
                    View view = ActivityViewRestaurant.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });


        /// initialize Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /// initialize google maps navigation touch
        Button navigateInGoogleMaps = (Button) findViewById(R.id.fix_scrolling);
        assert navigateInGoogleMaps != null;
        navigateInGoogleMaps.setOnTouchListener(disableScrollingOnTouch);


        /// initialize the whole scrollView
        final LockableNestedScrollView nestedScrollView = (LockableNestedScrollView) findViewById(R.id.nestedscrollview);
        assert nestedScrollView != null;
        nestedScrollView.setOnScrollChangeListener(new LockableNestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                /// move down
                if (scrollY - oldScrollY > 0) {
                    reserveButton.hide();
                    call.hide();
                }
                /// move up
                else if (scrollY - oldScrollY < 0) {
                    reserveButton.show();
                    call.show();
                }
            }
        });
    }

    public void initializeVariableViews() {


        /// make collapsing toolbar show title only when it is collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle(restaurantInfo.currentAddress == null ? "" : restaurantInfo.currentAddress.name);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


        /// initialize restaurant logo
        final CircleImageView restaurantLogo = (CircleImageView) findViewById(R.id.restaurant_logo);
        assert restaurantLogo != null;
        restaurantLogo.setOnTouchListener(enableScrollingOnTouch);
        Picasso.with(this)
                .load(Util.getImageURL(restaurantInfo.logoURL))
                .placeholder(ContextCompat.getDrawable(this, R.drawable.placeholder))
                .resizeDimen(R.dimen.restaurant_logo_size, R.dimen.restaurant_logo_size)
                .centerCrop()
                .into(restaurantLogo);


        /// initialize restaurant name
        final TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);
        assert restaurantName != null;
        restaurantName.setOnTouchListener(enableScrollingOnTouch);
        restaurantName.setText(restaurantInfo.name);

        /// initialize restaurant rating
        final RatingBar restaurantRating = (RatingBar) findViewById(R.id.restaurant_rating);
        assert restaurantRating != null;
        restaurantRating.setOnTouchListener(enableScrollingOnTouch);
        if (restaurantInfo.rating <= 5)
            restaurantRating.setRating(restaurantInfo.rating);

        /// initialize restaurant description
        final TextView restaurantDescription = (TextView) findViewById(R.id.restaurant_description);
        assert restaurantDescription != null;
        restaurantDescription.setOnTouchListener(enableScrollingOnTouch);
        restaurantDescription.setText(restaurantInfo.description);


        /// initialize current address
        restaurantCurrentAddress = (Button) findViewById(R.id.restaurant_current_address);
        assert restaurantCurrentAddress != null;
        restaurantCurrentAddress.setText(restaurantInfo.currentAddress == null ? "" : restaurantInfo.currentAddress.name);
    }


    @Override
    public void onAddressSelected(int position) {
        collapseAddressList();
        restaurantCurrentAddress.setText(restaurantInfo.currentAddress.name);
    }

    @Override
    public void onRestaurantReserved() {

        finish();
        Intent i = new Intent(this, ActivityPanoramaView.class);
        i.putExtra(Util.Tags.BUNDLE_RESTAURANT_NAME, restaurantInfo.name);
        i.putExtra(Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LAT, restaurantInfo.currentAddress.latLng.latitude);
        i.putExtra(Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LNG, restaurantInfo.currentAddress.latLng.longitude);
        startActivity(i);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        map = googleMap;
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(ActivityViewRestaurant.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityViewRestaurant.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ActivityViewRestaurant.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE_LOCATION);
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
