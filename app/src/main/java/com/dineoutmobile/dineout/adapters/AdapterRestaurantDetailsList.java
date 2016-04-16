package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dineoutmobile.dineout.R;


public class AdapterRestaurantDetailsList extends BaseAdapter {

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
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null ) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate( R.layout.restaurant_details_list_item, parent, false );
            holder = new ViewHolder( convertView );
            holder.image.setImageResource( images[position] );
            holder.description.setText( description[position] );

            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


    private static class ViewHolder {
        ImageButton image;
        TextView description;

        ViewHolder( View v ) {
            image = (ImageButton) v.findViewById( R.id.restaurant_details_list_image );
            description = (TextView ) v.findViewById( R.id.restaurant_details_list_description );
        }
    }
}
