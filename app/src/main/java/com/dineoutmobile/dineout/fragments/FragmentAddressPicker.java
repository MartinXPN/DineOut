package com.dineoutmobile.dineout.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dineoutmobile.dineout.adapters.AdapterReserveQuestionsPager;

public class FragmentAddressPicker extends Fragment {

    AdapterReserveQuestionsPager adapterReserveQuestionsPager;
    OnNewAddressPickListener onRestaurantReservedListener;

    public interface OnNewAddressPickListener {
        void newAddressPicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRestaurantReservedListener = (OnNewAddressPickListener) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        return null;
    }
}
