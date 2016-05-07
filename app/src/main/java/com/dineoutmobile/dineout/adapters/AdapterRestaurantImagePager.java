package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;


public class AdapterRestaurantImagePager extends PagerAdapter {

    private Context context;
    private static int NUMBER_OF_PAGES = 5;

    public AdapterRestaurantImagePager(Context context) {
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate( R.layout.restaurant_pager_image, collection, false);
        collection.addView(layout);

        int width= context.getResources().getDisplayMetrics().widthPixels;
        int height= context.getResources().getDisplayMetrics().heightPixels;
        Log.d( "AdapterPager", "width: " + width + "\theight: " + height );

        ImageView image = (ImageView) layout.findViewById( R.id.restaurant_photo );
        Picasso.with(context)
                .load(R.drawable.restaurant_background_image)
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
        return NUMBER_OF_PAGES;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}