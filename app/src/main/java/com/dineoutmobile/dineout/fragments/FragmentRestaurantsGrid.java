package com.dineoutmobile.dineout.fragments;


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

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantGrid;
import com.dineoutmobile.dineout.util.CacheUtil;
import com.dineoutmobile.dineout.util.Util;

public class FragmentRestaurantsGrid extends FragmentSuperRestaurantsList {

    public interface ShowAsListListener {
        void showAsList();
    }
    private String TAG = "FragRestGrid";
    ShowAsListListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AdapterRestaurantGrid( this );
        listener = (ShowAsListListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( TAG, "onCreateView" );
        View rootView = inflater.inflate( R.layout.fragment_restaurant_grid, container, false );
        GridView restaurantGrid = (GridView) rootView.findViewById(R.id.restaurant_grid);
        restaurantGrid.setAdapter( adapter );
        CacheUtil.setCache( getActivity(), Util.Tags.SHARED_PREFS_SHOW_AS_GRID, true );

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById( R.id.swipe_refresh );
        refreshLayout.setOnRefreshListener(this);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        /// provide options for viewing content as a list
        menu.findItem(R.id.action_view_type).setIcon( R.drawable.ic_view_list );
        menu.findItem(R.id.action_view_type).setTitle( getActivity().getResources().getString( R.string.view_as_list ) );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if( id == R.id.action_view_type)        { listener.showAsList();    return true; }
        return super.onOptionsItemSelected(item);
    }
}
