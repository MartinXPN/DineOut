package com.dineoutmobile.dineout.fragments;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentRestaurantHeader extends DataRequestingFragment {

    private String TAG = "FragRestHeader";
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_restaurant_header, container, false);
        initialize();
        return rootView;
    }

    @Override
    public void notifyDataSetChanged() {
        initialize();
    }


    public void initialize() {

        /// initialize restaurant logo
        final CircleImageView restaurantLogo = (CircleImageView) rootView.findViewById(R.id.restaurant_logo);
        assert restaurantLogo != null;
        Picasso.with(getActivity())
                .load(Util.getImageURL(getRestaurantFullInfo().logoURL))
                .placeholder(ContextCompat.getDrawable(getActivity(), R.drawable.placeholder))
                .resizeDimen(R.dimen.restaurant_logo_size, R.dimen.restaurant_logo_size)
                .centerCrop()
                .into(restaurantLogo);


        /// initialize restaurant name
        final TextView restaurantName = (TextView) rootView.findViewById(R.id.restaurant_name);
        assert restaurantName != null;
        restaurantName.setText(getRestaurantFullInfo().name);

        /// initialize restaurant rating
        final RatingBar restaurantRating = (RatingBar) rootView.findViewById(R.id.restaurant_rating);
        assert restaurantRating != null;
        if (getRestaurantFullInfo().rating <= 5)
            restaurantRating.setRating(getRestaurantFullInfo().rating);

        /// initialize restaurant description
        final TextView restaurantDescription = (TextView) rootView.findViewById(R.id.restaurant_description);
        assert restaurantDescription != null;
        restaurantDescription.setText(getRestaurantFullInfo().shortDescription);

    }
}
