package com.dineoutmobile.dineout.adapters;

import android.app.Fragment;
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
import com.dineoutmobile.dineout.models.RestaurantSchema;

import java.util.ArrayList;


public class AdapterRestaurantBasicInfoGrid extends RecyclerView.Adapter<AdapterRestaurantBasicInfoGrid.ViewHolder> {


    public interface OnDataRequestedListener {
        ArrayList<RestaurantSchema.BasicInfo> getBasicInfo();
        ArrayList<RestaurantSchema.BasicInfoWithLinks> getBasicInfoWithLinks();
    }

    private OnDataRequestedListener listener;
    ViewHolder holder;
    Fragment parentFragment;


    public AdapterRestaurantBasicInfoGrid(Fragment parentFragment ) {
        this.parentFragment = parentFragment;
        listener = (OnDataRequestedListener) parentFragment;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parentFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.restaurant_services_grid_item, parent, false );

        holder = new ViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ArrayList <RestaurantSchema.BasicInfo> basicInfo = listener.getBasicInfo();
        ArrayList <RestaurantSchema.BasicInfoWithLinks> basicInfoWithLinks = listener.getBasicInfoWithLinks();

        if (position < basicInfo.size()) {
            final RestaurantSchema.BasicInfo currentItem = basicInfo.get(position);
            holder.image.setImageResource(currentItem.backgroundResId);
            holder.description.setText(currentItem.description);
            holder.image.setBackgroundResource(R.drawable.circle_neutral);
            holder.restaurantDetailsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.showDialog( parentFragment.getActivity(), parentFragment.getActivity().getResources().getString( currentItem.titleResourceId ), currentItem.description );
                }
            });
        }
        else {
            position -= basicInfo.size();
            final RestaurantSchema.BasicInfoWithLinks currentItem = basicInfoWithLinks.get( position );
            holder.image.setImageResource( currentItem.backgroundResId);
            holder.description.setText( parentFragment.getActivity().getResources().getString( currentItem.descriptionResId ) );
            holder.image.setBackgroundResource( R.drawable.circle_neutral );
            holder.restaurantDetailsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Util.openUrlInBrowser( parentFragment.getActivity(), currentItem.URL );
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return listener.getBasicInfo().size() + listener.getBasicInfoWithLinks().size();
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
