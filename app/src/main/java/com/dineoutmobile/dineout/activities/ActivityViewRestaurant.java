package com.dineoutmobile.dineout.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantDetailsGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantDetailsList;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantImagePager;

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


        /// initialize restaurant details grid
        final GridView restaurantDetailsGrid = (GridView) findViewById( R.id.restaurant_details_grid );
        assert restaurantDetailsGrid != null;
        restaurantDetailsGrid.setAdapter( new AdapterRestaurantDetailsGrid(this) );

/*
        /// initialize restaurant details list
        final ListView restaurantDetailsList = (ListView) findViewById( R.id.restaurant_details_list );
        assert restaurantDetailsList != null;
        restaurantDetailsGrid.setAdapter( new AdapterRestaurantDetailsList(this) );
*/

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


        Log.d( "ActivityView", "Created!!!" );
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == android.R.id.home )   { finish();     return true; }
        return super.onOptionsItemSelected(item);
    }
}
