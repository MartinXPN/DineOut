package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;


public class AdapterRestaurantDetailsList extends RecyclerView.Adapter<AdapterRestaurantDetailsList.ViewHolder> {

    ViewHolder holder;
    Context context;

    private static final int images[] =
            {   R.mipmap.ic_link,
                R.mipmap.ic_restaurant_menu,
                R.mipmap.ic_restaurant_menu,
                R.mipmap.ic_music,
                R.mipmap.ic_feedback_white  };

    private static final String description[] =
            {   "Restaurant website",
                "Menu",
                "Seafood, Italian, National",
                "Jazz, Classic, Pop",
                "Feedbacks" };


    public AdapterRestaurantDetailsList( Context context ) {
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate( R.layout.restaurant_details_list_item, parent, false );

        holder = new ViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageResource( images[position] );
        holder.description.setText( description[position] );
    }

    @Override
    public int getItemCount() {
        return images.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton image;
        TextView description;

        ViewHolder( View v ) {
            super( v );
            image = (ImageButton) v.findViewById( R.id.restaurant_details_list_image );
            description = (TextView ) v.findViewById( R.id.restaurant_details_list_description );
        }
    }
}
