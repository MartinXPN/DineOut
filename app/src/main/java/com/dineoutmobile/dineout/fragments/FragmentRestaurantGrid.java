package com.dineoutmobile.dineout.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantGrid;


public class FragmentRestaurantGrid extends     Fragment
        implements  SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private GridView restaurantGrid;
    private OnFragmentInteractionListener mListener;
    private boolean isRefreshLayoutSpinning = false;
    private AdapterRestaurantGrid adapter;



    public static FragmentRestaurantGrid newInstance() {
        FragmentRestaurantGrid fragment = new FragmentRestaurantGrid();
        Bundle args = new Bundle();
        args.putInt( "Number", 1 );
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentRestaurantGrid() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Toast.makeText(getActivity(), "Hello from KFC", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+37410261264"));
                getActivity().startActivity(intent);
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
            throw new ClassCastException(getActivity().toString() + " must implement NavigationDrawerCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        updateViewState(ViewState.LOADING, null);
        Log.d("hello", "Refreshed");
        updateViewState(ViewState.DONE, null);
    }


    public interface OnFragmentInteractionListener {
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
