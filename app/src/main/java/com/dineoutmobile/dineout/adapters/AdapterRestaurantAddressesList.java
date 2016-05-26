package com.dineoutmobile.dineout.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.RestaurantFullInfo;

public class AdapterRestaurantAddressesList extends RecyclerView.Adapter<AdapterRestaurantAddressesList.ViewHolder> {

    ViewHolder holder;
    Context context;
    RestaurantFullInfo restaurantInfo;
    OnAddressSelectedListener listener;

    public interface OnAddressSelectedListener {
        void onAddressSelected( int position );
    }


    public AdapterRestaurantAddressesList(Context context, RestaurantFullInfo restaurantInfo) {
        this.context = context;
        this.restaurantInfo = restaurantInfo;
        listener = (OnAddressSelectedListener) context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.restaurant_addresses_list_item, parent, false );

        holder = new ViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.address.setText(restaurantInfo.allAddresses.get( position ).name);
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantInfo.setCurrentAddress( restaurantInfo.allAddresses.get( holder.getAdapterPosition() ) );
                listener.onAddressSelected( holder.getAdapterPosition() );
            }
        });
    }


    @Override
    public int getItemCount() {
        return restaurantInfo.allAddresses.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView address;

        ViewHolder( View v ) {
            super( v );
            address = (TextView) v.findViewById( R.id.restaurant_address );
        }
    }
}