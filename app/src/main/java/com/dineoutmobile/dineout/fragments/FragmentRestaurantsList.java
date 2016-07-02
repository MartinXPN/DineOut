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
import android.widget.ListView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantList;
import com.dineoutmobile.dineout.util.CacheUtil;
import com.dineoutmobile.dineout.util.Util;


public class FragmentRestaurantsList extends FragmentSuperRestaurantsList {


    public interface ShowAsGridListener {
        void showAsGrid();
    }
    ShowAsGridListener listener;
    private String TAG = "FragRestList";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( TAG, "onCreateView" );
        View rootView = inflater.inflate( R.layout.fragment_restaurant_list, container, false );
        ListView restaurantList = (ListView) rootView.findViewById(R.id.restaurant_list);
        adapter = new AdapterRestaurantList( getActivity() );
        restaurantList.setAdapter( adapter );
        CacheUtil.setCache( getActivity(), Util.Tags.SHARED_PREFS_SHOW_AS_GRID, false );


        listener = (ShowAsGridListener) getActivity();
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById( R.id.swipe_refresh );
        refreshLayout.setOnRefreshListener(this);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        /// provide options for viewing content as a grid
        menu.findItem(R.id.action_view_type).setIcon( R.drawable.ic_view_grid );
        menu.findItem(R.id.action_view_type).setTitle( getActivity().getResources().getString( R.string.view_as_grid ) );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if( id == R.id.action_view_type)        { listener.showAsGrid();    return true; }
        return super.onOptionsItemSelected(item);
    }
}
