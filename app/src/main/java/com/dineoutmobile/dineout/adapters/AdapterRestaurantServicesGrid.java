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


public class AdapterRestaurantServicesGrid extends RecyclerView.Adapter<AdapterRestaurantServicesGrid.ViewHolder> {

    ViewHolder holder;
    Context context;
    RestaurantFullInfo restaurantInfo;


    public AdapterRestaurantServicesGrid(Context context, RestaurantFullInfo restaurantInfo ) {

        this.context = context;
        this.restaurantInfo = restaurantInfo;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.restaurant_services_grid_item, parent, false );

        holder = new ViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.image.setImageResource( RestaurantFullInfo.Services.values()[position].resource );
        if( RestaurantFullInfo.Services.values()[position].supported )  holder.image.setBackgroundResource( R.drawable.circle_green );
        else                                                            holder.image.setBackgroundResource( R.drawable.circle_red );

        holder.description.setText( context.getResources().getString( RestaurantFullInfo.Services.values()[position].descriptionResId ) );
    }

    @Override
    public int getItemCount() {
        return RestaurantFullInfo.Services.values().length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton image;
        TextView description;

        ViewHolder( View v ) {
            super(v);
            image = (ImageButton) v.findViewById( R.id.restaurant_services_grid_image);
            description = (TextView ) v.findViewById( R.id.restaurant_services_grid_description);
        }
    }
}
