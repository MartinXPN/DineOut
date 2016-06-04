package com.dineoutmobile.dineout.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantAddressesList;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

public class FragmentAddressPicker extends Fragment implements AdapterRestaurantAddressesList.OnAddressSelectedListener {

    Button currentAddress;
    RecyclerView addressList;
    RestaurantFullInfo restaurantFullInfo;
    AdapterRestaurantAddressesList adapterRestaurantAddressesList;
    boolean isListExpanded;

    public interface OnAddressFragmentInteractionListener {
        void onTouch();
        RestaurantFullInfo getRestaurantFullInfo();
    }

    OnAddressFragmentInteractionListener onInteractionListener;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onInteractionListener.onTouch();
            return false;
        }
    };



    public void collapseAddressList() {
        isListExpanded = false;
        addressList.setVisibility(View.GONE);
        currentAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0);
    }
    public void expandAddressList() {
        isListExpanded = true;
        addressList.setVisibility(View.VISIBLE);
        currentAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0);
    }


    public void notifyDataSetChanged() {
        currentAddress.setText(restaurantFullInfo.currentAddress == null ? "" : restaurantFullInfo.currentAddress.name);
        adapterRestaurantAddressesList.notifyDataSetChanged();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate( R.layout.fragment_address_picker, container, false );
        currentAddress = (Button) rootView.findViewById( R.id.current_address );
        addressList = (RecyclerView) rootView.findViewById( R.id.address_list );


        onInteractionListener = (OnAddressFragmentInteractionListener) getActivity();
        restaurantFullInfo = onInteractionListener.getRestaurantFullInfo();
        rootView.setOnTouchListener( onTouchListener );
        currentAddress.setOnTouchListener( onTouchListener );
        addressList.setOnTouchListener( onTouchListener );
        adapterRestaurantAddressesList = new AdapterRestaurantAddressesList( getActivity(), this, restaurantFullInfo );



        /// initialize addresses list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getActivity() );
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert addressList != null;
        addressList.setHasFixedSize(true);
        addressList.setNestedScrollingEnabled(false);
        addressList.setLayoutManager(linearLayoutManager);
        addressList.setAdapter(adapterRestaurantAddressesList);


        /// initialize current address
        assert currentAddress != null;
        currentAddress.setText(restaurantFullInfo.currentAddress == null ? "" : restaurantFullInfo.currentAddress.name);
        currentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isListExpanded = !isListExpanded;
                if (isListExpanded)     expandAddressList();
                else                    collapseAddressList();
            }
        });
        if (isListExpanded)     expandAddressList();
        else                    collapseAddressList();

        return rootView;
    }

    @Override
    public void onAddressSelected(int position) {
        collapseAddressList();
    }
}
