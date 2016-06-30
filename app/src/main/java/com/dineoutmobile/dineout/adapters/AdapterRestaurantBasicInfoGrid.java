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
import com.dineoutmobile.dineout.util.Util;
import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

import java.util.ArrayList;


public class AdapterRestaurantBasicInfoGrid extends RecyclerView.Adapter<AdapterRestaurantBasicInfoGrid.ViewHolder> {


    public interface OnDataRequestedListener {
        RestaurantFullInfo getRestaurantFullInfo();
    }

    private OnDataRequestedListener listener;
    ViewHolder holder;
    Context context;


    public AdapterRestaurantBasicInfoGrid(Context context ) {
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

        ArrayList <RestaurantFullInfo.BasicInfo> basicInfo = listener.getRestaurantFullInfo().getAllBasicInfo();
        ArrayList <RestaurantFullInfo.BasicInfoWithLinks> basicInfoWithLinks = listener.getRestaurantFullInfo().getAllBasicInfoWithLinks();

        if (position < basicInfo.size()) {
            final RestaurantFullInfo.BasicInfo currentItem = basicInfo.get(position);
            holder.image.setImageResource(currentItem.backgroundResId);
            holder.description.setText(currentItem.description);
            holder.image.setBackgroundResource(R.drawable.circle_neutral);
            holder.restaurantDetailsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.showDialog( context, context.getResources().getString( currentItem.titleResourceId ), currentItem.description );
                }
            });
        }
        else {
            position -= basicInfo.size();
            final RestaurantFullInfo.BasicInfoWithLinks currentItem = basicInfoWithLinks.get( position );
            holder.image.setImageResource( currentItem.backgroundResId);
            holder.description.setText( context.getResources().getString( currentItem.descriptionResId ) );
            holder.image.setBackgroundResource( R.drawable.circle_neutral );
            holder.restaurantDetailsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.openUrlInBrowser( context, currentItem.URL );
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return listener.getRestaurantFullInfo().getAllBasicInfo().size() + listener.getRestaurantFullInfo().getAllBasicInfoWithLinks().size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton image;
        TextView description;
        LinearLayout restaurantDetailsContainer;

        ViewHolder( View v ) {
            super( v );
            image = (ImageButton) v.findViewById( R.id.restaurant_services_grid_image);
            description = (TextView ) v.findViewById( R.id.restaurant_services_grid_description);
            restaurantDetailsContainer = (LinearLayout) v.findViewById( R.id.restaurant_services_container);
        }
    }
}
