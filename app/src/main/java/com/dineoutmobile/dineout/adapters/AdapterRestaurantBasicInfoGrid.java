package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;


public class AdapterRestaurantBasicInfoGrid extends RecyclerView.Adapter<AdapterRestaurantBasicInfoGrid.ViewHolder> {

    ViewHolder holder;
    Context context;
    RestaurantFullInfo restaurantInfo;


    public AdapterRestaurantBasicInfoGrid(Context context, RestaurantFullInfo restaurantInfo ) {
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
        holder.image.setImageResource( RestaurantFullInfo.BasicInfo.values()[position].resource );
        holder.description.setText( RestaurantFullInfo.BasicInfo.values()[position].description );
        holder.image.setBackgroundResource( R.drawable.circle_neutral );
        holder.restaurantDetailsContainer.setOnClickListener( RestaurantFullInfo.BasicInfo.values()[position].onClickListener );
    }


    @Override
    public int getItemCount() {
        return RestaurantFullInfo.BasicInfo.values().length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton image;
        TextView description;
        LinearLayout restaurantDetailsContainer;

        ViewHolder( View v ) {
            super( v );
            image = (ImageButton) v.findViewById( R.id.restaurant_details_grid_image );
            description = (TextView ) v.findViewById( R.id.restaurant_details_grid_description );
            restaurantDetailsContainer = (LinearLayout) v.findViewById( R.id.restaurant_details_container );
        }
    }
}
