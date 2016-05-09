package com.dineoutmobile.dineout.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
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
import com.dineoutmobile.dineout.adapters.AdapterRestaurantBasicInfoGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantDetailsGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;
import com.dineoutmobile.dineout.databasehelpers.DatabaseHelper;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityViewRestaurant extends AppCompatActivity implements RestaurantFullInfo.DataLoading {


    private RestaurantFullInfo restaurantInfo = new RestaurantFullInfo(this);
    private AdapterRestaurantDetailsGrid adapterRestaurantDetailsGrid = new AdapterRestaurantDetailsGrid(this, restaurantInfo);
    private AdapterRestaurantBasicInfoGrid adapterRestaurantBasicInfoGrid = new AdapterRestaurantBasicInfoGrid(this, restaurantInfo);
    private AdapterRestaurantImagePager adapterRestaurantImagePager = new AdapterRestaurantImagePager(this, restaurantInfo);
    private long id;
    private boolean isLoaded = false;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /// initialize restaurant photo pager
        final ViewPager restaurantPhotoPager = (ViewPager) findViewById(R.id.restaurant_photos_pager);
        assert restaurantPhotoPager != null;
        restaurantPhotoPager.setAdapter(adapterRestaurantImagePager);

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        assert pageIndicator != null;
        pageIndicator.setViewPager(restaurantPhotoPager);


        /// initialize 360 photo-sphere button
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.reserve);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityViewRestaurant.this, "NO!", Toast.LENGTH_SHORT).show();
            }
        });

        /// initialize 360 photo-sphere button
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


        final NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedscrollview);
        assert nestedScrollView != null;
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0) {
                    fab.hide();
                    call.hide();
                } else if (scrollY - oldScrollY < 0) {
                    fab.show();
                    call.show();
                }
            }
        });


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
        adapterRestaurantDetailsGrid.notifyDataSetChanged();
        adapterRestaurantBasicInfoGrid.notifyDataSetChanged();
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

        float ht_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());  /// TODO replace 120 by getResources().getDimension...
        int itemWidth = (int) (getWindowManager().getDefaultDisplay().getWidth() / ht_px);

        /// make collapsing toolbar show title only when it is collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        assert collapsingToolbarLayout != null;
        collapsingToolbarLayout.setTitle( restaurantInfo.name );
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setCollapsedTitleTextColor( Color.WHITE );


        /// initialize restaurant logo
        final CircleImageView restaurantLogo = (CircleImageView) findViewById( R.id.restaurant_logo );
        Picasso.with(this)
                .load( restaurantInfo.logoURL )
                .resizeDimen(R.dimen.restaurant_logo_size, R.dimen.restaurant_logo_size)
                .centerCrop()
                .into(restaurantLogo);


        /// initialize restaurant name
        final TextView restaurantName = (TextView) findViewById( R.id.restaurant_name );
        assert restaurantName != null;
        restaurantName.setText( restaurantInfo.name );

        /// initialize restaurant rating
        final RatingBar restaurantRating = (RatingBar) findViewById( R.id.restaurant_rating );
        assert restaurantRating != null;
        restaurantRating.setRating( restaurantInfo.rating );

        /// initialize restaurant descriptionResId
        final TextView restaurantDescription = (TextView) findViewById( R.id.restaurant_description );
        assert restaurantDescription != null;
        restaurantDescription.setText( restaurantInfo.description );


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
}
