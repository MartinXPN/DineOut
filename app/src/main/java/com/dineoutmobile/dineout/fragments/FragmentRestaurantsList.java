package com.dineoutmobile.dineout.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantGrid;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantList;
import com.dineoutmobile.dineout.util.Util;


public class FragmentRestaurantsList extends     Fragment
                                    implements  SwipeRefreshLayout.OnRefreshListener,
                                                SearchView.OnQueryTextListener {

    private SwipeRefreshLayout refreshLayout;
    private GridView restaurantGrid;
    private ListView restaurantList;
    private AdapterRestaurantGrid adapterRestaurantGrid;
    private AdapterRestaurantList adapterRestaurantList;
    private static boolean showAsGrid = true;

    private boolean isRefreshLayoutSpinning = false;
    private SearchView searchView;
    private Menu menu;
    private ViewGroup container;



    public FragmentRestaurantsList() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( "FragmentRG", "onCreateView" );
        showAsGrid = getShowAsGrid();
        this.container = container;
        View rootView;

        if( showAsGrid ) {
            rootView = getActivity().getLayoutInflater().inflate( R.layout.fragment_restaurant_grid, container, false );
            restaurantGrid = (GridView) rootView.findViewById( R.id.restaurant_grid );
            adapterRestaurantGrid = new AdapterRestaurantGrid( getActivity() );
            restaurantGrid.setAdapter( adapterRestaurantGrid );
        }
        else {
            rootView = getActivity().getLayoutInflater().inflate( R.layout.fragment_restaurant_list, container, false );
            restaurantList = (ListView) rootView.findViewById( R.id.restaurant_list );
            adapterRestaurantList = new AdapterRestaurantList( getActivity() );
            restaurantList.setAdapter( adapterRestaurantList );
        }

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById( R.id.swipe_refresh );
        refreshLayout.setOnRefreshListener(this);
        return rootView;
    }


    public void updateCurrentContent() {

        FragmentManager manager = getActivity().getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        Fragment newFragment = this;
        this.onDestroy();
        ft.remove(this);
        ft.replace( container.getId(), newFragment, Util.Tags.RESTAURANT_LIST_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }


    public boolean getShowAsGrid() {
        SharedPreferences sp = getActivity().getSharedPreferences(Util.Tags.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean( Util.Tags.SHARED_PREFS_SHOW_AS_GRID, true );
    }
    public void setShowAsGrid( boolean asGrid ) {

        if( showAsGrid == asGrid )
            return;

        /// set value in shared preferences for later use
        showAsGrid = asGrid;
        SharedPreferences sp = getActivity().getSharedPreferences(Util.Tags.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean( Util.Tags.SHARED_PREFS_SHOW_AS_GRID, asGrid );
        editor.apply();
    }

    public void setLanguage( Util.Language language ) {

        menu.findItem(R.id.action_language).setIcon( language.iconResource );

        if( Util.getLanguage( getActivity() ) == language )
            return;
        Util.setLanguage( language, getActivity() );


        /// get the information one for time because the language is changed
        /// we need to update displayed information
        if( showAsGrid )    adapterRestaurantGrid.getAllRestaurantsBasicInfo();
        else                adapterRestaurantList.getAllRestaurantsBasicInfo();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.d("FragmentGrid", "hello created options Menu");
        inflater.inflate( R.menu.activity_choose_restaurant, menu );
        MenuItem menuItem = menu.findItem(R.id.action_search);
        this.menu = menu;

        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE); // A bad way of forcing the SearchView to expand as much as possible
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);


        /// update current icon
        if( showAsGrid ) {
            /// provide options for viewing content as a list
            menu.findItem(R.id.action_view_type).setIcon( R.drawable.ic_view_list );
            menu.findItem(R.id.action_view_type).setTitle( "View as list" );
        }
        else {
            /// provide options for viewing content as a grid
            menu.findItem(R.id.action_view_type).setIcon( R.drawable.ic_view_grid );
            menu.findItem(R.id.action_view_type).setTitle( "View as grid" );
        }

        /// update current language
        setLanguage( Util.getLanguage( getActivity() ) );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d( "FragmentRG", "menu item selected -> " + item.getItemId() );
        int id = item.getItemId();
        if( id == R.id.action_view_type)        { setShowAsGrid( !showAsGrid );     updateCurrentContent();     return true; }
        if( id == R.id.action_language_arm )    { setLanguage( Util.Language.HY );  return true; }
        if( id == R.id.action_language_rus )    { setLanguage( Util.Language.RU );  return true; }
        if( id == R.id.action_language_eng )    { setLanguage( Util.Language.EN );  return true; }
        return super.onOptionsItemSelected(item);
    }


    /////////////////////////////// SEARCH /////////////////////////////////////////////
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d( "SearchView", query );
        searchView.clearFocus();
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }



    /////////////////////////////// REFRESH /////////////////////////////////////////////
    @Override
    public void onRefresh() {
        updateViewState(ViewState.LOADING, null);
        Log.d("Grid", "Refreshed");
        Toast.makeText( getActivity(), "Data is up-to-date", Toast.LENGTH_SHORT ).show();
        updateViewState(ViewState.DONE, null);
    }
    private enum ViewState {
        LOADING, DONE, ERROR
    }
    private void updateViewState(ViewState state, String errorMessage) {

        switch (state) {
            case LOADING:
                isRefreshLayoutSpinning = true;
                break;
            case DONE:
                isRefreshLayoutSpinning = false;
                break;
            case ERROR:
                isRefreshLayoutSpinning = false;
                Toast.makeText( getActivity(), errorMessage, Toast.LENGTH_LONG ).show();
                break;
        }
        refreshLayout.setRefreshing( isRefreshLayoutSpinning );
    }
}
