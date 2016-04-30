package com.dineoutmobile.dineout.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantsList;
import com.dineoutmobile.dineout.util.Util;


public class ActivityChooseRestaurant
        extends     AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener {



    FragmentRestaurantsList fragmentRestaurantsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_restaurant);


        /// make toolbar our default action-bar
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        /// set up navigation-toggle
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /// set up navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        /// display the list of all restaurants
        navigationView.getMenu().findItem( R.id.nav_restaurant_list ).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().findItem( R.id.nav_restaurant_list ) );
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START))   drawer.closeDrawer(GravityCompat.START);
        else                                            super.onBackPressed();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if( id == R.id.nav_restaurant_list )    showRestaurantList();
        else if( id == R.id.nav_nearby )        showRestaurantsInGoogleMaps();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showRestaurantList() {

        // get fragment manager
        FragmentManager fm = getFragmentManager();
        // Make sure the current transaction finishes first
        fm.executePendingTransactions();

        // If there is no fragment yet with this tag...
        if( fm.findFragmentByTag( Util.Tags.RESTAURANT_LIST_FRAGMENT) == null ) {
            // Add fragment
            FragmentTransaction ft = fm.beginTransaction();
            fragmentRestaurantsList = new FragmentRestaurantsList();
            ft.replace( R.id.container, fragmentRestaurantsList, Util.Tags.RESTAURANT_LIST_FRAGMENT);
            ft.commit();
        }
    }

    public void showRestaurantsInGoogleMaps() {
        Log.d( "hello", "nothing" );
    }
}
