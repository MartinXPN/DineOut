package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.activities.ActivityViewRestaurant;
import com.dineoutmobile.dineout.util.RestaurantBasicInfo;
import com.dineoutmobile.dineout.util.Util;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterRestaurantGrid extends AdapterRestaurantListSuper {

    ViewHolder holder;


    public AdapterRestaurantGrid( Context context ) {
        super( context );
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if( convertView == null ) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate( R.layout.restaurant_grid_item, parent, false );
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
        holder.rating.setText( String.valueOf( restaurantInfo.rating ) );
        //holder.rating.getBackground().setColorFilter(Util.calculateRatingColor( Float.parseFloat( holder.rating.getText().toString() ) ), PorterDuff.Mode.SRC );
        holder.gridItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent( context, ActivityViewRestaurant.class );
                i.putExtra( Util.Tags.BUNDLE_RESTAURANT_ID, restaurantInfo.id );
                context.startActivity(i);
            }
        });

        Picasso.with(context)
                .load( Util.getImageURL( restaurantInfo.logoURL ) )
                .placeholder( ContextCompat.getDrawable( context,R.drawable.placeholder ) )
                .resizeDimen( R.dimen.restaurant_grid_item_size, R.dimen.restaurant_grid_item_size )
                .centerInside()
                .into( holder.logo );
    }


    private static class ViewHolder {
        LinearLayout gridItem;
        CircleImageView logo;
        TextView name;
        TextView rating;

        ViewHolder( View v ) {
            gridItem = (LinearLayout) v.findViewById( R.id.restaurant_grid_item );
            logo = (CircleImageView) v.findViewById( R.id.restaurant_logo );
            name = (TextView ) v.findViewById( R.id.restaurant_name );
            rating = (TextView) v.findViewById( R.id.restaurant_rating );
        }
    }
}
