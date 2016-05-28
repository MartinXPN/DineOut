package com.dineoutmobile.dineout.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.dineoutmobile.dineout.util.LanguageUtil;
import com.dineoutmobile.dineout.util.Util;


public class FragmentRestaurantsList extends    Fragment
                                    implements  SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private GridView restaurantGrid;
    private ListView restaurantList;
    private AdapterRestaurantGrid adapterRestaurantGrid;
    private AdapterRestaurantList adapterRestaurantList;
    private static boolean showAsGrid = true;

    private boolean isRefreshLayoutSpinning = false;
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
            rootView = inflater.inflate( R.layout.fragment_restaurant_grid, container, false );
            restaurantGrid = (GridView) rootView.findViewById( R.id.restaurant_grid );
            adapterRestaurantGrid = new AdapterRestaurantGrid( getActivity() );
            restaurantGrid.setAdapter( adapterRestaurantGrid );
        }
        else {
            rootView = inflater.inflate( R.layout.fragment_restaurant_list, container, false );
            restaurantList = (ListView) rootView.findViewById( R.id.restaurant_list );
            adapterRestaurantList = new AdapterRestaurantList( getActivity() );
            restaurantList.setAdapter( adapterRestaurantList );
        }

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById( R.id.swipe_refresh );
        refreshLayout.setOnRefreshListener(this);
        return rootView;
    }


    public void recreate() {

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

    public void setLanguage( LanguageUtil.Language language ) {

        menu.findItem(R.id.action_language).setIcon( language.iconResource );

        if( LanguageUtil.getLanguage( getActivity() ) == language )
            return;
        LanguageUtil.setLanguage( language, getActivity() );

        /// recreate the whole activity
        getActivity().recreate();


        /// get the information one for time because the language is changed
        /// we need to update displayed information
        if( showAsGrid )    adapterRestaurantGrid.getAllRestaurantsBasicInfo();
        else                adapterRestaurantList.getAllRestaurantsBasicInfo();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.d("FragmentGrid", "hello created options Menu");
        inflater.inflate( R.menu.activity_choose_restaurant, menu );
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);


        /// update current icon
        if( showAsGrid ) {
            /// provide options for viewing content as a list
            menu.findItem(R.id.action_view_type).setIcon( R.drawable.ic_view_list );
            menu.findItem(R.id.action_view_type).setTitle( getActivity().getResources().getString( R.string.view_as_list ) );
        }
        else {
            /// provide options for viewing content as a grid
            menu.findItem(R.id.action_view_type).setIcon( R.drawable.ic_view_grid );
            menu.findItem(R.id.action_view_type).setTitle( getActivity().getResources().getString( R.string.view_as_grid ) );
        }

        /// update current language
        setLanguage( LanguageUtil.getLanguage( getActivity() ) );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d( "FragmentRG", "menu item selected -> " + item.getItemId() );
        int id = item.getItemId();
        if( id == R.id.action_view_type)        { setShowAsGrid( !showAsGrid );     recreate();     return true; }
        if( id == R.id.action_language_arm )    { setLanguage( LanguageUtil.Language.HY );          return true; }
        if( id == R.id.action_language_rus )    { setLanguage( LanguageUtil.Language.RU );          return true; }
        if( id == R.id.action_language_eng )    { setLanguage( LanguageUtil.Language.EN );          return true; }
        return super.onOptionsItemSelected(item);
    }


    /////////////////////////////// REFRESH /////////////////////////////////////////////
    @Override
    public void onRefresh() {
        updateViewState(ViewState.LOADING, null);
        Log.d("Grid", "Refreshed");
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
