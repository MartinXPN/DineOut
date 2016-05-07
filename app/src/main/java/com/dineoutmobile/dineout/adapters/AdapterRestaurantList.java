package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.activities.ActivityViewRestaurant;
import com.dineoutmobile.dineout.util.RestaurantBasicInfo;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Locale;


public class AdapterRestaurantList extends AdapterRestaurantListSuper {

    ViewHolder holder;

    public AdapterRestaurantList( Context context ) {
        super( context );
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate( R.layout.restaurant_list_item, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        setValues( (ViewHolder) convertView.getTag(), restaurants.get( position ) );
        return convertView;
    }

    private void setValues(ViewHolder holder, final RestaurantBasicInfo restaurantInfo ) {

        if( holder == null )
            return;

        holder.name.setText( restaurantInfo.name );
        holder.rating.setText( String.format( Locale.ENGLISH, "%.1f", Math.random()*4 + 1 ) );
        //holder.rating.getBackground().setColorFilter(Util.calculateRatingColor( Float.parseFloat( holder.rating.getText().toString() ) ), PorterDuff.Mode.SRC );
        holder.restaurantBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent( context, ActivityViewRestaurant.class );
                i.putExtra( Util.Tags.BUNDLE_RESTAURANT_ID, restaurantInfo.id );
                context.startActivity(i);
            }
        });

        Picasso.with(context)
                .load( restaurantInfo.backgroundPhotoURL )
                .resize( Util.getWindowWidth( context ), Util.dpToPx( context.getResources().getDimension( R.dimen.restaurant_list_item_size ), context ) )
                .centerInside()
                .into( holder.restaurantBackgroundImage );
    }


    private static class ViewHolder {

        RelativeLayout restaurantBackground;
        ImageView restaurantBackgroundImage;
        TextView name;
        TextView rating;

        ViewHolder( View v ) {
            restaurantBackground = (RelativeLayout) v.findViewById( R.id.restaurant_background );
            restaurantBackgroundImage = (ImageView) v.findViewById( R.id.restaurant_background_image );
            name = (TextView ) v.findViewById( R.id.restaurant_name );
            rating = (TextView) v.findViewById( R.id.restaurant_rating );
        }
    }
}
