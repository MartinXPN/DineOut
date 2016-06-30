package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;


public class AdapterRestaurantImagePager extends PagerAdapter {

    public interface OnDataRequestedListener {
        RestaurantFullInfo getRestaurantFullInfo();
    }

    private Context context;
    private OnDataRequestedListener listener;

    public AdapterRestaurantImagePager(Context context) {
        this.context = context;
        listener = (OnDataRequestedListener) context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate( R.layout.restaurant_pager_image, collection, false);
        collection.addView(layout);

        ImageView image = (ImageView) layout.findViewById( R.id.restaurant_photo );
        Picasso.with(context)
                .load( listener.getRestaurantFullInfo().backgroundPhotoURLs.get( position ) )
                .resize( Util.getWindowWidth( context ), Util.dpToPx( context.getResources().getDimension( R.dimen.restaurant_background_photo_height ), context ) )
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
        RestaurantFullInfo restaurantInfo = listener.getRestaurantFullInfo();
        if( restaurantInfo == null )                        return 0;
        if( restaurantInfo.backgroundPhotoURLs == null )    return 0;
        return restaurantInfo.backgroundPhotoURLs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}