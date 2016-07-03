package com.dineoutmobile.dineout.adapters;

import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterRestaurantImagePager extends PagerAdapter {

    public interface OnDataRequestedListener {
        ArrayList <String> getRestaurantBackgroundPhotos();
    }

    private Fragment parentFragment;
    private OnDataRequestedListener listener;

    public AdapterRestaurantImagePager( Fragment parentFragment ) {
        this.parentFragment = parentFragment;
        listener = (OnDataRequestedListener) parentFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(parentFragment.getActivity());
        ViewGroup layout = (ViewGroup) inflater.inflate( R.layout.restaurant_pager_image, collection, false);
        collection.addView(layout);

        ImageView image = (ImageView) layout.findViewById( R.id.restaurant_photo );
        Picasso.with(parentFragment.getActivity())
                .load( listener.getRestaurantBackgroundPhotos().get( position ) )
                .resize( Util.getWindowWidth( parentFragment.getActivity() ), Util.dpToPx( parentFragment.getActivity().getResources().getDimension( R.dimen.restaurant_background_photo_height ), parentFragment.getActivity() ) )
                .centerInside()
                .into(image);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return listener.getRestaurantBackgroundPhotos().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}