package com.dineoutmobile.dineout.fragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterRestaurantAddressesList;
import com.dineoutmobile.dineout.models.AddressSchema;
import com.dineoutmobile.dineout.models.RestaurantSchema;

import java.util.ArrayList;

public class FragmentAddressPicker extends DataRequestingFragment implements AdapterRestaurantAddressesList.OnAddressListInteractionListener {

    private String TAG = "Frag-AddressPick";
    Button currentAddress;
    RecyclerView addressList;
    AdapterRestaurantAddressesList adapterRestaurantAddressesList;
    OnAddressFragmentInteractionListener onInteractionListener;
    boolean isListExpanded;

    public interface OnAddressFragmentInteractionListener {
        void onNewAddressSelected();
    }


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


    @Override
    public void notifyDataSetChanged() {
        currentAddress.setText(getRestaurantFullInfo().currentAddress == null ? "" : getRestaurantFullInfo().currentAddress.name);
        adapterRestaurantAddressesList.notifyDataSetChanged();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d( TAG, "onCreate" );
        onInteractionListener = (OnAddressFragmentInteractionListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d( TAG, "onCreateView" );
        View rootView = inflater.inflate( R.layout.fragment_address_picker, container, false );
        currentAddress = (Button) rootView.findViewById( R.id.current_address );
        addressList = (RecyclerView) rootView.findViewById( R.id.address_list );
        adapterRestaurantAddressesList = new AdapterRestaurantAddressesList( getActivity(), this );



        /// initialize addresses list
        assert addressList != null;
        addressList.setHasFixedSize(true);
        addressList.setNestedScrollingEnabled(false);
        addressList.setAdapter(adapterRestaurantAddressesList);


        /// initialize current address
        assert currentAddress != null;
        currentAddress.setText(getRestaurantFullInfo().currentAddress == null ? "" : getRestaurantFullInfo().currentAddress.name);
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
        RestaurantSchema restaurantSchema = getRestaurantFullInfo();
        /// compare addresses by their ID
        if( restaurantSchema.allAddresses.get(position).addressId != restaurantSchema.currentAddress.addressId ) {
            restaurantSchema.currentAddress = restaurantSchema.allAddresses.get( position );
            onInteractionListener.onNewAddressSelected();
            notifyDataSetChanged();
        }
        collapseAddressList();
    }

    @Override
    public ArrayList<AddressSchema> getAllAddresses() {
        return getRestaurantFullInfo().allAddresses;
    }
}
