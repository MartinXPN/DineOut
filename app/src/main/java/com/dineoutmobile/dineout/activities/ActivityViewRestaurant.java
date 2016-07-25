package com.dineoutmobile.dineout.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.restapi.DataTransferAPI;
import com.dineoutmobile.dineout.fragments.DataRequestingFragment;
import com.dineoutmobile.dineout.fragments.FragmentAddressPicker;
import com.dineoutmobile.dineout.fragments.FragmentReserveQuestions;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantBackgroundPager;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantBasicInfo;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantHeader;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantMap;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantServices;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.LockableNestedScrollView;
import com.dineoutmobile.dineout.util.Util;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityViewRestaurant extends     AppCompatActivity
                                    implements  FragmentReserveQuestions.OnRestaurantReservedListener,
                                                DataRequestingFragment.OnDataRequestedListener,
                                                FragmentRestaurantMap.OnDataRequestedListener,
                                                FragmentRestaurantMap.OnMapInteractionListener,
                                                FragmentAddressPicker.OnAddressFragmentInteractionListener {


    private RestaurantFullInfo restaurantInfo = new RestaurantFullInfo();
    private FragmentRestaurantBackgroundPager fragmentRestaurantBackgroundPager;
    private FragmentRestaurantHeader fragmentRestaurantHeader;
    private FragmentRestaurantBasicInfo fragmentRestaurantBasicInfo;
    private FragmentRestaurantServices fragmentRestaurantServices;
    private FragmentAddressPicker fragmentAddressPicker;
    private FragmentRestaurantMap fragmentRestaurantMap;
    private LockableNestedScrollView lockableNestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LanguageUtil.setLanguage( LanguageUtil.getLanguage( this ), this );
        setContentView(R.layout.activity_view_restaurant);

        /// initialize whole scrollview
        lockableNestedScrollView = (LockableNestedScrollView) findViewById(R.id.nestedscrollview);
        assert lockableNestedScrollView != null;


        /// initialize restaurant information
        Bundle extras = getIntent().getExtras();
        restaurantInfo.restaurantId = extras.getLong(Util.Tags.BUNDLE_RESTAURANT_ID);
        loadRestaurantInfo();
        initializeNavigationInGoogleMaps( lockableNestedScrollView );

        /// initialize actionbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeReserveAndCall();
        initializeCollapsingToolbar();



        /// initialize fragments
        FragmentManager fm = getFragmentManager();
        fm.executePendingTransactions();
        fragmentRestaurantHeader = (FragmentRestaurantHeader) fm.findFragmentById(R.id.restaurant_header);
        fragmentRestaurantBasicInfo = (FragmentRestaurantBasicInfo) fm.findFragmentById(R.id.restaurant_basic_info);
        fragmentRestaurantServices = (FragmentRestaurantServices) fm.findFragmentById(R.id.restaurant_services);
        fragmentAddressPicker = (FragmentAddressPicker) fm.findFragmentById( R.id.restaurant_addresses );
        fragmentRestaurantBackgroundPager = (FragmentRestaurantBackgroundPager) fm.findFragmentById( R.id.restaurant_background_pager );

        /// initialize Google maps fragment
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isFinishing()) {
                    FragmentManager fm = getFragmentManager();
                    fm.executePendingTransactions();

                    if( fm.findFragmentByTag( Util.Tags.GOOGLE_MAPS_FRAGMENT) == null ) {
                        if( fragmentRestaurantMap == null )
                            fragmentRestaurantMap = new FragmentRestaurantMap();
                        fm.beginTransaction().replace(R.id.map, fragmentRestaurantMap, Util.Tags.GOOGLE_MAPS_FRAGMENT).commit();
                    }
                }
            }
        }, 777);
    }



    /// enable scrolling when touching views out of google maps
    /// disable scrolling when touching views inside google maps
    public void initializeNavigationInGoogleMaps(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                child.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        lockableNestedScrollView.setScrollingEnabled( true );
                        return false;
                    }
                });
                initializeNavigationInGoogleMaps((ViewGroup) child);
            }
            else if (child != null) {
                if( child.getId() == R.id.fix_scrolling) {
                    child.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            lockableNestedScrollView.setScrollingEnabled( false );
                            return false;
                        }
                    });
                }
                else {
                    child.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            lockableNestedScrollView.setScrollingEnabled( true );
                            return false;
                        }
                    });
                }
            }
        }
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
        fragmentRestaurantHeader.notifyDataSetChanged();
        fragmentRestaurantBasicInfo.notifyDataSetChanged();
        fragmentRestaurantServices.notifyDataSetChanged();
        fragmentAddressPicker.notifyDataSetChanged();
        fragmentRestaurantBackgroundPager.notifyDataSetChanged();
        if( fragmentRestaurantMap != null ) /// because we create it later than everything else is rendered
            fragmentRestaurantMap.notifyDataSetChanged();
        initializeCollapsingToolbar();
    }

    public void loadRestaurantInfo() {

        // Retrofit needs to know how to deserialize response, for instance into JSON
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( DataTransferAPI.BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        Map<String, String> options = new HashMap<>();
        options.put( "language", LanguageUtil.getLanguage( this ).locale);
        options.put( "restaurantId", ""+restaurantInfo.restaurantId );
        DataTransferAPI api = retrofit.create(DataTransferAPI.class);
        api.getRestaurantFullInfo(options).enqueue(new Callback<RestaurantFullInfo>() {
            @Override
            public void onResponse(Call<RestaurantFullInfo> call, Response<RestaurantFullInfo> response) {
//                /// GSON TEST
//                final Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                String json = gson.toJson( response.body() );
//                Log.d( "RESPONSE IN JSON", json );

                if( response.body() != null ) {
                    restaurantInfo = response.body();
                    onDataLoaded();
                    Toast.makeText(ActivityViewRestaurant.this, "success", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ActivityViewRestaurant.this, "success - null", Toast.LENGTH_SHORT).show();
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


    public void initializeReserveAndCall() {


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
                    Util.hideKeyboard( ActivityViewRestaurant.this );
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
    }

    public void initializeCollapsingToolbar() {

        /// make collapsing toolbar show title only when it is collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle(restaurantInfo.currentAddress == null ? "" : restaurantInfo.currentAddress.name);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
    }

    @Override
    public void onRestaurantReserved() {

        if( !Util.isNetworkAvailable( this ) ) {

            Toast.makeText( this, "Network not available\nPlease check internet connection and try again", Toast.LENGTH_LONG ).show();
            return;
        }
        /// 1. check user in black-list
        /// 2. check if there are available seats -> API needed
        Util.showDialog( this, "Your reservation was successful!",
                    "We'll call you shortly for confirmation\n" +
                    "If you don't receive a call please submit your reservation again" );
        //finish();
    }

    @Override
    public void onNewAddressSelected() {
        //Toast.makeText( this, "New address was selected", Toast.LENGTH_SHORT ).show();
        loadRestaurantInfo();
    }

    @Override
    public RestaurantFullInfo getRestaurantFullInfo() {
        return restaurantInfo;
    }
}
