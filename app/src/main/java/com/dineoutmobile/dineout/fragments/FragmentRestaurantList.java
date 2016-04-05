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
import android.widget.ListView;
import android.widget.Toast;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantList;


public class FragmentRestaurantList extends     Fragment
                                    implements  SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private ListView restaurantList;
    private OnFragmentInteractionListener mListener;
    private boolean isRefreshLayoutSpinning = false;
    private AdapterRestaurantList adapter;


    public FragmentRestaurantList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        restaurantList = (ListView) rootView.findViewById(R.id.restaurant_list);
        adapter = new AdapterRestaurantList(getActivity());
        restaurantList.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);

        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        updateViewState(ViewState.LOADING, null);
        Log.d("List", "Refreshed");
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
