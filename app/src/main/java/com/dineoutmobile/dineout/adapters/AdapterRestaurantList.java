package com.dineoutmobile.dineout.adapters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.activities.ActivityViewRestaurant;
import com.dineoutmobile.dineout.models.RestaurantPreviewSchema;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;


public class AdapterRestaurantList extends AdapterSuperRestaurantList {

    ViewHolder holder;

    public AdapterRestaurantList( Fragment parentFragment ) {
        super( parentFragment );
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ) {

            LayoutInflater inflater = (LayoutInflater) parentFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate( R.layout.restaurant_list_item, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        setValues( (ViewHolder) convertView.getTag(), listener.getRestaurants().get( position ) );
        return convertView;
    }

    private void setValues(ViewHolder holder, final RestaurantPreviewSchema restaurantInfo ) {

        if( holder == null )
            return;

        holder.name.setText( restaurantInfo.restaurantName);
        holder.rating.setText( String.valueOf( restaurantInfo.rating ) );
        //holder.rating.getBackground().setColorFilter(Util.calculateRatingColor( Float.parseFloat( holder.rating.getText().toString() ) ), PorterDuff.Mode.SRC );
        holder.restaurantBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent( parentFragment.getActivity(), ActivityViewRestaurant.class );
                i.putExtra( Util.Tags.BUNDLE_RESTAURANT_ID, restaurantInfo.restaurantId);
                parentFragment.getActivity().startActivity(i);
            }
        });

        if( restaurantInfo.backgroundURL == null || restaurantInfo.backgroundURL.isEmpty() ) {
            restaurantInfo.backgroundURL = "http://dineoutmobile.com/images/placeholder.png";
        }
        Picasso.with(parentFragment.getActivity())
                .load( restaurantInfo.backgroundURL )
                .placeholder( ContextCompat.getDrawable( parentFragment.getActivity(),R.drawable.placeholder ) )
                .resize( Util.getWindowWidth( parentFragment.getActivity() ), Util.dpToPx( parentFragment.getActivity().getResources().getDimension( R.dimen.restaurant_list_item_size ), parentFragment.getActivity() ) )
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
