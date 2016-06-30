package com.dineoutmobile.dineout.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterSearchFilters;
import com.dineoutmobile.dineout.fragments.FragmentNearbyPlaces;
import com.dineoutmobile.dineout.fragments.FragmentReservedRestaurants;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantsGrid;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantsList;
import com.dineoutmobile.dineout.util.CacheUtil;
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.Util;
import com.dineoutmobile.dineout.util.models.SearchInfo;


public class ActivityChooseRestaurant
        extends     AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    FragmentRestaurantsList.ShowAsGridListener,
                    FragmentRestaurantsGrid.ShowAsListListener,
                    AdapterSearchFilters.OnSearchOptionsChangedListener {



    ActionBarDrawerToggle toggle;
    AdapterSearchFilters adapterSearchFilters = new AdapterSearchFilters( this );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LanguageUtil.setLanguage( LanguageUtil.getLanguage( this ), this );
        setContentView(R.layout.activity_choose_restaurant);


        /// make toolbar our default action-bar
        final Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        /// set up navigation-toggle
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /// set up navigation drawer
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        /// display the list of all restaurants
        navigationView.getMenu().findItem( R.id.nav_restaurant_list ).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().findItem( R.id.nav_restaurant_list ) );


        /// search
        final FloatingActionButton search = (FloatingActionButton) findViewById( R.id.search );
        assert search != null;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSearch();
            }
        });

        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }




    private void showSearch() {

        final FloatingActionButton searchButton = (FloatingActionButton) findViewById( R.id.search );
        assert searchButton != null;
        searchButton.hide();
        toggle.setDrawerIndicatorEnabled(false);


        /// hide all menu items
        final Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        assert toolbar != null;
        Menu menu = toolbar.getMenu();
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(false);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        LayoutInflater inflater = LayoutInflater.from(ActivityChooseRestaurant.this);
        View searchLayout = inflater.inflate(R.layout.search_toolbar, null);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setCustomView(searchLayout, layoutParams);
        actionBar.setDisplayShowCustomEnabled(true);


        Toolbar parent = (Toolbar) searchLayout.getParent();
        parent.setContentInsetsAbsolute(0,0);

        /// normal set-up
        final Toolbar searchQueryToolbar = (Toolbar) findViewById( R.id.search_query_toolbar );
        assert searchQueryToolbar != null;
        searchQueryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearch();
            }
        });


        final RecyclerView searchFilters = (RecyclerView) findViewById( R.id.search_filters );
        assert searchFilters != null;
        searchFilters.setAdapter( adapterSearchFilters );


        EditText searchText = (EditText) findViewById( R.id.search_text );
        searchText.setSelection(0);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Util.hideKeyboard( ActivityChooseRestaurant.this );
                    //performSearch();
                    return true;
                }
                return false;
            }
        });

        Util.showKeyboard( this );

    }
    private void hideSearch() {


        final FloatingActionButton searchButton = (FloatingActionButton) findViewById( R.id.search );
        assert searchButton != null;
        searchButton.show();


        /// show all menu items
        final Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        assert toolbar != null;
        Menu menu = toolbar.getMenu();
        for(int i = 0; i < menu.size(); i++)
            menu.getItem(i).setVisible(true);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);

        Util.hideKeyboard( this );
    }



    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;

        final FloatingActionButton searchButton = (FloatingActionButton) findViewById( R.id.search );
        assert searchButton != null;

        if (drawer.isDrawerOpen(GravityCompat.START))   drawer.closeDrawer(GravityCompat.START);
        else if( !searchButton.isShown() )              hideSearch();
        else                                            super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if( id == R.id.nav_restaurant_list &&  CacheUtil.getCache( this, Util.Tags.SHARED_PREFS_SHOW_AS_GRID, true ) )
            showFragment( id, Util.Tags.RESTAURANT_GRID_FRAGMENT );
        else if( id == R.id.nav_restaurant_list &&  !CacheUtil.getCache( this, Util.Tags.SHARED_PREFS_SHOW_AS_GRID, true ) )
            showFragment( id, Util.Tags.RESTAURANT_LIST_FRAGMENT );
        else if( id == R.id.nav_nearby )                showFragment( id, Util.Tags.NEARBY_PLACES_FRAGMENT );
        else if( id == R.id.nav_reserved_restaurants )  showFragment( id, Util.Tags.RESERVED_RESTAURANTS_FRAGMENT );
        else if( id == R.id.nav_help )                  Util.openUrlInBrowser( this, "http://dineoutmobile.com/" );
        else if( id == R.id.nav_feedback )              Util.writeFeedback( this );
        else if( id == R.id.nav_about )                 Util.openUrlInBrowser( this, "http://dineoutmobile.com/" );


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showFragment(int navId, String fragmentTag ) {

        // get fragment manager
        FragmentManager fm = getFragmentManager();
        // Make sure the current transaction finishes first
        fm.executePendingTransactions();

        // If there is a fragment with this tag...
        if( fm.findFragmentByTag( fragmentTag ) != null )
            return;

        Fragment fragment = null;
        if( navId == R.id.nav_restaurant_list ) {
            if( CacheUtil.getCache( this, Util.Tags.SHARED_PREFS_SHOW_AS_GRID, true ) ) fragment = new FragmentRestaurantsGrid();
            else                                                                        fragment = new FragmentRestaurantsList();
        }
        else if( navId == R.id.nav_nearby )                 fragment = new FragmentNearbyPlaces();
        else if( navId == R.id.nav_reserved_restaurants )   fragment = new FragmentReservedRestaurants();

        // Add fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace( R.id.container, fragment, fragmentTag );
        //fm.popBackStack();
        ft.commit();
    }

    @Override
    public void showAsGrid() {

        CacheUtil.setCache( this, Util.Tags.SHARED_PREFS_SHOW_AS_GRID, true );
        showFragment( R.id.nav_restaurant_list, Util.Tags.RESTAURANT_GRID_FRAGMENT );
    }

    @Override
    public void showAsList() {

        CacheUtil.setCache( this, Util.Tags.SHARED_PREFS_SHOW_AS_GRID, false );
        showFragment( R.id.nav_restaurant_list, Util.Tags.RESTAURANT_LIST_FRAGMENT );
    }

    @Override
    public void onSearchOptionsChanged(SearchInfo searchInfo) {
        /// TODO -> get search response from server
    }
}
