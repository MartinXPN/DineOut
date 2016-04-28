package com.dineoutmobile.dineout.activities;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantDetailsGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantBasicInfoGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;
import com.dineoutmobile.dineout.util.Util;
import com.viewpagerindicator.CirclePageIndicator;

public class ActivityViewRestaurant extends AppCompatActivity implements RestaurantFullInfo.DataLoading {


    private RestaurantFullInfo restaurantInfo = new RestaurantFullInfo( this );
    private AdapterRestaurantDetailsGrid adapterRestaurantDetailsGrid = new AdapterRestaurantDetailsGrid( this, restaurantInfo );
    private AdapterRestaurantBasicInfoGrid adapterRestaurantBasicInfoGrid = new AdapterRestaurantBasicInfoGrid( this, restaurantInfo );
    private AdapterRestaurantImagePager adapterRestaurantImagePager = new AdapterRestaurantImagePager( this );
    private long id;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restaurant);

        /// initialize restaurant information
        Bundle extras = getIntent().getExtras();
        id = extras.getLong( Util.Tags.BUNDLE_RESTAURANT_ID);
        Log.d( "ActivityVR", "created, id = " + id );
        if( !isLoaded ) {
            Log.d( "Data", "is not loaded" );
            restaurantInfo.loadData( id );
            isLoaded = true;
        }

        /// initialize actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /// initialize restaurant photo pager
        final ViewPager restaurantPhotoPager = (ViewPager) findViewById( R.id.restaurant_photos_pager );
        assert restaurantPhotoPager != null;
        restaurantPhotoPager.setAdapter( adapterRestaurantImagePager );

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById( R.id.indicator );
        assert pageIndicator != null;
        pageIndicator.setViewPager( restaurantPhotoPager );



        /// initialize 360 photo-sphere button
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_show_directions);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This restaurant doesn't have 360 photo-sphere", Snackbar.LENGTH_LONG).setAction("ADD", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText( ActivityViewRestaurant.this, "NO!", Toast.LENGTH_SHORT ).show();
                    }
                }).show();
            }
        });


        initializeDetails();
        initializeVariables();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == android.R.id.home )   { finish();     return true; }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataLoaded() {
        adapterRestaurantDetailsGrid.notifyDataSetChanged();
        adapterRestaurantBasicInfoGrid.notifyDataSetChanged();
        Log.d( "ActivityVR", "data loaded" );
        initializeVariables();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initializeDetails();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            initializeDetails();
        }
    }


    public void initializeDetails() {

        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());  /// TODO replace 120 by getResources().getDimension...
        int itemWidth = (int) (getWindowManager().getDefaultDisplay().getWidth() / ht_px);

        /// initialize restaurant basic info grid
        GridLayoutManager restaurantDescriptionListLayoutManager = new GridLayoutManager(this, itemWidth);
        restaurantDescriptionListLayoutManager.setAutoMeasureEnabled( true );
        final RecyclerView restaurantBasicInfoGrid = (RecyclerView) findViewById( R.id.restaurant_basic_info_grid );
        assert restaurantBasicInfoGrid != null;
        restaurantBasicInfoGrid.setHasFixedSize( true );
        restaurantBasicInfoGrid.setNestedScrollingEnabled( false );
        restaurantBasicInfoGrid.setLayoutManager( restaurantDescriptionListLayoutManager );
        restaurantBasicInfoGrid.setAdapter( adapterRestaurantBasicInfoGrid );

        /// initialize restaurant details grid
        GridLayoutManager restaurantDescriptionGridLayoutManager = new GridLayoutManager(this, itemWidth);
        restaurantDescriptionGridLayoutManager.setAutoMeasureEnabled( true );
        final RecyclerView restaurantDetailsGrid = (RecyclerView) findViewById( R.id.restaurant_details_grid );
        assert restaurantDetailsGrid != null;
        restaurantDetailsGrid.setHasFixedSize( true );
        restaurantDetailsGrid.setNestedScrollingEnabled( false );
        restaurantDetailsGrid.setLayoutManager( restaurantDescriptionGridLayoutManager );
        restaurantDetailsGrid.setAdapter( adapterRestaurantDetailsGrid );
    }
    public void initializeVariables() {

        /// make collapsing toolbar show title only when it is collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle( restaurantInfo.name );
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor( Color.WHITE );


        /// initialize restaurant name
        final TextView restaurantName = (TextView) findViewById( R.id.restaurant_name );
        assert restaurantName != null;
        restaurantName.setText( restaurantInfo.name );

        /// initialize restaurant rating
        final RatingBar restaurantRating = (RatingBar) findViewById( R.id.restaurant_rating );
        assert restaurantRating != null;
        restaurantRating.setRating( restaurantInfo.rating );

        /// initialize restaurant description
        final TextView restaurantDescription = (TextView) findViewById( R.id.restaurant_description );
        assert restaurantDescription != null;
        restaurantDescription.setText( restaurantInfo.description );
    }
}
