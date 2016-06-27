package com.dineoutmobile.dineout.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantBasicInfoGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantServicesGrid;
import com.dineoutmobile.dineout.databasehelpers.DataTransferAPI;
import com.dineoutmobile.dineout.fragments.FragmentAddressPicker;
import com.dineoutmobile.dineout.fragments.FragmentReserveQuestions;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantMap;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.LockableNestedScrollView;
import com.dineoutmobile.dineout.util.Util;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityViewRestaurant extends     AppCompatActivity
                                    implements  FragmentReserveQuestions.OnRestaurantReservedListener,
                                                FragmentAddressPicker.OnAddressFragmentInteractionListener {


    private RestaurantFullInfo restaurantInfo = new RestaurantFullInfo(this);
    private AdapterRestaurantServicesGrid adapterRestaurantServicesGrid = new AdapterRestaurantServicesGrid(this, restaurantInfo);
    private AdapterRestaurantBasicInfoGrid adapterRestaurantBasicInfoGrid = new AdapterRestaurantBasicInfoGrid(this, restaurantInfo);
    private AdapterRestaurantImagePager adapterRestaurantImagePager = new AdapterRestaurantImagePager(this, restaurantInfo);
    private FragmentAddressPicker fragmentAddressPicker;
    private LockableNestedScrollView lockableNestedScrollView;
    private boolean isLoaded = false;
    private long restaurantId;
    private View.OnTouchListener enableScrollingOnTouch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        /// initialize whole scrollview
        lockableNestedScrollView = (LockableNestedScrollView) findViewById(R.id.nestedscrollview);
        assert lockableNestedScrollView != null;


        /// initialize restaurant information
        Bundle extras = getIntent().getExtras();
        restaurantId = extras.getLong(Util.Tags.BUNDLE_RESTAURANT_ID);
        if (!isLoaded) {
            isLoaded = true;
            loadRestaurantInfo();
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isFinishing()) {
                    FragmentManager fm = getFragmentManager();
                    fm.executePendingTransactions();

                    if( fm.findFragmentByTag( Util.Tags.GOOGLE_MAPS_FRAGMENT) == null ) {
                        FragmentRestaurantMap mapFragment = new FragmentRestaurantMap();
                        fm.beginTransaction().replace(R.id.map, mapFragment, Util.Tags.GOOGLE_MAPS_FRAGMENT).commit();
                    }
                }
            }
        }, 777);

        enableScrollingOnTouch = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lockableNestedScrollView.setScrollingEnabled( true );
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if( id == R.id.action_view_photosphere ) {
            Intent i = new Intent(ActivityViewRestaurant.this, ActivityPanoramaView.class);
            i.putExtra(Util.Tags.BUNDLE_RESTAURANT_NAME, restaurantInfo.name);
            i.putExtra(Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LAT, restaurantInfo.currentAddress.latLng.latitude);
            i.putExtra(Util.Tags.BUNDLE_RESTAURANT_COORDINATE_LNG, restaurantInfo.currentAddress.latLng.longitude);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void onDataLoaded() {
        try {
            adapterRestaurantServicesGrid.notifyDataSetChanged();
            adapterRestaurantBasicInfoGrid.notifyDataSetChanged();
            adapterRestaurantImagePager.notifyDataSetChanged();
            fragmentAddressPicker.notifyDataSetChanged();

            Log.d("ActivityVR", "data loaded");
            initializeVariableViews();
        }
        catch (Exception e){e.printStackTrace();}
    }

    public void loadRestaurantInfo() {

        //// GSON TEST
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // Retrofit needs to know how to deserialize response, for instance into JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( DataTransferAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        Map<String, String> options = new HashMap<>();
        options.put( "language", LanguageUtil.getLanguage( this ).languageLocale );
        options.put( "restaurantId", ""+restaurantId );
        DataTransferAPI api = retrofit.create(DataTransferAPI.class);
        api.getRestaurantFullInfo(options).enqueue(new Callback<RestaurantFullInfo>() {
            @Override
            public void onResponse(Call<RestaurantFullInfo> call, Response<RestaurantFullInfo> response) {

                Toast.makeText(ActivityViewRestaurant.this, "success", Toast.LENGTH_SHORT).show();
                String json = gson.toJson( response.body() );
                Log.d( "RESPONSE IN JSON", json );

                if( response.body() != null ) {
                    restaurantInfo = response.body();
                    onDataLoaded();
                }
            }

            @Override
            public void onFailure(Call<RestaurantFullInfo> call, Throwable t) {

                Toast.makeText(ActivityViewRestaurant.this, "failure", Toast.LENGTH_SHORT).show();
                Toast.makeText(ActivityViewRestaurant.this, t.toString(), Toast.LENGTH_LONG).show();
                Log.d( "FAILURE", t.toString() );
                Log.d( "FAILURE", call.toString() );
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            initializeStaticViews();
        }
    }


    public void initializeStaticViews() {

        /// initialize restaurant photo pager
        final ViewPager restaurantPhotoPager = (ViewPager) findViewById(R.id.restaurant_photos_pager);
        assert restaurantPhotoPager != null;
        restaurantPhotoPager.setAdapter(adapterRestaurantImagePager);
        restaurantPhotoPager.setOnTouchListener( enableScrollingOnTouch );

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        assert pageIndicator != null;
        pageIndicator.setViewPager(restaurantPhotoPager);


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
        restaurantBasicInfoGrid.setOnTouchListener( enableScrollingOnTouch );


        /// initialize restaurant services grid
        GridLayoutManager restaurantDescriptionGridLayoutManager = new GridLayoutManager(this, numberOfItems);
        restaurantDescriptionGridLayoutManager.setAutoMeasureEnabled(true);
        final RecyclerView restaurantServicesGrid = (RecyclerView) findViewById(R.id.restaurant_details_grid);
        assert restaurantServicesGrid != null;
        restaurantServicesGrid.setHasFixedSize(true);
        restaurantServicesGrid.setNestedScrollingEnabled(false);
        restaurantServicesGrid.setLayoutManager(restaurantDescriptionGridLayoutManager);
        restaurantServicesGrid.setAdapter(adapterRestaurantServicesGrid);
        restaurantServicesGrid.setOnTouchListener( enableScrollingOnTouch );



        /// initialize address picker fragment
        fragmentAddressPicker = (FragmentAddressPicker) getFragmentManager().findFragmentById( R.id.restaurant_addresses );


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


        /// initialize the whole scrollView
        lockableNestedScrollView.setOnScrollChangeListener(new LockableNestedScrollView.OnScrollChangeListener() {
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

        /// initialize google maps navigation touch
        Button navigateInGoogleMaps = (Button) findViewById(R.id.fix_scrolling);
        assert navigateInGoogleMaps != null;
        navigateInGoogleMaps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lockableNestedScrollView.setScrollingEnabled( false );
                return false;
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
        restaurantLogo.setOnTouchListener( enableScrollingOnTouch );
        Picasso.with(this)
                .load(Util.getImageURL(restaurantInfo.logoURL))
                .placeholder(ContextCompat.getDrawable(this, R.drawable.placeholder))
                .resizeDimen(R.dimen.restaurant_logo_size, R.dimen.restaurant_logo_size)
                .centerCrop()
                .into(restaurantLogo);


        /// initialize restaurant name
        final TextView restaurantName = (TextView) findViewById(R.id.restaurant_name);
        assert restaurantName != null;
        restaurantName.setOnTouchListener( enableScrollingOnTouch );
        restaurantName.setText(restaurantInfo.name);

        /// initialize restaurant rating
        final RatingBar restaurantRating = (RatingBar) findViewById(R.id.restaurant_rating);
        assert restaurantRating != null;
        restaurantRating.setOnTouchListener( enableScrollingOnTouch );
        if (restaurantInfo.rating <= 5)
            restaurantRating.setRating(restaurantInfo.rating);

        /// initialize restaurant descriptionResId
        final TextView restaurantDescription = (TextView) findViewById(R.id.restaurant_description);
        assert restaurantDescription != null;
        restaurantDescription.setOnTouchListener( enableScrollingOnTouch );
        restaurantDescription.setText(restaurantInfo.shortDescription);
    }


    protected void showDialog( String title, String message ) {
        new AlertDialog
                .Builder( this, AlertDialog.THEME_DEVICE_DEFAULT_DARK )
                .setTitle( title )
                .setMessage( message ).show();
    }

    @Override
    public void onRestaurantReserved() {

        if( !Util.isNetworkAvailable( this ) ) {

            Toast.makeText( this, "Network not available\nPlease check internet connection and try again", Toast.LENGTH_LONG ).show();
            return;
        }
        /// 1. check user in black-list
        /// 2. check if there are available seats -> API needed
        showDialog( "Your reservation was successful!",
                    "We'll call you shortly for confirmation\n" +
                    "If you don't receive a call please submit your reservation again" );
        //finish();
    }

    @Override
    public void onTouch() {
        lockableNestedScrollView.setScrollingEnabled( true );
    }

    @Override
    public void onNewAddressSelected() {
        loadRestaurantInfo();
    }

    @Override
    public RestaurantFullInfo getRestaurantFullInfo() {
        return restaurantInfo;
    }
}
