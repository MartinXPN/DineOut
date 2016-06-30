package com.dineoutmobile.dineout.adapters;


import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

public class AdapterRestaurantAddressesList extends RecyclerView.Adapter<AdapterRestaurantAddressesList.ViewHolder> {

    ViewHolder holder;
    Context context;
    OnAddressListInteractionListener listener;

    public interface OnAddressListInteractionListener {
        void onAddressSelected( int position );
        RestaurantFullInfo getRestaurantFullInfo();
    }


    public AdapterRestaurantAddressesList(Context context, Fragment parentFragment) {
        this.context = context;
        listener = (OnAddressListInteractionListener) parentFragment;
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
        holder.address.setText(listener.getRestaurantFullInfo().allAddresses.get( position ).name);
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddressSelected( holder.getAdapterPosition() );
            }
        });
    }


    @Override
    public int getItemCount() {
        return listener.getRestaurantFullInfo().allAddresses.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView address;

        ViewHolder( View v ) {
            super( v );
            address = (TextView) v.findViewById( R.id.restaurant_address );
        }
    }
}
