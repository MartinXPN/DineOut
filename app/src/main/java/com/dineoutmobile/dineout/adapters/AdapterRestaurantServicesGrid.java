package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

import java.util.ArrayList;


public class AdapterRestaurantServicesGrid extends RecyclerView.Adapter<AdapterRestaurantServicesGrid.ViewHolder> {

    public interface OnDataRequestedListener {
        RestaurantFullInfo getRestaurantFullInfo();
    }

    private OnDataRequestedListener listener;
    ViewHolder holder;
    Context context;


    public AdapterRestaurantServicesGrid(Context context ) {

        this.context = context;
        listener = (OnDataRequestedListener) context;
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

        ArrayList<RestaurantFullInfo.Services> services = listener.getRestaurantFullInfo().getAllServices();
        holder.image.setImageResource( services.get(position).resource );
        if (services.get(position).isSupported)     holder.image.setBackgroundResource( R.drawable.circle_green );
        else                                        holder.image.setBackgroundResource( R.drawable.circle_red );

        holder.description.setText( context.getResources().getString( services.get(position).descriptionResId ) );
    }

    @Override
    public int getItemCount() {
        return listener.getRestaurantFullInfo().getAllServices().size();
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
