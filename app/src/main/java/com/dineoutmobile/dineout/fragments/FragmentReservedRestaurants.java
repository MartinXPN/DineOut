package com.dineoutmobile.dineout.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterReservedRestaurantsList;


public class FragmentReservedRestaurants extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate( R.layout.fragment_my_reservations, container, false );
        ListView reservedRestaurantsList = (ListView) rootView.findViewById( R.id.reserved_restaurants_list );
        reservedRestaurantsList.setAdapter( new AdapterReservedRestaurantsList( getActivity() ) );
        return rootView;
    }
}
