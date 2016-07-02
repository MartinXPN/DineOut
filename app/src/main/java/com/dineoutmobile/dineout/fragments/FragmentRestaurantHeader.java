package com.dineoutmobile.dineout.fragments;


import android.app.Fragment;
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
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentRestaurantHeader extends Fragment {

    public interface OnDataRequestedListener {
        RestaurantFullInfo getRestaurantFullInfo();
    }

    private String TAG = "FragRestHeader";
    OnDataRequestedListener listener;
    RestaurantFullInfo restaurantFullInfo;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        listener = (OnDataRequestedListener) getActivity();
        setRetainInstance( true );
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_restaurant_header, container, false);
        restaurantFullInfo = listener.getRestaurantFullInfo();
        initialize();

        return rootView;
    }

    public void notifyDataSetChanged() {

        restaurantFullInfo = listener.getRestaurantFullInfo();
        initialize();
    }


    public void initialize() {

        /// initialize restaurant logo
        final CircleImageView restaurantLogo = (CircleImageView) rootView.findViewById(R.id.restaurant_logo);
        assert restaurantLogo != null;
        Picasso.with(getActivity())
                .load(Util.getImageURL(restaurantFullInfo.logoURL))
                .placeholder(ContextCompat.getDrawable(getActivity(), R.drawable.placeholder))
                .resizeDimen(R.dimen.restaurant_logo_size, R.dimen.restaurant_logo_size)
                .centerCrop()
                .into(restaurantLogo);


        /// initialize restaurant name
        final TextView restaurantName = (TextView) rootView.findViewById(R.id.restaurant_name);
        assert restaurantName != null;
        restaurantName.setText(restaurantFullInfo.name);

        /// initialize restaurant rating
        final RatingBar restaurantRating = (RatingBar) rootView.findViewById(R.id.restaurant_rating);
        assert restaurantRating != null;
        if (restaurantFullInfo.rating <= 5)
            restaurantRating.setRating(restaurantFullInfo.rating);

        /// initialize restaurant description
        final TextView restaurantDescription = (TextView) rootView.findViewById(R.id.restaurant_description);
        assert restaurantDescription != null;
        restaurantDescription.setText(restaurantFullInfo.shortDescription);

    }
}
