package com.dineoutmobile.dineout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dineoutmobile.dineout.MapsActivity;
import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantGrid;
import com.dineoutmobile.dineout.fragments.FragmentRestaurantList;


public class ActivityChooseRestaurant
        extends     AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    FragmentRestaurantGrid.OnFragmentInteractionListener,
                    FragmentRestaurantList.OnFragmentInteractionListener {



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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /// set up navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /// display the list of all restaurants
        navigationView.getMenu().findItem( R.id.nav_restaurant_list ).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().findItem( R.id.nav_restaurant_list ) );
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))   drawer.closeDrawer(GravityCompat.START);
        else                                            super.onBackPressed();
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_choose_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if( id == R.id.action_view )    updateCurrentContent( id );

        return super.onOptionsItemSelected(item);
    }*/



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if( id == R.id.nav_restaurant_list ) {
            updateCurrentContent(id);
        }
        else if( id == R.id.nav_nearby ) {
            Intent i = new Intent( ActivityChooseRestaurant.this, MapsActivity.class );
            startActivity( i );
            updateCurrentContent(id);
        }
        else {
            Log.d("hello", "world");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void updateCurrentContent( int id ) {

        if( !PlaceholderFragment.isCurrentFragment( id ) ) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(id)).commit();
        }
    }

    @Override
    public void showRestaurantsAsList() {
        updateCurrentContent( R.id.action_view );
    }


    public static class PlaceholderFragment extends Fragment {

        private static int currentId = -1;
        private static Fragment currentFragment = null;


        public static boolean isCurrentFragment( int id ) {
            return currentFragment != null && currentId == id;
        }

        public static Fragment newInstance(int id) {

            if( !isCurrentFragment( id ) ) {

                if( id == R.id.nav_restaurant_list )    currentFragment = new FragmentRestaurantGrid();
                if( id == R.id.action_view )            currentFragment = new FragmentRestaurantList();
                else if( id == R.id.nav_nearby )        { }/*currentFragment = new ActivityNearbyRestaurants();*/
                currentId = id;

                return currentFragment;
            }
            else
                return currentFragment;
        }

        public PlaceholderFragment() {}
    }
}
