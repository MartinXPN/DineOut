package com.dineoutmobile.dineout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.dineoutmobile.dineout.Properties;
import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.activities.ActivityViewRestaurant;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantGrid;


public class FragmentRestaurantGrid extends     Fragment
                                    implements  SwipeRefreshLayout.OnRefreshListener,
                                                SearchView.OnQueryTextListener{

    private SwipeRefreshLayout refreshLayout;
    private GridView restaurantGrid;
    private OnFragmentInteractionListener mListener;
    private boolean isRefreshLayoutSpinning = false;
    private AdapterRestaurantGrid adapter;
    private SearchView searchView;



    public FragmentRestaurantGrid() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_grid, container, false);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById( R.id.swipe_refresh );
        restaurantGrid = (GridView) rootView.findViewById( R.id.restaurant_grid );
        adapter = new AdapterRestaurantGrid( getActivity() );
        restaurantGrid.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);

        restaurantGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                Toast.makeText(getActivity(), "Hello from KFC", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+37410261264"));
                getActivity().startActivity(intent);
                */

                int uid = adapter.CalcRestaurantID(position);

                Intent i = new Intent(getActivity(), ActivityViewRestaurant.class);
                i.putExtra("RESTID", uid);
                getActivity().startActivity(i);
            }
        });

        return rootView;
    }


    @Override
    public void onStart() {

        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement Interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        ((BaseAdapter)restaurantGrid.getAdapter()).notifyDataSetChanged();
        updateViewState(ViewState.LOADING, null);
        Log.d("Grid", "Refreshed");
        updateViewState(ViewState.DONE, null);
        restaurantGrid.invalidateViews();
        restaurantGrid.invalidate();
        restaurantGrid.refreshDrawableState();

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        Log.d("FragmentGrid", "hello created options Menu");
        inflater.inflate( R.menu.activity_choose_restaurant, menu );
        MenuItem menuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE); // A bad way of forcing the SearchView to expand as much as possible
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if( id == R.id.action_view )    mListener.showRestaurantsAsList();

        if( id == R.id.action_language_arm )  {
            //AlertDialog("Armenian");
            Properties.getInstance().setLanguage("hy");
            onRefresh();
        }
        if( id == R.id.action_language_rus )  {
            //AlertDialog("Russian");
            Properties.getInstance().setLanguage("ru");
            onRefresh();
        }
        if( id == R.id.action_language_eng )  {
            //AlertDialog("English");
            Properties.getInstance().setLanguage("en");
            onRefresh();
        }




        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////SEARCH AND STUFF.../////////////////////////////////////////////
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




    public interface OnFragmentInteractionListener {

        void showRestaurantsAsList();
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
                //adapter.removeErrorMessage();
                //adapter.notifyDataSetChanged(calendarData);
                //adapter.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                isRefreshLayoutSpinning = false;
                //adapter.setErrorMessage((errorMessage == null) ? "Unknown error occurred" : errorMessage);
                //adapter.notifyDataSetChanged();
                //adapter.setVisibility(View.VISIBLE);
                break;
        }
        refreshLayout.setRefreshing( isRefreshLayoutSpinning );
    }
}
