package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;


public class AdapterRestaurantDetailsGrid extends RecyclerView.Adapter<AdapterRestaurantDetailsGrid.ViewHolder> {

    ViewHolder holder;
    Context context;
    RestaurantFullInfo restaurantInfo;


    public AdapterRestaurantDetailsGrid( Context context, RestaurantFullInfo restaurantInfo ) {

        this.context = context;
        this.restaurantInfo = restaurantInfo;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.restaurant_details_grid_item, parent, false );

        holder = new ViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.image.setImageResource( RestaurantFullInfo.Details.values()[position].resource );
        if( RestaurantFullInfo.Details.values()[position].supported )   holder.image.setBackgroundResource( R.drawable.circle_green );
        else                                                            holder.image.setBackgroundResource( R.drawable.circle_red );

        holder.description.setText( RestaurantFullInfo.Details.values()[position].description );
    }

    @Override
    public int getItemCount() {
        return RestaurantFullInfo.Details.values().length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton image;
        TextView description;

        ViewHolder( View v ) {
            super(v);
            image = (ImageButton) v.findViewById( R.id.restaurant_details_grid_image );
            description = (TextView ) v.findViewById( R.id.restaurant_details_grid_description );
        }
    }
}
